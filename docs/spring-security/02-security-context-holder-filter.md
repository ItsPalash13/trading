# SecurityContextHolderFilter

[Index](README.md)

This filter runs on every request that entered the security chain. On a login it does not authenticate anyone. It binds a lazy `SecurityContext` to the current thread, then clears that binding when the request ends.

## What arrives

The `HttpServletRequest` and `HttpServletResponse` from `FilterChainProxy`.

## What is created

A `Supplier<SecurityContext>`. The session is not read yet. The supplier reads it only when later code calls `SecurityContextHolder.getContext()`.

```java
Supplier<SecurityContext> deferredContext = this.securityContextRepository.loadDeferredContext(request);
try {
    this.securityContextHolderStrategy.setDeferredContext(deferredContext);
    chain.doFilter(request, response);
}
finally {
    this.securityContextHolderStrategy.clearContext();
}
```

The strategy is `ThreadLocalSecurityContextHolderStrategy`. `SecurityContextHolder` is a static facade. Every `getContext`, `setContext`, and `clearContext` call goes to this strategy. The strategy keeps the supplier in a `ThreadLocal`:

```java
private static final ThreadLocal<Supplier<SecurityContext>> contextHolder = new ThreadLocal<>();

public void setDeferredContext(Supplier<SecurityContext> deferredContext) {
    contextHolder.set(deferredContext);
}

public SecurityContext getContext() {
    return getDeferredContext().get();
}

public SecurityContext createEmptyContext() {
    return new SecurityContextImpl();
}

public void clearContext() {
    contextHolder.remove();
}
```

On a first login there is no session. If something calls `getContext()` before authentication succeeds, the supplier yields an empty `SecurityContextImpl`.

## What is passed on

The same request and response, via `chain.doFilter`. The `ThreadLocal` is visible to every filter and bean on this thread.

## What remains after the request

`clearContext()` runs in `finally`, including after a redirect. The `ThreadLocal` is empty. A context saved in the `HttpSession` is left in place.

Next: [CsrfFilter](03-csrf-filter.md)

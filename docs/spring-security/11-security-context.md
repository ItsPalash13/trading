# SecurityContext

[Index](README.md)

## What arrives

The authenticated `UsernamePasswordAuthenticationToken` with `credentials` already null. `ChangeSessionIdAuthenticationStrategy` has already returned.

## What is created

`successfulAuthentication` asks the holder strategy for an empty context. That is a new `SecurityContextImpl`. The token is set on it, then the same instance is bound to this thread, replacing the deferred supplier installed at the start of the request.

```java
SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
context.setAuthentication(authResult);
this.securityContextHolderStrategy.setContext(context);
this.securityContextRepository.saveContext(context, request, response);
```

`SecurityContextImpl` holds the `Authentication`. After `setContext`, `SecurityContextHolder.getContext()` on this thread returns this instance.

```text
SecurityContextImpl
  authentication = UsernamePasswordAuthenticationToken
    authenticated = true
    principal     = UserDetails ("ada", stored hash, ROLE_USER)
    credentials   = null
    authorities   = [ROLE_USER, FACTOR_PASSWORD]
    details       = WebAuthenticationDetails
```

## What is passed on

That `SecurityContext` is the argument to `SecurityContextRepository.saveContext`, together with the request and response.

The filter still has to publish `InteractiveAuthenticationSuccessEvent` and call the success handler. It does not call `chain.doFilter`.

Next: [DelegatingSecurityContextRepository](12-delegating-security-context-repository.md)

# DelegatingFilterProxy to FilterChainProxy

[Index](README.md)

Tomcat receives `POST /login` and calls the servlet filters registered for `/*`. One of those is `DelegatingFilterProxy`.

## What arrives

The container passes the raw `HttpServletRequest` and `HttpServletResponse`.

## What this does

`DelegatingFilterProxy` does not contain security rules. It looks up the Spring bean named `springSecurityFilterChain` and calls that bean with the same request and response.

On Spring Security 7 that bean is a `CompositeFilterChainProxy`. It runs `ServletRequestPathFilter`, then `FilterChainProxy`.

`FilterChainProxy` wraps the request in a `FirewalledRequest`, then takes the filter list from the first `SecurityFilterChain` that matches. With one default chain, every URL matches, and that chain is a `DefaultSecurityFilterChain`.

```java
FirewalledRequest firewallRequest = this.firewall.getFirewalledRequest((HttpServletRequest) request);
HttpServletResponse firewallResponse = this.firewall.getFirewalledResponse((HttpServletResponse) response);
List<Filter> filters = getFilters(firewallRequest);
this.filterChainDecorator.decorate(reset, filters).doFilter(firewallRequest, firewallResponse);
```

```java
for (SecurityFilterChain chain : this.filterChains) {
    if (chain.matches(request)) {
        return chain.getFilters();
    }
}
```

## What is passed on

The same request and response, into the first filter of the selected chain. Each later filter either calls `chain.doFilter` or writes the response and returns.

Nothing is stored.

Next: [SecurityContextHolderFilter](02-security-context-holder-filter.md)

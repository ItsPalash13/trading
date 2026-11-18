# HttpSessionSecurityContextRepository

[Index](README.md)

## What arrives

The `SecurityContextImpl` for user `ada`, forwarded by `DelegatingSecurityContextRepository`.

## What is stored

The repository calls `request.getSession(true)` when session creation is allowed, which it is by default. That call is what makes Tomcat allocate a session and add `Set-Cookie: JSESSIONID`. Spring Security does not write the cookie itself.

```java
HttpSession session = request.getSession(createSession);
session.setAttribute("SPRING_SECURITY_CONTEXT", context);
```

Session attribute:

```text
SPRING_SECURITY_CONTEXT = SecurityContextImpl
  authentication = UsernamePasswordAuthenticationToken
    authenticated = true
    principal     = UserDetails
                      username    = "ada"
                      password    = "$2a$10$..."
                      authorities = [ROLE_USER]
    credentials   = null
    authorities   = [ROLE_USER, FACTOR_PASSWORD]
    details       = WebAuthenticationDetails
```

The raw password is already null. The hash remains on the `UserDetails` principal.

## Load on a later request

`SecurityContextHolderFilter` asks for a deferred load. This repository does not create a session while reading.

```java
public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
    Supplier<SecurityContext> supplier = () -> readSecurityContextFromSession(request.getSession(false));
    return new SupplierDeferredSecurityContext(supplier, this.securityContextHolderStrategy);
}
```

`readSecurityContextFromSession` returns `session.getAttribute("SPRING_SECURITY_CONTEXT")`. No cookie, or a dead id, produces an empty context.

Next: [RequestAttributeSecurityContextRepository](14-request-attribute-security-context-repository.md)

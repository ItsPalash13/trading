# AuthorizationFilter

[Index](README.md)

This is the protected request after login, not `POST /login`. The context was loaded by [SecurityContextHolderFilter](02-security-context-holder-filter.md) from [HttpSessionSecurityContextRepository](13-http-session-security-context-repository.md) when something on this thread called `getContext()`.

## What arrives

```text
SecurityContextHolder.getContext().getAuthentication()
  authenticated = true
  name          = "ada"
  principal     = UserDetails
  credentials   = null
  authorities   = [ROLE_USER, FACTOR_PASSWORD]
```

`request.getUserPrincipal()` is that same `Authentication`. The login filter did not build a new token. This URL is not `POST /login`, so that filter called `chain.doFilter`.

## What is processed

`AuthorizationFilter` reads the `Authentication` already on the `SecurityContext`. `isAuthenticated()` is true, and the request's authorities satisfy the default rule that every URL requires an authenticated user. The filter calls `chain.doFilter`.

A request with an empty context has no authenticated user. The filter does not call the controller. `ExceptionTranslationFilter` starts authentication, which for form login means the entry point sends the browser to the login page.

## What remains

The controller runs. When the request ends, `SecurityContextHolderFilter` clears the `ThreadLocal`. The session attribute `SPRING_SECURITY_CONTEXT` stays, so the next request can load the same `SecurityContextImpl`.

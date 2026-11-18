# CsrfFilter

[Index](README.md)

## What arrives

`POST /login` with form field `_csrf`. The expected token was stored by `HttpSessionCsrfTokenRepository` when `GET /login` rendered the form.

## What is created and compared

The filter loads a `DeferredCsrfToken`, stores it as a request attribute, and resolves the token the browser sent (parameter `_csrf` or the CSRF header).

```java
DeferredCsrfToken deferredCsrfToken = this.tokenRepository.loadDeferredToken(request, response);
request.setAttribute(DeferredCsrfToken.class.getName(), deferredCsrfToken);
CsrfToken csrfToken = deferredCsrfToken.get();
String actualToken = this.requestHandler.resolveCsrfTokenValue(request, csrfToken);
if (!equalsConstantTime(csrfToken.getToken(), actualToken)) {
    AccessDeniedException exception = (!missingToken)
            ? new InvalidCsrfTokenException(csrfToken, actualToken)
            : new MissingCsrfTokenException(actualToken);
    this.accessDeniedHandler.handle(request, response, exception);
    return;
}
filterChain.doFilter(request, response);
```

| Object | Value on this request |
| --- | --- |
| Expected token | `CsrfToken` saved for the login page |
| `actualToken` | request parameter `_csrf` |
| Match | `filterChain.doFilter` |
| Mismatch | `AccessDeniedHandlerImpl` writes 403 and returns |

The comparison uses a constant-time equals. A mismatch does not pass the `CsrfToken` any further, and the login filter does not run.

`GET`, `HEAD`, `TRACE`, and `OPTIONS` skip the comparison and continue. `POST`, `PUT`, `PATCH`, and `DELETE` are checked.

Next: [LogoutFilter](04-logout-filter.md)

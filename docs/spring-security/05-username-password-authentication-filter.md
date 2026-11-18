# UsernamePasswordAuthenticationFilter

[Index](README.md)

## What arrives

`POST /login` and the form body `username=ada&password=secret`.

The filter attempts authentication only when the request matches `POST /login`. Any other method or path is passed to `chain.doFilter` with no token created.

## What is created

Two request parameters become an unauthenticated `UsernamePasswordAuthenticationToken`. The username is trimmed. A missing password becomes an empty string.

```java
String username = obtainUsername(request);          // request.getParameter("username")
username = (username != null) ? username.trim() : "";
String password = obtainPassword(request);          // request.getParameter("password")
password = (password != null) ? password : "";
UsernamePasswordAuthenticationToken authRequest =
        UsernamePasswordAuthenticationToken.unauthenticated(username, password);
setDetails(request, authRequest);
return this.getAuthenticationManager().authenticate(authRequest);
```

`unauthenticated(...)` sets `authenticated` to false. Calling `setAuthenticated(true)` on this class throws. A trusted token can only be built by the constructor that receives authorities.

`setDetails` uses `WebAuthenticationDetailsSource`. That copies the client address and, when a session already exists, its id. It calls `request.getSession(false)`, so it does not create a session.

```java
public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
    return new WebAuthenticationDetails(context);
}
```

Token passed to `AuthenticationManager`:

```text
class:           UsernamePasswordAuthenticationToken
authenticated:   false
principal:       "ada"          // String
credentials:     "secret"       // String, the raw password
authorities:     empty
details:         WebAuthenticationDetails
                   remoteAddress = request.getRemoteAddr()
                   sessionId     = existing session id, or null
```

## What is passed on

That token is the only argument to `AuthenticationManager.authenticate`. The filter waits for either an authenticated `Authentication` or an `AuthenticationException`.

What it does with the result is in [ChangeSessionIdAuthenticationStrategy](10-change-session-id-strategy.md), [SecurityContext](11-security-context.md), and the two handlers.

Next: [AuthenticationManager to ProviderManager](06-provider-manager.md)

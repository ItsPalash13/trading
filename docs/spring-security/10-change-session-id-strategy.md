# ChangeSessionIdAuthenticationStrategy

[Index](README.md)

## What arrives

The authenticated token, back inside `AbstractAuthenticationProcessingFilter`, which is the base of `UsernamePasswordAuthenticationFilter`. The password has already been erased from the token.

`SessionManagementFilter` is not in the default chain. This strategy is how session fixation protection runs. It is the default `SessionAuthenticationStrategy`, wrapped in a `CompositeSessionAuthenticationStrategy`.

```java
this.sessionStrategy.onAuthentication(authenticationResult, request, response);
successfulAuthentication(request, response, chain, authenticationResult);
```

## What is processed

The strategy looks for a session that already exists. It does not create one. `alwaysCreateSession` defaults to false.

```java
boolean hadSessionAlready = request.getSession(false) != null;
if (!hadSessionAlready && !this.alwaysCreateSession) {
    return;
}
if (hadSessionAlready && request.isRequestedSessionIdValid()) {
    request.changeSessionId();
}
```

| Request | Result |
| --- | --- |
| First login, no `JSESSIONID` | returns. No session yet |
| Browser sent a valid session id | `request.changeSessionId()`. The old id is abandoned and session attributes move to the new id |

The session used to store the login is created later, when the context repository calls `request.getSession(true)`.

Next: [SecurityContext](11-security-context.md)

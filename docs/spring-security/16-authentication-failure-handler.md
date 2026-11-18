# AuthenticationFailureHandler

[Index](README.md)

## What arrives

An `AuthenticationException` thrown while `attemptAuthentication` was running. The usual ones are:

| Exception | Cause |
| --- | --- |
| `BadCredentialsException` | wrong password, or unknown username |
| `LockedException` | account locked |
| `DisabledException` | account disabled |
| `AccountExpiredException` | account expired |
| `CredentialsExpiredException` | credentials expired |

No authenticated token exists on this path.

## What is processed

```java
this.securityContextHolderStrategy.clearContext();
this.rememberMeServices.loginFail(request, response);
this.failureHandler.onAuthenticationFailure(request, response, failed);
```

The `ThreadLocal` is cleared. The default handler is `SimpleUrlAuthenticationFailureHandler`, which redirects to `/login?error`.

No `SPRING_SECURITY_CONTEXT` attribute is written. The filter returns, so the controller does not run.

Next: [AuthorizationFilter](17-authorization-filter.md)

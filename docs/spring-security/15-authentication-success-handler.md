# AuthenticationSuccessHandler

[Index](README.md)

## What arrives

`successfulAuthentication` has already stored the `SecurityContext` in the `ThreadLocal` and in the session. It then calls the success handler with the request, the response, and the authenticated token.

```java
this.rememberMeServices.loginSuccess(request, response, authResult);
this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
this.successHandler.onAuthenticationSuccess(request, response, authResult);
```

`rememberMeServices` is a no-op unless Remember Me is configured. The event is `InteractiveAuthenticationSuccessEvent`. `ProviderManager` has already published its own authentication-success event.

## What is written

The default handler is `SavedRequestAwareAuthenticationSuccessHandler`. It redirects to the request that was saved before login, or to `/` when nothing was saved.

The login filter then returns. It does not call `chain.doFilter`. `AuthorizationFilter`, `ExceptionTranslationFilter`, and the controller do not see this `POST /login`.

As the stack unwinds, `SecurityContextHolderFilter` clears the `ThreadLocal`. The session attribute `SPRING_SECURITY_CONTEXT` stays.

Next: [AuthenticationFailureHandler](16-authentication-failure-handler.md)

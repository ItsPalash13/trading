# RequestAttributeSecurityContextRepository

[Index](README.md)

## What arrives

The same `SecurityContextImpl` `DelegatingSecurityContextRepository` just passed to the session repository.

## What is stored

The context is stored as a request attribute. It exists only until this request finishes. It is not sent to the browser and it is not in the `HttpSession`.

The session copy is the one that survives for the next request. This copy lets code later in the same request see the context even when the session is not consulted again.

Next: [AuthenticationSuccessHandler](15-authentication-success-handler.md)

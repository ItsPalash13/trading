# LogoutFilter

[Index](README.md)

## What arrives

The same `POST /login` request, after CSRF passed.

## What is processed

`LogoutFilter` looks at the request URL. The default logout matcher is `POST /logout`. `POST /login` does not match, so the filter does not run logout handlers and does not invalidate a session.

## What is passed on

The same request and response, via `chain.doFilter`.

A request that does match logout is handled here: logout handlers run, the success handler writes the response, and the filter returns. The login filter is not called.

Next: [UsernamePasswordAuthenticationFilter](05-username-password-authentication-filter.md)

# DaoAuthenticationProvider

[Index](README.md)

## What arrives

The unauthenticated token, passed by `ProviderManager` because `supports(UsernamePasswordAuthenticationToken.class)` is true.

```text
principal:   "ada"
credentials: "secret"
```

## What it calls

1. [UserDetailsService](08-user-details-service.md) `loadUserByUsername("ada")`.
2. Account checks on that `UserDetails`.
3. [PasswordEncoder](09-password-encoder.md) `matches(rawPassword, storedHash)`.

Order of the checks:

1. Account locked → `LockedException`
2. Disabled → `DisabledException`
3. Account expired → `AccountExpiredException`
4. Password does not match → `BadCredentialsException`
5. Credentials expired → `CredentialsExpiredException`

An unknown username is turned into `BadCredentialsException` before these checks (`hideUserNotFoundExceptions` defaults to true). The provider still runs `matches` against a dummy hash first, so a missing user and a wrong password take similar time.

## What is created

A new authenticated token. The principal is no longer the `String` `"ada"`. It is the `UserDetails`. Spring Security 7 adds the authority `FACTOR_PASSWORD` beside the user's own authorities. `details` is copied from the request token.

```java
UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.authenticated(
        principal, authentication.getCredentials(), authorities);
result.setDetails(authentication.getDetails());
return result;
```

Returned to `ProviderManager`, before `eraseCredentials()`:

```text
authenticated: true
principal:     UserDetails (username "ada", stored hash, ROLE_USER)
credentials:   "secret"
authorities:   [ROLE_USER, FACTOR_PASSWORD]
details:       the same WebAuthenticationDetails
```

`ProviderManager` then sets `credentials` to null and returns that object to the login filter.

Next: [UserDetailsService](08-user-details-service.md)

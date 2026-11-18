# PasswordEncoder

[Index](README.md)

## What arrives

Two strings from `DaoAuthenticationProvider`:

```text
rawPassword:  "secret"        // token.getCredentials()
storedHash:   "$2a$10$..."    // UserDetails.getPassword()
```

```java
String presentedPassword = authentication.getCredentials().toString();
if (!this.passwordEncoder.get().matches(presentedPassword, userDetails.getPassword())) {
    throw new BadCredentialsException("Bad credentials");
}
```

## What is processed

`PasswordEncoder` is the interface. `BCryptPasswordEncoder` is the usual implementation. `matches` hashes `secret` with the salt embedded in the stored hash and compares the two hashes. The stored hash is not decoded, and a new hash is not written back on a normal login.

## What is returned

`true` continues the provider's success path. `false` becomes `BadCredentialsException`, which `ProviderManager` returns to the login filter as a failure.

If no `PasswordEncoder` bean is set, the provider falls back to `DelegatingPasswordEncoder`.

Next: [ChangeSessionIdAuthenticationStrategy](10-change-session-id-strategy.md)

# UserDetailsService

[Index](README.md)

## What arrives

The username string from the token, `"ada"`. `DaoAuthenticationProvider.retrieveUser` calls `loadUserByUsername`.

## What is created

A `UserDetails`. The service is the application's lookup. The framework only requires this object back:

```text
username:              "ada"
password:              "$2a$10$..."     // stored hash, never the raw password
authorities:           [ROLE_USER]
enabled:               true
accountNonLocked:      true
accountNonExpired:     true
credentialsNonExpired: true
```

The returned object is passed back to `DaoAuthenticationProvider`. It is used for the account checks and as the password-hash argument to `PasswordEncoder.matches`. On success it becomes the principal of the authenticated token.

## Missing user

`loadUserByUsername` throws `UsernameNotFoundException`. The provider catches it, runs a dummy `matches` so the failure is not faster than a real password check, and throws `BadCredentialsException` with the `UsernameNotFoundException` as the cause.

Next: [PasswordEncoder](09-password-encoder.md)

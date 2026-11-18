# AuthenticationManager to ProviderManager

[Index](README.md)

## What arrives

The unauthenticated `UsernamePasswordAuthenticationToken` from `UsernamePasswordAuthenticationFilter`. The filter holds an `AuthenticationManager`. The object it calls is a `ProviderManager`.

```text
authenticated: false
principal:     "ada"
credentials:   "secret"
```

## What is processed

`ProviderManager.authenticate` asks each `AuthenticationProvider` whether `supports(UsernamePasswordAuthenticationToken.class)` is true. `DaoAuthenticationProvider` returns true. The first provider that returns a non-null `Authentication` wins.

```java
for (AuthenticationProvider provider : getProviders()) {
    if (!provider.supports(toTest)) {
        continue;
    }
    result = provider.authenticate(authentication);
    if (result != null) {
        copyDetails(authentication, result);
        break;
    }
}
if (result == null && this.parent != null) {
    result = this.parent.authenticate(authentication);
}
if (result != null) {
    if (this.eraseCredentialsAfterAuthentication && (result instanceof CredentialsContainer)) {
        ((CredentialsContainer) result).eraseCredentials();
    }
    this.eventPublisher.publishAuthenticationSuccess(result);
    return result;
}
```

If this manager has no matching provider, the same token is passed to the parent `AuthenticationManager`. In the default setup the parent is the `ProviderManager` that holds the `DaoAuthenticationProvider` bean.

## What is returned

`eraseCredentials()` runs on the successful token before it is returned to the login filter. The raw password is cleared. The password hash inside `UserDetails` stays.

```java
public void eraseCredentials() {
    super.eraseCredentials();
    this.credentials = null;
}
```

`BadCredentialsException` does not stop the provider loop by itself. Another provider may still be tried. `AccountStatusException` and `InternalAuthenticationServiceException` propagate to the login filter immediately.

Next: [DaoAuthenticationProvider](07-dao-authentication-provider.md)

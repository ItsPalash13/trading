# DelegatingSecurityContextRepository

[Index](README.md)

## What arrives

The `SecurityContextImpl` from `successfulAuthentication`, plus the request and response.

```java
this.securityContextRepository.saveContext(context, request, response);
```

The repository on the login filter and on `SecurityContextHolderFilter` is this delegator. `SessionManagementConfigurer` builds it with two delegates, session first:

1. [HttpSessionSecurityContextRepository](13-http-session-security-context-repository.md)
2. [RequestAttributeSecurityContextRepository](14-request-attribute-security-context-repository.md)

## What is passed on

`saveContext` calls `saveContext` on every delegate with the same context, request, and response. It does not choose one. Both stores are written.

```java
public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
    for (SecurityContextRepository delegate : this.delegates) {
        delegate.saveContext(context, request, response);
    }
}
```

`loadDeferredContext` also walks the delegates. On a later request the session delegate is asked first. If that load is only an empty generated context, the request-attribute delegate is used.

Next: [HttpSessionSecurityContextRepository](13-http-session-security-context-repository.md)

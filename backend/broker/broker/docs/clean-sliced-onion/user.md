# User module — Clean Onion

Owns people: identity, email, password hash.

```text
user/
├── domain/model/
├── application/
│   ├── port/in/
│   ├── port/out/
│   ├── command/
│   ├── result/
│   └── service/
└── adapter/
    ├── in/web/v1/
    └── out/persistence/
```

## Layer diagram with classes

```mermaid
flowchart TB
  subgraph fw["Frameworks & Drivers"]
    JPA["Spring Data JPA"]
    SEC["Spring Security<br/>(consumes UserCredentials)"]
    MVC["Spring MVC"]
  end

  subgraph adapters["Interface Adapters"]
    subgraph inbound["adapter/in"]
      C["UserV1Controller"]
      M["UserV1Mapper"]
      R["UserV1Response"]
    end
    subgraph outbound["adapter/out"]
      A["UserPersistenceAdapter"]
      E["UserJpaEntity"]
      SD["SpringDataUserRepository"]
      MAP["UserMapper"]
    end
  end

  subgraph application["Application — Use Cases"]
    CU["CreateUserUseCase"]
    UQ["UserQuery"]
    CUS["CreateUserService"]
    UQS["UserQueryService"]
    CMD["CreateUserCommand"]
    UV["UserView"]
    UC["UserCredentials"]
    UR["UserRepository<br/>(port/out)"]
  end

  subgraph domain["Domain — Entities"]
    U["User"]
  end

  MVC --> C
  C --> UQ
  C --> M
  M --> R
  CUS --> CU
  UQS --> UQ
  CUS --> UR
  CUS --> CMD
  CUS --> UV
  UQS --> UR
  UQS --> UV
  UQS --> UC
  A -.->|implements| UR
  A --> SD
  A --> MAP
  MAP --> E
  MAP --> U
  CUS --> U
  JPA --> SD
  SEC -.->|via UserQuery| UQ
```

## Class placement

| Layer | Package | Classes |
|---|---|---|
| Domain | `user.domain.model` | `User` |
| Application (in) | `user.application.port.in` | `CreateUserUseCase`, `UserQuery` |
| Application (out) | `user.application.port.out` | `UserRepository` |
| Application | `user.application.command` | `CreateUserCommand` |
| Application | `user.application.result` | `UserView`, `UserCredentials` |
| Application | `user.application.service` | `CreateUserService`, `UserQueryService` |
| Adapter in | `user.adapter.in.web.v1` | `UserV1Controller`, `UserV1Mapper`, `UserV1Response` |
| Adapter out | `user.adapter.out.persistence` | `UserPersistenceAdapter`, `UserJpaEntity`, `SpringDataUserRepository`, `UserMapper` |

## Runtime flows

**Create user** (called by auth signup):

```text
CreateUserUseCase.create(CreateUserCommand)
  → User.register(...)
  → UserRepository.save
  → UserPersistenceAdapter → SpringDataUserRepository → users table
  → UserView
```

**Query /me**:

```text
GET /api/v1/users/me
  → UserV1Controller
  → UserQuery.getByEmail
  → UserV1Mapper → UserV1Response
```

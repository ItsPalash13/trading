# Account module — Clean Onion

Owns trading accounts: ownership, type, name, selection in the HTTP session.

```text
account/
├── domain/model/
├── application/
│   ├── port/in/
│   ├── port/out/
│   ├── result/
│   └── service/
└── adapter/
    ├── in/web/
    └── out/persistence/
```

## Layer diagram with classes

```mermaid
flowchart TB
  subgraph fw["Frameworks & Drivers"]
    JPA["Spring Data JPA"]
    SERVLET["HttpSession"]
  end

  subgraph adapters["Interface Adapters"]
    subgraph inbound["adapter/in"]
      CAS["CurrentAccountSession"]
    end
    subgraph outbound["adapter/out"]
      A["AccountPersistenceAdapter"]
      E["AccountJpaEntity"]
      SD["SpringDataAccountRepository"]
      MAP["AccountMapper"]
    end
  end

  subgraph application["Application — Use Cases"]
    OA["OpenAccountUseCase"]
    AQ["AccountQuery"]
    OAS["OpenAccountService"]
    AQS["AccountQueryService"]
    AV["AccountView"]
    AR["AccountRepository<br/>(port/out)"]
  end

  subgraph domain["Domain — Entities"]
    ACC["Account"]
    AT["AccountType"]
  end

  SERVLET --> CAS
  OAS --> OA
  AQS --> AQ
  OAS --> AR
  OAS --> AV
  AQS --> AR
  AQS --> AV
  A -.->|implements| AR
  A --> SD
  A --> MAP
  MAP --> E
  MAP --> ACC
  OAS --> ACC
  AQS --> ACC
  JPA --> SD
```

## Class placement

| Layer | Package | Classes |
|---|---|---|
| Domain | `account.domain.model` | `Account`, `AccountType` |
| Application (in) | `account.application.port.in` | `OpenAccountUseCase`, `AccountQuery` |
| Application (out) | `account.application.port.out` | `AccountRepository` |
| Application | `account.application.result` | `AccountView` |
| Application | `account.application.service` | `OpenAccountService`, `AccountQueryService` |
| Adapter in | `account.adapter.in.web` | `CurrentAccountSession` |
| Adapter out | `account.adapter.out.persistence` | `AccountPersistenceAdapter`, `AccountJpaEntity`, `SpringDataAccountRepository`, `AccountMapper` |

## Notes

- `Account.openRegular` and `Account.ensureOwnedBy` protect invariants in the domain.
- `CurrentAccountSession` is a web adapter (stores selected account id in `HttpSession`). Auth’s web/security adapters depend on it deliberately.
- There is no account REST controller yet; accounts are opened during signup and selected during login/session.

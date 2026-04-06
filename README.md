# JAX-RS Step-By-Step

A template project demonstrating JAX-RS, CDI, and embedded TomEE integration using Jakarta EE 10.

## Architecture

- **CDI beans** — `ApplicationScopedBean` demonstrates application-scoped CDI injection
- **JAX-RS resources** — `HelloResource` (plain REST) and `CdiAwareResource` (CDI-injected REST)
- **Embedded TomEE** — `ServerStarter` boots the application without an external server

## Requirements

- Java 25+
- Maven 3.6.3+

## Build

```bash
# Default build (TomEE embedded integration tests)
mvn clean verify

# CDI SE unit tests using dynamic-cdi-test-bean-addon
mvn clean verify -Pcdi-se

# Run the embedded server
mvn clean package
java -cp "target/classes:target/dependency/*" org.os890.template.embedded.ServerStarter
```

## Test Profiles

| Profile   | Description                                      |
|-----------|--------------------------------------------------|
| `tomee`   | TomEE embedded integration tests (default)       |
| `cdi-se`  | CDI SE tests using dynamic-cdi-test-bean-addon   |

## Test Addon

CDI SE tests use the [dynamic-cdi-test-bean-addon](https://github.com/os890/dynamic-cdi-test-bean-addon)
which boots a CDI SE container, discovers beans automatically via `@EnableTestBeans`,
and auto-mocks unsatisfied injection points with Mockito.

## Quality Plugins

- **Compiler** — `-Xlint:all`, fail on warnings
- **Enforcer** — Java 25+, Maven 3.6.3+, dependency convergence, banned `javax.*`
- **Checkstyle** — no star imports, brace rules, whitespace, coding standards
- **Apache RAT** — license header verification
- **Surefire** — test execution (pinned version)
- **JaCoCo** — code coverage reporting
- **Javadoc** — `doclint=none`, `quiet=true`

## License

This project is licensed under the [Apache License, Version 2.0](LICENSE).

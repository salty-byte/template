# gRPC-Spring-Kotlin template

[![build sample](https://img.shields.io/badge/build-passing-brightgreen)](https://shields.io/category/build)
[![coverage sample](https://img.shields.io/badge/coverage-100%25-brightgreen)](https://shields.io/category/coverage)

サンプルのため、OAuth2.0 ではなくカスタム JWT を使っています。
実際には OAuth2.0 を使った方が良いです。

**Settings**

- Java 17 (or later)
- Gradle 8.2

## Demo

_Demo here: images, gif animations, etc._

## Requirement

- Java: 17 (openjdk-17)
- Gradle: 8.2

## Usage

_Usage here_

```shell
git clone https://github.com/salty-byte/template
cd spling-kotlin
```

- Rename `rootProject.name` in settings.gradle
- Change `group` , `version` and `sourceCompatibility` in build.gradle

**Example:**

- Generate proto

```shell
./gradlew generateProto
```

- Run main server

```shell
# production
./gradlew bootRun --args="--SERVER_SECRET=XXXXXXXX"

# development
./gradlew bootRun --args="--spring.profiles.active=development"
```

- Check via a browser

```shell
./grpcui -plaintext localhost:6565
```

## Installation

_Installation here_

## Test

```shell
./gradlew test
```

## License

[MIT](/LICENSE)

## Author

[salty-byte](https://github.com/salty-byte)

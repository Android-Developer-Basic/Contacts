# Демо-приложение Kotlin Multiplatform

Это проект, построенный на Kotlin Multiplatform, который работает на Android, iOS, Desktop а также
простой сервер на Ktor для авторизации и загрузки данных.
Программа - простой мультиплатформенный список контактов, при написании которой использовались следующие 
библиотеки:

- [Ktor](https://github.com/ktorio/ktor) for network communication and to build a server using common data.
- [SqlDelight](https://github.com/cashapp/sqldelight) for a local data cache.
- [CommonStateMachine](https://github.com/motorro/CommonStateMachine) to build application logic.
- [MockMP](https://github.com/kosi-libs/MocKMP) for multiplatform test mocks

* `/composeApp` содержит код, общий для всех клиентских платформ. Использует Compose Multiplatform.
  Содержит следующие папки:
  - `commonMain` Общий код для всех платформ.
  - Другие папки предназначены для Kotlin кода, который будет скомпилирован только для платформы, соответствующей имени папки.
    Например, если вы хотите использовать CoreCrypto от Apple для части iOS вашего приложения Kotlin,
    `iosMain` будет подходящей папкой для таких вызовов.

* `/iosApp` содержит приложение для iOS. Даже если вы пишете весь UI на Compose Multiplatform,
  этот модуль - это точка входа в приложение iOS. Также это место, где можно разместить код SwiftUI вашего проекта.

* `/server` простой сервер Ktor, имитирующий сервер приложения. Предоставляет аутентификацию и список контактов.

* `/shared` содержит определение data-слассов и общих утилит клиентского и серверного приложений.


Узнайте больше о [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

# Kotlin Multiplatform demo application

This is a Kotlin Multiplatform project targeting Android, iOS, Desktop, plus Ktor Server for contact data.
The program is a simple multiplatform contact list written with the following libraries:

- [Ktor](https://github.com/ktorio/ktor) for network communication and to build a server using common data.
- [SqlDelight](https://github.com/cashapp/sqldelight) for a local data cache.
- [CommonStateMachine](https://github.com/motorro/CommonStateMachine) to build application logic.
- [MockMP](https://github.com/kosi-libs/MocKMP) for multiplatform test mocks

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/server` A simple Ktor server to provide authentication and contacts data.

* `/shared` contains common data class definition and utils used both client and server side.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
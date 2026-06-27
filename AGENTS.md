# AGENTS.md

Этот файл дает ИИ-агентам контекст по проекту, чтобы они могли безопасно вносить изменения в репозиторий.

## Общая Структура Проекта

`kabanchik` - это Kotlin Multiplatform монорепозиторий с двумя связанными приложениями:

- `composeApp` - клиентское приложение.
- `proApp` - приложение для сотрудников/исполнителей.

Приложения во многом похожи, поэтому общая логика и UI должны выноситься в общие модули, когда они действительно переиспользуются обоими приложениями.

Основные группы модулей:

- `common/*` содержит переиспользуемую инфраструктуру, UI kit, tools, network, data store, state store и общую обработку ошибок.
- `domain/common/*`, `data/common/*`, `features/common/*` содержат бизнес-логику, data-логику и feature-логику, используемую обоими приложениями.
- `domain/client/*`, `data/client/*`, `features/client/*` содержат логику только для клиентского приложения.
- `domain/pro/*`, `data/pro/*`, `features/pro/*` содержат логику только для приложения сотрудников/исполнителей.
- `iosApp` - iOS entry point для клиентского приложения.
- `iosProApp` - iOS entry point для pro-приложения.
- `build-logic` содержит Gradle convention plugins, которые используются feature/domain/data/common модулями.

## Архитектурные Правила

- Клиентский код должен оставаться в `client` модулях, pro-код - в `pro` модулях.
- Переносите логику в `common` модули только если она действительно общая для обоих приложений. Не добавляйте app-specific ветвления в common-код без явной абстракции.
- Соблюдайте направление зависимостей между модулями:
  - `features/*` могут зависеть от domain-модулей, common UI kit, common store, tools и error handling.
  - `domain/*` должен предоставлять интерфейсы и бизнес-модели; избегайте зависимостей на data-реализации.
  - `data/*` реализует repositories и sources, может зависеть от domain-интерфейсов/моделей и API-моделей.
  - app-модули (`composeApp`, `proApp`) собирают финальный граф зависимостей.
- Публичные API фич должны находиться в `api` пакетах. Реализации должны находиться в `internal` пакетах.
- На границах слоев предпочитайте узкие интерфейсы и мапперы вместо протаскивания API DTO в domain или UI.

## Именование И Пакеты

- Корневой package - `ru.kabanchik`.
- Клиентские packages используют `ru.kabanchik.client...`.
- Pro packages используют `ru.kabanchik.pro...`.
- Общие packages используют `ru.kabanchik.common...`.
- Koin-модули обычно называются по шаблону `DomainClientAuthModule`, `DataCommonChatModule` и т.п.
- Default-реализации обычно называются с префиксом `Default`, например `DefaultClientAuthInteractor`.
- Feature contracts используют классы `Contract` с вложенными типами `State`, `Event` и `SideEffect`.
- При использовании однострочных Kotlin-функций с expression body (`fun foo(): Bar = ...`) обязательно указывайте явный возвращаемый тип.

## UI И State

- UI написан на Compose Multiplatform.
- Навигация и lifecycle компонентов основаны на Decompose и Essenty.
- Feature components отдают state как `StateFlow<...>` и делегируют события в stores.
- Stores обычно наследуются от `BaseCoroutineStore<Event, State, SideEffect>` из `common/store`.
- Используйте `retainedInstance` для stores внутри Decompose components, если следуете существующему feature-паттерну.
- Общие UI widgets, theme, icons, scaffold, snack bar и screen-size utilities должны находиться в `common/ui-kit`.
- String resources для Compose-модулей лежат в `src/commonMain/composeResources`.

## Dependency Injection

- DI построен на Koin.
- Добавляйте bindings в том модуле, которому принадлежит реализация.
- App-level списки модулей собираются здесь:
  - `composeApp/src/commonMain/kotlin/ru/kabanchik/client/di/Modules.kt`
  - `proApp/src/commonMain/kotlin/ru/kabanchik/pro/di/ProModules.kt`
- Platform-specific модули предоставляются через `expect val platformModules` и actual-реализации в platform source sets.

## Gradle Conventions

- Для library-модулей предпочитайте convention plugins из `build-logic`:
  - `alias(libs.plugins.build.kmp)` для настройки KMP library.
  - `alias(libs.plugins.build.compose)` для Compose UI модулей.
  - `alias(libs.plugins.build.decompose)` для Decompose feature-модулей.
  - `alias(libs.plugins.build.koin)` для модулей с Koin bindings.
- Не дублируйте зависимости, которые уже добавляются convention plugins, если модулю не требуется что-то дополнительное.
- Версии зависимостей храните в `gradle/libs.versions.toml`.
- Новые модули регистрируйте в `settings.gradle.kts`.
- JVM target и Java compatibility - 21.
- Android compile/target/min SDK берутся из version catalog.

## Команды Сборки И Проверки

По возможности используйте сфокусированные Gradle-команды:

```shell
./gradlew :composeApp:assembleDebug
./gradlew :proApp:assembleDebug
./gradlew :composeApp:run
./gradlew :proApp:run
./gradlew :domain:common:chat:logic:allTests
./gradlew jvmTest
```

Для широкой проверки:

```shell
./gradlew build
```

Тесты запускайте через `./gradlew jvmTest`. Если меняется только один модуль, сначала предпочитайте релевантную задачу этого модуля: `compileKotlin...`, `assemble` или `allTests`. Полную сборку запускайте при необходимости.

## Source Set Guidance

- Общий Kotlin-код кладите в `src/commonMain/kotlin`.
- Общие тесты кладите в `src/commonTest/kotlin`.
- Используйте `androidMain`, `iosMain` и `jvmMain` только для platform-specific кода.
- Избегайте platform APIs в `commonMain`; вместо этого вводите expect/actual или platform provider.

## Добавление Новой Фичи

1. Определите, фича относится к `client`, `pro` или `common`.
2. Сначала добавьте или обновите domain model/logic модули.
3. Если нужны сеть или persistence, добавьте data-реализации и мапперы.
4. Добавьте feature UI/component/store модули.
5. Зарегистрируйте DI-модули в соответствующем app-level списке.
6. Добавьте зависимость на feature-модуль в `composeApp` или `proApp`.
7. Добавьте сфокусированные тесты для бизнес-логики или нетривиального поведения store.

## Правила Безопасности Для Агентов

- Перед редактированием проверяйте `git status --short` и не откатывайте изменения пользователя.
- Для поиска предпочитайте `rg`.
- Держите изменения в рамках запрошенной фичи или исправления.
- Не переименовывайте модули, packages, app IDs или framework names без явного запроса.
- Осторожно относитесь к опечаткам в существующих package paths; исправляйте их только если это часть задачи или они блокируют компиляцию.
- При добавлении зависимостей сначала используйте существующие библиотеки и паттерны проекта, прежде чем вводить новые.

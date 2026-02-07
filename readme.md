# Type Service

TypeService - приложение для получения данных с [monkeytype](https://monkeytype.com/).

В целом, реализованы почти все методы [monkeytype API](https://api.monkeytype.com/docs) из разделов:

* [user](/src/main/java/holymagic/typeservice/controller/UserController.java);
* [test results](/src/main/java/holymagic/typeservice/controller/ResultController.java);
* [public](/src/main/java/holymagic/typeservice/controller/PublicDataController.java);
* [leaderboards](/src/main/java/holymagic/typeservice/controller/LeaderboardController.java).

# Использованные технологии:

* [Spring Boot](https://spring.io/projects/spring-boot) – как основной фрэймворк;
* [PostgreSQL](https://www.postgresql.org/) – как основная реляционная база данных;
* [Liquibase](https://www.liquibase.org/) – для ведения миграций схемы БД;
* [Gradle](https://gradle.org/) – как система сборки приложения;
* [Mapstruct](https://mapstruct.org/) - для маппинга сущности на dto и обратно.

# Основные моменты:

* для успешной отправки любого http-запроса нужен Ape Key, получить который можно в настройках учетной записи <br />
account settings -> ape keys -> generate new key (https://monkeytype.com/account-settings?tab=apeKeys);
* все запросы осуществляет [restClient](/src/main/java/holymagic/typeservice/config/RestClientConfig.java), который 
автоматически прикрепляет ключ в header авторизации;
* сам ключ вынесен в файл конфигурации application.properties;
* для локального запуска нужно поднять базу данных и создать файл конфигурации;
* проект носит ознакомительный характер - написан с целью познакомиться с api, компонентами spring и т.д.

# Код

Restful-приложение с endpoint'ами:

* стандартная трёхслойная архитектура - [Controller](/src/main/java/holymagic/typeservice/controller),
[Service](/src/main/java/holymagic/typeservice/service), [Repository](/src/main/java/holymagic/typeservice/repository);
* написан [GlobalExceptionHandler](/src/main/java/holymagic/typeservice/exception/GlobalExceptionHandler.java), есть конфигурация
[OpenApi](/src/main/java/holymagic/typeservice/config/OpenApiConfig.java);
* слой Repository реализован на JPA (Hibernate).

# База данных

* База данных поднимается в Docker;
* Для перевода базы данных из одного состояния в другое используется [liquibase](/src/main/resources/db/changelog/db.changelog-master.yaml);
* В коде продемонстрирована работа с JPA (Hibernate);

# Тесты

Тесты(на данный момент) написаны для сервисного слоя и мапперов.

* SpringBootTest;
* AssertJ;
* JUnit;
* Parameterized tests;

# Todo

* фильтрация результатов;
* добавить скриншоты из Postman, DBeaver;
* интеграционное тестирование;
* ...
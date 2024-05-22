# Monitoring-Service

**Monitoring-Service** - это web приложение для подачи показаний счетчиков отопления, горячей и холодной воды.

## Функциональность

- Регистрация пользователей
- Авторизация пользователей
- Получение актуальных показаний счетчиков
- Подача показаний
- Просмотр показаний за конкретный месяц
- Просмотр истории подачи показаний
- Контроль прав пользователя
- Аудит действий пользователя

## Запуск приложения

1. Склонируйте репозиторий на вашем компьютере.
2. Откройте проект в вашей любимой среде разработки.
3. Запустите Tomcat 10 версии для начала взаимодействия с приложением в консоли.

## API Endpoints

- `POST /auth/sign-up`: Регистрация нового пользователя.
```json
{
  "login": "login",
  "password": "password"
}
```
- `POST /auth/sign-in`: Авторизация пользователя.
```json
{
  "login": "login",
  "password": "password"
}
```

- `GET /meter-reading/history`: Получение всех показаний счетчиков.
- `GET /meter-reading/current`: Получение актуальных показаний счетчиков.
- `POST /meter-reading/submit`: Подача новых показаний счетчика.
```json
{
  "counterNumber": 192101,
  "meterTypeId": 1
}
```
- `GET /meter-reading/date`: Получение показаний счетчика по определенному сроку.
```json
{
  "month": 1,
  "year": 2024
}
```
- `GET /admin/all-users`: Получение всех зарегистрированных пользователей.
- `GET /admin/meter-type`: Добавление нового типа счетчика.
```json
{
  "name": "SOME_NAME"
}
```

## Связаться со мной

- fluman123@yandex.ru
- telegram: [@baydachok](https://t.me/baydachok)
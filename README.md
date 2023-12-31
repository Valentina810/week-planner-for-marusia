# Схема диалога

https://miro.com/app/board/uXjVMBAr0bg=/?share_link_id=84728232957

<img width="1041" alt="Снимок экрана 2023-10-10 в 21 29 15" src="https://github.com/Valentina810/week-planner-for-marusia/assets/83814517/2bf6457f-0112-4607-8e0f-6a2f8b71c447">

# Бэклог

- [X] Добавить обработку WeeklyPlan https://github.com/Valentina810/week-planner-for-marusia/pull/8
- [X] Добавить юнит-тесты на WeeklyPlan https://github.com/Valentina810/week-planner-for-marusia/pull/14
- [ ] В Enum добавить все экшены, каждому экшену сопоставить токены (слова во фразе)
- [X] Добавить обработку TodayPlan https://github.com/Valentina810/week-planner-for-marusia/pull/20
- [X] Добавить юнит-тесты на TodayPlan https://github.com/Valentina810/week-planner-for-marusia/pull/21
- [ ] Разделить логику работы с хранилищем: создание, если оно не заполнено и получение если оно заполнено
- [X] Добавить обработку TomorrowPlan https://github.com/Valentina810/week-planner-for-marusia/pull/27
- [X] Добавить юнит-тесты на TomorrowPlan https://github.com/Valentina810/week-planner-for-marusia/pull/29
- [ ] Добавить запрос события на день недели, например, фраза: "расскажи план на среду"
- [X] Добавить обработку Unknown https://github.com/Valentina810/week-planner-for-marusia/pull/32 (ранее уже было реализовано), отдельного обработчика нет, если не распознана входная команда, то в методе BasePhraseFactory возвращается UNKNOWN
- [X] Добавить юнит-тесты на Unknown https://github.com/Valentina810/week-planner-for-marusia/pull/33
- [X] Добавить обработку Exit https://github.com/Valentina810/week-planner-for-marusia/issues/34
- [X] Добавить юнит-тесты на Exit https://github.com/Valentina810/week-planner-for-marusia/issues/36
- [X] Добавить обработку Help https://github.com/Valentina810/week-planner-for-marusia/pull/40
- [X] Добавить юнит-тесты на Help https://github.com/Valentina810/week-planner-for-marusia/pull/43 🥳🥳🥳
- [ ] Добавить обработку AddEvent
- [ ] Добавить юнит-тесты на AddEvent
- [ ] Сквозные тесты на контроллер и сервис
- [ ] Добавить распознавание команд по токенам (по вхождению всех слов в ключевой фразе в любом порядке)
- [ ] При первом обращении к навыку (session.new: true и session.message_id: 0) выполнять обновление расписания: первый день недели - сегодня,
  остальные дни дозаполнить и добавить в конец, не затирая при этом уже добавленные события (если они есть)

# Фичи на развитие:

- [X] в автотесты добавить параметризацию (где 3 и более теста)
- [ ] добавить чтобы в session_state не затирались другие данные, если они там есть (нужно ли если будет перенос в бд?)
- [ ] добавить экшен "расскажи план на <любой день недели>"
- [ ] добавить получение данных порциями при озвучивании плана на неделю. Например, озвучить 3 события, сообщить "У вас
  на
  этой неделе ешё 5 событий, называть дальше?"
- [ ] добавить обработку команды с учетом вхождения: если в тексте от пользователя найдены все совпадающие во фразе
  токены (
  в любой последовательности) - то распознавать фразу как команду, игнорируя остальной текст
- [ ] добавить экшен DeleteEvent - удаление события
- [ ] перенести хранение событий в БД MongoDB (пример: в ответе приходит user_id и auth_token, продумать как хранить,
  как
  связать)
- [ ] добавить подробное логирование и сохранение логов в файл

# Поддержка Маруси
marusia-support@corp.mail.ru



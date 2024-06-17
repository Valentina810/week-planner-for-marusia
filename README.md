# Планировщик недели

[Документация](https://github.com/Valentina810/week-planner-for-marusia/blob/main/src/docs/index.md)

<img width="1041" alt="Снимок экрана 2023-10-10 в 21 29 15" src="https://github.com/Valentina810/week-planner-for-marusia/assets/83814517/2bf6457f-0112-4607-8e0f-6a2f8b71c447">

# Бэклог MVP

- [X] Добавить обработку WeeklyPlan https://github.com/Valentina810/week-planner-for-marusia/pull/8
- [X] Добавить юнит-тесты на WeeklyPlan https://github.com/Valentina810/week-planner-for-marusia/pull/14
- [X] В Enum добавить все экшены - сделано, каждому экшену сопоставить токены (слова во фразе) - сделано пока в файле
- [X] Добавить обработку TodayPlan https://github.com/Valentina810/week-planner-for-marusia/pull/20
- [X] Добавить юнит-тесты на TodayPlan https://github.com/Valentina810/week-planner-for-marusia/pull/21
- [X] Изменена логика по работе с хранилищем: если событий нет, не храним ключи без значений, то есть ситуация когда
  хранилище пустое - нормально. Но если не пустое, то нужно сортировать при получении данных по ключам - сначала ранние
  события
- [X] Добавить обработку TomorrowPlan https://github.com/Valentina810/week-planner-for-marusia/pull/27
- [X] Добавить юнит-тесты на TomorrowPlan https://github.com/Valentina810/week-planner-for-marusia/pull/29
- [X] Добавить обработку Unknown https://github.com/Valentina810/week-planner-for-marusia/pull/32 (ранее уже было
  реализовано), отдельного обработчика нет, если не распознана входная команда, то возвращается UNKNOWN
- [X] Добавить юнит-тесты на Unknown https://github.com/Valentina810/week-planner-for-marusia/pull/33
- [X] Добавить обработку Exit https://github.com/Valentina810/week-planner-for-marusia/issues/34
- [X] Добавить юнит-тесты на Exit https://github.com/Valentina810/week-planner-for-marusia/issues/36
- [X] Добавить обработку Help https://github.com/Valentina810/week-planner-for-marusia/pull/40
- [X] Добавить юнит-тесты на Help https://github.com/Valentina810/week-planner-for-marusia/pull/43 🥳🥳🥳
- [X] Добавить обработку AddEvent https://github.com/Valentina810/week-planner-for-marusia/pull/46 🤠
- [X] Добавить юнит-тесты на AddEvent https://github.com/Valentina810/week-planner-for-marusia/pull/48
- [X] Добавить подробное логирование и сохранение логов в
  файл https://github.com/Valentina810/week-planner-for-marusia/pull/56

После написания всех тестов :

- [ ] При получении неизвестной команды озвучивать список основных команд, если находимся внутри пункта - проговорить какого и как из него выйти
- [X] При первом обращении к навыку (session.new: true и session.message_id: 0) выполнять обновление расписания: все ключи с прошедшими датами удалить https://github.com/Valentina810/week-planner-for-marusia/pull/54
- [ ] Написать документацию 

# Фичи на развитие:

- [ ] добавить чтобы в session_state не затирались другие данные: перенести хранение событий в MongoDB (пример: в ответе
  приходит user_id - можно взять как ключ)
- [X] в автотесты добавить параметризацию (где 3 и более теста)
- [ ] добавить экшен "расскажи план на <любой день недели>", например, фраза: "расскажи план на среду"
- [ ] добавить получение данных порциями при озвучивании плана на неделю. Например, озвучить 3 события, сообщить "У вас
  на этой неделе ешё 5 событий, называть дальше?"
- [ ] добавить обработку команды с учетом вхождения: если в тексте от пользователя найдены все совпадающие во фразе
  токены (в любой последовательности) - то распознавать фразу как команду, игнорируя остальной текст - частично
  реализовано
- [ ] добавить экшен DeleteEvent - удаление события

# Поддержка Маруси

marusia-support@corp.mail.ru

# Опубликованные версии

| Дата       | Номер версии | Описание                                                                                                                                                                                                                                                                    | 
|------------|--------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 17.06.2024 | 1.0.2        | Изменено дерево команд: <br/> - добавлено приветственное сообщение,<br/> - команда справка упрощена (без дерева внутри).<br/> При выводе плана на неделю время всегда выводится после даты. <br/> Добавлена обработка служебной фразы on_interrupt - фраза выхода из скилла |
| 14.06.2024 | 1.0.1        | Первая тестовая версия                                                                                                                                                                                                                                                      |
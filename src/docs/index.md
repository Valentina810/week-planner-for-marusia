#  Навык "Планировщик недели"
Приложение представляет собой навык для голосового помощника Маруся, разработанного компанией VK.
Навык предназначен для составления плана дел на неделю.

Команды основного меню:

* план на неделю
* план на сегодня
* план на завтра
* добавь событие
* справка

###  Ответ от помощника представляет собой json вида:
```
{
  "meta": {
    "client_id": "MailRu-VC/1.0",
    "locale": "ru_RU",
    "timezone": "Europe/Moscow",
    "interfaces": {
      "screen": {}
    },
    "_city_ru": "Москва"
  },
  "request": {
    "command": "phrase",
    "original_utterance": "phrase",
    "type": "SimpleUtterance",
    "nlu": {
      "tokens": [
        "phrase"
      ],
      "entities": []
    }
  },
  "session": {
    "session_id": "cc6",
    "user_id": "878",
    "skill_id": "524",
    "new": false,
    "message_id": 8,
    "application": {
      "application_id": "8783",
      "application_type": "web"
    },
    "auth_token": "636",
    "user": {
      "user_id": "a45"
    }
  },
  "state": {
    "session": {},
    "user": {
      "week": {
        "days": {
          "01-11-2023": [
            {
              "time": "18:00",
              "name": "Лекция"
            },
            {
              "time": "16:00",
              "name": "Свидание"
            },
            {
              "time": "09:00",
              "name": "Завтрак"
            }
          ],
          "29-10-2023": [
            {
              "time": "10:00",
              "name": "Завтрак"
            },
            {
              "time": "23:00",
              "name": "Ужин"
            },
            {
              "time": "12:00",
              "name": "Обед"
            }
          ]
        }
      }
    }
  },
  "version": "1.0"
}
```

### Команда "План на неделю"
При выборе данной команды происходит чтение данных из поля ответа user.week, далее события сортируются по датам и разворачиваются в список событий по дням

### Команда "План на сегодня"
При выборе данной команды происходит чтение данных из поля ответа user.week, далее происходит поиск по сегодняшней дате, если найдены события - то возвращается их список, если событий нет, то сообщение "На сегодня событий не найдено"

### Команда "План на сегодня"
При выборе данной команды происходит чтение данных из поля ответа user.week, далее происходит поиск по текущей дате+1 день, если найдены события - то возвращается их список, если событий нет, то сообщение "На завтра событий не найдено"

### Команда "Добавь событие"
При выборе данной команды запускается цепочка команд-запросов для получения необходимых данных для добавления события:
[cхема](https://github.com/Valentina810/week-planner-for-marusia/blob/main/src/docs/picture/add_event.png)
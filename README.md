# О проекте "Современный словарь русского языка"

Проект представляет собой мобильное приложение к [словарному сервису](https://github.com/t-o-n-y-p/dictionary-kotlin), \
в котором словам русского языка соответствуют их значения.

Пользователи имеют возможность узнавать значения интересующих их слов, \
а также участвовать в пополнении словаря новыми словами и/или значениями.

Администратор сервиса имеет возможность отсматривать поступающие дополнения \
и принимать решение о включении их в состав словаря либо удалении.

# Технологии

- Проект реализован по Single Activity Application паттерну
- Навигация организована при помощи Navigation Component, Navigation View
- Используется архитектура MVVM на Android Architecure Components (ViewModel + LiveData)
- Для организации архитектуры используется DI библиотека Hilt
- Для асинхронных операций используется Kotlin Coroutines
- Для организации сетевого взаимодействия используется Retrofit
- Для сериализации/десериализации json используется Kotlin Serialization
- Для хранения данных используется SharedPreferences с шифрованием чувствительных данных

# UI и UX приложения

Главное меню реализовано при помощи [Navigation bar](https://m3.material.io/components/navigation-bar/overview) \
и содержит следующие пункты:
1. Поиск
2. Недавние
3. Входящие (только если залогинен пользователь с ролью администратора)
4. Профиль

### Поиск

На экране "Поиск" пользователь видит [Search](https://m3.material.io/components/search/guidelines), \
а после ввода в поле слова или части слова видит расположенный под ним [List](https://m3.material.io/components/lists/overview) \
с результатами поиска из словарного сервиса.

При нажатии на элемент списка открывается [Bottom sheet](https://m3.material.io/components/bottom-sheets/overview) \
с заголовком в виде самого слова и текстовым полем, в котором перечислены значения данного слова в порядке их добавления в словарь.
- Если пользователь залогинен и состоит в группе заблокированных, никакие кнопки не отображаются. 
- Если пользователь залогинен и не состоит в группе заблокированных, присутствует кнопка добавления значения к данному слову. 
- Если пользователь не залогинен, присутствует кнопка, которая предлагает залогиниться, чтобы добавить значение. \
После того, как пользователь залогинился, он переходит к добавлению слова, если не состоит в группе заблокированных.

С правой стороны [Search](https://m3.material.io/components/search/guidelines) также присутствует кнопка "+" \
для перехода к добавлению нового слова в словарь. \
Процесс аналогичен добавлению нового значения для уже существующего слова.

### Недавние

На экране "Недавние" пользователь видит [Top app bar](https://m3.material.io/components/top-app-bar/specs) \
и расположенный под ним [List](https://m3.material.io/components/lists/overview) \
со списком последних просмотренных на данном устройстве слов.

При нажатии на элемент списка открывается [Bottom sheet](https://m3.material.io/components/bottom-sheets/overview) \
с аналогичным функционалом из сценария экрана "Поиск".

### Входящие

На экране "Входящие" пользователь видит [Top app bar](https://m3.material.io/components/top-app-bar/specs) \
и расположенный под ним [List](https://m3.material.io/components/lists/overview) \
со списком предложенных пользователями слов и/или значений, ожидающих рассмотрения.
Каждый элемент списка имеет заголовок в виде самого слова и подзаголовок в виде одной строки предлагаемого значения, \
обрезанный многоточием в том случае, если вся строка целиком не помещается.

При нажатии на элемент списка открывается [Bottom sheet](https://m3.material.io/components/bottom-sheets/overview) \
с заголовком в виде самого слова и текстовым полем, в котором указано предлагаемое значение.
Присутствуют также две кнопки: принять и отклонить значение.

### Профиль

На экране "Профиль" незалогиненный пользователь видит [Top app bar](https://m3.material.io/components/top-app-bar/specs) \
и расположенный под ним блок входа в профиль пользователя с полями ввода логина, пароля, кнопкой входа. \
После выполнения входа он заменяется на блок информации о пользователе: имя, роли, кнопка выхода.

Залогиненный пользователь при входе на экран "Профиль" видит сразу блок информации о себе.
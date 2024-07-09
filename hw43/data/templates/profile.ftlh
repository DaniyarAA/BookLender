<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Профиль пользователя</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h1>Профиль пользователя</h1>
    <p><strong>Идентификатор:</strong> ${employee.identifier}</p>
    <p><strong>Имя:</strong> ${employee.firstName}</p>
    <p><strong>Фамилия:</strong> ${employee.lastName}</p>
    <a href="/books" class="btn btn-primary">Список книг</a>
    <a href="/borrow" class="btn btn-primary">Взять книгу</a>
    <a href="/return" class="btn btn-primary">Вернуть книгу</a>
    <a href="/logout" class="btn btn-secondary">Выйти</a>
</div>
</body>
</html>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Информация о сотруднике</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h1>Информация о сотруднике</h1>
    <p><strong>ID:</strong> ${employee.id}</p>
    <p><strong>Имя:</strong> ${employee.firstName}</p>
    <p><strong>Фамилия:</strong> ${employee.lastName}</p>

    <h2>Текущие книги</h2>
    <ul>
        <#list employee.currentBooks as bookId>
            <#assign book = books?filter(b -> b.id == bookId)?first>
            <li>${book.title} (${book.author})</li>
        </#list>
    </ul>

    <h2>Прошедшие книги</h2>
    <ul>
        <#list employee.historyBooks as bookId>
            <#assign book = books?filter(b -> b.id == bookId)?first>
            <li>${book.title} (${book.author})</li>
        </#list>
    </ul>

    <a href="/employees" class="btn btn-secondary">Назад к списку сотрудников</a>
</div>
</body>
</html>

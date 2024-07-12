<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Список книг</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="d-flex justify-content-between align-items-center mt-3 mb-4">
        <h1>Список книг</h1>
        <div>
            <a href="/employees" class="btn btn-info mr-2">Список сотрудников</a>
            <a href="/profile" class="btn btn-info">Мой профиль</a>
        </div>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Название</th>
            <th>Автор</th>
            <th>Статус</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <#list books as book>
            <tr>
                <td>${book.id}</td>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td><#if book.borrowed>Выдана<#else>В наличии</#if></td>
                <td><a href="/book?id=${book.id}" class="btn btn-info">Просмотр</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>

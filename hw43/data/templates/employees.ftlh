<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Список сотрудников</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="d-flex justify-content-between align-items-center mt-3 mb-4">
        <h1>Список сотрудников</h1>
        <div>
            <a href="/books" class="btn btn-info mr-2">Список книг</a>
            <a href="/profile" class="btn btn-info">Мой профиль</a>
        </div>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Фамилия</th>
            <th>Текущие книги</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <#list employees as employee>
            <tr>
                <td>${employee.id}</td>
                <td>${employee.firstName}</td>
                <td>${employee.lastName}</td>
                <td>
                    <#list employee.currentBooks as bookId>
                        <#assign book = books?filter(b -> b.id == bookId)?first>
                        <span class="badge badge-info">${book.title}</span><br>
                    </#list>
                </td>
                <td><a href="/employee?id=${employee.id}" class="btn btn-info">Просмотр</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>

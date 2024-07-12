<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Профиль пользователя</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .profile-header {
            margin-bottom: 30px;
        }
        .card {
            margin-bottom: 20px;
        }
        .card-header {
            background-color: #007bff;
            color: white;
        }
        .btn-custom {
            margin-right: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="profile-header text-center">
        <h1 class="my-4">Профиль пользователя</h1>
        <p><strong>Идентификатор:</strong> ${employee.identifier}</p>
        <p><strong>Имя:</strong> ${employee.firstName}</p>
        <p><strong>Фамилия:</strong> ${employee.lastName}</p>
        <a href="/books" class="btn btn-primary btn-custom">Список книг</a>
        <a href="/borrow" class="btn btn-primary btn-custom">Взять книгу</a>
        <a href="/return" class="btn btn-primary btn-custom">Вернуть книгу</a>
        <a href="/logout" class="btn btn-secondary">Выйти</a>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    Текущие книги
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <#list employee.currentBooks as bookId>
                            <#assign book = books?filter(b -> b.id == bookId)?first>
                            <li class="list-group-item">${book.title} (${book.author})</li>
                        </#list>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    Прошлые книги
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <#list employee.historyBooks as bookId>
                            <#assign book = books?filter(b -> b.id == bookId)?first>
                            <li class="list-group-item">${book.title} (${book.author})</li>
                        </#list>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

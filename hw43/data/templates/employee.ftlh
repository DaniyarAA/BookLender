<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Информация о сотруднике</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            margin-bottom: 20px;
        }
        .card-header {
            background-color: #007bff;
            color: white;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Информация о сотруднике</h1>
    <p><strong>ID:</strong> ${employee.id}</p>
    <p><strong>Имя:</strong> ${employee.firstName}</p>
    <p><strong>Фамилия:</strong> ${employee.lastName}</p>
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
    <a href="/employees" class="btn btn-info mt-3">Назад к списку сотрудников</a>
</div>
</body>
</html>

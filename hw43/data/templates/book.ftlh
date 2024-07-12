<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Информация о книге</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .fixed-size-img {
            width: 50%;
            height: 50%;
        }
        .card-custom {
            margin-top: 20px;
        }
        .card-text {
            font-size: 1.2rem;
        }
        .btn-custom {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card card-custom">
                <div class="card-header text-center">
                    <h1>${book.title}</h1>
                </div>
                <div class="card-body">
                    <div class="text-center">
                        <img src="/images/${book.image}" alt="${book.title}" class="img-thumbnail fixed-size-img">
                    </div>
                    <p class="card-text mt-3"><strong>Автор:</strong> ${book.author}</p>
                    <p class="card-text"><strong>Статус:</strong> <#if book.borrowed>Выдана<#else>В наличии</#if></p>
                    <#if book.borrowed>
                        <p class="card-text"><strong>Выдана сотруднику:</strong> ${book.borrowedBy}</p>
                    <#else>
                        <form action="/borrow" method="post">
                            <input type="hidden" name="bookId" value="${book.id}">
                            <button type="submit" class="btn btn-primary btn-custom">Взять книгу</button>
                        </form>
                    </#if>
                    <a href="/books" class="btn btn-info btn-custom">Назад к списку книг</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

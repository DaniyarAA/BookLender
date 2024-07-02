<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Информация о книге</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h1>${book.title}</h1>
    <p><strong>Автор:</strong> ${book.author}</p>
    <p><strong>Статус:</strong> <#if book.borrowed>Выдана<#else>В наличии</#if></p>
    <img src="/images/${book.image}" alt="${book.title}" class="img-thumbnail">
    <#if book.borrowed>
        <p><strong>Выдана сотруднику ID:</strong> ${book.borrowedBy}</p>
    </#if>
    <a href="/books" class="btn btn-secondary">Назад к списку книг</a>
</div>
</body>
</html>

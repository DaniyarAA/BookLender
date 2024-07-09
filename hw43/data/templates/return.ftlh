<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Возврат книги</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h1>Возврат книги</h1>
    <form action="/return" method="post">
        <div class="form-group">
            <label for="bookId">Выберите книгу</label>
            <select class="form-control" id="bookId" name="bookId" required>
                <#list books as book>
                    <option value="${book.id}">${book.title}</option>
                </#list>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Вернуть книгу</button>
    </form>
</div>
</body>
</html>

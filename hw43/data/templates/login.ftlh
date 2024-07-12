<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Вход в систему</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../css/forms.css">
</head>
<body>
<div class="container">
    <main>
        <form action="/login" method="post">
            <fieldset>
                <div class="legend">
                    <p>Welcome!</p>
                    <img src="/images/1.jpg" alt="coins" style="display:block; width: 200px; height: 200px;">
                </div>
                <div class="form-group">
                    <label for="identifier">Идентификатор (email)</label>
                    <input type="text" class="form-control" id="identifier" name="identifier" placeholder="your email" required autofocus>
                </div>
                <div class="form-group">
                    <label for="password">Пароль</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="your password" required>
                </div>
                <div class="form-element">
                    <button class="register-button" type="submit">Вход</button>
                </div>
                <div class="form-element">
                    <button class="register-button" onclick="window.location.href='/register'">Регистрация</button>
                </div>
            </fieldset>
        </form>
    </main>
</div>
</body>
</html>

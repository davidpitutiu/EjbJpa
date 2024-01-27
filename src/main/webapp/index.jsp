<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<head>
    <title>Login Page</title>
</head>
<body>
<h1>Login</h1>
<form action="login" method="post">
    <label for="email">Email:</label>
    <input type="text" id="email" name="email" required><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br>

    <input type="submit" value="Login">
</form>

<p>Don't have an account? <a href="signup.jsp">Sign up</a></p>
</body>
</html>
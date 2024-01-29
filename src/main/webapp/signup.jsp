<!DOCTYPE html>
<html>
<head>
    <title>Sign Up</title>
    <script>
        function validatePassword() {
            var password = document.getElementById("password").value;
            var confirmPassword = document.getElementById("cpassword").value;
            var uppercaseRegex = /[A-Z]/;
            var numberRegex = /[0-9]/;

            if (password.length < 8) {
                alert("Password must be at least 8 characters long.");
                return false;
            }

            if (!uppercaseRegex.test(password)) {
                alert("Password must contain at least one uppercase letter.");
                return false;
            }

            if (!numberRegex.test(password)) {
                alert("Password must contain at least one number.");
                return false;
            }

            if (password !== confirmPassword) {
                alert("Passwords do not match.");
                return false;
            }

            return true;
        }
    </script>
</head>
<body>
<h1>Sign Up</h1>
<form action="signup" method="post" onsubmit="return validatePassword()">
    <label for="email">Email:</label>
    <input type="text" id="email" name="email" required><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br>

    <label for="cpassword">Confirm Password:</label>
    <input type="password" id="cpassword" name="cpassword" required><br>

    <input type="submit" value="Sign Up">
</form>

<p>Already have an account? <a href="index.jsp">Login</a></p>
</body>
</html>

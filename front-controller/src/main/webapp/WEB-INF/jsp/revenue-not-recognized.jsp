<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Recognize revenue</title>
</head>
<body>
    <p>Revenue has NOT been recognized for a contract with id:</p>
    <h2>${param.get("contract-id")}</h2>
    <p>Error occurred:</p>
    <h2>${requestScope.get("error-message")}</h2>
</body>
</html>
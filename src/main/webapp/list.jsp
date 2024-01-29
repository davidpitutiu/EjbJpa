<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>User List</title>
</head>
<body>
<h1>User List</h1>

<%
  String userId = request.getParameter("user_id");
%>

<p>Welcome, User ID: <%= userId %></p>

<p>User Items:</p>
<ul>
  <c:forEach items="${userItems}" var="item">
    <li>
        ${item}
      <form action="list" method="post" style="display: inline;">
        <input type="hidden" name="user_id" value="<%= userId %>">
        <input type="hidden" name="deleteItemId" value="${item}">
        <input type="submit" value="X" style="color: red;">
      </form>
    </li>
  </c:forEach>
</ul>

<form action="list" method="post">
  <label for="newItem">Add Item:</label>
  <input type="hidden" name="user_id" value="<%= userId %>">
  <input type="text" id="newItem" name="newItem" required>
  <input type="submit" name="addItem" value="Add">
</form>

<p><a href="logout.jsp">Logout</a></p>
</body>
</html>

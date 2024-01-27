<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://xmlns.jcp.org/jsf/html" prefix="h" %>
<%@ taglib uri="http://xmlns.jcp.org/jsf/core" prefix="f" %>
s
<f:view>
    <f:metadata>
        <f:viewParam name="email" value="#{signupBean.email}" />
        <f:viewParam name="password" value="#{signupBean.password}" />
    </f:metadata>
</f:view>

<!DOCTYPE html>
<html>
<head>
    <title>Sign Up Page</title>
</head>
<body>
<h1>Sign Up</h1>
<h:form id="signupForm">
    <h:outputLabel for="email">Email:</h:outputLabel>
    <h:inputText id="email" value="#{signupBean.email}" required="true" />
    <br/>

    <h:outputLabel for="password">Password:</h:outputLabel>
    <h:inputSecret id="password" value="#{signupBean.password}" required="true" />
    <br/>

    <h:commandButton value="Sign Up" action="#{signupBean.register}" />
</h:form>
</body>
</html>

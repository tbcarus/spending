<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:useBean id="user" class="ru.spending.model.User" scope="request"/>
    <title>User ${user.name}</title>
</head>
<body>
<section>
    <form method="post" action="settings" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${user.uuid}">
        <input type="hidden" name="email" value="${user.email}">
        <table border="1">
            <tr>
                <td>
                    Имя:
                </td>
                <td>
                    <input type="text" name="name" value="${user.name}">
                </td>
            </tr>
            <tr>
                <td>
                    Начальный день месяца:
                </td>
                <td>
                    <select name="start_day">
                        <option value="${user.startPeriodDate.dayOfMonth}" selected
                                hidden>${user.startPeriodDate.dayOfMonth}</option>
                        <c:forEach var="i" begin="1" end="28">
                            <option value="${i}">${i}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    Старый пароль:
                </td>
                <td>
                    <input type="password" name="old_pass">
                </td>
            </tr>
            <tr>
                <td>
                    Новый пароль:
                </td>
                <td>
                    <input type="password" name="new_pass">
                </td>
            </tr>
            <tr>
                <td>
                    Повторить пароль:
                </td>
                <td>
                    <input type="password" name="new_pass_reply">
                </td>
            </tr>
        </table>
        <button type="submit">Сохранить</button>
        <input type="button" value=" Отменить " onclick="history.go(-1);"/>
    </form>
</section>

</body>
</html>

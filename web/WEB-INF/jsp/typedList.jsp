<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.spending.model.PaymentType" %>
<%@ page import="ru.spending.util.DateUtil" %>
<%@ page import="java.time.Month" %>
<%@ page import="java.time.Year" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:useBean id="user" class="ru.spending.model.User" scope="request"/>
    <jsp:useBean id="payment" class="ru.spending.model.Payment" scope="request"/>
    <jsp:useBean id="paymentType" type="ru.spending.model.PaymentType" scope="request"/>
    <jsp:useBean id="list" type="java.util.List<ru.spending.model.Payment>" scope="request"/>
    <title>${paymentType.title}-${user.name}</title>
</head>
<body>
<section>
    <table border="1">
        <tr>
            <th colspan="5">${paymentType.title}</th>
        </tr>
        <tr>
            <th colspan="5">${sum}</th>
        </tr>
        <tr>
            <td>Стоимость</td>
            <td>Описание</td>
            <td>Дата</td>
            <td>Имя пользователя</td>
            <td></td>
        </tr>
        <c:forEach var="payment" items="${list}">
            <tr>
                <td>${payment.prise}</td>
                <td>${payment.description}</td>
                <td>${payment.date.format(DateUtil.DTFORMATTER_DATE_ONLY_RU)}</td>
                <td>${user.name}</td>
                <td>
                    <a href="typedList?type=${paymentType.name()}&email=${user.email}&id=${payment.id}&action=delete">
                    <img src="../img/delete.png">Удалить<img src="../img/delete.png">
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <input type="button" value="Назад" onclick="window.location.href = '../spending'"/>
</section>

</body>
</html>

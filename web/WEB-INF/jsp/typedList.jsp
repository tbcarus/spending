<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="ru.spending.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/w3.css">
    <link rel="stylesheet" href="../css/w3-colors-win8.css">
    <link rel="stylesheet" href="../css/w3-colors-metro.css">
    <jsp:useBean id="user" class="ru.spending.model.User" scope="request"/>
    <jsp:useBean id="payment" class="ru.spending.model.Payment" scope="request"/>
    <jsp:useBean id="paymentType" type="ru.spending.model.PaymentType" scope="request"/>
    <jsp:useBean id="list" type="java.util.List<ru.spending.model.Payment>" scope="request"/>
    <title>${paymentType.title}-${user.name}</title>
</head>
<body class="w3-metro-light-blue">
<div class="w3-display-topmiddle">
    <section>
        <table class="w3-table w3-striped w3-bordered w3-centered w3-card-4 w3-small">
            <tr>
                <th colspan="5">${paymentType.title}</th>
            </tr>
            <tr>
                <th colspan="5">${sum}</th>
            </tr>
            <tr>
                <td style="vertical-align: middle">Стоимость</td>
                <td style="vertical-align: middle">Описание</td>
                <td style="vertical-align: middle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Дата&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td style="vertical-align: middle">Имя пользователя</td>
                <td style="vertical-align: middle"></td>
            </tr>
            <c:forEach var="payment" items="${list}">
                <tr>
                    <td style="vertical-align: middle">
<%--                            ${payment.prise}--%>
                        <input type="button" onclick="window.location.href =
                                'edit?id=${payment.id}&action=edit'"
                               value="${payment.prise}"
                               class="w3-button w3-amber w3-hover-light-green w3-round-large"/>
                    </td>
                    <td style="vertical-align: middle">${payment.description}</td>
                    <td style="vertical-align: middle">${payment.date.format(DateUtil.DTFORMATTER_DATE_ONLY_RU)}</td>
                    <td style="vertical-align: middle">${user.name}</td>
                    <td style="vertical-align: middle">
                        <input type="button" onclick="window.location.href =
                                'typedList?type=${paymentType.name()}&email=${user.email}&id=${payment.id}&action=delete'"
                               value="Удалить запись"
                               class="w3-button w3-red w3-hover-deep-purple w3-round-large"/>
                    </td>
                </tr>
            </c:forEach>
        <tr>
            <td colspan="5">
                <input type="button" value="Назад" onclick="window.location.href = '../spending'"
                       class="w3-button w3-light-green w3-hover-amber w3-round-large">
            </td>
        </tr>
        </table>
    </section>
</div>
</body>
</html>

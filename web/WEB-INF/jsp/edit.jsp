<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.spending.model.PaymentType" %>
<%@ page import="ru.spending.util.DateUtil" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="payment" class="ru.spending.model.Payment" scope="request"/>
    <title>Запись ${payment.id}</title>
</head>
<body>
<%--<jsp:include page="fragments/header.jsp"/>--%>
<section>
    <form method="post" action="spending" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="post_type" value="edit">
        <input type="hidden" name="uuid" value="${payment.id}">
        <table border="1">
            <tr>
                <td>
                    Тип:
                </td>
                <td>
                    <select name="payment_type">
                        <option value="${payment.type.name()}" selected hidden>${payment.type.title}</option>
                        <option value="${PaymentType.DINNER.name()}">${PaymentType.DINNER.title}</option>
                        <option value="${PaymentType.CAR.name()}">${PaymentType.CAR.title}</option>
                        <option value="${PaymentType.GAS.name()}">${PaymentType.GAS.title}</option>
                        <option value="${PaymentType.FOOD.name()}">${PaymentType.FOOD.title}</option>
                        <option value="${PaymentType.TRANSIT.name()}">${PaymentType.TRANSIT.title}</option>
                        <option value="${PaymentType.ENTERTAINMENT.name()}">${PaymentType.ENTERTAINMENT.title}</option>
                        <option value="${PaymentType.CLOTH.name()}">${PaymentType.CLOTH.title}</option>
                        <option value="${PaymentType.PHARMACY.name()}">${PaymentType.PHARMACY.title}</option>
                        <option value="${PaymentType.CHILDREN.name()}">${PaymentType.CHILDREN.title}</option>
                        <option value="${PaymentType.OTHER.name()}">${PaymentType.OTHER.title}</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    Сумма:
                </td>
                <td>
                    <input type="text" name="prise" value="${payment.prise}">
                </td>
            </tr>
            <tr>
                <td>
                    Описание:
                </td>
                <td>
                    <input type="text" name="description" value="${payment.description}">
                </td>
            </tr>
            <tr>
                <td>
                    Дата:
                </td>
                <td>
                    <input type="date" name="date" value="${DateUtil.FORMATTER.format(payment.date)}">
                </td>
            </tr>
        </table>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<%--<jsp:include page="fragments/footer.jsp"/>--%>
</body>
</html>
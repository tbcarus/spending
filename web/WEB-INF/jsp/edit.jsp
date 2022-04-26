<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.spending.model.PaymentType" %>
<%@ page import="ru.spending.util.DateUtil" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/w3.css">
    <link rel="stylesheet" href="../css/w3-colors-win8.css">
    <link rel="stylesheet" href="../css/w3-colors-metro.css">
    <jsp:useBean id="payment" class="ru.spending.model.Payment" scope="request"/>
    <title>Запись ${payment.id}</title>
</head>
<body class="w3-metro-light-blue">
<section>
    <form method="post" action="edit" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="post_type" value="edit">
        <input type="hidden" name="id" value="${payment.id}">
        <div class="w3-display-container w3-display-topmiddle">
            <table class="w3-table w3-striped w3-bordered w3-card-4 w3-small">
                <c:if test="${!payment.id.equals('new')}">
                    <tr>
                        <td colspan="2" class="w3-center">
                            <input type="button" onclick="window.location.href = 'edit?id=${payment.id}&action=delete'"
                                   value="Удалить запись"
                                   class="w3-button w3-red w3-hover-deep-purple w3-round-large"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td style="vertical-align: middle; text-align: right">
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
                    <td style="vertical-align: middle; text-align: right">
                            Сумма:
                    </td>
                    <td>
                        <input type="text" name="prise" value="${payment.prise}"
                               class="w3-input w3-border w3-round-large" style="width: 70px">
                    </td>
                </tr>
                <tr>
                    <td style="vertical-align: middle; text-align: right">
                            Описание:
                    </td>
                    <td>
                        <input type="text" name="description" value="${payment.description}"
                               class="w3-input w3-border w3-round-large" style="width: 150px">
                    </td>
                </tr>
                <tr>
                    <td style="vertical-align: middle; text-align: right">
                            Дата:
                    </td>
                    <td>
                        <input type="date" name="date" value="${payment.date}">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="w3-center">
                        <button type="submit"
                                class="w3-button w3-light-green w3-hover-amber w3-round-large">Сохранить
                        </button>
                        <input type="button" value=" Отменить " onclick="window.location.href='../spending'"
                               class="w3-button w3-light-green w3-hover-amber w3-round-large">
                        <%--                    <c:if test="${!payment.id.equals('new')}">--%>
                        <%--                    <input type="button" value="Удалить запись"--%>
                        <%--                           onclick="window.location.href = 'edit?id=${payment.id}&action=delete'"--%>
                        <%--                           class="w3-button w3-red w3-hover-deep-purple w3-round-large">--%>
                        <%--                    </c:if>--%>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</section>
<%--<jsp:include page="fragments/footer.jsp"/>--%>
</body>
</html>
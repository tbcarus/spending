<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.spending.model.PaymentType" %>
<%@ page import="ru.spending.model.User" %>
<%@ page import="ru.spending.util.DateUtil" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список затрат</title>
</head>
<body>

<section>
    <form method="post" action="spending" enctype="application/x-www-form-urlencoded">
        <table border="1" cellpadding="1" cellspacing="1">
            <tr>
                <td colspan="7">
                    <button type="submit">Сохранить</button>
                </td>
                <td colspan="7" align="right">
                    <c:set var="user" value="${user}"/>
                    <jsp:useBean id="user" class="ru.spending.model.User"/>
                    <b><a href="spending?uuid=${user.uuid}&action=settings">${user.name}</a></b>
                </td>
            </tr>
            <input type="hidden" name="post_type" value="list">
            <c:set var="map" value="${map}"/>
            <c:set var="delta" value="2"/>
            <jsp:useBean id="map"
                         type="java.util.Map<ru.spending.model.PaymentType, java.util.List<ru.spending.model.Payment>>"/>

            <%--            Вывод суммы всех затрат --%>
            <tr>
                <td colspan="14">
                    <b>Всего: ${sumAll}</b>
                </td>
            </tr>

            <%--            Вывод названий колонок, сумм затрат по типам и ячеек для добавления затрат--%>
            <c:set var="sumMapByType" value="${sumMapByType}"/>
            <jsp:useBean id="sumMapByType"
                         type="java.util.Map<ru.spending.model.PaymentType, java.lang.Integer>"/>
            <c:forEach var="i" begin="0" end="3" step="1">
                <tr>
                    <c:forEach var="pt1" items="${PaymentType.values()}">
                        <jsp:useBean id="pt1" type="ru.spending.model.PaymentType"/>
                        <c:choose>
                            <c:when test="${i == 0}">
                                <th>${pt1.title}</th>
                                <c:if test="${pt1 == PaymentType.CAR || pt1 == PaymentType.ENTERTAINMENT ||
                                                                    pt1 == PaymentType.CHILDREN || pt1 == PaymentType.OTHER}">
                                    <th>Описание</th>
                                </c:if>
                            </c:when>
                            <c:when test="${i == 1}">
                                <td align="center">
                                        ${sumMapByType.get(pt1).toString()}
                                </td>
                                <c:if test="${pt1 == PaymentType.CAR || pt1 == PaymentType.ENTERTAINMENT ||
                                                                    pt1 == PaymentType.CHILDREN || pt1 == PaymentType.OTHER}">
                                    <td></td>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <td>
                                    <input type="text" name="${pt1.name()}" size="5">
                                </td>
                                <c:if test="${pt1 == PaymentType.CAR || pt1 == PaymentType.ENTERTAINMENT ||
                                                                    pt1 == PaymentType.CHILDREN || pt1 == PaymentType.OTHER}">
                                    <td>
                                        <input type="text" name="${pt1.name()}description" size="10">
                                    </td>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tr>
            </c:forEach>

            <%--            Вывод записей затрат в таблицу--%>
            <c:forEach var="i" begin="0" end="${maxSize-1}" step="1">
                <tr>
                    <c:forEach var="pt" items="${PaymentType.values()}">
                        <jsp:useBean id="pt" type="ru.spending.model.PaymentType"/>
                        <c:set var="payment_record" value="${map.get(pt)[i]}"/>
                        <jsp:useBean id="payment_record" class="ru.spending.model.Payment" scope="request"/>
                        <td>
                            <c:if test="${i < map.get(pt).size()}">
                                <a href="spending?uuid=${payment_record.id}&action=edit">${payment_record.prise}</a>
                                <a href="spending?uuid=${payment_record.id}&action=delete"><img
                                        src="img/delete.png"></a>
                            </c:if>
                        </td>
                        <c:if test="${pt == PaymentType.CAR || pt == PaymentType.ENTERTAINMENT ||
                            pt == PaymentType.CHILDREN || pt == PaymentType.OTHER}">
                            <td>
                                <c:if test="${i < map.get(pt).size()}">
                                    ${map.get(pt)[i].description}
                                </c:if>
                            </td>
                        </c:if>
                    </c:forEach>
                </tr>
            </c:forEach>

        </table>
        <a href="spending?action=create">Add new payment <img src="img/add.png"></a><br>
        <a href="spending?action=refill">RefillDB</a>
        ${maxSize}
    </form>

    <form method="post" action="spending" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="post_type" value="start_date_change">
        <BR>
        <input type="date" name="start_date" value="${DateUtil.startDatePeriod()}">
        <br>
        <input type="checkbox" name="showToNow"> Показать записи до текущей даты
        <br>
        <input type="checkbox" name="showAll"> Показать записи за всё время
        <br>
        <button type="submit">Изменить период</button>
    </form>

    <hr>
    test data section <br>
    <input type="text" name="qwer" value="${DateUtil.NOW}">
    <input type="text" name="qwer" value="${DateUtil.startDatePeriod()}">
    <input type="text" name="qwer" value="${DateUtil.endDatePeriod()}">
    <input type="text" name="qwer" value="${DateUtil.customStartDatePeriod}">

</section>
</body>
</html>

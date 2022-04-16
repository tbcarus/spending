<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.spending.model.PaymentType" %>
<%@ page import="ru.spending.util.DateUtil" %>
<%@ page import="java.time.Month" %>
<%@ page import="java.time.Year" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список затрат</title>
</head>
<body>

<section>

    <form method="post" action="spending" enctype="application/x-www-form-urlencoded">

        <input type="hidden" name="post_type" value="list">
        <input type="hidden" name="uuid" value="${user.uuid}">
        <input type="hidden" name="email" value="${user.email}">
        <table border="1" cellpadding="1" cellspacing="1">
            <tr>
                <td colspan="3">
                    <button type="submit">Сохранить / Обновить</button>
                </td>
                <td colspan="9" align="center">
                    <input type="date" name="chosen_date" value="${DateUtil.NOW.format(DateUtil.DTFORMATTER_DATE_ONLY)}">
                    <input type="checkbox" name="at_this_date_checkbox">Добавить записи на указанную дату
                </td>
                <td colspan="2" align="right">
                    <c:set var="user" value="${user}"/>
                    <b><a href="spending/settings?uuid=${user.uuid}">${user.name}</a></b>
                </td>
            </tr>
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
                                <th><a href="spending/typedList?type=${pt1.name()}&email=${user.email}">${pt1.title}</a></th>
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
            <c:if test="${maxSize > 0}">
                <c:forEach var="i" begin="0" end="${maxSize-1}" step="1">
                    <tr>
                        <c:forEach var="pt" items="${PaymentType.values()}">
                            <jsp:useBean id="pt" type="ru.spending.model.PaymentType"/>
                            <c:set var="payment_record" value="${map.get(pt)[i]}"/>
                            <jsp:useBean id="payment_record" class="ru.spending.model.Payment" scope="request"/>
                            <td align="center">
                                <c:if test="${i < map.get(pt).size()}">
                                    <a href="spending/edit?id=${payment_record.id}&action=edit">${payment_record.prise}</a>
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
            </c:if>

        </table>
        <a href="spending/edit?action=create">Add new payment <img src="img/add.png"></a><br>
        <input type="button" onclick="window.location.href = 'spending?view=toCurrentDate';"
               value="Показать записи до сегодняшней даты"/>
        <input type="button" onclick="window.location.href = 'spending?view=allTime';"
               value="Показать записи за всё время"/>
        <input type="button" onclick="window.location.href = 'spending?view=allUsersPayments';"
               value="Показать записи всех пользователей"/>
        <br>
    </form>
    <hr>
    <form method="post" action="spending" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="post_type" value="start_date_change">
        <input type="hidden" name="uuid" value="${user.uuid}">
        <input type="hidden" name="email" value="${user.email}">
        <select name="start_day">
            <option value="${user.startPeriodDate.dayOfMonth}" selected
                    hidden>${user.startPeriodDate.dayOfMonth}</option>
            <c:forEach var="i" begin="1" end="28">
                <option value="${i}">${i}</option>
            </c:forEach>
        </select>
        <select name="start_month">
            <option value="${user.startPeriodDate.month.value}" selected
                    hidden>${user.startPeriodDate.month.name()}</option>
            <c:forEach var="i" begin="1" end="12">
                <option value="${i}">${Month.of(i).name()}</option>
            </c:forEach>
        </select>
        <select name="start_year">
            <option value="${user.startPeriodDate.year}" selected hidden>${user.startPeriodDate.year}</option>
            <c:forEach var="i" begin="2010" end="${Year.now().getValue()}">
                <option value="${i}">${i}</option>
            </c:forEach>
        </select>
        <br>
        <button type="submit">Изменить начальную дату</button>
    </form>
    <hr>
    Test data section
    <br>
    <a href="spending?action=refill">RefillDB</a>
    ${maxSize}
    <br>
    <input value="${user.uuid}">
    <input value="${user.getStartPeriodDate()}">
    <input value="${user.getEndPeriodDate()}">
    <br>

</section>
</body>
</html>

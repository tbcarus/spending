<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.spending.model.PaymentType" %>
<%@ page import="ru.spending.util.DateUtil" %>
<%@ page import="java.time.Month" %>
<%@ page import="java.time.Year" %>
<%@ page import="java.time.format.TextStyle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Locale.Category" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/w3.css">
    <link rel="stylesheet" href="css/w3-colors-win8.css">
    <link rel="stylesheet" href="css/w3-colors-metro.css">
    <title>Список затрат</title>
</head>
<body class="w3-metro-light-blue">
<jsp:useBean id="user" scope="request" type="ru.spending.model.User"/>
<c:set var="user" value="${user}"/>
<section>
    <div <%--class="w3-display-topmiddle"--%>>
        <form method="post" action="spending" enctype="application/x-www-form-urlencoded" class="w3-container">
            <input type="hidden" name="post_type" value="list">
            <input type="hidden" name="uuid" value="${user.uuid}">
            <input type="hidden" name="email" value="${user.email}">
            <div <%--class="w3-display-container"--%>>
                <table class="w3-table w3-striped w3-bordered w3-centered w3-card-4 w3-small"
                       style="width: 50%; height: 30%; margin: 0 auto">
                    <tr class="w3-deep-orange">
                        <td colspan="2">

                        </td>
                        <c:choose>
                            <c:when test="${user.startPeriodDate.isBefore(DateUtil.getLocalDateNow()) &&
                                user.startPeriodDate.plusMonths(1).isAfter(DateUtil.getLocalDateNow())}">
                                <td colspan="10" class="w3-deep-orange">
                                    Период:
                                        ${user.startPeriodDate.format(DateUtil.DTFORMATTER_DATE_ONLY_RU)} -
                                        ${user.getEndPeriodDate().format(DateUtil.DTFORMATTER_DATE_ONLY_RU)}
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td colspan="10" class="w3-purple">
                                    Выбранная начальная дата вне текущего месяца
                                </td>
                            </c:otherwise>
                        </c:choose>

                        <td colspan="2">
                            <div class="w3-right">
                                <b><a href="spending/settings?uuid=${user.uuid}">${user.name}</a></b>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="vertical-align: middle">
                            <div class="w3-left">
                                <input type="button" onclick="window.location.href = 'spending'"
                                       value="Обновить"
                                       class="w3-button w3-light-green w3-hover-amber w3-round-large"
                                />
                            </div>
                        </td>
                        <td colspan="10" style="vertical-align: middle">
                            <div class="w3-center">
                                <button type="submit"
                                        class="w3-button w3-block w3-light-green w3-hover-amber w3-round-large">
                                    Сохранить
                                </button>
                            </div>
                        </td>
                        <td colspan="2" class="">
                            <div class="w3-right">
                                <input type="button" onclick="window.location.href = 'spending/edit?action=create'"
                                       value="Добавить новую запись"
                                       class="w3-button w3-light-green w3-hover-amber w3-round-large"/>
                            </div>
                        </td>
                    </tr>
                    <c:set var="map" value="${map}"/>
                    <c:set var="delta" value="2"/>
                    <jsp:useBean id="map"
                                 type="java.util.Map<ru.spending.model.PaymentType, java.util.List<ru.spending.model.Payment>>"/>

                    <%--            Вывод суммы всех затрат --%>
                    <tr class="w3-text-orange w3-blue-gray">
                        <td colspan="2" style="vertical-align: middle">
                            <div class="w3-left">
                                <b>Всего: ${sumAll}</b>
                            </div>
                        </td>
                        <td colspan="10" style="vertical-align: middle">
                            <div class="w3-center">
                                <b>Внести записи на дату:</b>
                                <input type="date" name="chosen_date"
                                       value="${DateUtil.NOW.format(DateUtil.DTFORMATTER_DATE_ONLY)}">
                            </div>
                        </td>
                        <td colspan="2">

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

                                    <%--                            Вывод названий колонок--%>
                                    <c:when test="${i == 0}">
                                        <th class="w3-"><a
                                                href="spending/typedList?type=${pt1.name()}&email=${user.email}">${pt1.title}</a>
                                        </th>
                                        <c:if test="${pt1.isDescriptionOutput()}">
                                            <th>Описание</th>
                                        </c:if>
                                    </c:when>

                                    <%--                            Вывод сумм затрат по типам--%>
                                    <c:when test="${i == 1}">
                                        <td>
                                                ${sumMapByType.get(pt1).toString()}
                                        </td>
                                        <c:if test="${pt1 == PaymentType.CAR || pt1 == PaymentType.ENTERTAINMENT ||
                                                                    pt1 == PaymentType.CHILDREN || pt1 == PaymentType.OTHER}">
                                            <td></td>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>

                                        <%--                                Вывод ячеек для добавления трат--%>
                                        <td class="w3-pale-green">
                                            <input type="text" name="${pt1.name()}"
                                                   class="w3-input w3-border w3-round-large" style="width: 70px">
                                        </td>
                                        <c:if test="${pt1.isDescriptionOutput()}">
                                            <td class="w3-pale-green">
                                                <input type="text" name="${pt1.name()}description"
                                                       class="w3-input w3-border w3-round-large" style="width: 120px">
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
                                    <td align="center" class="w3-container">
                                        <c:if test="${i < map.get(pt).size()}">
                                            <input type="button" onclick="window.location.href =
                                                    'spending/edit?id=${payment_record.id}&action=edit'"
                                                   value="${payment_record.price}"
                                                   class="w3-button w3-amber w3-hover-light-green w3-round-large"/>
                                        </c:if>
                                    </td>
                                    <c:if test="${pt.isDescriptionOutput()}">
                                        <td style="vertical-align: middle" class="w3-container">
                                            <c:if test="${i < map.get(pt).size()}">
                                                ${map.get(pt)[i].description}
                                            </c:if>
                                        </td>
                                    </c:if>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <tr class="w3-medium">
                        <td colspan="5">
                            <input type="button" onclick="window.location.href = 'spending?view=toCurrentDate';"
                                   value="Показать записи до сегодняшней даты"
                                   class="w3-button w3-hover-light-green w3-light-blue w3-round-large"/>
                        </td>
                        <td colspan="4">
                            <input type="button" onclick="window.location.href = 'spending?view=allTime';"
                                   value="Показать записи за всё время"
                                   class="w3-button w3-hover-light-green w3-light-blue w3-round-large"/>
                        </td>
                        <td colspan="5">
                            <input type="button" onclick="window.location.href = 'spending?view=allUsersPayments';"
                                   value="Показать записи всех пользователей"
                                   class="w3-button w3-hover-light-green w3-light-blue w3-round-large"/>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
        <hr>
        <div class="w3-center">
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
                            hidden>${user.startPeriodDate.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))}</option>
                    <c:forEach var="i" begin="1" end="12">
                        <option value="${i}">${Month.of(i).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))}</option>
                    </c:forEach>
                </select>
                <select name="start_year">
                    <option value="${user.startPeriodDate.year}" selected hidden>${user.startPeriodDate.year}</option>
                    <c:forEach var="i" begin="2010" end="${Year.now().getValue()}">
                        <option value="${i}">${i}</option>
                    </c:forEach>
                </select>
                <button type="submit" class="w3-button w3-light-green w3-hover-amber w3-round-large">Изменить начальную
                    дату
                </button>
            </form>
        </div>
    </div>
</section>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.Month" %>
<%@ page import="java.time.Year" %>
<%@ page import="java.time.format.TextStyle" %>
<%@ page import="java.util.Locale" %>

<html>
<head>
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/w3.css">
    <link rel="stylesheet" href="../css/w3-colors-win8.css">
    <link rel="stylesheet" href="../css/w3-colors-metro.css">
    <jsp:useBean id="user" class="ru.spending.model.User" scope="request"/>
    <title>User ${user.name}</title>
</head>
<body class="w3-metro-light-blue">
<div class="w3-display-container" style="width:100%; height: 100%;">
    <div class="w3-display-middle">
        <section>
            <form method="post" action="settings" enctype="application/x-www-form-urlencoded">
                <input type="hidden" name="uuid" value="${user.uuid}">
                <input type="hidden" name="email" value="${user.email}">
                <input type="hidden" name="post_type" value="update">
                <table class="w3-table w3-striped w3-bordered w3-card-4 w3-large">
                    <tr>
                        <td style="vertical-align: middle; text-align: right">
                            Имя:
                        </td>
                        <td>
                            <input type="text" name="name" value="${user.name}"
                                   class="w3-input w3-border w3-round-large" style="width: 150px">
                        </td>
                    </tr>
                    <tr>
                        <td style="vertical-align: middle; text-align: right">
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
                        <td style="vertical-align: middle; text-align: right">
                            Старый пароль:
                        </td>
                        <td>
                            <input type="password" name="old_pass"
                                   class="w3-input w3-border w3-round-large" style="width: 150px">
                        </td>
                    </tr>
                    <tr>
                        <td style="vertical-align: middle; text-align: right">
                            Новый пароль:
                        </td>
                        <td>
                            <input type="password" name="new_pass"
                                   class="w3-input w3-border w3-round-large" style="width: 150px">
                        </td>
                    </tr>
                    <tr>
                        <td style="vertical-align: middle; text-align: right">
                            Повторить пароль:
                        </td>
                        <td>
                            <input type="password" name="new_pass_reply"
                                   class="w3-input w3-border w3-round-large" style="width: 150px">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="w3-center">
                            <button type="submit"
                                    class="w3-button w3-light-green w3-hover-amber w3-round-large">Сохранить
                            </button>
                            <input type="button" value="На главную" onclick="window.location.href = '../spending'"
                                   class="w3-button w3-light-green w3-hover-amber w3-round-large">
                        </td>
                    </tr>
                </table>
            </form>
        </section>

        <form method="post" action="settings" enctype="application/x-www-form-urlencoded">
            <input type="hidden" name="uuid" value="${user.uuid}">
            <input type="hidden" name="email" value="${user.email}">
            <input type="hidden" name="post_type" value="export">
            <br>
            <table class="w3-table w3-centered w3-card-4 w3-large">
                <tr class="w3-border">
                    <td class="w3-wide">
                        <b>Экспорт файла</b>
                    </td>
                </tr>
                <tr>
                    <td>
                        ${user.startPeriodDate.dayOfMonth}
                        <select name="period_month">
                            <option value="${user.startPeriodDate.minusMonths(1).month.value}" selected
                                    hidden>${user.startPeriodDate.minusMonths(1).month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))}</option>
                            <c:forEach var="i" begin="1" end="12">
                                <option value="${i}">${Month.of(i).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))}</option>
                            </c:forEach>
                        </select>
                        <select name="period_year">
                            <option value="${user.startPeriodDate.minusMonths(1).year}" selected
                                    hidden>${user.startPeriodDate.minusMonths(1).year}</option>
                            <c:forEach var="i" begin="2010" end="${Year.now().getValue()}">
                                <option value="${i}">${i}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <button type="submit" class="w3-button w3-block w3-light-green w3-hover-amber w3-round-large">
                            Записать
                            данные
                        </button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>

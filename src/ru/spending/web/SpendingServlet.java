package ru.spending.web;

import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.model.User;
import ru.spending.model.Users;
import ru.spending.storage.SqlStorage;
import ru.spending.util.Config;
import ru.spending.util.DateUtil;
import ru.spending.util.UtilsClass;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// Вывод списка трат.
// Можно добавить записи в двух предусмотренных для этого строках или изменить период вывода трат пользователя
public class SpendingServlet extends HttpServlet {

    private final static SqlStorage storage = Config.getINSTANCE().getSqlStorage();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid"); //ID пользователя
        String action = request.getParameter("action"); // параметр выбор действия
        String view = request.getParameter("view"); // параметр выбора период отображения трат
        User user = Users.getUserByEmail("l@og.in");
        request.setAttribute("user", user);
        if (action == null) {
            Map<PaymentType, List<Payment>> allSorted;
            if (view == null) {
                view = "";
            }
            switch (view) {
                // Выбор типа отображения трат
                case "toCurrentDate":
                    // Все траты пользователя до текущей даты
                    allSorted = storage.getAllSortedByUser(user.getUuid(), user.getStartPeriodDate(), DateUtil.getLocalDateTimeNow().toLocalDate());
                    break;
                case "allTime":
                    // Все траты пользователя за всё время
                    allSorted = storage.getAllSortedByUser(user.getUuid(), DateUtil.ALL_TIME_START.toLocalDate(), DateUtil.getLocalDateTimeNow().toLocalDate());
                    break;
                case "allUsersPayments":
                    // Траты всех пользователей за всё время
                    allSorted = storage.getAllSorted(DateUtil.ALL_TIME_START.toLocalDate(), DateUtil.getLocalDateTimeNow().toLocalDate());
                    break;
                default:
                    // Траты пользователя за период (месяц)
                    allSorted = storage.getAllSortedByUser(user.getUuid(), user.getStartPeriodDate(), user.getEndPeriodDate());
                    break;
            }
            int maxSize = UtilsClass.maxSize(allSorted);
            request.setAttribute("map", allSorted);
            request.setAttribute("maxSize", maxSize);
            Map<PaymentType, Integer> sumMapByType = UtilsClass.getSumMapByType(allSorted);
            request.setAttribute("sumMapByType", sumMapByType);
            request.setAttribute("sumAll", UtilsClass.getSumAll(sumMapByType));
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
//        Payment p;
//        switch (action) {
//            case "view":
//                p = storage.get(uuid);
//                break;
//            case "refill":
//                Config.getINSTANCE().refillDB();
//                response.sendRedirect("spending");
//                return;
//            default:
//                throw new IllegalArgumentException("Action " + action + " is not recognized");
//        }
//        request.setAttribute("payment", p);
//        request.getRequestDispatcher(
//                "view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp"
//        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String postType = request.getParameter("post_type");

        switch (postType) {
            case "list":
                // Добавление трат в БД, которые записаны в ячейках на странице
                for (PaymentType pt : PaymentType.values()) {
                    int counter = 0;
                    for (String str : request.getParameterValues(pt.name())) {
                        if (str != null && !str.isEmpty()) {
                            String description = "";
                            if (pt == PaymentType.CAR || pt == PaymentType.ENTERTAINMENT ||
                                    pt == PaymentType.CHILDREN || pt == PaymentType.OTHER) {
                                description = request.getParameterValues(pt.name() + "description")[counter];
                            }
                            try {
                                LocalDate localDate = LocalDate.parse(request.getParameter("chosen_date"));
                                Payment p = new Payment(pt, UtilsClass.toInt(str), description, localDate);
                                storage.save(p);
                            } catch (NumberFormatException exc) {
                                exc.printStackTrace();
                            }
                            counter++;
                        }
                    }
                }
                break;
            case "start_date_change":
                // Смена начальной даты периода отображения трат
                String userID = request.getParameter("uuid");
                String userEmail = request.getParameter("email");
                int startDay = Integer.parseInt(request.getParameter("start_day"));
                int startMonth = Integer.parseInt(request.getParameter("start_month"));
                int startYear = Integer.parseInt(request.getParameter("start_year"));
                User user = Users.getUserByEmail(userEmail);
                user.setStartPeriodDate(LocalDate.of(startYear, startMonth, startDay));
                storage.updateUser(user);
                break;
        }
        response.sendRedirect("spending");
    }
}

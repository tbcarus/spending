package ru.spending.web;

import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.model.User;
import ru.spending.model.Users;
import ru.spending.storage.SqlStorage;
import ru.spending.util.Config;
import ru.spending.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpendingServlet extends HttpServlet {

    private final static SqlStorage storage = Config.getINSTANCE().getSqlStorage();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        String view = request.getParameter("view");
        User user = Users.getUser("l@og.in");
        if (action == null) {
            Map<PaymentType, List<Payment>> allSorted;
            if (view == null) {
                view = "";
            }
            switch (view) {
                case "toCurrentDate":
                    allSorted = storage.getAllSortedByUser(user.getUuid(), user.getStartPeriodDate(), DateUtil.NOW.toLocalDate());
                    break;
                case "allTime":
                    allSorted = storage.getAllSortedByUser(user.getUuid(), DateUtil.ALL_TIME_START.toLocalDate(), DateUtil.NOW.toLocalDate());
                    break;
                case "allUsersPayments":
                    allSorted = storage.getAllSorted(DateUtil.ALL_TIME_START.toLocalDate(), DateUtil.NOW.toLocalDate());
                    break;
                default:
                    allSorted = storage.getAllSortedByUser(user.getUuid(), user.getStartPeriodDate(), user.getEndPeriodDate());
                    break;
            }
            int maxSize = maxSize(allSorted);
            request.setAttribute("user", user);
            request.setAttribute("map", allSorted);
            request.setAttribute("maxSize", maxSize);
            Map<PaymentType, Integer> sumMapByType = getSumMapByType(allSorted);
            request.setAttribute("sumMapByType", sumMapByType);
            request.setAttribute("sumAll", getSumAll(sumMapByType));
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Payment p;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("spending");
                return;
            case "create":
                p = new Payment("new", LocalDate.now());
                break;
            case "view":
                p = storage.get(uuid);
                break;
            case "edit":
                p = storage.get(uuid);
                break;
            case "settings":
                request.getRequestDispatcher("/WEB-INF/jsp/settings.jsp").forward(request, response);
                return;
            case "refill":
                Config.getINSTANCE().refillDB();
                response.sendRedirect("spending");
                return;
            default:
                throw new IllegalArgumentException("Action " + action + " is not recognized");
        }
        request.setAttribute("payment", p);
        request.getRequestDispatcher(
                "view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String postType = request.getParameter("post_type");

        switch (postType) {
            case "list":
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
                                Payment p;
                                if ("on".equals(request.getParameter("at_this_date_checkbox"))) {
                                    LocalDate localDate = LocalDate.parse(request.getParameter("chosen_date"));
                                    p = new Payment(pt, Integer.parseInt(str), description, localDate);
                                } else {
                                    p = new Payment(pt, Integer.parseInt(str), description);
                                }
                                storage.save(p);
                            } catch (NumberFormatException exc) {
                                exc.printStackTrace();
                            }
                            counter++;
                        }
                    }
                }
                break;
            case "edit":
                String uuid = request.getParameter("uuid");
                PaymentType paymentType = PaymentType.valueOf(request.getParameter("payment_type"));
                int prise = Integer.parseInt(request.getParameter("prise"));
                String description = request.getParameter("description");
                LocalDate date = LocalDate.parse(request.getParameter("date"));
                if (uuid.equals("new")) {
                    storage.save(new Payment(paymentType, prise, description, date, "1"));
                } else {
                    storage.update(new Payment(uuid, paymentType, prise, description, date, "1"));
                }
                break;
            case "start_date_change":
                String userID = request.getParameter("uuid");
                String  userEmail = request.getParameter("email");
                int startDay = Integer.parseInt(request.getParameter("start_day"));
                int startMonth = Integer.parseInt(request.getParameter("start_month"));
                int startYear = Integer.parseInt(request.getParameter("start_year"));
                User user = Users.getUser(userEmail);
                user.setStartPeriodDate(LocalDate.of(startYear, startMonth, startDay));
                storage.updateUser(user);
                break;
        }
        response.sendRedirect("spending");
    }

    private int maxSize(Map<PaymentType, List<Payment>> map) {
        int max = 0;
        for (PaymentType pt : map.keySet()) {
            if (max < map.get(pt).size()) {
                max = map.get(pt).size();
            }
        }
        return max;
    }

    private int getSumByType(List<Payment> list) {
        int sum = 0;
        for (Payment p : list) {
            sum += p.getPrise();
        }
        return sum;
    }

    private Map<PaymentType, Integer> getSumMapByType(Map<PaymentType, List<Payment>> map) {
        Map<PaymentType, Integer> sumMap = new HashMap<>();
        for (PaymentType pt : map.keySet()) {
            sumMap.put(pt, getSumByType(map.get(pt)));
        }
        return sumMap;
    }

    private int getSumAll(Map<PaymentType, Integer> map) {
        int sum = 0;
        for (PaymentType pt : map.keySet()) {
            sum += map.get(pt);
        }
        return sum;
    }

}

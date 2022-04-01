package ru.spending.web;

import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.storage.SqlStorage;
import ru.spending.util.Config;
import ru.spending.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
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
        if (action == null) {
            String startDate = request.getParameter("start_date");
            Map<PaymentType, List<Payment>> allSorted = storage.getAllSorted();
//            allSorted = storage.getAllSortedByDate(java.sql.Date.valueOf(DateUtil.startDatePeriodStr()),
//                    java.sql.Date.valueOf(DateUtil.endDatePeriodStr()));
            int maxSize = maxSize(allSorted);
            request.setAttribute("map", allSorted);
            request.setAttribute("maxSize", maxSize);
            request.setAttribute("sumAll", storage.getSumAll());
            request.setAttribute("sumMapByType", storage.getSumMapByType());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Payment p = null;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("spending");
                return;
            case "create":
                p = new Payment("new", DateUtil.NOW);
                break;
            case "view":
                p = storage.get(uuid);
                break;
            case "edit":
                p = storage.get(uuid);
                break;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                                Payment p = new Payment(pt, Integer.parseInt(str), description);
                                storage.save(p);
                            } catch (NumberFormatException exc) {
                            }
                            counter++;
                        }
                    }
                }
                break;
            case "edit":
                try {
                    String uuid = request.getParameter("uuid");
                    PaymentType paymentType = PaymentType.valueOf(request.getParameter("payment_type"));
                    int prise = Integer.parseInt(request.getParameter("prise"));
                    String description = request.getParameter("description");
                    Date date = DateUtil.FORMATTER.parse(request.getParameter("date"));
                    if (uuid.equals("new")) {
                        storage.save(new Payment(paymentType, prise, description, date, "1"));
                    } else {
                        storage.update(new Payment(uuid, paymentType, prise, description, date, "1"));
                    }
                } catch (NullPointerException exc) {
                } catch (NumberFormatException exc) {
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "start_date_change":
                DateUtil.customStartDatePeriod = request.getParameter("start_date");
                System.out.println(12312);
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
}

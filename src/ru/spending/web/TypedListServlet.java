package ru.spending.web;

import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.model.User;
import ru.spending.model.Users;
import ru.spending.storage.SqlStorage;
import ru.spending.util.Config;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// Сервлет для вывода трат определённого типа.
// Выводится полная информация о тратах
// Здесь есть возможность удалить трату
public class TypedListServlet extends HttpServlet {
    private final static SqlStorage storage = Config.getINSTANCE().getSqlStorage();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        PaymentType pt = PaymentType.valueOf(request.getParameter("type"));
        String email = request.getParameter("email");
        String paymentId = request.getParameter("id");
        User user = Users.getUserByEmail(email);
        if ("delete".equals(action)) {
            storage.delete(paymentId);
            response.sendRedirect("typedList?type="+pt.name()+"&email="+user.getEmail());
            return;
        }
        List<Payment> list = storage.getAllByType(pt, user.getUuid(), user.getStartPeriodDate(), user.getEndPeriodDate());
        int sum = 0;
        for (Payment p : list) {
            sum += p.getPrice();
        }
        request.setAttribute("user", user);
        request.setAttribute("list", list);
        request.setAttribute("sum", sum);
        request.setAttribute("paymentType", pt);
        request.getRequestDispatcher("/WEB-INF/jsp/typedList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }
}

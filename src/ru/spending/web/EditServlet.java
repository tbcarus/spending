package ru.spending.web;

import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.storage.SqlStorage;
import ru.spending.util.Config;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

public class EditServlet extends HttpServlet {
    private final static SqlStorage storage = Config.getINSTANCE().getSqlStorage();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String paymentId = request.getParameter("id");
        String action = request.getParameter("action");
        Payment p = null;
        switch (action) {
            case "delete":
                storage.delete(paymentId);
                response.sendRedirect("../spending");
                return;
            case "edit":
                p = storage.get(paymentId);
                break;
            case "create":
                p = new Payment("new", LocalDate.now());
                break;
        }

        String userId = request.getParameter("uuid");
        request.setAttribute("payment", p);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String paymentId = request.getParameter("id");
        try {
            PaymentType paymentType = PaymentType.valueOf(request.getParameter("payment_type"));
            int prise = Integer.parseInt(request.getParameter("prise"));
            if (prise == 0) {
                throw new IllegalArgumentException();
            }
            String description = request.getParameter("description");
            LocalDate date = LocalDate.parse(request.getParameter("date"));
            if (paymentId.equals("new")) {
                storage.save(new Payment(paymentType, prise, description, date, "1"));
            } else {
                storage.update(new Payment(paymentId, paymentType, prise, description, date, "1"));
            }
        } catch (IllegalArgumentException exc) {
            if (paymentId.equals("new")) {
                response.sendRedirect("edit?action=create");
                return;
            } else {
                response.sendRedirect("edit?id="+paymentId+"&action=edit");
                return;
            }
        }
        response.sendRedirect("../spending");
    }
}

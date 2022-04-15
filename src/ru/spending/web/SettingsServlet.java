package ru.spending.web;

import ru.spending.model.User;
import ru.spending.model.Users;
import ru.spending.storage.SqlStorage;
import ru.spending.util.Config;
import ru.spending.util.DateUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Objects;

public class SettingsServlet extends HttpServlet {
    private final static SqlStorage storage = Config.getINSTANCE().getSqlStorage();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("uuid");
        User user = storage.getUserById(userId);
        request.setAttribute("user", user);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/jsp/settings.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String email = request.getParameter("email");
        User user = Users.getUserByEmail(email);
        user.setName(request.getParameter("name"));
        LocalDate startPeriod = DateUtil.getStartPeriod(Integer.parseInt(request.getParameter("start_day")));
        user.setStartPeriodDate(startPeriod);
        if (user.getPassword().equals(request.getParameter("old_pass"))) {
            String newPassword = request.getParameter("new_pass");
            if (Objects.equals(newPassword, request.getParameter("new_pass_reply"))) {
                user.setPassword(newPassword);
            } else {

            }
        }
        storage.updateUser(user);
        response.sendRedirect("../spending");
    }
}

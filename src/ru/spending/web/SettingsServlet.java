package ru.spending.web;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.model.User;
import ru.spending.model.Users;
import ru.spending.storage.SqlStorage;
import ru.spending.util.Config;
import ru.spending.util.DateUtil;
import ru.spending.util.ToXls;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

// Сервлет для обработки настроек пользователя
// Так же здесь можно экспортировать данные в файл эксель
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
        // Передача юзера в jsp
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
        String postType = request.getParameter("post_type");
        switch (postType) {
            // Выбор обновления данных пользователя или экспорт данных в эксель
            case "update":
                user.setName(request.getParameter("name"));
                LocalDate startPeriod = DateUtil.getStartPeriod(Integer.parseInt(request.getParameter("start_day")));
                user.setStartPeriodDate(startPeriod);
                if (user.getPassword().equals(request.getParameter("old_pass"))) {
                    String newPassword = request.getParameter("new_pass");
                    if (Objects.equals(newPassword, request.getParameter("new_pass_reply"))) {
                        user.setPassword(newPassword);
                    }
                }
                storage.updateUser(user);
                response.sendRedirect("../spending");
                return;
            case "export":
                int periodDay = user.getStartPeriodDate().getDayOfMonth();
                int periodMonth = Integer.parseInt(request.getParameter("period_month"));
                int periodYear = Integer.parseInt(request.getParameter("period_year"));
                LocalDate periodStart = LocalDate.of(periodYear, periodMonth, periodDay);
                Map<PaymentType, List<Payment>> allSorted = storage.getAllSortedByUser(user.getUuid(), periodStart, periodStart.plusMonths(1));
                String bookName = periodStart.getDayOfMonth() + " " +
                        periodStart.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru")) + " " +
                        periodStart.format(DateTimeFormatter.ofPattern("yy"));// Форматирование структуры названия книги

                XSSFWorkbook book = ToXls.write(allSorted, bookName);
                response.setContentType("application/vnd.ms-excel");
                String fileName = URLEncoder.encode(bookName, StandardCharsets.UTF_8);
                fileName = URLDecoder.decode(fileName, "ISO8859_1");
                response.setHeader("Content-disposition", "attachment; filename="+fileName+".xlsx");
                try (OutputStream out = response.getOutputStream()) {
                    book.write(out);
                }
                book.close();
                response.sendRedirect("/spending");
                return;
        }
        response.sendRedirect("../spending/settings?uuid=" + uuid);
    }
}

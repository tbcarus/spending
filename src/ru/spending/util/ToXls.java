package ru.spending.util;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// Утильный класс для экспорта данных за выбранный период в файл эксель. Каждый раз содаётся новый файл с новой книгой.
// Файл ранее вёлся на компьютере вручную, поэтому создаётся похожая структура ранее существующего файла
// из-за чего организация вывода выглядит немного запутанной.
// Экспорт производится по умолчанию на рабочий стол. Если он не доступен, то предлагается указать путь вручную.
public class ToXls {
    public static final File DESKTOP_DIR = new File(System.getProperty("user.home") + "\\Desktop");

    public static boolean isRightDir(File outDir) {
        // Проверка, что это папка, она существует, доступна для чтения и записи.
        return outDir.exists() && outDir.canWrite() && outDir.canRead() && outDir.isDirectory();
    }

    // Метод для записи мапы трат
    public static void write(Map<PaymentType, List<Payment>> allSorted, LocalDate periodStart, File dir) {
        XSSFWorkbook book = new XSSFWorkbook(); // Создание новой книги

        StringBuilder bookName = new StringBuilder(); // Форматирование структуры названия книги
        bookName.append(periodStart.getDayOfMonth()).append(" ");
        bookName.append(periodStart.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault())).append(" ");
        bookName.append(periodStart.format(DateTimeFormatter.ofPattern("yy")));
        XSSFSheet sheet = book.createSheet(bookName.toString()); // Создание листа книги

        writeHeaderLine(sheet); // Запись шапки таблицы
        writeSumLine(allSorted, sheet); // Запись строки с суммой трат
        writeSpending(allSorted, sheet); // Запись данных трат в таблицу

        File fout = new File(dir, bookName + ".xlsx"); // Выходной файл

        try (FileOutputStream outputStream = new FileOutputStream(fout)) {
            book.write(outputStream);
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeHeaderLine(XSSFSheet sheet) {
        XSSFRow headerRow = sheet.createRow(2); // Создание строки
        int count = 1;
        for (PaymentType pt : PaymentType.values()) {
            XSSFCell headerCell = headerRow.createCell(count); // Создание ячейки
            headerCell.setCellValue(pt.getTitle());

            switch (pt) {
                // Установка ширины колонок
                case CAR:
                    sheet.setColumnWidth(count, 18*256);
                    break;
                case GAS:
                    sheet.setColumnWidth(count, 20*256);
                    break;
                case FOOD:
                    sheet.setColumnWidth(count, 13*256);
                    break;
                case ENTERTAINMENT:
                    sheet.setColumnWidth(count, 12*256);
                    break;
            }

            if (count == 1) { // Особенности первоначального файла
                count++;
            }
            if (pt.isDescriptionOutput()) {
                // Вывод описания траты, если этот признак установлен для данного типа траты
                XSSFCell headerCellDescription = headerRow.createCell(++count);
                headerCellDescription.setCellValue("Примечание");
                sheet.setColumnWidth(count, 12*256);
            }
            count++;
        }
    }

    private static void writeSumLine(Map<PaymentType, List<Payment>> allSorted, XSSFSheet sheet) {
        Map<PaymentType, Integer> sumByType = UtilsClass.getSumMapByType(allSorted);
        XSSFRow sumRow = sheet.createRow(3);
        XSSFCell sumCell = sumRow.createCell(0);

        XSSFCellStyle style = sumCell.getSheet().getWorkbook().createCellStyle(); // Редактирование стиля ячейки
        style.setFillForegroundColor(new XSSFColor(new Color(22,54,92), null)); // Установка цвета ячейки
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND); // Установка стиля заполнения ячейки - заполнение цветом полностью
        XSSFFont font = sumCell.getSheet().getWorkbook().createFont(); // Редактирование стиля шрифта ячейки
        font.setBold(true); // Жирный шрифт
        font.setColor(new XSSFColor(new Color(255,255,255), null)); // Установка цвета шрифта
        style.setFont(font); // Установка шрифта стилю ячейки
        sumCell.setCellStyle(style); // Установка ячейке изменённого стиля
        sumCell.setCellFormula("SUM(B4:O4)"); // Запись формулы в ячейку

        int count = 1;
        for (PaymentType pt : PaymentType.values()) {
            sumCell = sumRow.createCell(count);

            style = sumCell.getSheet().getWorkbook().createCellStyle();
            style.setFillForegroundColor(new XSSFColor(new Color(253,233,217), null));
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            sumCell.setCellStyle(style);
            font = sumCell.getSheet().getWorkbook().createFont();
            font.setBold(true);
            style.setFont(font);
            sumCell.setCellValue(sumByType.get(pt));


            if (count == 1 || pt.isDescriptionOutput()) {
                sumCell = sumRow.createCell(++count);

                style = sumCell.getSheet().getWorkbook().createCellStyle();
                style.setFillForegroundColor(new XSSFColor(new Color(253,233,217), null));
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                sumCell.setCellStyle(style);
            }
            count++;
        }
    }

    private static void writeSpending(Map<PaymentType, List<Payment>> allSorted, XSSFSheet sheet) {
        int maxSize = UtilsClass.maxSize(allSorted);
        int deltaRow = 4;
        for (int i = deltaRow; i < maxSize + deltaRow; i++) {
            XSSFRow row = sheet.createRow(i);
            int count = 1;
            for (PaymentType pt : PaymentType.values()) {
                if (allSorted.get(pt).size() > i - deltaRow) {
                    XSSFCell cell = row.createCell(count);
                    cell.setCellValue(allSorted.get(pt).get(i - deltaRow).getPrice());
                }
                if (count == 1) { // Особенности первоначального файла
                    count++;
                }
                if (pt.isDescriptionOutput()) {
                    count++;
                    if (allSorted.get(pt).size() > i - deltaRow) {
                        XSSFCell cell = row.createCell(count);
                        cell.setCellValue(allSorted.get(pt).get(i - deltaRow).getDescription());
                    }
                }
                count++;
            }
        }
    }
}

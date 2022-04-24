package ru.spending.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import ru.spending.model.Payment;
import ru.spending.model.PaymentType;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static ru.spending.model.PaymentType.*;

public class ToXls {
    public static void write(Map<PaymentType, List<Payment>> allSorted, LocalDate periodStart) {
        XSSFWorkbook book = new XSSFWorkbook();

        StringBuilder bookName = new StringBuilder();
        bookName.append(periodStart.getDayOfMonth() + " ");
        bookName.append(periodStart.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " ");
        bookName.append(periodStart.format(DateTimeFormatter.ofPattern("yy")));
        XSSFSheet sheet = book.createSheet(bookName.toString());

        writeHeaderLine(sheet);
        writeSumLine(allSorted, sheet);
        writeSpending(allSorted, sheet);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(Path.of("C:\\projects\\spending\\storage\\Test.xlsx").toFile());
            book.write(outputStream);
            book.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeHeaderLine(XSSFSheet sheet) {
        XSSFRow headerRow = sheet.createRow(2);
        int count = 1;
        for (PaymentType pt : values()) {
            XSSFCell headerCell = headerRow.createCell(count);
            headerCell.setCellValue(pt.getTitle());

            switch (pt) {
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
                XSSFCell headerCellDescription = headerRow.createCell(++count);
                headerCellDescription.setCellValue("Примечание");
                sheet.setColumnWidth(count, 12*256);
            }
            count++;
        }
    }

    private static void writeSumLine(Map<PaymentType, List<Payment>> allSorted, XSSFSheet sheet) {
        Map<PaymentType, Integer> sumByType = UtilsClass.getSumMapByType(allSorted);
//        int sum = UtilsClass.getSumAll(sumByType);
        XSSFRow sumRow = sheet.createRow(3);
        XSSFCell sumCell = sumRow.createCell(0);

        XSSFCellStyle style = sumCell.getSheet().getWorkbook().createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new Color(22,54,92), null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = sumCell.getSheet().getWorkbook().createFont();
        font.setBold(true);
        font.setColor(new XSSFColor(new Color(255,255,255), null));
        style.setFont(font);
        sumCell.setCellStyle(style);
        sumCell.setCellFormula("SUM(B4:O4)");

        int count = 1;
        for (PaymentType pt : values()) {
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
            for (PaymentType pt : values()) {
                if (allSorted.get(pt).size() > i - deltaRow) {
                    XSSFCell cell = row.createCell(count);
                    cell.setCellValue(allSorted.get(pt).get(i - deltaRow).getPrise());
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

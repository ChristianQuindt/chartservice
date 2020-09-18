package de.itscope.chartservice.util;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateStringUtils {
    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static String daysAgo(int daysAgo) {
        return formatter.format(DateTime.now().minusDays(daysAgo).toDate());
    }

    public static Date getDateFromString(String dateString) {
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isFirstDateBeforeSecond(String firstDate, String secondDate) throws ParseException {
        Date first = formatter.parse(firstDate);
        Date second = formatter.parse(secondDate);
        return first.before(second);
    }

    public static String getStringFromDate(Date date) {
        return formatter.format(date);
    }

    public static ArrayList<String> getDateStringsForRange(String from_date, String to_date) {
        DateTime fromDate = new DateTime(getDateFromString(from_date));
        DateTime toDate = new DateTime(getDateFromString(to_date));

        ArrayList<String> dateStrings = new ArrayList<>();

        for (DateTime currentDate = fromDate; currentDate.isBefore(toDate.plusDays(1)); currentDate = currentDate.plusDays(1)) {
            dateStrings.add(formatter.format(currentDate.toDate()));
        }

        return dateStrings;
    }

    public static String monthsAgo(int monthsAgo) {
        return formatter.format(DateTime.now().minusMonths(monthsAgo).toDate());
    }

    public static String today() {
        return formatter.format(DateTime.now().toDate());
    }

    public static String weeksAgo(int weeksAgo) {
        return formatter.format(DateTime.now().minusWeeks(weeksAgo).toDate());
    }

    public static String weeksAgo(int weeksAgo, int offsetInDays) {
        return formatter.format(DateTime.now().minusDays(offsetInDays).minusWeeks(weeksAgo).toDate());
    }

    public static String yearsAgo(int yearsAgo) {

        return formatter.format(DateTime.now().minusYears(yearsAgo).toDate());
    }

    public static String yearsAgo(int yearsAgo, int offsetInDays) {

        return formatter.format(DateTime.now().minusDays(offsetInDays).minusYears(yearsAgo).toDate());
    }

    public static String yesterday() {
        return formatter.format(DateTime.now().minusDays(1).toDate());
    }
}

package com.appsfeature.global.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimeUtil {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MMM-dd";

    public static String getDateInServerFormat(String cDate) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.US).parse(cDate);
            return date!=null ? outputFormat.format(date) : "0";
        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }
    }

     public static String getDateInMobileViewFormat(String cDate) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-dd", Locale.US);
        try {
            Date date = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.US).parse(cDate);
            return date!=null ? outputFormat.format(date) : "0";
        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static String getDateInMobileViewFormatFromServer(String cDate) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(cDate);
            return date!=null ? outputFormat.format(date) : "0";
        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static String getDateInMobileViewFormatFromDOB(String cDate) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(cDate);
            return date!=null ? outputFormat.format(date) : "0";
        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static List<String> getDates(String dateString1, String dateString2) {
        ArrayList<String> dates = new ArrayList<>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);

        try {
            Date date1 = df1.parse(dateString2);
            Date date2 = df1.parse(dateString1);

            Calendar cal1 = Calendar.getInstance();
            if (date1 != null) {
                cal1.setTime(date1);
            }


            Calendar cal2 = Calendar.getInstance();
            if (date2 != null) {
                cal2.setTime(date2);
            }

            while (!cal1.after(cal2)) {
                dates.add(df1.format(cal1.getTime()));
                cal1.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }

}

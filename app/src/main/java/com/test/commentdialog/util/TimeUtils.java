package com.test.commentdialog.util;

import java.util.Locale;

public class TimeUtils {

    public static String getRecentTimeSpanByNow(final long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 1000) {
            return "刚刚";
        } else if (span < TimeConstants.MIN) {
            return String.format(Locale.getDefault(), "%d秒前", span / TimeConstants.SEC);
        } else if (span < TimeConstants.HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / TimeConstants.MIN);
        } else if (span < TimeConstants.DAY) {
            return String.format(Locale.getDefault(), "%d小时前", span / TimeConstants.HOUR);
        } else if (span < TimeConstants.MONTH) {
            return String.format(Locale.getDefault(), "%d天前", span / TimeConstants.DAY);
        } else if (span < TimeConstants.YEAR) {
            return String.format(Locale.getDefault(), "%d月前", span / TimeConstants.MONTH);
        } else if (span > TimeConstants.YEAR) {
            return String.format(Locale.getDefault(), "%d年前", span / TimeConstants.YEAR);
        } else {
            return String.format("%tF", millis);
        }
    }


}

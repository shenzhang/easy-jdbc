package com.github.shenzhang.ejdbc.rowMapper.extractor;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 7:28 PM
 */
public class CalendarValueExtractor extends DateValueExtractor {
    @Override
    public Object extract(ResultSet rs, String column) {
        Date date = (Date) super.extract(rs, column);
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}

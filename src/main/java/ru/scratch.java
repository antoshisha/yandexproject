package ru;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

public class scratch {
    public static void main(String[] args) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss.SSSZ";
        OffsetDateTime lastAvailableDateTime = OffsetDateTime.parse("2022-06-23T17:50:48.141+00:00");
        System.out.println(new Date(lastAvailableDateTime.toInstant().toEpochMilli()));
//        simpleDateFormat.parse("2022-06-23T17:50:48.141+00:00");
    }
}

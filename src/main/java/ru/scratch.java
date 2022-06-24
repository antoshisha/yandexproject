package ru;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class scratch {
    public static void main(String[] args) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss.SSSZ";
        OffsetDateTime lastAvailableDateTime = OffsetDateTime.parse("2022-06-23T17:50:48.141+00:00");
//        System.out.println(new Date(lastAvailableDateTime.toInstant().toEpochMilli()));
        System.out.println(lastAvailableDateTime);
//        System.out.println(LocalDateTime.now());
        System.out.println(OffsetDateTime.now(ZoneOffset.of("+03:00")));
        System.out.println(OffsetDateTime.now());
        System.out.println(OffsetDateTime.parse("2022-06-24T14:08:20.852740631+03:00"));
//        LocalDateTime localDateTime = LocalDateTime.parse("2022-05-28T21:12:01.000Z", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nnn'Z'"));
//        LocalDateTime localDateTime2 = LocalDateTime.parse("2022-05-28T21:12:01", DateTimeFormatter.I);
//        System.out.println(localDateTime);
//        System.out.println(localDateTime2);
//        simpleDateFormat.parse("2022-06-23T17:50:48.141+00:00");
    }
}

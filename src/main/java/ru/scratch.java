package ru;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class scratch {
    public static void main(String[] args) throws ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        OffsetDateTime lastAvailableDateTime = OffsetDateTime.parse("2022-02-03T12:00:00.000Z");
        System.out.println(lastAvailableDateTime);
        OffsetDateTime odt = OffsetDateTime.parse("2020-03-25T22:00:00.000Z", dtf);
        OffsetDateTime time = OffsetDateTime.parse("2022-02-03T12:00Z");
        System.out.println(time.format(dtf));
        String formatted = odt.format(dtf);
        System.out.println(formatted);
    }
}

package ru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class YandexBackEndSummerSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(YandexBackEndSummerSchoolApplication.class, args);
    }

}

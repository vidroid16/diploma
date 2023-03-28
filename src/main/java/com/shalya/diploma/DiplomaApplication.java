package com.shalya.diploma;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiplomaApplication {

    static final Logger log =
            LoggerFactory.getLogger(DiplomaApplication.class);

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        SpringApplication.run(DiplomaApplication.class, args);
    }

}

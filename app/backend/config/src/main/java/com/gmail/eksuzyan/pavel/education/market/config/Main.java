package com.gmail.eksuzyan.pavel.education.market.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

//        Map<String, String> props = new HashMap<>();
//
//        props.put("hello", "world");
//        props.put("goodbye", "people");

        Properties properties = new Properties();

        properties.setProperty("goodbye", "people");
        properties.setProperty("hello", "world");
        properties.setProperty("password", "223ssdcd3%");

        Configuration.current().write(properties);

//        Configuration.current().read().forEach((key, value) -> System.out.println(key + " - " + value));

    }

}

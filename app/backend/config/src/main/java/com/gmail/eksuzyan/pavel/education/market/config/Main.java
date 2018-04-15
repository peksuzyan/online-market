package com.gmail.eksuzyan.pavel.education.market.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

//        Map<String, String> props = new HashMap<>();
//
//        props.put("hello", "world");
//        props.put("goodbye", "people");

        Configuration.current().read().forEach((key, value) -> System.out.println(key + " - " + value));

    }

}

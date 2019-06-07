package com.me.locallib.handler;

import java.io.*;

import java.util.Properties;

public class PropertiesHandler {

    public static Properties loadProperties(){

        Properties properties = null;

        try (InputStream input = new FileInputStream("config.properties")) {

            properties = new Properties();

            properties.load(input);

        } catch (IOException ex) {
            System.out.println("unable to load config file");
            properties = new Properties();
        }

        return properties;

    }

    public static void writeProperties(Properties properties){

        try (OutputStream output = new FileOutputStream("config.properties")) {

            properties.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}

package com.me.locallib.handler;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.me.locallib.model.Book;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ersinn on 6.06.2019.
 */
public class DataHandler {

    public static void saveBook(Book book){

        String completePath = PropertiesHandler.loadProperties().getProperty("BooksDirectoryPath") + "\\" + book.id + ".json";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(completePath),book);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static Book loadBook(long id){

        String filePath = PropertiesHandler.loadProperties().getProperty("BooksDirectoryPath") + "\\" + id + ".json";

        Book book = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            book = objectMapper.readValue(new File(filePath),Book.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return book;
    }

    public static String loadBookJSON(long id){
        String bookPath = PropertiesHandler.loadProperties().getProperty("BooksDirectoryPath") + "\\" + id + ".json";

        try {
            String json = new String(Files.readAllBytes(Paths.get(bookPath)),"UTF-8");
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Long> loadAllBookIds(){

        File booksDirectory = new File(PropertiesHandler.loadProperties().getProperty("BooksDirectoryPath"));

        File[] bookFiles = booksDirectory.listFiles();

        List<Long> list = Arrays.stream(bookFiles).map(file -> {
            try {
                return Long.parseLong(file.getName().split(".json")[0]);
            }catch (NumberFormatException e){
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return list;
    }

    public static long getNextId(){

        Properties properties = PropertiesHandler.loadProperties();

        long nextId;

        try{
            nextId = Long.parseLong(PropertiesHandler.loadProperties().getProperty("NextId"));
        }catch (NumberFormatException e){
            nextId = 0;
        }

        String booksDirectoryPath = properties.getProperty("BooksDirectoryPath");
        String completePath = booksDirectoryPath + "\\" + nextId + ".json";
        while (Files.exists(Paths.get(completePath))){
            nextId++;
            completePath = booksDirectoryPath + "\\" + nextId + ".json";
        }

        properties.setProperty("NextId", String.valueOf(nextId));
        PropertiesHandler.writeProperties(properties);

        return nextId;
    }

    public static Image loadBookImage(Object identifier, int index){
        Book book = null;

        try {
            if(Book.class.getName().equals(identifier.getClass().getName())) book = ((Book) identifier);
            else if(Long.class.getName().equals(identifier.getClass().getName())) book = loadBook(((Long) identifier));
        }catch (Exception ignored) {}

        if(book == null || book.imageNames[index] == null) return new Image(String.valueOf(DataHandler.class.getResource("/image/no-image-found.png")));

        String booksDirectoryPath = PropertiesHandler.loadProperties().getProperty("ImagesDirectoryPath");
        String url = "file:\\" + booksDirectoryPath + "\\" + book.imageNames[index];

        return new Image(url);
    }

}

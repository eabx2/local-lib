package com.me.locallib.controller;

import com.me.locallib.handler.PropertiesHandler;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javafx.stage.DirectoryChooser;

import java.io.File;

import java.net.URL;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingsSceneController implements Initializable{

    @FXML
    TextField booksDirectoryTextField;

    @FXML
    TextField packetsDirectoryTextField;

    @FXML
    TextField imagesDirectoryTextField;

    public void onActionSelectButton(ActionEvent event){
        String callerId = ((Button)event.getSource()).getId();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);

        // case: unselected
        if(directory == null) return;

        // refresh textfield
        loadSettingsScene(callerId,directory.getPath());

        Properties properties = PropertiesHandler.loadProperties();

        // for a new setting expand switch clause
        switch (callerId){
            case "selectBooksDirectoryButton":
                properties.setProperty("BooksDirectoryPath",directory.getPath());
                break;
            case "selectPacketsDirectoryButton":
                properties.setProperty("PacketsDirectoryPath",directory.getPath());
                break;
            case "selectImagesDirectoryButton":
                properties.setProperty("ImagesDirectoryPath",directory.getPath());
                break;
        }

        PropertiesHandler.writeProperties(properties);

    }

    public void consume(KeyEvent event){
        if(event.getCode().isArrowKey()) return;
        else event.consume();
    }

    private void loadSettingsScene(String setting, String value){

        setting = setting.toLowerCase(new Locale("en"));

        // for a new setting expand switch clause

        if(setting.contains("book")) booksDirectoryTextField.setText(value);

        else if(setting.contains("packet")) packetsDirectoryTextField.setText(value);

        else if(setting.contains("images")) imagesDirectoryTextField.setText(value);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Properties properties = PropertiesHandler.loadProperties();
        properties.forEach((key,value) -> loadSettingsScene((String) key, (String) value));

    }
}

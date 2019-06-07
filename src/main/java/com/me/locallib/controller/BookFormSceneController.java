package com.me.locallib.controller;

import com.me.locallib.handler.DataHandler;
import com.me.locallib.handler.PropertiesHandler;
import com.me.locallib.model.Book;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import java.net.URL;

import java.util.ResourceBundle;

public class BookFormSceneController implements Initializable{

    /*** Basic ***/

    @FXML
    TextField nameTextField;

    @FXML
    TextField writerTextField;

    @FXML
    TextField translatorTextField;

    @FXML
    TextField publisherTextField;

    @FXML
    TextField originalNameTextField;

    @FXML
    TextField pagesTextField;

    @FXML
    CheckBox readCheckBox;

    @FXML
    CheckBox inLibraryCheckBox;

    /*** Basic ***/

    ////////////////////////////////////////////////////

    /*** Media ***/

    @FXML
    Pagination imagesPagination;

    /*** Media ***/

    ////////////////////////////////////////////////////

    /*** Purchased ***/

    @FXML
    TextField purchasedFromTextField;

    @FXML
    TextField packetTextField;

    @FXML
    TextField priceTextField;

    @FXML
    DatePicker purchasedDateDatePicker;

    /*** Purchased ***/

    ////////////////////////////////////////////////////

    /*** Extra ***/

    @FXML
    TextField seriesNameTextField;

    @FXML
    TextField seriesNoTextField;

    @FXML
    TextField editionNoTextField;

    @FXML
    TextArea descriptionTextArea;

    /*** Extra ***/

    ////////////////////////////////////////////////////

    Book currentBook;

    public void onActionSelectButton(){
        FileChooser fileChooser = new FileChooser();
        String imageDirectoryPath = PropertiesHandler.loadProperties().getProperty("ImagesDirectoryPath");
        fileChooser.setInitialDirectory(new File(imageDirectoryPath));
        File file = fileChooser.showOpenDialog(null);

        // case: not selected
        if(file == null) return;

        currentBook.imageNames[imagesPagination.getCurrentPageIndex()] = file.getName();

        // iterate next page
        imagesPagination.setCurrentPageIndex((imagesPagination.getCurrentPageIndex() + 1 ) % imagesPagination.getPageCount());

    }


    public void onActionSaveButton(){

        // Basic
        currentBook.name = nameTextField.getText();
        currentBook.writer = writerTextField.getText();
        currentBook.translator = translatorTextField.getText();
        currentBook.publisher = publisherTextField.getText();
        currentBook.originalName = originalNameTextField.getText();
        currentBook.pages = pagesTextField.getText();
        currentBook.isRead = readCheckBox.isSelected();
        currentBook.isInLibrary = inLibraryCheckBox.isSelected();

        // Media is already saved

        // Purchase
        currentBook.purchasedFrom = purchasedFromTextField.getText();
        currentBook.packet = packetTextField.getText();
        currentBook.price = priceTextField.getText();
        currentBook.purchasedDate = purchasedDateDatePicker.getValue();

        // Extra
        currentBook.seriesName = seriesNameTextField.getText();
        currentBook.seriesNo = seriesNoTextField.getText();
        currentBook.editionNo = editionNoTextField.getText();
        currentBook.description = descriptionTextArea.getText();

        // Identifier
        if(currentBook.id == null){
            currentBook.id = DataHandler.getNextId();
            ((Stage) nameTextField.getScene().getWindow()).close();
        }

        DataHandler.saveBook(currentBook);

    }

    public void loadBookForm(Book book){
        currentBook = book;

        // Basic
        nameTextField.setText(currentBook.name);
        writerTextField.setText(currentBook.writer);
        translatorTextField.setText(currentBook.translator);
        publisherTextField.setText(currentBook.publisher);
        originalNameTextField.setText(currentBook.originalName);
        pagesTextField.setText(currentBook.pages);
        readCheckBox.setSelected(currentBook.isRead);
        inLibraryCheckBox.setSelected(currentBook.isInLibrary);

        // Media is already saved

        // Purchase
        purchasedFromTextField.setText(currentBook.purchasedFrom);
        packetTextField.setText(currentBook.packet);
        priceTextField.setText(currentBook.price);
        purchasedDateDatePicker.setValue(currentBook.purchasedDate);

        // Extra
        seriesNameTextField.setText(currentBook.seriesName);
        seriesNoTextField.setText(currentBook.seriesNo);
        editionNoTextField.setText(currentBook.editionNo);
        descriptionTextArea.setText(currentBook.description);

        // refresh
        int pageCount = imagesPagination.getPageCount();
        imagesPagination.setPageCount(Integer.MAX_VALUE);
        imagesPagination.setPageCount(pageCount);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        currentBook = new Book();

        imagesPagination.setPageFactory( param -> {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(imagesPagination.getPrefWidth());
            imageView.setFitHeight(imagesPagination.getPrefHeight());
            Image image = DataHandler.loadBookImage(currentBook,param);
            imageView.setImage(image);
            return imageView;
        });

    }
}

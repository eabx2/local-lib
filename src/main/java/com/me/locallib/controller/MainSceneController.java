package com.me.locallib.controller;

import com.me.locallib.handler.DataHandler;
import com.me.locallib.model.Book;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import java.net.URL;

import java.util.*;
import java.util.stream.Collectors;

public class MainSceneController implements Initializable{

    @FXML
    TextField searchTextField;

    @FXML
    Pagination searchResultsPagination;

    @FXML
    VBox outerVBox2;

    ////////////////////////////////////////////////////

    private static final int booksPerPage = 4;

    private List<Long> searchedResults;

    private BookFormSceneController bookFormSceneController;

    public void onActionSearchTextField(){
        searchedResults = searchEngine(searchTextField.getText());

        int pageCount = searchedResults.size() / booksPerPage;
        if(pageCount == 0 || searchedResults.size() % booksPerPage != 0) pageCount++;

        searchResultsPagination.setPageCount(Integer.MAX_VALUE);
        searchResultsPagination.setPageCount(pageCount);
    }

    private List<Long> searchEngine(String searchString){
        searchString = searchString.toLowerCase();

        String[] parts = searchString.split(" ");

        Map<Long,Integer> scorePair = new HashMap<>();

        DataHandler.loadAllBookIds().forEach(aLong -> {

            String json = DataHandler.loadBookJSON(aLong);
            int count = 0;
            for (String part : parts) {
                count += StringUtils.countMatches(json.toLowerCase(),part);
            }

            if (count == 0) return;

            scorePair.put(aLong,count);
        });

        // return keys (book ids) in ascending order
        return scorePair.entrySet()
                .stream()
                .sorted((o1, o2) -> {
            if(o1.getValue()>o2.getValue()) return -1;
            else if(o1.getValue()<o2.getValue()) return 1;
            else return 0;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

    }

    private Node createBookShowcase(Book book){
        HBox outerHBox = new HBox(5);

        ImageView imageView = new ImageView(DataHandler.loadBookImage(book,0));
        imageView.setFitHeight(150);
        imageView.setFitWidth(100);

        VBox textVBox = new VBox(3);

        Label nameLabel = new Label(book.name);
        nameLabel.setFont(Font.font("",FontWeight.EXTRA_BOLD,16));

        HBox writerHBox = new HBox();
        Text writerText = new Text("writer: ");
        Label writerLabel = new Label(book.writer);
        writerLabel.setFont(Font.font("",FontWeight.BOLD,14));
        writerHBox.getChildren().addAll(writerText,writerLabel);

        HBox translatorHBox = new HBox();
        Text translatorText = new Text("translator: ");
        Label translatorLabel = new Label(book.translator);
        translatorLabel.setFont(Font.font("",FontWeight.BOLD,14));
        translatorHBox.getChildren().addAll(translatorText,translatorLabel);

        HBox publisherHBox = new HBox();
        Text publisherText = new Text("publisher: ");
        Label publisherLabel = new Label(book.publisher);
        publisherLabel.setFont(Font.font("",FontWeight.BOLD,14));
        publisherHBox.getChildren().addAll(publisherText,publisherLabel);

        HBox checkBoxesHBox = new HBox(5);
        CheckBox isRead = new CheckBox("Read");
        isRead.setSelected(book.isRead);
        isRead.setDisable(true);
        isRead.setStyle("-fx-opacity: 1");
        CheckBox isInLibrary = new CheckBox("In Library");
        isInLibrary.setSelected(book.isInLibrary);
        isInLibrary.setDisable(true);
        isInLibrary.setStyle("-fx-opacity: 1");
        checkBoxesHBox.getChildren().addAll(isRead,isInLibrary);


        textVBox.getChildren().addAll(nameLabel,writerHBox,translatorHBox,publisherHBox,checkBoxesHBox);

        outerHBox.getChildren().addAll(imageView,textVBox);

        outerHBox.setId(String.valueOf(book.id));

        // set functionality
        outerHBox.setOnMouseClicked(this::onMouseClickedBookShowcase);

        return outerHBox;
    }

    private void onMouseClickedBookShowcase(MouseEvent event){
        long bookId = Long.parseLong(((Node) event.getSource()).getId());

        showBookForm();

        bookFormSceneController.loadBookForm(DataHandler.loadBook(bookId));

    }

    private void showBookForm(){

        if(bookFormSceneController != null) return;

        Parent bookForm = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((getClass().getResource("/fxml/BookFormScene.fxml")));
            bookForm = fxmlLoader.load();
            bookFormSceneController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outerVBox2.getChildren().add(bookForm);
    }

    public void onActionAddNewBookMenuItem() throws IOException {
        Stage addNewBook = new Stage();
        addNewBook.initModality(Modality.APPLICATION_MODAL);
        addNewBook.setTitle("New Book");

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/BookFormScene.fxml"));
        addNewBook.setScene(new Scene(root));

        addNewBook.show();
    }

    public void onActionSettingsMenuItem() throws IOException{
        Stage settingScene = new Stage();
        settingScene.initModality(Modality.APPLICATION_MODAL);
        settingScene.setTitle("Settings");

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SettingsScene.fxml"));

        settingScene.setScene(new Scene(root));

        settingScene.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchedResults = new ArrayList<>();

        searchResultsPagination.setPageFactory(param -> {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setPadding(new Insets(5,5,5,5));
            VBox contentVBox = new VBox(10);

            for (int i = param * booksPerPage; i < (param + 1) * booksPerPage && i <searchedResults.size(); i++) {
                Book book = DataHandler.loadBook(searchedResults.get(i));
                contentVBox.getChildren().add(createBookShowcase(book));
            }

            scrollPane.setContent(contentVBox);
            return scrollPane;
        });
    }
}

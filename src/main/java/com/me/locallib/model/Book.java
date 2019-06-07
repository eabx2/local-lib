package com.me.locallib.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {

    /*** Identifier ***/

    public Long id;

    /*** Identifier ***/

    ////////////////////////////////////////////////////

    /*** Basic ***/

    public String name;

    public String writer;

    public String translator;

    public String publisher;

    public String originalName;

    public String pages;

    public boolean isRead;

    public boolean isInLibrary;

    /*** Basic ***/

    ////////////////////////////////////////////////////

    /*** Media ***/

    public String[] imageNames = new String[2];

    /*** Basic ***/

    ////////////////////////////////////////////////////

    /*** Purchased ***/

    public String purchasedFrom;

    public LocalDate purchasedDate;

    public String packet;

    public String price;

    /*** Purchased ***/

    ////////////////////////////////////////////////////

    /*** Extra ***/

    public String seriesName;

    public String seriesNo;

    public String editionNo;

    public String description;

    /*** Extra ***/

}

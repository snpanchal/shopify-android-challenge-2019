package com.example.shopifyandroid.models;

public class Product {

    private String name;
    private int inventory;
    private String collectionTitle;
    private String imageLink;

    public Product(String name, int inventory, String collectionTitle, String imageLink) {
        this.name = name;
        this.inventory = inventory;
        this.collectionTitle = collectionTitle;
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getCollectionTitle() {
        return collectionTitle;
    }

    public void setCollectionTitle(String collectionTitle) {
        this.collectionTitle = collectionTitle;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}

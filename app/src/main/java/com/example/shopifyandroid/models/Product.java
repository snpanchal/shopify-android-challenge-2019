package com.example.shopifyandroid.models;

public class Product {

    private String name;
    private int inventory;
    private String collectionTitle;
    private String imageLink;
    private String vendor;
    private String description;

    public Product(String name, int inventory, String collectionTitle, String imageLink, String vendor, String description) {
        this.name = name;
        this.inventory = inventory;
        this.collectionTitle = collectionTitle;
        this.imageLink = imageLink;
        this.vendor = vendor;
        this.description = description;
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

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

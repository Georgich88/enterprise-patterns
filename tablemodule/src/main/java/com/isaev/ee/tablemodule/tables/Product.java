package com.isaev.ee.tablemodule.tables;

public class Product {

    private int id;
    private String name;
    private String type;

    public Product(int productId, String name, String type) {
        this.id = productId;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

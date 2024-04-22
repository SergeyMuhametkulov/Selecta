package com.example.selectasclad;

public class ProductInList {
    private String name,material,article;

    public ProductInList(String name, String material, String article) {
        this.name = name;
        this.material = material;
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public String getMaterial() {
        return material;
    }

    public String getArticle() {
        return article;
    }
}

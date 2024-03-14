package com.example.selectasclad;

public class Product {
    private String material,article,name,param,location,total;
    public Product(){}

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Product(String material, String article, String name, String param, String location, String total) {
        this.material = material;
        this.article = article;
        this.name = name;
        this.param = param;
        this.location = location;
        this.total = total;
    }
}

package com.henghe.bean;

public class Column{
    private String id;
    private String name;

    public Column(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Column() {
    }

    @Override
    public String toString() {
        return "Column{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

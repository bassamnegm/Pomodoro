package com.example.budegtapp.Model;

public class Data {
    private String title,descreption,id,date;
    public Data()
    {


    }

    public Data(String title, String descreption, String id, String date) {
        this.title = title;
        this.descreption = descreption;
        this.id = id;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

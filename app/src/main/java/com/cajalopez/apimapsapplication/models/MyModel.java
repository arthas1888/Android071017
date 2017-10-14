package com.cajalopez.apimapsapplication.models;

import java.io.Serializable;

/**
 * Created by 74 on 07/10/2017.
 */

public class MyModel implements Serializable{

    public int id;
    public String joke;
    public String[] categories;

    public MyModel() {
    }

    public MyModel(int id, String joke, String[] categories) {
        this.id = id;
        this.joke = joke;
        this.categories = categories;
    }
}

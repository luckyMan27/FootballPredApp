package com.fortunato.footballpredictions.DataStructures;

import java.util.LinkedList;
import java.util.List;

public class SingletonFavorite {
    private static List<BaseType> instance = null;

    protected SingletonFavorite() { }

    public static List getInstance(){
        if(instance == null){
            instance = new LinkedList<BaseType>();
            return instance;
        }
        else return instance;
    }

    public static void setInstance(List<BaseType> instance) {
        SingletonFavorite.instance = instance;
    }
}

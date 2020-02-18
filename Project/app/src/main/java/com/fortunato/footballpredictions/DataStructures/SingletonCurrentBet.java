package com.fortunato.footballpredictions.DataStructures;


import java.util.LinkedList;
import java.util.List;


public class SingletonCurrentBet {
    private static List<BaseType> instance = null;

    public static List getInstance(){
        if(instance == null){
            instance = new LinkedList<BaseType>();
            return instance;
        }
        else return instance;
    }

    public static void clearInstance(){
        instance = new LinkedList<BaseType>();
    }
}

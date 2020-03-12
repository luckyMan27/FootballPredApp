package com.fortunato.footballpredictions.DataStructures;

public class SingletonCurrentFragment {
    private static int currentId;

    public static int getCurrentId(){
        return currentId;
    }

    public static void setCurrentId(int id){
        currentId = id;
    }

}

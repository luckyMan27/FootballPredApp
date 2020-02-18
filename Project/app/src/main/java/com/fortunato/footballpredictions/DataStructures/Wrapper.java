package com.fortunato.footballpredictions.DataStructures;

import java.util.ArrayList;

public class Wrapper {
    //used to pass list of objects
    ArrayList<Bet_Item> it = null;
    public Wrapper(){

    }

    public void setWrapperList(ArrayList<Bet_Item> list){
        this.it = list;
    }

    public ArrayList<Bet_Item> getList() {
        return it;
    }
}

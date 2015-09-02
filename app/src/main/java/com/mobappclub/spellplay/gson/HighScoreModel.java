package com.mobappclub.spellplay.gson;

import java.util.ArrayList;

/**
 * Created by ayush on 03/09/15.
 */
public class HighScoreModel {

    int count = 0;
    String next = " ";
    String previous = " ";
    ArrayList<IndividualScoreModel> results = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public ArrayList<IndividualScoreModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<IndividualScoreModel> results) {
        this.results = results;
    }

    public HighScoreModel(int count, String next, String previous, ArrayList<IndividualScoreModel> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }
}

package com.mobappclub.spellplay.events;

/**
 * Created by ayush on 03/09/15.
 */
public class PostResultsEvent {

    private int total_words;
    private int num_wrong_words;
    private int num_incorrect_start_words;
    private int final_score;

    public PostResultsEvent(int total_words, int num_wrong_words, int num_incorrect_start_words) {
        this.total_words = total_words;
        this.num_wrong_words = num_wrong_words;
        this.num_incorrect_start_words = num_incorrect_start_words;
        this.final_score = total_words - num_wrong_words - 3 * num_incorrect_start_words;
    }

    public int getTotal_words() {
        return total_words;
    }

    public void setTotal_words(int total_words) {
        this.total_words = total_words;
    }

    public int getNum_wrong_words() {
        return num_wrong_words;
    }

    public void setNum_wrong_words(int num_wrong_words) {
        this.num_wrong_words = num_wrong_words;
    }

    public int getNum_incorrect_start_words() {
        return num_incorrect_start_words;
    }

    public void setNum_incorrect_start_words(int num_incorrect_start_words) {
        this.num_incorrect_start_words = num_incorrect_start_words;
    }

    public int getFinal_score() {
        return final_score;
    }

    public void setFinal_score(int final_score) {
        this.final_score = final_score;
    }
}

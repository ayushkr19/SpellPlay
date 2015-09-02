package com.mobappclub.spellplay.gson;

/**
 * Created by ayush on 03/09/15.
 */
public class IndividualScoreModel {
    private int id;
    private int total_words;
    private int num_wrong_words;
    private int num_incorrect_start_words;
    private int final_score;
    private String created_at;
    private String modified_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public IndividualScoreModel(int id, int total_words, int num_wrong_words, int num_incorrect_start_words, int final_score, String created_at, String modified_at) {
        this.id = id;
        this.total_words = total_words;
        this.num_wrong_words = num_wrong_words;
        this.num_incorrect_start_words = num_incorrect_start_words;
        this.final_score = final_score;
        this.created_at = created_at;
        this.modified_at = modified_at;
    }
}

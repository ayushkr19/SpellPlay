package com.mobappclub.spellplay.events;

/**
 * Created by ayush on 15/8/15.
 */
public class ResultFetchedEvent {

    private String result;
    private String error;

    public ResultFetchedEvent(String result, String error) {
        this.result = result;
        this.error = error;
    }

    public ResultFetchedEvent(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

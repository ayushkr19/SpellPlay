package com.mobappclub.spellplay.util;

/**
 * Created by ayush on 15/8/15.
 */
public class Constants {
    public static final String authKey = "rdPVQej8dmmsh9AYKKumjfAcg1fVp1zRTLPjsnWOPF2l1F8JFJ";
    public static final String authHeader = "X-Mashape-Key";
    public static final String URL = "https://montanaflynn-spellcheck.p.mashape.com/check/";

    public static final String SERVER_URL = "http://45.55.133.118";
    public static final String PORT = ":8010";

    public static final String APP = "spellplay";

    public static final String SCORES = "scores";

    public static final String SEP = "/";

    public static final String FETCH_HIGH_SCORES = SERVER_URL + PORT +  SEP + APP + SEP + SCORES + SEP;
    public static final String POST_SCORES = SERVER_URL + PORT + SEP + APP + SEP + SCORES + SEP;

}

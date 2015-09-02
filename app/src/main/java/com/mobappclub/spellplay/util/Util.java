package com.mobappclub.spellplay.util;

/**
 * Created by ayush on 29/08/15.
 */
public class Util {

    public static int countNumWords(String words) {
        String[] splitted = words.split("\\W+");
        return splitted.length - 1;
    }

    public static String stripInvalidWords(String text, char startLetter){
        String[] splitted = text.split("\\W+");
        StringBuilder stringBuilder = new StringBuilder();

        for(String s: splitted){
            if(!s.isEmpty() && s.charAt(0) == startLetter){
                stringBuilder.append(s);
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    public static int countInvalidWords(String text, char startLetter){
        String[] splitted = text.split("\\W+");
        int count = 0;
        for(String s: splitted){
            if(!s.isEmpty() && s.charAt(0) != startLetter){
                count++;
            }
        }
        return count;
    }

    public static int calculateFinalScore(int totalWords, int numWrongWords, int numInvalidWords){
        return totalWords * 10 - 2 * numWrongWords - 5 * numInvalidWords;
    }
}

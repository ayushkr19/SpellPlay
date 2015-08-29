package com.mobappclub.spellplay.util;

/**
 * Created by ayush on 29/08/15.
 */
public class Util {

    public static int countNumWords(String words) {
        String[] splitted = words.split(" ");
        return splitted.length;
    }

    public static String stripInvalidWords(String text, char startLetter){
        String[] splitted = text.split(" ");
        StringBuilder stringBuilder = new StringBuilder();

        for(String s: splitted){
            if(!s.isEmpty() && s.charAt(0) == startLetter){
                stringBuilder.append(s);
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }
}

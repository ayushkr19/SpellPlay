package com.mobappclub.spellplay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobappclub.spellplay.events.CheckWordsFromAPIEvent;
import com.mobappclub.spellplay.events.DisplayResultsEvent;
import com.mobappclub.spellplay.events.ResultFetchedEvent;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.greenrobot.event.EventBus;


public class ScoreResultFragment extends BaseFragment {

    private static final String TAG = "ScoreResultFragment";

    public ScoreResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score_result, container, false);
    }


    public String stripInvalidWords(String text, char startLetter){
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

    public void onEventBackgroundThread(CheckWordsFromAPIEvent checkWordsFromAPIEvent) {
        OkHttpClient client = new OkHttpClient();

        String cleanedWords = stripInvalidWords(checkWordsFromAPIEvent.getWords(), checkWordsFromAPIEvent.getStartLetter());

        HttpUrl URL = new HttpUrl.Builder()
                .scheme("https")
                .host("montanaflynn-spellcheck.p.mashape.com")
                .addPathSegment("check")
                .addQueryParameter("text", cleanedWords).build();

        Request request = new Request.Builder()
                .url(URL)
                .addHeader(Constants.authHeader, Constants.authKey)
                .addHeader("Accept", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            EventBus.getDefault().post(new ResultFetchedEvent(response.body().string(), ""));

        }catch (IOException e){
            Log.e(TAG, e.getMessage());
            EventBus.getDefault().post(new ResultFetchedEvent("", e.getMessage()));
        }

    }


    public void onEventBackgroundThread(ResultFetchedEvent r){

        int numWrongWords = 0;
        HashMap<String,Object> wrongWords = new HashMap<>();

        JsonObject jsonObject = new JsonParser().parse(r.getResult()).getAsJsonObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Object o = gsonBuilder.create().fromJson(jsonObject, Object.class);
        Log.d(TAG, o.toString());
        Map m = (Map)o;
        if(((Map) o).containsKey("corrections")){
            Map<String, Object> corrections = (Map)m.get("corrections");
            Log.d(TAG, "Size : " + corrections.size());
            Log.d(TAG, corrections.toString());

            numWrongWords = corrections.size();

            for(Map.Entry<String, Object> entry : corrections.entrySet()){
                wrongWords.put(entry.getKey(), entry.getValue());
            }

            Log.d(TAG, wrongWords.toString());

            EventBus.getDefault().post(new DisplayResultsEvent(numWrongWords, wrongWords));
        }

    }

    public void onEventMainThread(DisplayResultsEvent d) {
        TextView result = (TextView) getActivity().findViewById(R.id.tv_result);

        int numWrongWords = d.getNumWrongWords();
        String text = "You got " + numWrongWords + " words wrong!\n\n";

        if(numWrongWords != 0){
            text += "The words that you got wrong are : \n\n";

            for(Map.Entry<String,Object> entry: d.getWrongWords().entrySet()){
                text += (entry.getKey() + " : " + entry.getValue().toString() + "\n");
            }
        }

        result.setText(text);
    }

}

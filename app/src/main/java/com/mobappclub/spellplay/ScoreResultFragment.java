package com.mobappclub.spellplay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobappclub.spellplay.events.CheckWordsFromAPIEvent;
import com.mobappclub.spellplay.events.DisplayResultsEvent;
import com.mobappclub.spellplay.events.PostResultsEvent;
import com.mobappclub.spellplay.events.ResultFetchedEvent;
import com.mobappclub.spellplay.util.Constants;
import com.mobappclub.spellplay.util.Util;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;


public class ScoreResultFragment extends BaseFragment {

    private static final String TAG = "ScoreResultFragment";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public ScoreResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score_result, container, false);
    }


    public void onEventBackgroundThread(CheckWordsFromAPIEvent checkWordsFromAPIEvent) {

        int numWordInRawInput = Util.countNumWords(checkWordsFromAPIEvent.getWords());
        int numInvalidWords = Util.countInvalidWords(checkWordsFromAPIEvent.getWords(),checkWordsFromAPIEvent.getStartLetter());
        String cleanedWords = Util.stripInvalidWords(checkWordsFromAPIEvent.getWords(), checkWordsFromAPIEvent.getStartLetter());

        OkHttpClient client = new OkHttpClient();
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
            EventBus.getDefault().post(new ResultFetchedEvent(response.body().string(), "", numWordInRawInput, numInvalidWords));

        }catch (IOException e){
            Log.e(TAG, e.getMessage());
            EventBus.getDefault().post(new ResultFetchedEvent("", e.getMessage(), numWordInRawInput, numInvalidWords));
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

            EventBus.getDefault().post(new DisplayResultsEvent(numWrongWords, wrongWords, r.getNumWordsInRawInput(), r.getNumInvalidWords()));
            EventBus.getDefault().post(new PostResultsEvent(r.getNumWordsInRawInput(), numWrongWords, r.getNumInvalidWords()));
        }

    }

    public void onEventMainThread(DisplayResultsEvent d) {
        TextView result = (TextView) getActivity().findViewById(R.id.tv_result);

        int numWrongWords = d.getNumWrongWords();
        int numInvalidWords = d.getNumInvalidWords();
        int totalWords = d.getNumWordsInRawInput();
        String text = "Out of the " + totalWords +  " words entered, "
                + numInvalidWords + " words are invalid & " + numWrongWords + " are wrong!\n\n";


        if(numWrongWords != 0){
            text += "The words that you got wrong are : \n\n";

            for(Map.Entry<String,Object> entry: d.getWrongWords().entrySet()){
                text += (entry.getKey() + " : " + entry.getValue().toString() + "\n");
            }
        }

        text += ("\n\n\n Your final score is : " + Util.calculateFinalScore(totalWords, numWrongWords, numInvalidWords));
        result.setText(text);
    }

    public void onEventBackgroundThread(PostResultsEvent p){

        Gson gson = new Gson();
        String json = gson.toJson(p);
        Log.d(TAG, json);

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Constants.POST_SCORES)
                .post(body)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.d(TAG, "Status code for post scores : " + response.code());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

    }

}

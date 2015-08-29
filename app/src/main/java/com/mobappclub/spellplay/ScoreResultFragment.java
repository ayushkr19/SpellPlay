package com.mobappclub.spellplay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobappclub.spellplay.events.CheckWordsFromAPIEvent;
import com.mobappclub.spellplay.events.DisplayResultsEvent;
import com.mobappclub.spellplay.events.ResultFetchedEvent;
import com.mobappclub.spellplay.util.Constants;
import com.mobappclub.spellplay.util.Util;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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


    public void onEventBackgroundThread(CheckWordsFromAPIEvent checkWordsFromAPIEvent) {

        int numWordInRawInput = Util.countNumWords(checkWordsFromAPIEvent.getWords());
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
            EventBus.getDefault().post(new ResultFetchedEvent(response.body().string(), "", numWordInRawInput));

        }catch (IOException e){
            Log.e(TAG, e.getMessage());
            EventBus.getDefault().post(new ResultFetchedEvent("", e.getMessage(), numWordInRawInput));
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

            EventBus.getDefault().post(new DisplayResultsEvent(numWrongWords, wrongWords, r.getNumWordsInRawInput()));
        }

    }

    public void onEventMainThread(DisplayResultsEvent d) {
        TextView result = (TextView) getActivity().findViewById(R.id.tv_result);

        int numWrongWords = d.getNumWrongWords();
        String text = "Out of the " + d.getNumWordsInRawInput() +  " words entered, you got " + numWrongWords + " words wrong!\n\n";

        if(numWrongWords != 0){
            text += "The words that you got wrong are : \n\n";

            for(Map.Entry<String,Object> entry: d.getWrongWords().entrySet()){
                text += (entry.getKey() + " : " + entry.getValue().toString() + "\n");
            }
        }

        result.setText(text);
    }

}

package com.mobappclub.spellplay;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobappclub.spellplay.adapter.MainFragmentAdapter;
import com.mobappclub.spellplay.events.CheckWordsFromAPIEvent;
import com.mobappclub.spellplay.events.DisplayResultsEvent;
import com.mobappclub.spellplay.events.FetchHighScoresEvent;
import com.mobappclub.spellplay.events.HighScoreFetchedEvent;
import com.mobappclub.spellplay.events.PostResultsEvent;
import com.mobappclub.spellplay.events.ResultFetchedEvent;
import com.mobappclub.spellplay.events.SetCurrentTabToScoresEvent;
import com.mobappclub.spellplay.gson.HighScoreModel;
import com.mobappclub.spellplay.gson.IndividualScoreModel;
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

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private static final String TAG = "MainActivity";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        setupToolbar();

        MainFragmentAdapter mainFragmentAdapter = MainFragmentAdapter.newInstance(getSupportFragmentManager());
        viewPager.setAdapter(mainFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_highscores) {
            Intent intent = new Intent(this, HighScoreActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    public void onEventMainThread(SetCurrentTabToScoresEvent s){
        viewPager.setCurrentItem(1, true);
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
        TextView result = (TextView) findViewById(R.id.tv_result);

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

    public void onEventBackgroundThread(FetchHighScoresEvent f){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(f.getUrl())
                .addHeader("Accept", "application/json")
                .build();

        try {
            Response response  = client.newCall(request).execute();
            String jsonString = response.body().string();


            Gson gson = new Gson();
            HighScoreModel highScoreModel = gson.fromJson(jsonString, HighScoreModel.class);

            EventBus.getDefault().post(new HighScoreFetchedEvent(highScoreModel));
        } catch (IOException e) {
            //TODO: Fix Log Tag
            Log.e(TAG, e.getMessage());
        }
    }

    public void onEventMainThread(HighScoreFetchedEvent h){

        TextView tv_high_scores = (TextView) findViewById(R.id.tv_high_scores);

        HighScoreModel highScoresModel = h.getHighScoreModel();
        Log.d(TAG, h.getHighScoreModel().toString());
        Log.d(TAG, h.getHighScoreModel().getResults().toString());
        Log.d(TAG, h.getHighScoreModel().getNext() + "");
        Log.d(TAG, h.getHighScoreModel().getPrevious() + "");
        Log.d(TAG, h.getHighScoreModel().getCount() + "");

        String highScores = "";
        int i = 1;
        for(IndividualScoreModel score : highScoresModel.getResults()){

            highScores += ("" + i + ".          " + score.getFinal_score() + "\n\n");
            i++;
        }

        tv_high_scores.setText(highScores);

    }
}

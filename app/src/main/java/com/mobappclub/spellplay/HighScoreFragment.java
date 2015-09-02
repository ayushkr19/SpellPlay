package com.mobappclub.spellplay;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobappclub.spellplay.events.FetchHighScoresEvent;
import com.mobappclub.spellplay.events.HighScoreFetchedEvent;
import com.mobappclub.spellplay.gson.HighScoreModel;
import com.mobappclub.spellplay.gson.IndividualScoreModel;
import com.mobappclub.spellplay.util.Constants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class HighScoreFragment extends BaseFragment {

    private static final String TAG = "HighScoreFragment";

    public HighScoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_high_score, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().post(new FetchHighScoresEvent(Constants.FETCH_HIGH_SCORES));
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

        TextView tv_high_scores = (TextView) getActivity().findViewById(R.id.tv_high_scores);

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

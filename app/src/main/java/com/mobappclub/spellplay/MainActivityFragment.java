package com.mobappclub.spellplay;

import android.content.pm.InstrumentationInfo;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobappclub.spellplay.events.CheckWordsFromAPIEvent;

import java.nio.charset.CharacterCodingException;
import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Button bt_start = (Button) getActivity().findViewById(R.id.bt_start);

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCountdown((Button) view);
            }
        });
    }

    public void onEvent(CheckWordsFromAPIEvent c){

    }

    private void startCountdown(final Button button) {
        final TextView tv_counter = (TextView)getActivity().findViewById(R.id.tv_counter);
        final EditText et_text = (EditText) getActivity().findViewById(R.id.et_text);
        et_text.setEnabled(true);
        et_text.setText("");

        int randomASCIICode = randInt(97, 122);
        final char randomCharacter = (char) randomASCIICode;
        button.setText("Enter words starting with " + randomCharacter);

        button.setEnabled(false);
        new CountDownTimer(30000,1000){
            @Override
            public void onTick(long millisUntilFinish) {
                tv_counter.setText("Time remaining : " + millisUntilFinish/1000);
            }

            @Override
            public void onFinish() {
                tv_counter.setText("Game over.");
                button.setEnabled(true);
                button.setText("START");
                et_text.setEnabled(false);

                String text = et_text.getText().toString().toLowerCase();
                String splitted[] = text.split("\n");
                String textt = "";
                for (String t:splitted){
                    textt = textt + " " + t;
                }

                EventBus.getDefault().post(new CheckWordsFromAPIEvent(textt, randomCharacter));

            }
        }.start();
    }

    private int randInt(int min, int max) {

        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return  rand.nextInt((max - min) + 1) + min;

    }


}

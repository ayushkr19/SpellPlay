package com.mobappclub.spellplay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mobappclub.spellplay.events.CheckWordsFromAPIEvent;
import com.mobappclub.spellplay.events.ResultFetchedEvent;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import de.greenrobot.event.EventBus;


public class ScoreResultFragment extends BaseFragment {

    private static final String TAG = "ScoreResultFragment";

    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;*/

    /*private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScoreResultFragment.
     *//*
    // TODO: Rename and change types and number of parameters
    public static ScoreResultFragment newInstance(String param1, String param2) {
        ScoreResultFragment fragment = new ScoreResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    public ScoreResultFragment() {
        // Required empty public constructor
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score_result, container, false);
    }



    public void onEventBackgroundThread(CheckWordsFromAPIEvent checkWordsFromAPIEvent) {
        OkHttpClient client = new OkHttpClient();
        // code request code here

        HttpUrl URL = new HttpUrl.Builder()
                .scheme("https")
                .host("montanaflynn-spellcheck.p.mashape.com")
                .addPathSegment("cehck")
                .addQueryParameter("text", checkWordsFromAPIEvent.getWords()).build();

        Request request = new Request.Builder()
                .url(URL)
                .addHeader(Constants.authHeader, Constants.authKey)
                .addHeader("Accept", "application/json")
                .build();
//        TextView result = (TextView) getActivity().findViewById(R.id.tv_result);
        try {
            Response response = client.newCall(request).execute();
            EventBus.getDefault().post(new ResultFetchedEvent(response.body().string(), ""));
//            result.setText(response.body().toString());

        }catch (IOException e){
            Log.e(TAG, e.getMessage());
            EventBus.getDefault().post(new ResultFetchedEvent("", e.getMessage()));
//            result.setText(e.getMessage());
        }


    }

    public void onEventMainThread(ResultFetchedEvent r) {
        TextView result = (TextView) getActivity().findViewById(R.id.tv_result);
        if(r.getError().equals(""))
        result.setText("Success :" + r.getResult());
        else result.setText("Error " + r.getError());

    }


   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

}

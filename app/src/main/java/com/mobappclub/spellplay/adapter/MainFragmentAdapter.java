package com.mobappclub.spellplay.adapter;


import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.mobappclub.spellplay.HighScoreFragment;
import com.mobappclub.spellplay.MainActivityFragment;
import com.mobappclub.spellplay.ScoreResultFragment;

import java.util.HashMap;

/**
 * Created by ayush on 03/09/15.
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {

    private HashMap<Integer, String> fragmentNames;

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
        setFragmentNames();
    }

    public HashMap<Integer, String> getFragmentNames() {
        return fragmentNames;
    }

    public void setFragmentNames() {
        this.fragmentNames = new HashMap<>();
        fragmentNames.put(0, "Game");
        fragmentNames.put(1, "Scores");
        fragmentNames.put(2, "High Scores");
    }

    public static MainFragmentAdapter newInstance(FragmentManager fm){
        return new MainFragmentAdapter(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MainActivityFragment();

            case 1:
                return new ScoreResultFragment();

            case 2:
                return new HighScoreFragment();

            default: return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

   /* @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();
        super.destroyItem(container, position, object);
    }*/

    @Override
    public CharSequence getPageTitle(int position) {

        return fragmentNames.get(position);
    }
}

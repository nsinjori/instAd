package com.foi.air1712.instad.fragmenti;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foi.air1712.instad.R;

/**
 * Created by Nikola on 7.12.2017..
 */


public class PrikazSvihFragment extends Fragment {
    public static PrikazSvihFragment newInstance() {
        PrikazSvihFragment fragment = new PrikazSvihFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prikaz_svih, container, false);
    }
}
package com.fernando.tastypoll.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.fernando.tastypoll.R;

public class AboutUs extends Fragment {
    public AboutUs(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragmen_about_us, container, false);
        Button botonAtras =  view.findViewById(R.id.button);
        botonAtras.setOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}

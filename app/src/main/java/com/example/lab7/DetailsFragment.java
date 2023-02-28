package com.example.lab7;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailsFragment extends Fragment {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            String height = bundle.getString("height");
            String mass = bundle.getString("mass");


            TextView nameTextView = view.findViewById(R.id.textView1);
            TextView heightTextView = view.findViewById(R.id.textView2);
            TextView massTextView = view.findViewById(R.id.textView3);

            nameTextView.setText(name);
            heightTextView.setText(height);
            massTextView.setText(mass);
        }
    }
}
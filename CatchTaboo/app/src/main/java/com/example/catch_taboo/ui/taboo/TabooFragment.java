package com.example.catch_taboo.ui.taboo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.catch_taboo.R;
import com.example.catch_taboo.ui.slideshow.SlideshowViewModel;

public class TabooFragment extends Fragment {
    private TabooViewModel tabooViewModel;

    private String gameName;
    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        tabooViewModel =
                ViewModelProviders.of(this).get(TabooViewModel.class);

//        tabooViewModel.setGameName(gameName);

        View root = inflater.inflate(R.layout.fragment_taboo, container, false);
        final TextView taboo1 = root.findViewById(R.id.taboo1);
        tabooViewModel.getmTaboo1().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                taboo1.setText(s);
            }
        });
        final TextView taboo2 = root.findViewById(R.id.taboo2);
        tabooViewModel.getmTaboo2().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                taboo2.setText(s);
            }
        });
        final TextView taboo3 = root.findViewById(R.id.taboo3);
        tabooViewModel.getmTaboo3().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                taboo3.setText(s);
            }
        });
        final TextView taboo4 = root.findViewById(R.id.taboo4);
        tabooViewModel.getmTaboo4().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                taboo4.setText(s);
            }
        });
        final TextView taboo5 = root.findViewById(R.id.taboo5);
        tabooViewModel.getmTaboo5().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                taboo5.setText(s);
            }
        });
        return root;
    }
}

package com.example.catch_taboo.ui.word;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.catch_taboo.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WordFragment extends Fragment {


    private WordViewModel wordViewModel;


    private String gameName;
    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        CryptoTrackerViewModelFactory factory = new CryptoTrackerViewModelFactory(this.getApplication(), 5);
//        cryptoViewModel = ViewModelProviders.of(this, factory).get(CryptoTrackerViewModel.class);
        WordModelFactory wordFactory = new WordModelFactory(gameName);
        Log.v("Game name in Frag: ", gameName);
        wordViewModel = ViewModelProviders.of(this, wordFactory).get(WordViewModel.class);

// TestPassingGameName
        View root = inflater.inflate(R.layout.fragment_word, container, false);
        final Button button = (Button) root.findViewById(R.id.screen);
        wordViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                button.setText(s); //change word random
            }
        });

        return root;
    }

}


package com.example.catch_taboo.ui.word;

import android.os.Bundle;
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

public class WordFragment extends Fragment {
    private WordViewModel wordViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
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


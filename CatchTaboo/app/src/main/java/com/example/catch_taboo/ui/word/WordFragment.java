package com.example.catch_taboo.ui.word;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        final TextView textView = root.findViewById(R.id.word);
        wordViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}

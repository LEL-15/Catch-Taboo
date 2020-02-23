package com.example.catch_taboo.ui.word;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.catch_taboo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class WordFragment extends Fragment {
    private WordViewModel wordViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        View root = inflater.inflate(R.layout.fragment_word, container, false);
        final Button button = (Button) root.findViewById(R.id.word);
        wordViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                button.setText(s); //change word random
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click = change word
//                final TextView textView = root.findViewById(R.id.word);
//                wordViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//                    @Override
//                    public void onChanged(@Nullable String s) {
//                        button.setText(s); //change word random
//                    }
//                });

                //change active team in database
                //change active player

                //onClick change the screen of the player to a different fragment
//                FragmentTransaction ft = getFragmentManager().beginTransaction(); //reload fragment
//                ft.detach(WordFragment.this).attach(WordFragment.this).commit();
            }
        });

        return root;
    }

//    public changeWord() {
//
//    }
}


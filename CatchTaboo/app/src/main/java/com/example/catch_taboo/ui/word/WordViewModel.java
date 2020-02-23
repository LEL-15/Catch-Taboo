package com.example.catch_taboo.ui.word;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catch_taboo.HomeActivity;
import com.example.catch_taboo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WordViewModel extends ViewModel {
    private int number = 10;
    public WordViewModel(String mGameName){
        Log.v("GameName: ", mGameName);
        gameName = mGameName;

        mText = new MutableLiveData<>();
        mText.setValue( "default"); //change word here!
        Log.v("Before randNum(): ", gameName);
        setRandNum();
        String wordChoice = String.valueOf(randNum);
        DocumentReference docRef = db.collection("words").document(wordChoice);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        mText.setValue( snapshot.getString("word")); //change word here!

                    } else {
//                        Log.d(TAG, "No such document");
//                        mText = new MutableLiveData<>();
                        mText.setValue( "error"); //change word here!

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });

    }


    private MutableLiveData<String> mText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //variable that gives me access to the database

    private FirebaseFirestore rootRef = db.getInstance();
    private CollectionReference ref = rootRef.collection("words");

    private String gameName;

    private double randNum = 1.0;

    public double getRandNum() {
        return randNum;
    }

    public void setRandNum() {
        Random rand = new Random();
        this.randNum = rand.nextInt(number)+1.0;//range 1 to 5
//        set in database
//        String gameName = "Better Game Name";
//        Intent loadIntent = getIntent();
//        gameName = loadIntent.getStringExtra("GAME");
        Log.v("GameName before Data: ", gameName);
        DocumentReference docRef = db.collection("games").document(gameName);

        final Map<String, Object> game = new HashMap<>();
        game.put("word", randNum);
        docRef.update(game);

    }


    public LiveData<String> getText() {

        return mText;
    }

}

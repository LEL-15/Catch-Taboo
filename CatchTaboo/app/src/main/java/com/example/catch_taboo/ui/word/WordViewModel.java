package com.example.catch_taboo.ui.word;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catch_taboo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WordViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //variable that gives me access to the database

    private FirebaseFirestore rootRef = db.getInstance();
    private CollectionReference ref = rootRef.collection("words");

    public WordViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue( "default"); //change word here!

        Random rand = new Random();
        int randNum = rand.nextInt(4)+1;//range 1 to 4
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



//        DocumentSnapshot snapshot = db.collection("words").document("1");
//        mText = new MutableLiveData<>();
//        mText.setValue( snapshot.getString("word")); //change word here!
    }

    public LiveData<String> getText() {

        return mText;
    }

}

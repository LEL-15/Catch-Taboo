package com.example.catch_taboo.ui.taboo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catch_taboo.ui.word.WordViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class TabooViewModel extends ViewModel {
    private WordViewModel wordViewModel;

    private MutableLiveData<String> mTaboo1;
    private MutableLiveData<String> mTaboo2;
    private MutableLiveData<String> mTaboo3;
    private MutableLiveData<String> mTaboo4;
    private MutableLiveData<String> mTaboo5;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //variable that gives me access to the database
    private FirebaseFirestore rootRef = db.getInstance();
    private double randNum;
//    private String wordChoice;

    private String gameName;
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


    public TabooViewModel() {
        mTaboo1 = new MutableLiveData<>();
        mTaboo2 = new MutableLiveData<>();
        mTaboo3 = new MutableLiveData<>();
        mTaboo4 = new MutableLiveData<>();
        mTaboo5 = new MutableLiveData<>();

//        String
        gameName = "Better Game Name";
//        Intent loadIntent = getIntent();
//        gameName = loadIntent.getStringExtra("GAME");
        DocumentReference docRefGame = db.collection("games").document(gameName);
        docRefGame.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshotGame = task.getResult();
                    if (snapshotGame.exists()) {
                        randNum = snapshotGame.getDouble("word");
                        Log.v("RandNum", String.valueOf(randNum));



                        String wordChoice = String.valueOf(randNum);
                        Log.v("WordChoice", wordChoice);
                        DocumentReference docRef = db.collection("words").document(wordChoice);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){

                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                                if (task.isSuccessful()) {
                                    DocumentSnapshot snapshot = task.getResult();
                                    if (snapshot.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                        mTaboo1.setValue( snapshot.getString("taboo1")); //change word here!
                                        mTaboo2.setValue( snapshot.getString("taboo2")); //change word here!
                                        mTaboo3.setValue( snapshot.getString("taboo3")); //change word here!
                                        mTaboo4.setValue( snapshot.getString("taboo4")); //change word here!
                                        mTaboo5.setValue( snapshot.getString("taboo5")); //change word here!

                                    } else {
//                        Log.d(TAG, "No such document");
//                        mText = new MutableLiveData<>();
                                        mTaboo1.setValue( "error");
                                        mTaboo2.setValue( "error");
                                        mTaboo3.setValue( "error");
                                        mTaboo4.setValue( "error");
                                        mTaboo5.setValue( "error");

                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }

                        });



                    }
                    else
                    {
                        randNum = 1.0;
                    }
                }
            }

        });





//        mText.setValue("Taboo Word");
    }

    public LiveData<String> getmTaboo1() {
        return mTaboo1;
    }
    public LiveData<String> getmTaboo2() {
        return mTaboo2;
    }

    public LiveData<String> getmTaboo3() {
        return mTaboo3;
    }

    public LiveData<String> getmTaboo4() {
        return mTaboo4;
    }

    public LiveData<String> getmTaboo5() {
        return mTaboo5;
    }
}

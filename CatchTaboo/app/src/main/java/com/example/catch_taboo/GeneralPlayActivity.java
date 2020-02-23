package com.example.catch_taboo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.catch_taboo.ui.taboo.TabooFragment;
import com.example.catch_taboo.ui.word.WordFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GeneralPlayActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //variable that gives me access to the database

    @Override
    protected void onCreate(Bundle savedInstanceState) { //method gets triggered as soon as the activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_play);
//        ((TextView)findViewById(R.id.points)).setText("10");//works

        //get game name
        final String docName = "Timer Test"; //need to pull from database
        final Context hold  = this;
        DocumentReference docRef = db.collection("games").document(docName);
//      DocumentReference docRef = db.collection("games");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        ((TextView) findViewById(R.id.team_one_score)).setText(snapshot.getString("teamOneName")+": "+ String.valueOf(snapshot.getDouble("teamOneScore")));
                        ((TextView) findViewById(R.id.team_two_score)).setText(snapshot.getString("teamTwoName")+": "+ String.valueOf(snapshot.getDouble("teamTwoScore")));
//                        ((TextView) findViewById(R.id.team_two_score)).setText(snapshot.getString("teamTwoName")+": "+ String.valueOf(snapshot.getDouble("teamTwoScore")));
                        int time = (int)Math.round(snapshot.getDouble("timeRemaining"));
                        new CountDownTimer(time * 1000, 1000) {
                            final TextView _tv = (TextView) findViewById( R.id.timePassed);

                            public void onTick(long millisUntilFinished) {
                                _tv.setText("Seconds Left: " +new SimpleDateFormat("ss").format(new Date( millisUntilFinished)));
                            }

                            public void onFinish() {
                                Log.d(TAG, "onFinish: Here");
                                Intent intent = new Intent(hold, EndGameActivity.class);
                                intent.putExtra("ID", docName);
                                startActivity(intent);
                            }
                        }.start();

                    } else {
//                        Log.d(TAG, "No such document");
//                        mText = new MutableLiveData<>();
//                        mText.setValue("error"); //change word here!
                        ((TextView) findViewById(R.id.team_one_score)).setText("Error");
                        ((TextView) findViewById(R.id.team_two_score)).setText("Error");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new WordFragment());
        ft.commit();

    }


    



}

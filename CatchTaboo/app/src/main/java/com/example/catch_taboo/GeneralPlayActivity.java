package com.example.catch_taboo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.catch_taboo.ui.taboo.TabooFragment;
import com.example.catch_taboo.ui.user.GalleryViewModel;
import com.example.catch_taboo.ui.wait.WaitFragment;
import com.example.catch_taboo.ui.word.WordFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.lang.Double.parseDouble;

public class GeneralPlayActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //variable that gives me access to the database
    //private double timeLeft = 30;
    private String currentUserID;
    private String gameName;
    private String team = "team2";
    private Boolean first;
    private ListenerRegistration registration;
    private Long startTime = Long.valueOf("0");
    private String playerTurn;
    private int count = 0;
    private int number = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //method gets triggered as soon as the activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_play);

        //get game name
        Intent loadIntent = getIntent();
        //Find out what the game's id is
        gameName = loadIntent.getStringExtra("ID");
        final Context hold = this;
        final DocumentReference docRef = db.collection("games").document(gameName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                currentUserID = user.getUid();
                if (task.isSuccessful()) {
                    final DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        ((TextView) findViewById(R.id.team_one_score)).setText(snapshot.getString("teamOneName") + ": " + String.valueOf(snapshot.getDouble("teamOneScore")));
                        ((TextView) findViewById(R.id.team_two_score)).setText(snapshot.getString("teamTwoName") + ": " + String.valueOf(snapshot.getDouble("teamTwoScore")));
//                        ((TextView) findViewById(R.id.team_two_score)).setText(snapshot.getString("teamTwoName")+": "+ String.valueOf(snapshot.getDouble("teamTwoScore")));
                        int time = (int) Math.round(snapshot.getDouble("timeRemaining"));
                        new CountDownTimer(time * 1000, 1000) {
                            final TextView _tv = (TextView) findViewById(R.id.timePassed);

                            public void onTick(long millisUntilFinished) {
                                _tv.setText("Seconds Left: " + (int) millisUntilFinished/1000);
                            }

                            public void onFinish() {
                                Log.d(TAG, "onFinish: Here");
                                if(registration != null){
                                    registration.remove();
                                }
                                Intent intent = new Intent(hold, EndGameActivity.class);
                                intent.putExtra("ID", gameName);
                                startActivity(intent);
                            }
                        }.start();
                        CollectionReference team1 = docRef.collection("team1");
                        team1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //If succesfully accessed firebase
                                if (task.isSuccessful()) {
                                    ArrayList<String> names = new ArrayList<String>();
                                    //For every plyaer in the database
                                    for (final QueryDocumentSnapshot document : task.getResult()) {
                                        if (currentUserID.equals(document.getString("id"))) {
                                            team = "team1";
                                        }
                                    }
                                    pickLayout(snapshot.getData());
                                }
                                //If failed to access firebase
                                else {
                                    Log.d("Testing", "Problem");
                                }
                            }
                        });

                    }
                    else {
                        ((TextView) findViewById(R.id.team_one_score)).setText("Error");
                        ((TextView) findViewById(R.id.team_two_score)).setText("Error");
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setLayoutActive(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        WordFragment Fragment = new WordFragment();
        Fragment.setGameName(gameName);
        ft.replace(R.id.fragment_container, Fragment);
        ft.commit();
        Long comparison = Long.valueOf("0");
        if(startTime.equals(comparison)){
            Log.d(TAG, "setLayoutActive: Set start time to a value");
            startTime = System.currentTimeMillis();
        }

        DocumentReference docRef = db.collection("games").document(gameName);
        registration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";
                if (snapshot != null && snapshot.exists()) {
                    updateActivePlayer(snapshot.getData());
                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
    }

    private void setLayoutOther(){
        startTime = Long.valueOf("0");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        TabooFragment TFragment = new TabooFragment();
        TFragment.setGameName(gameName);
        ft.replace(R.id.fragment_container, TFragment);
        ft.commit();

        DocumentReference docRef = db.collection("games").document(gameName);
        registration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";
                if (snapshot != null && snapshot.exists()) {
                    updateOtherPlayer(snapshot.getData());
                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
    }

    private void setLayoutSame(){
        startTime = Long.valueOf(0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new WaitFragment());
        ft.commit();
        DocumentReference docRef = db.collection("games").document(gameName);
        registration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";
                if (snapshot != null && snapshot.exists()) {
                    updateOtherPlayer(snapshot.getData());
                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
    }

    private void pickLayout(Map<String, Object> data) {
        if(registration != null){
            registration.remove();
        }
        Log.d(TAG, "picking layout");
        first = Boolean.parseBoolean(data.get("teamOneActive").toString());
        playerTurn = data.get("activePlayer").toString();
        //Your turn
        if (currentUserID.equals(playerTurn)) {
            setLayoutActive();
        }
        //Other Team
        else if ((team.equals("team1") != (Boolean.parseBoolean(data.get("teamOneActive").toString())))) {
            setLayoutOther();
        }
        //Same team
        else {
            setLayoutSame();
        }
    }

    //Update values when word gotten
    public void endTurn(View view) {
        Log.d(TAG, "in endturn");
        first = !first;
        //Switch which team goes first
        final Map<String, Object> data = new HashMap<>();
        data.put("teamOneActive", first);
        //Switch to new word
        Random rand = new Random();
        Double randomNumber = rand.nextInt(number)+1.0;
        data.put("word", randomNumber);
        //Switch active player
        String teamFirst = "team2";
        if (first) {
            teamFirst = "team1";
        }
        CollectionReference teamNext = db.collection("games").document(gameName).collection(teamFirst);
        teamNext.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //If succesfully accessed firebase
                if (task.isSuccessful()) {
                    // create instance of Random class
                    Random rand = new Random();
                    // Generate random integers in range 0 to 999
                    int randomPlayer = rand.nextInt(task.getResult().size());
                    //For every plyaer in the database
                    int count = 0;
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "onComplete: per player");
                        if (count == randomPlayer) {
                            Log.d(TAG, "onComplete: found right player with id " + document.get("id"));
                            data.put("activePlayer", document.get("id"));
                        }
                        count += 1;
                    }
                    Log.d(TAG, "data is " + data);
                    DocumentReference game = db.collection("games").document(gameName);
                    game.update(data);
                }
                //If failed to access firebase
                else {
                    Log.d("Testing", "Problem");
                }
            }
        });
    }

    private void updateActivePlayer(Map<String, Object> data) {
        Log.d(TAG, "updateActivePlayer: count" + count);
        Log.d(TAG, "data is" + data);
        if(count > 0){
            //Got the word
            if (!currentUserID.equals(data.get("activePlayer"))) {
                //Change Score
                int scoreEarned = 100 - (int)((System.currentTimeMillis() - startTime) / 1000);
                Log.d(TAG, "Score earned was: " + scoreEarned);
                Log.d(TAG, "start time: " + startTime);
                Log.d(TAG, "now time: " + System.currentTimeMillis());
                if (scoreEarned < 0) {
                    scoreEarned = 0;
                }
                if (team.equals("team1")) {
                    DocumentReference document = db.collection("games").document(gameName);
                    document.update("teamOneScore", FieldValue.increment(scoreEarned));
                } else {
                    DocumentReference document = db.collection("games").document(gameName);
                    document.update("teamTwoScore", FieldValue.increment(scoreEarned));
                }
                pickLayout(data);
                updateScore();
                count = 0;
            }
            //Got buzzed
            else{
                Log.d(TAG, "updateActivePlayer: got buzzed");
                pickLayout(data);
                count = 0;
            }
        }
        else{
            count++;
        }
    }

    private void updateOtherPlayer(Map<String, Object> data) {
        Log.d(TAG, "updateOtherPlayer: " + count);
        if(count > 1){
            pickLayout(data);
            updateScore();
            count = 0;
        }
        count++;
    }
    public void buzz(View view){
        setRandNum();
        setLayoutOther();
    }
    public void updateScore(){
        final DocumentReference docRef = db.collection("games").document(gameName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                currentUserID = user.getUid();
                if (task.isSuccessful()) {
                    final DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String teamOneScore = snapshot.getString("teamOneName") + ": " + String.valueOf(snapshot.getDouble("teamOneScore"));
                        String teamTwoScore = snapshot.getString("teamTwoName") + ": " + String.valueOf(snapshot.getDouble("teamTwoScore"));
                        ((TextView) findViewById(R.id.team_one_score)).setText(teamOneScore);
                        ((TextView) findViewById(R.id.team_two_score)).setText(teamTwoScore);
                    }
                    else {
                        ((TextView) findViewById(R.id.team_one_score)).setText("Error");
                        ((TextView) findViewById(R.id.team_two_score)).setText("Error");
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    public int setRandNum() {
        //Made new thing
        Random rand = new Random();
        Double randomNumber = rand.nextInt(number)+1.0;//range 1 to 5
        DocumentReference docRef = db.collection("games").document(gameName);
        final Map<String, Object> game = new HashMap<>();
        game.put("word", randomNumber);
        docRef.update(game);

        return 1;
    }
}

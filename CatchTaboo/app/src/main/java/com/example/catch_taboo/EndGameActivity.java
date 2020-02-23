package com.example.catch_taboo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EndGameActivity extends AppCompatActivity {
    private String gameName;
    private String firstTeam;
    private String secondTeam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent loadIntent = getIntent();
        //Find out what the game's id is
        gameName = loadIntent.getStringExtra("ID");
        setContentView(R.layout.activity_end_game);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d(TAG, "onCreate: " + gameName);
        final DocumentReference docRef = db.collection("games").document(gameName);
        final CollectionReference collectionRef = docRef.collection("team1");
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //If succesfully accessed firebase
                if (task.isSuccessful()) {
                    ArrayList<String> names = new ArrayList<String>();
                    //For every plyaer in the database
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        String documentId = document.getId();
                        collectionRef.document(documentId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                }
                //If failed to access firebase
                else {
                    Log.d("Testing", "Problem");
                }
            }
        });
        final CollectionReference collectionRef1 = docRef.collection("team2");
        collectionRef1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //If succesfully accessed firebase
                if (task.isSuccessful()) {
                    ArrayList<String> names = new ArrayList<String>();
                    //For every plyaer in the database
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        String documentId = document.getId();
                        collectionRef1.document(documentId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                }
                //If failed to access firebase
                else {
                    Log.d("Testing", "Problem");
                }
            }
        });
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Double teamOneScore = snapshot.getDouble("teamOneScore");
                    Double teamTwoScore = snapshot.getDouble("teamTwoScore");
                    String winner;
                    Double bestScore;
                    Double worstScore;
                    if(teamOneScore.equals(teamTwoScore)){
                        firstTeam = snapshot.getString("teamOneName");
                        secondTeam = snapshot.getString("teamTwoName");
                        bestScore = teamOneScore;
                        worstScore = teamTwoScore;
                        winner = "It's a tie!";
                    }
                    else if(teamOneScore > teamTwoScore){
                        firstTeam = snapshot.getString("teamOneName");
                        secondTeam = snapshot.getString("teamTwoName");
                        bestScore = teamOneScore;
                        worstScore = teamTwoScore;
                        winner = firstTeam + " won!";
                    }
                    else{
                        firstTeam = snapshot.getString("teamTwoName");
                        secondTeam = snapshot.getString("teamOneName");
                        bestScore = teamTwoScore;
                        worstScore = teamOneScore;
                        winner = firstTeam + " won!";
                    }
                    TextView winText = findViewById(R.id.winner);
                    winText.setText(winner);
                    TextView firstTeamText = findViewById(R.id.firstTeamScore);
                    firstTeamText.setText(firstTeam + ": " + bestScore );
                    TextView secondTeamText = findViewById(R.id.secondTeamScore);
                    secondTeamText.setText(secondTeam + ": " + worstScore );
                }
                else {

                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
            }

        });
    }

    public void done(final View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void restart(final View view){
        Intent intent = new Intent(this, JoinTeamActivity.class);
        intent.putExtra("GAME", gameName);
        startActivity(intent);
    }

}

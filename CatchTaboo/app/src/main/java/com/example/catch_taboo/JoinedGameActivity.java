package com.example.catch_taboo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoinedGameActivity extends AppCompatActivity {
    String TAG = "testing";

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context hold = this;
        Intent loadIntent = getIntent();
        //Find out what the game's id is
        String id = loadIntent.getStringExtra("ID");
        Log.d(TAG, "onCreate: id is" + id);
        setContentView(R.layout.activity_joined_game);
        DocumentReference game = rootRef.collection("games").document(id);
        CollectionReference team1 = game.collection("team1");
        team1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //If succesfully accessed firebase
                if (task.isSuccessful()) {
                    ArrayList<String> names = new ArrayList<String>();
                    //For every plyaer in the database
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        names.add(document.getString("name"));
                    }
                    // Create an ArrayAdapter from List
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (hold, android.R.layout.simple_list_item_1, names);

                    // DataBind ListView with items from ArrayAdapter
                    final ListView lv = (ListView) findViewById(R.id.teamOneList);
                    lv.setAdapter(arrayAdapter);
                }
                //If failed to access firebase
                else {
                    Log.d("Testing", "Problem");
                }
            }
        });
        CollectionReference team2 = game.collection("team2");
        team2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //If succesfully accessed firebase
                if (task.isSuccessful()) {
                    ArrayList<String> names = new ArrayList<String>();
                    //For every plyaer in the database
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        names.add(document.getString("name"));
                    }
                    // Create an ArrayAdapter from List
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (hold, android.R.layout.simple_list_item_1, names);

                    // DataBind ListView with items from ArrayAdapter
                    final ListView lv = (ListView) findViewById(R.id.teamTwoList);
                    lv.setAdapter(arrayAdapter);
                }
                //If failed to access firebase
                else {
                    Log.d("Testing", "Problem");
                }
            }
        });
        game.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Set Name of Team 1
                final TextView teamOneName = (TextView) findViewById(R.id.teamOneName);
                String teamOneNameString = documentSnapshot.getString("teamOneName");
                teamOneName.setText(teamOneNameString);
                //Set Name of Team 2
                final TextView teamTwoName = (TextView) findViewById(R.id.teamTwoName);
                String teamTwoNameString = documentSnapshot.getString("teamTwoName");
                teamTwoName.setText(teamTwoNameString);
                //Set game name
                final TextView gameName = (TextView) findViewById(R.id.gameName);
                String gameNameString = documentSnapshot.getString("name");
                gameName.setText(gameNameString);

            }
        });
    }
}

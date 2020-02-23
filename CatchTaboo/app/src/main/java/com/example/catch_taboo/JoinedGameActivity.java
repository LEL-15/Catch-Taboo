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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JoinedGameActivity extends AppCompatActivity {
    String TAG = "testing";

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private int changes = 0;
    private int teamChanges = 0;
    private Boolean first;
    private String gameName;
    private ListenerRegistration registration;
    private ListenerRegistration registration1;
    private ListenerRegistration registration2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context hold = this;
        Intent loadIntent = getIntent();
        //Find out what the game's id is
        gameName = loadIntent.getStringExtra("ID");
        setContentView(R.layout.activity_joined_game);
        DocumentReference game = db.collection("games").document(gameName);
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
                    gameValueChanged(snapshot.getData());
                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
        Query query = db.collection("games").document(gameName).collection("team1");
        registration1 = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            // [START_EXCLUDE]
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                teamValueChanged();
            }
        });
        query = db.collection("games").document(gameName).collection("team2");
        registration2 = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            // [START_EXCLUDE]
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                teamValueChanged();
            }
        });
    }
    public void gameValueChanged(Map<String, Object> game) {
        Log.d(TAG, "gameValueChanged");
        changes+=1;
        //You just loaded the page
        if(changes == 1){
            first = Boolean.parseBoolean(game.get("teamOneActive").toString());
        }
        else{
            //Somebody started the game
            if(Boolean.parseBoolean(game.get("teamOneActive").toString()) != first){
                registration.remove();
                registration1.remove();
                registration2.remove();
                Intent intent = new Intent(this, GeneralPlayActivity.class);
                intent.putExtra("ID", gameName);
                startActivity(intent);
            }
        }
    }
    public void teamValueChanged() {
        Log.d(TAG, "teamValueChanged: ");
        teamChanges+=1;
        //You just loaded the page
        if(teamChanges > 2){
            Intent intent = new Intent(this, JoinedGameActivity.class);
            intent.putExtra("ID", gameName);
            startActivity(intent);
        }
    }
    public void startGame(View v){
        final Context hold = this;
        //Switch which team goes first
        final Map<String, Object> data = new HashMap<>();
        data.put("teamOneActive", !first);
        //Switch active player
        String teamFirst = "team2";
        if(first){
            teamFirst = "team1";
        }
        CollectionReference team2 = db.collection("games").document(gameName).collection(teamFirst);
        team2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //If succesfully accessed firebase
                if (task.isSuccessful()) {
                    // create instance of Random class
                    Random rand = new Random();
                    // Generate random integers in range 0 to 999
                    int randomPlayer = rand.nextInt(task.getResult().size());
                    Log.d(TAG, "random player: " + randomPlayer);
                    //For every plyaer in the database
                    int count = 0;
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "onComplete: per player");
                        if(count == randomPlayer){
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
}

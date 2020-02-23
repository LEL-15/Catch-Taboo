package com.example.catch_taboo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.catch_taboo.ui.user.GalleryViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateGameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When this function is called
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
    }

    //Functions for each button pushed
    public void createGame(View item) {
        //Write new player
        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference games = rootRef.collection("games");
        //Get game name
        EditText gameBox = findViewById(R.id.gameName);
        final String gameName = gameBox.getText().toString();
        //Get team one name
        EditText oneBox = findViewById(R.id.teamOneName);
        final String teamOneName = oneBox.getText().toString();
        //Get team two name
        EditText twoBox = findViewById(R.id.teamTwoName);
        final String teamTwoName = twoBox.getText().toString();
        //Get time
        Spinner timeSpinner = (Spinner) findViewById(R.id.pickTime);
        final String time = timeSpinner.getSelectedItem().toString();
        int timeRemaining = 0;
        if(time.equals("30 Seconds")){
            timeRemaining = 30;
        }
        else if(time.equals("1 Minute")){
            timeRemaining = 60;
        }
        else if(time.equals("1 Minute 30 Seconds")){
            timeRemaining = 90;
        }
        else if(time.equals("2 Minutes")){
            timeRemaining = 120;
        }
        else if(time.equals("3 Minutes")){
            timeRemaining = 180;
        }
        else if(time.equals("4 Minutes")){
            timeRemaining = 240;
        }
        else if(time.equals("5 Minutes")){
            timeRemaining = 300;
        }


        final Context hold = this;

        final Map<String, Object> game = new HashMap<>();
        game.put("name", gameName);
        game.put("timeRemaining", timeRemaining);
        game.put("teamOneScore", 0);
        game.put("teamTwoScore", 0);
        game.put("teamOneName", teamOneName);
        game.put("teamTwoName", teamTwoName);
        game.put("teamOneActive", (Math.random() < 0.5));
        game.put("activePlayer", "");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String UID = user.getUid();
        DocumentReference users = rootRef.collection("users").document(UID);
        users.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("name");
                final Map<String, Object> player = new HashMap<>();
                player.put("name", name);
                player.put("id", UID);
                DocumentReference newGame = games.document(gameName);
                CollectionReference team1 = newGame.collection("team1");
                CollectionReference team2 = newGame.collection("team2");
                DocumentReference newPlayer = team1.document(UID);
                newPlayer.set(player);
                newGame.set(game).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(hold, JoinedGameActivity.class);
                        intent.putExtra("ID", gameName);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}

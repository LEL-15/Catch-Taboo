package com.example.catch_taboo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JoinTeamActivity extends AppCompatActivity {
    String TAG = "testing";
    String gameName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_team);
        final Context hold = this;
        Intent loadIntent = getIntent();
        gameName = loadIntent.getStringExtra("GAME");
        DocumentReference game = db.collection("games").document(gameName);
        game.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Set Name of Team 1
                final TextView teamOneName = (TextView) findViewById(R.id.team1);
                String teamOneNameString = documentSnapshot.getString("teamOneName");
                teamOneName.setText(teamOneNameString);
                //Set Name of Team 2
                final TextView teamTwoName = (TextView) findViewById(R.id.team2);
                String teamTwoNameString = documentSnapshot.getString("teamTwoName");
                teamTwoName.setText(teamTwoNameString);

            }
        });
    }

    public void joinGame(View item) {
        //Write new player
        final Context hold = this;
        RadioButton team1 = findViewById(R.id.team1);
        RadioButton team2 = findViewById(R.id.team2);
        String team = "";
        if (team1.isChecked()) {
            team = team1.getText().toString();
        } else if (team2.isChecked()) {
            team = team2.getText().toString();
        }
        final String teamName = team;
        if (!team.equals("")) {

            final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            final DocumentReference game = rootRef.collection("games").document(gameName);
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
                    game.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String teamOneName = documentSnapshot.getString("teamOneName");
                            String collectionName;
                            if (teamName.equals(teamOneName)) {
                                collectionName = "team1";
                            } else {
                                collectionName = "team2";
                            }
                            game.collection(collectionName).add(player);
                            Intent intent = new Intent(hold, JoinedGameActivity.class);
                            intent.putExtra("ID", gameName);
                            startActivity(intent);
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(JoinTeamActivity.this, "You must pick a team.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.catch_taboo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.google.firebase.Timestamp.now;

public class CreateGameActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Boolean> picked = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When this function is called
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Populate the categories list
        final Context hold = this;
        DocumentReference codesRef = db.collection("categories").document("categories");
        codesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> map = task.getResult().getData();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        Log.d("TAG", entry.getValue().toString());
                        names.add(entry.getValue().toString());
                        picked.add(false);
                    }
                    // Create an ArrayAdapter from List
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (hold, android.R.layout.simple_list_item_multiple_choice, names);
                    // DataBind ListView with items from ArrayAdapter
                    final ListView lv = findViewById(R.id.categories);
                    lv.setAdapter(arrayAdapter);
                }
            }
        });
        ListView categories = (ListView)findViewById(R.id.categories);
        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // change the checkbox state
                CheckedTextView checkedTextView = ((CheckedTextView)view);
                checkedTextView.setChecked(!checkedTextView.isChecked());
                picked.set(position, checkedTextView.isChecked());
            }
        });
    }

    //Functions for each button pushed
    public void createGame(View item) {
        //Write new player
        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference games = rootRef.collection("games");
        final Context hold = this;
        //Get game name
        EditText gameBox = findViewById(R.id.gameName);
        final String gameName = gameBox.getText().toString();
        Log.d("TAG", "gameName is: " + gameName);
        //Check that gameName is not already in database
        DocumentReference docRef = games.document(gameName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Boolean valid = true;
                    String validText = "";
                    if (document.exists()) {
                        Date createDate =document.getTimestamp("createTime").toDate();
                        Duration d =
                                Duration.between(                   // Calculate the span of time between two moments as a number of hours, minutes, and seconds.
                                        createDate.toInstant() ,    // Convert legacy class to modern class by calling new method added to the old class.
                                        Instant.now()                   // Capture the current moment in UTC. About two and a half hours later in this example.
                                )
                                ;
                        long hoursPassed = d.toHours();
                        //Check if it's been 15 hours since create time
                        if (hoursPassed > 15){
                            games.document(gameName).delete();
                        }
                        else {
                            valid = false;
                            validText = "That game name is not available";
                        }
                    }
                    //Get team one name
                    EditText oneBox = findViewById(R.id.teamOneName);
                    final String teamOneName = oneBox.getText().toString();

                    //Get team two name
                    EditText twoBox = findViewById(R.id.teamTwoName);
                    final String teamTwoName = twoBox.getText().toString();

                    //Make sure name for everything was provided
                    if(gameName.length() == 0 || teamOneName.length() == 0 || teamTwoName.length() == 0){
                        valid = false;
                        validText = "Be sure to give each team and the game a name";
                    }
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
                    else{
                        valid = false;
                        validText = "You must pick a time.";
                    }

                    final Map<String, Object> game = new HashMap<>();

                    //Get and set categories
                    Boolean aCategory = false;
                    for (int i = 0; i < picked.size(); i++) {
                        game.put(names.get(i), picked.get(i));
                        if(picked.get(i)){
                            aCategory = true;
                        }
                    }
                    if(!aCategory) {
                        valid = false;
                        validText = "You must pick at least one category";
                    }
                    if(valid) {
                        //Put all other game data
                        Timestamp tsTemp = now();
                        game.put("createTime", tsTemp);
                        game.put("name", gameName);
                        game.put("timeRemaining", timeRemaining);
                        game.put("teamOneScore", 0);
                        game.put("teamTwoScore", 0);
                        game.put("teamOneName", teamOneName);
                        game.put("teamTwoName", teamTwoName);
                        game.put("teamOneActive", (Math.random() < 0.5));
                        game.put("activePlayer", "");
                        game.put("category", 1.0);
                        game.put("word", 1.0);
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
                    else{
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        Toast.makeText(context, validText, duration).show();
                    }
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

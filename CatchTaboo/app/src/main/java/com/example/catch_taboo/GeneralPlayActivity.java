package com.example.catch_taboo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GeneralPlayActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //variable that gives me access to the database



    @Override
    protected void onCreate(Bundle savedInstanceState) { //method gets triggered as soon as the activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_play);
//        ((TextView)findViewById(R.id.points)).setText("10");//works
    }

//    private AppBarConfiguration mAppBarConfiguration;
//    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//    CollectionReference ref = rootRef.collection("games");
//    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //should change the value of team_one_score on data change replicate for team_two_score if works
    DocumentReference docRef = db.collection("games").document("oslnyO5nLR3TQU8Vq7hX");
    public void onDataChange(DocumentReference docRef) {
        ((TextView)findViewById(R.id.team_one_score)).setText((CharSequence) docRef);
    }




}

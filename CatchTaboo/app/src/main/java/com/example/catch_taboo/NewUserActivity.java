package com.example.catch_taboo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewUserActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //variable that gives me access to the database
    String TAG = "Testing";

    @Override
    protected void onCreate(Bundle savedInstanceState) { //method gets triggered as soon as the activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        mAuth = FirebaseAuth.getInstance();
    }

    public void goToHomeScreen(View view) {

        EditText editText = findViewById(R.id.enter_text);
        final String name = editText.getText().toString();

        //makes sure there is valid values for the text fields
        if(name.length() != 0){
            //once it meets the previous criteria, it can attempt to create a new account in Firebase
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            CollectionReference ref = rootRef.collection("users");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("id", user.getUid());

            DocumentReference newPlayer = ref.document(user.getUid());
            newPlayer.set(data);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        else {
            //if user leaves some of the fields empty
            Toast.makeText(NewUserActivity.this, "Please enter a value.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

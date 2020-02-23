package com.example.catch_taboo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.catch_taboo.ui.user.GalleryViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    String TAG = "testing";

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void goToJoScreen(View view) {
        Intent intent = new Intent(this, GeneralPlayActivity.class);
        intent.putExtra("ID", "Timer Test");
        startActivity(intent);
    }

    public void updateUserName(View view){
        EditText editText = findViewById(R.id.name);
        final String name = editText.getText().toString();

        //makes sure there is valid values for the text fields
        if(name.length() != 0){
            //once it meets the previous criteria, it can attempt to create a new account in Firebase
            CollectionReference ref = rootRef.collection("users");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);

            DocumentReference newPlayer = ref.document(user.getUid());
            newPlayer.update(data);
            Toast.makeText(HomeActivity.this, "Changes saved.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            //if user leaves some of the fields empty
            Toast.makeText(HomeActivity.this, "Please enter a value.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void signOut(View v) {
        // Firebase sign out
        mAuth.signOut();
        final Context hold = this;
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(hold, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
    public void createGame(View v){
        Intent intent = new Intent(this, CreateGameActivity.class);
        startActivity(intent);
    }
    public void joinGame(View v){
        Intent intent = new Intent(this, JoinGameActivity.class);
        startActivity(intent);
    }

}

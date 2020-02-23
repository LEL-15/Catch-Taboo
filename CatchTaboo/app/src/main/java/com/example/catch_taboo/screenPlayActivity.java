package com.example.catch_taboo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.catch_taboo.ui.taboo.TabooFragment;
import com.example.catch_taboo.ui.word.WordFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class screenPlayActivity extends FragmentActivity {
    //listen for button and change screens of players
//    android:name="com.example.catch_taboo.ui.word.WordFragment"
//    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //variable that gives me access to the database
//    private FirebaseFirestore rootRef = db.getInstance();
//    private String activeId;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.d("Place", "Made it just inside onCreate");
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.active_play);
//
//        // Check that the activity is using the layout version with
//        // the fragment_container FrameLayout
//        if (findViewById(R.id.fragment_container) != null) {
//
//            // However, if we're being restored from a previous state,
//            // then we don't need to do anything and should return or else
//            // we could end up with overlapping fragments.
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            WordFragment wordFragment = new WordFragment();
//            wordFragment.setArguments(getIntent().getExtras());
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, wordFragment).commit();
//
//            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//            CollectionReference ref = rootRef.collection("users");
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//            String Id = user.getUid();
//            Log.d("USER ID", Id);
//
//
//            //get activePlayer info
//    //        String activeId = "";
//
//            String gameName = "Better Game Name"; //need to pull from database
////            Intent loadIntent = getIntent();
////            gameName = loadIntent.getStringExtra("GAME");
//
//            DocumentReference docRef = FirebaseFirestore.getInstance().collection("games").document(gameName);
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot snapshot = task.getResult();
//                        if (snapshot.exists()) {
//                            activeId = snapshot.getString("activePlayer");
//                            Log.d("Active ID", activeId);
//
//                        } else {
//                            activeId = "error";
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                }
//
//            });
//
//
//
//    //        View root = inflater.inflate(R.layout.fragment_user, container, false);
//            //nested if statements
//            if (Id.equals(activeId))
//            {
//                //they are the active player and screen fragment should be WordFragment
//    //            final TextView textView = root.findViewById(R.id.screen);
//    //            textView.setName();
//                WordFragment wordFragment = new WordFragment();
//                wordFragment.setArguments(getIntent().getExtras());
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.fragment_container, wordFragment).commit();
//
//            }
//            else //either on the same team or on different team
//            {
//                //is on different teams
//                TabooFragment tabooFragment = new TabooFragment();
//                tabooFragment.setArguments(getIntent().getExtras());
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.fragment_container, tabooFragment).commit();
//
//            }
//
//        }
//    }

}

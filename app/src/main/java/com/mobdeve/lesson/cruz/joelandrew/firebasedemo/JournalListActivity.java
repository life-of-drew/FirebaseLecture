package com.mobdeve.lesson.cruz.joelandrew.firebasedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class JournalListActivity extends AppCompatActivity {

    //Firebase Authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //Firebase Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Journal");

    //Firebase Storage
    private StorageReference storageReference;

    //List of Journals
    private List<Journal> journalList;

    //RecyclerView
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    // Widgets
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        //Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //Widgets
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Posts ArrayList
        journalList = new ArrayList<>();
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JournalListActivity.this,
                        AddJournalActivity.class);
                startActivity(i);
            }
        });
    }

    //Add a Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_add) {

            if (user != null && firebaseAuth != null) {
                Intent i = new Intent(JournalListActivity.this,
                        AddJournalActivity.class);
                startActivity(i);
            }
        } else if (itemId == R.id.action_sign_out) {
            if (user != null && firebaseAuth != null) {
                firebaseAuth.signOut();
                Intent i = new Intent(JournalListActivity.this,
                        MainActivity.class);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //QueryDocument Snapshot is an object that represents a single document
                //retrieved from a Firestore query.

                for (QueryDocumentSnapshot journals: queryDocumentSnapshots){

                    // Convert the document into a custom Object (Journal)
                    Journal journal = journals.toObject(Journal.class);
                    journalList.add(journal);
                }

                //RecyclerView
                myAdapter = new MyAdapter(JournalListActivity.this, journalList);
                recyclerView.setAdapter(myAdapter);

                //DON'T FORGET: This refreshes the RecyclerView for incoming new data
                myAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(JournalListActivity.this,
                        "Oops... Something went wrong",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
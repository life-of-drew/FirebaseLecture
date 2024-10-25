package com.mobdeve.lesson.cruz.joelandrew.firebasedemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    //Initialize Widgets
    EditText password_create, username_create, email_create;
    Button createBtn;

    //Firebase Authentication
    //An object from FirebaseAuth class that allows user Interact with the authentication system

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firebase Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        password_create = findViewById(R.id.password_create);
        username_create = findViewById(R.id.username_create_ET);
        email_create = findViewById(R.id.email_create);
        createBtn = findViewById(R.id.acc_signUp_btn);

        //Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //Listening for changes in the authentication state and respond accordingly with changes
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               currentUser = firebaseAuth.getCurrentUser();

               //Check the user if logged in or not
                if (currentUser !=null){
                    //User is Logged In
                }else{
                    //User is Logged Out
                }
            }
        };

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(email_create.getText().toString())
                        &&  !TextUtils.isEmpty(username_create.getText().toString())
                        && !TextUtils.isEmpty(password_create.getText().toString())){

                    String email = email_create.getText().toString().trim();
                    String pass = password_create.getText().toString().trim();
                    String username = username_create.getText().toString().trim();

                    CreateUser(email, pass, username);

                }else{
                    Toast.makeText(
                            SignUpActivity.this,
                            "Please fill out all the fields!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CreateUser(String email, String pass, String username){
        if(!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(pass)&& !TextUtils.isEmpty(username)){
            //Only accept non-null values
            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //The user is created successfully!
                        Toast.makeText(SignUpActivity.this,
                                "Account Created Successfully",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
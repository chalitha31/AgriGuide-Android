package lk.chalitha.agriguide;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lk.chalitha.agriguide.model.User;

public class Sign_up extends AppCompatActivity {
    EditText username, fname, lname, password, email, contact;
    Button signUpButton;

    FirebaseAuth mAuth;

    ProgressBar progressBar;

    private DatabaseReference mDatabase;

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        if (currentUser != null) {
//            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//            finish();
//
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final dialog loadingdialog = new dialog(Sign_up.this);

        TextView gotoSignIn = findViewById(R.id.goSignIn);
        gotoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


//    signup authentication  //

//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }


        username = findViewById(R.id.signUpUsername);
        fname = findViewById(R.id.signUpfname);
        lname = findViewById(R.id.signUplname);
        password = findViewById(R.id.signUpPassword);
        email = findViewById(R.id.signUpEmail);
        contact = findViewById(R.id.signUpContact2);
        signUpButton = findViewById(R.id.signUpButton);
//        progressBar = findViewById(R.id.progressBar);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String getusername, getfname, getlname, getpassword, getemail, getcontact;

                getusername = username.getText().toString();
                getfname = fname.getText().toString();
                getlname = lname.getText().toString();
                getpassword = password.getText().toString();
                getemail = email.getText().toString();
                getcontact = contact.getText().toString();

//                System.out.println(getusername);
//                System.out.println(getfname);
//                System.out.println(getlname);
//                System.out.println(getpassword);
//                System.out.println(getemail);
//                System.out.println(getcontact);

                if (TextUtils.isEmpty(getusername)) {
                    Toast.makeText(Sign_up.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(getfname)) {
                    Toast.makeText(Sign_up.this, "Please Enter first Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(getlname)) {
                    Toast.makeText(Sign_up.this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(getpassword)) {
                    Toast.makeText(Sign_up.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (getpassword.length() < 6) {
                    Toast.makeText(Sign_up.this, "Password should be more than 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(getemail)) {
                    Toast.makeText(Sign_up.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(getemail).matches()) {
                    Toast.makeText(Sign_up.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (TextUtils.isEmpty(getemail)){
//                    Toast.makeText(Sign_up.this,"This Email already exist!",Toast.LENGTH_SHORT).show();
//                    return;
//                }

                else if (TextUtils.isEmpty(getcontact)) {
                    Toast.makeText(Sign_up.this, "Please Enter Contact Number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (getcontact.length() != 10) {
                    Toast.makeText(Sign_up.this, "Invalid Contact Number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    progressBar.setVisibility(View.VISIBLE);

                    loadingdialog.startLoadingdialog();

                    mAuth.createUserWithEmailAndPassword(getemail, getpassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    progressBar.setVisibility(View.GONE);
                                    loadingdialog.dismissdialog();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Sign_up.this, "Sign Up Successfully!",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        saveUserDataToFirebase(currentUser.getUid(), getusername, getfname, getlname, getemail, getcontact);

                                        startActivity(new Intent(Sign_up.this, MainActivity.class));
                                        finish();
                                        // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "signInWithEmail:success");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                                                       // Update UI or navigate to the next screen
//                                    updateUI(currentUser);
                                    } else {
                                        // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Sign_up.this, "Authentication failed." + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                    }
                                }
                            });
                }

            }
        });


//    signup authentication  //

    }

    private void saveUserDataToFirebase(String userId, String username, String fname, String lname, String email, String contact) {
//        User user = new User(username, fname, lname, email, contact);

        User user = new User(username, fname, lname, email, contact);

        // Save user data to "users" node in Firebase Realtime Database
        System.out.println("1");
        mDatabase.child(userId).setValue(user);
        System.out.println("2");
    }

}
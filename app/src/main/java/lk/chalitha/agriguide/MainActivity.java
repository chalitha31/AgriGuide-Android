package lk.chalitha.agriguide;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class MainActivity extends AppCompatActivity {

    EditText signinemail, signinpassword, lname;
    Button signInButton;

    FirebaseAuth mAuth;

    ProgressBar progressBar;

//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//            finish();
//
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final dialog loadingdialog = new dialog(MainActivity.this);

        TextView goToSignUp = findViewById(R.id.goSignIn);
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Sign_up.class);
                startActivity(intent);
                finish();
            }
        });


//      sign in firebase


//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }


        signinemail = findViewById(R.id.signInEmail);
        signinpassword = findViewById(R.id.signInPassword);
//        lname = findViewById(R.id.signUplname);
//        password = findViewById(R.id.signUpPassword);
//        email = findViewById(R.id.signUpEmail);
//        contact = findViewById(R.id.signUpContact2);
        signInButton = findViewById(R.id.signInButton);
//        progressBar = findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String getsigninemail, getsignpassword;

                getsigninemail = signinemail.getText().toString();
                getsignpassword = signinpassword.getText().toString();

                if (TextUtils.isEmpty(getsigninemail)) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(getsignpassword)) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    progressBar.setVisibility(View.VISIBLE);
                    loadingdialog.startLoadingdialog();

                    mAuth.signInWithEmailAndPassword(getsigninemail, getsignpassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    progressBar.setVisibility(View.GONE);
                                    loadingdialog.dismissdialog();
                                    if (task.isSuccessful()) {

                                        Toast.makeText(MainActivity.this, "Login Successful",
                                                Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();

                                        // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "signInWithEmail:success");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                    }
                                }
                            });
                }

            }
        });

    }
}
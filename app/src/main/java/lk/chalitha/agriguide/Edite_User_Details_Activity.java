package lk.chalitha.agriguide;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edite_User_Details_Activity extends AppCompatActivity {

    public static final String TAG = Edite_User_Details_Activity.class.getName();

    TextInputEditText cfname, clname, cmobile, cuid, cusername, curnntpassword;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_user_details);

        Toolbar t = findViewById(R.id.toolbar2);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final dialog loadingdialog = new dialog(Edite_User_Details_Activity.this);
        //        view details //

        cusername = findViewById(R.id.cusername);
        cfname = findViewById(R.id.cfname);
        clname = findViewById(R.id.clname);
        cmobile = findViewById(R.id.cmobile);
        curnntpassword = findViewById(R.id.curnntpassword);

        // Assuming you have a unique identifier for the user, like their user ID
        String userId = user.getUid();

        // Set up ValueEventListener to listen for changes in real-time
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Handle the data changes
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String fname = dataSnapshot.child("fname").getValue(String.class);
                    String lname = dataSnapshot.child("lname").getValue(String.class);
                    String mobile = dataSnapshot.child("contact").getValue(String.class);
                    // Update UI with the username
                    cusername.setText(username);
                    cfname.setText(fname);
                    clname.setText(lname);
                    cmobile.setText(mobile);
                } else {
                    // Handle the case where no data is found for the specified user ID

                    Log.e(TAG, "No data found for this user ID");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });

        //        view details //


//        update details //

        Button updateDetails = findViewById(R.id.detailsUpdatebutton);
        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println(user.getEmail());

                String typepassword = curnntpassword.getText().toString();
                String currnetEmail = user.getEmail();

                if (TextUtils.isEmpty(typepassword)) {
                    Toast.makeText(Edite_User_Details_Activity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    curnntpassword.requestFocus();
                    return;
                } else {

                    loadingdialog.startLoadingdialog();
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(currnetEmail, typepassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
//                       progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {

//                                        AtomicInteger fieldsUpdatedCount = new AtomicInteger(0);


                                        String newusername = cusername.getText().toString();
                                        String newfirstName = cfname.getText().toString();
                                        String newlastName = clname.getText().toString();
                                        String newContact = cmobile.getText().toString();

                                        updateFieldInFirebase(userId, "username", newusername);
                                        updateFieldInFirebase(userId, "fname", newfirstName);
                                        updateFieldInFirebase(userId, "lname", newlastName);
                                        updateFieldInFirebase(userId, "contact", newContact);
                                        loadingdialog.dismissdialog();
                                    } else {
                                        // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        loadingdialog.dismissdialog();
                                        curnntpassword.setText("");
                                        Toast.makeText(Edite_User_Details_Activity.this, "Invalide Password",
                                                Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                    }
                                }
                            });
                }
            }
        });

    }


    private void updateFieldInFirebase(String userId, String filename, String value) {
        databaseReference.child(userId).child(filename).setValue(value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                Log.d(TAG, fieldName + " updated successfully");
//                        int count = fieldsUpdatedCount.incrementAndGet();
//
                        // All fields have been successfully updated
                        Log.d(TAG, "All fields updated successfully");
                        Toast.makeText(getApplicationContext(), "updated successfully", Toast.LENGTH_SHORT).show();
                        cfname.setText("");
                        clname.setText("");
                        cmobile.setText("");
                        cusername.setText("");
                        curnntpassword.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to update " + filename + ": " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Failed to update " + filename + " : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

//        update details //
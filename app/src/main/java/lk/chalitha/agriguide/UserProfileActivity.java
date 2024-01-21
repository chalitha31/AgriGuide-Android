
package lk.chalitha.agriguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.chalitha.agriguide.model.User;

public class UserProfileActivity extends AppCompatActivity {

    public static final String TAG = UserProfileActivity.class.getName();
    FirebaseAuth auth, mAuth;
    FirebaseUser userLoginData;
    private DatabaseReference mDatabase, databaseReference;

    private ImageView changeImageProfile;

    CircleImageView profileImage;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;

    String currentUserUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

//        profile imge view //

        // Get the current authenticated user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            auth = FirebaseAuth.getInstance();
            currentUserUid = auth.getCurrentUser().getUid();
            // Get a reference to the Firebase Storage instance
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            mDatabase = FirebaseDatabase.getInstance().getReference("users");
            databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
            storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("profile-pic");

            profileImage = findViewById(R.id.commentuserProfile);


            // Construct the path to the user's profile image in Firebase Storage
//            String uid = currentUser.getUid();

            StorageReference profileImageRef = storageRef.child("profile-pic/" + currentUserUid + ".jpg");


            // Load the profile image into the ImageView using Glide


//            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(UserProfileActivity.this)
//                            .load(uri)
//                            .into(profileImage);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle any errors
//                    Log.e("Error", "Glide Error: " + exception.getMessage(), exception);
//                }
//            });

//            System.out.println(profileImageRef.getDownloadUrl());

            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Log.e("Error", "download Error: " + e.getMessage(), e);
                    if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        // The file does not exist, handle accordingly
//                        profileImage.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/agri-guide-760a7.appspot.com/o/profile-pic%2Favatar_2388584_640.png?alt=media&token=d75854b8-d636-4f4c-aab4-2631553f40a7"));
                        Log.e("Error", "File does not exist at the specified location");
                        profileImage.setImageResource(R.drawable.avatar_2388584_640);
//                        profileImage.setImageResource(R.drawable.avatar_2388584_640);
                        // You might want to set a default image or show a placeholder here
                    } else {
                        // Handle other types of failures
                        Log.e("Error", "Download error: " + e.getMessage(), e);
                    }
                }
            });

            // Load the profile image into the ImageView using Glide

            //        profile imge view //

            Toolbar toolbar = findViewById(R.id.toolbarViewGuide);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            //      profile pic uplode //


//                    changeImageProfile = findViewById(R.id.changeImageProfile);

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    choosePictuere();

                }
            });

            //        profile pic uplode //

            mDatabase.child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve user data
                        User user = dataSnapshot.getValue(User.class);

                        // Now you can access the mobile number
                        String email = user.getEmail();
                        String fullname = user.getFname() + " " + user.getLname();

                        // Use the mobile number as needed (e.g., display it in a TextView)
                        TextView profileEmail = findViewById(R.id.profileEmail);
                        TextView nametag = findViewById(R.id.profileName);
                        profileEmail.setText(email);
                        nametag.setText(fullname);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Log.e(TAG, "load Data Error" + error);

                }
            });


            Button logout = findViewById(R.id.logoutbutton);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();

                }
            });

            TextView userDetails = findViewById(R.id.userDetails);
            userDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Edite_User_Details_Activity.class);
                    startActivity(intent);
//                    finish();
                }
            });

            TextView changePassword = findViewById(R.id.changePassword);
            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ChngePwIntent = new Intent(getApplicationContext(), Change_password.class);
                    startActivity(ChngePwIntent);
//                    finish();
                }
            });

        }

    }


    //        profile pic uplode //

    private void choosePictuere() {

//        Intent picintern = new Intent();
//        picintern.setType("image/*");
//        picintern.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(picintern, 1);

        CropImage.activity().setAspectRatio(1, 1).start(UserProfileActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            imageUri = result.getUri();
            profileImage.setImageURI(imageUri);
            uplodepicture();
        } else {
            Toast.makeText(getApplicationContext(), "Error, Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uplodepicture() {
//        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("uploading Image...");
        pd.show();

//        final String randomKey = UUID.randomUUID().toString();

        StorageReference riversRef = storageProfilePicsRef.child(currentUserUid + ".jpg");
        uploadTask = riversRef.putFile(imageUri);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();

                Toast.makeText(getApplicationContext(), "image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });

    }

    //        profile pic uplode //


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();

        } else {
            Log.e("weda ne", "Errrr");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
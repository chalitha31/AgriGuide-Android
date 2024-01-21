package lk.chalitha.agriguide;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Add_Guide_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Button select, previous, next, uploadButton;
    ImageSwitcher imageView;

    EditText title, rcategory, problem;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    List<String> imagesEncodedList;
    FirebaseFirestore db;
    FirebaseStorage storage;

    Spinner spinner;


    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = currentUser.getUid();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;

    final dialog loadingdialog = new dialog(Add_Guide_Activity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guide);


        title = findViewById(R.id.title);

        problem = findViewById(R.id.problem);

        select = findViewById(R.id.select);

        imageView = findViewById(R.id.image);
        previous = findViewById(R.id.previous);
        mArrayUri = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


        uploadButton = findViewById(R.id.uploadButton);


        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select Category");
        categories.add("Vegetables");
        categories.add("Fruits");
        categories.add("Seeds");
        categories.add("Others");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner

        spinner.setAdapter(dataAdapter);

        // ... (Your existing code)

        // click here to upload data to Firestore
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingdialog.startLoadingdialog();
                // Get other input values
//                String title = "chlit"; // Replace with your logic to get title
//                String name = "chamod";   // Replace with your logic to get name
//                String description = "Nadee"; // Replace with your logic to get description

                String Dtitle = title.getText().toString();
                String Rcategory = spinner.getSelectedItem().toString();

                String Rproblem = problem.getText().toString();


                // Upload images and data to Firestore
                uploadDataToFirestore(Dtitle, Rcategory, Rproblem);
            }
        });

        mArrayUri = new ArrayList<Uri>();

        // showing all images in imageswitcher
        imageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                return imageView1;
            }
        });
        next = findViewById(R.id.next);

        // click here to select next image
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    imageView.setImageURI(mArrayUri.get(position));
                } else {
                    Toast.makeText(Add_Guide_Activity.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // click here to view previous image
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    imageView.setImageURI(mArrayUri.get(position));

                }
            }
        });


        imageView = findViewById(R.id.image);

        // click here to select image
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);
                }
                // setting 1st selected image into image switcher
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    // ... (Your existing code)

    private void uploadDataToFirestore(String title, String category, String problem) {
        // Check if there are images to upload

        if (mArrayUri.size() > 0) {
            // Create a new document in the "reports" collection
            String uniqueID = UUID.randomUUID().toString();


            Map<String, Object> reportData = new HashMap<>();
            reportData.put("userId", uid);
            reportData.put("postId", uniqueID);
            reportData.put("title", title);
            reportData.put("category", category);
            reportData.put("content", problem);


            // Store the images as an array in Firestore
            List<String> imageUrls = new ArrayList<>();
            for (Uri imageUri : mArrayUri) {
                // Upload the image to Firestore and get its download URL
//                uploadImageToStorage(imageUri, imageUrls);
                uploadImageToStorage(imageUri, imageUrls, reportData, uniqueID);
            }


        } else {
            Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToStorage(Uri imageUri, final List<String> imageUrls, final Map<String, Object> reportData, final String reportUniqueID) {


        if (currentUser != null) {


            // Generate a unique filename for each image
            String imageName = "image_" + System.currentTimeMillis() + ".jpg";

            // Create a storage reference
            StorageReference storageRef = storage.getReference().child("Guide_post_images/" + uid + "/" + reportUniqueID + "/" + imageName);

            // Upload the image to Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully
                            // Get the download URL and add it to the list
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    imageUrls.add(downloadUrl.toString());

                                    // Check if all images are uploaded
                                    if (imageUrls.size() == mArrayUri.size()) {
                                        // All images uploaded, proceed to Firestore

                                        // Add the image URLs to the document data
                                        reportData.put("images", imageUrls);

                                        // Add the document to Firestore
                                        db.collection("Guide_post")
                                                .add(reportData)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        loadingdialog.dismissdialog();
                                                        clearData();
                                                        // Document added successfully
                                                        Toast.makeText(Add_Guide_Activity.this, "Post added to Firestore", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        loadingdialog.dismissdialog();
                                                        // Handle errors
                                                        Toast.makeText(Add_Guide_Activity.this, "Error adding post to Firestore", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle errors
                            Toast.makeText(Add_Guide_Activity.this, "Error uploading image to storage", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // User not authenticated
            Toast.makeText(Add_Guide_Activity.this, "User not authenticated.", Toast.LENGTH_SHORT).show();
        }

    }

    public void clearData() {

        title.setText("");
        spinner.setSelection(0);

        problem.setText("");
        imageView.setImageDrawable(null);
        title.requestFocus();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
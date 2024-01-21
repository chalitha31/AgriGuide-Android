package lk.chalitha.agriguide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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

public class Add_Report_Activity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    Button select, previous, next, uploadButton;
    ImageSwitcher imageView;
    Switch locationSwitch;
    EditText title, rcategory, city, problem;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    List<String> imagesEncodedList;
    FirebaseFirestore db;
    FirebaseStorage storage;
    ConstraintLayout maplayout;
    Spinner spinner;
    private GoogleMap map;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker marker_current;

    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = currentUser.getUid();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;

    final dialog loadingdialog = new dialog(Add_Report_Activity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

//        TextView tec = findViewById(R.id.textlo2);
//        tec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                System.out.println("latlung :" + currentLocation.getLatitude());
//                System.out.println("latlung :" + currentLocation.getLongitude());
//
//            }
//        });

//        location picker


        //        location picker


//        map

//        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.googlemap);
//
//        client = LocationServices.getFusedLocationProviderClient(this);
//
//        if (ActivityCompat.checkSelfPermission(Add_Report_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//
//            getCurrentLocation();
//        } else {
//            ActivityCompat.requestPermissions(Add_Report_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//        }

//        map

        locationSwitch = findViewById(R.id.loaction);
        maplayout = findViewById(R.id.maplayout);

        title = findViewById(R.id.title);
        city = findViewById(R.id.city);
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
                String Rcity = city.getText().toString();
                String Rproblem = problem.getText().toString();


                // Upload images and data to Firestore
                uploadDataToFirestore(Dtitle, Rcategory, Rcity, Rproblem);
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
                    Toast.makeText(Add_Report_Activity.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
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

        locationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationSwitch.isChecked()) {
                    maplayout.setVisibility(View.VISIBLE);
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Add_Report_Activity.this);

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(Add_Report_Activity.this);
                } else {
                    maplayout.setVisibility(View.GONE);
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

    private void uploadDataToFirestore(String title, String category, String city, String problem) {
        // Check if there are images to upload

        if (mArrayUri.size() > 0) {
            // Create a new document in the "reports" collection
            String uniqueID = UUID.randomUUID().toString();


            Map<String, Object> reportData = new HashMap<>();
            reportData.put("userId", uid);
            reportData.put("reportId", uniqueID);
            reportData.put("title", title);
            reportData.put("category", category);
            reportData.put("problems", problem);
            reportData.put("city", city);

            if (currentLocation != null) {
                // Create a nested map for the location
                Map<String, Object> locationData = new HashMap<>();
                locationData.put("latitude", currentLocation.getLatitude());
                locationData.put("longitude", currentLocation.getLongitude());

                // Add the nested map to the main reportData map under the "location" key
                reportData.put("location", locationData);
            }else{
                Map<String, Object> locationData = new HashMap<>();
                locationData.put("latitude", null);
                locationData.put("longitude", null);

                // Add the nested map to the main reportData map under the "location" key
                reportData.put("location", locationData);
            }


            // Store the images as an array in Firestore
            List<String> imageUrls = new ArrayList<>();
            for (Uri imageUri : mArrayUri) {
                // Upload the image to Firestore and get its download URL
//                uploadImageToStorage(imageUri, imageUrls);
                uploadImageToStorage(imageUri, imageUrls, reportData, uniqueID);
            }


            // Add the image URLs to the document data
//            reportData.put("images", imageUrls);

            // Add the document to Firestore
//            db.collection("reports")
//                    .add(reportData)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            // Document added successfully
//                            Toast.makeText(Add_Report_Activity.this, "Report added to Firestore", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // Handle errors
//                            Toast.makeText(Add_Report_Activity.this, "Error adding report to Firestore", Toast.LENGTH_SHORT).show();
//                        }
//                    });

        } else {
            Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToStorage(Uri imageUri, final List<String> imageUrls, final Map<String, Object> reportData, final String reportUniqueID) {


        if (currentUser != null) {


            // Generate a unique filename for each image
            String imageName = "image_" + System.currentTimeMillis() + ".jpg";

            // Create a storage reference
            StorageReference storageRef = storage.getReference().child("Report_images/" + uid + "/" + reportUniqueID + "/" + imageName);

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
                                        db.collection("reports")
                                                .add(reportData)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        loadingdialog.dismissdialog();
                                                        clearData();
                                                        // Document added successfully
                                                        Toast.makeText(Add_Report_Activity.this, "Report added to Firestore", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        loadingdialog.dismissdialog();
                                                        // Handle errors
                                                        Toast.makeText(Add_Report_Activity.this, "Error adding report to Firestore", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Add_Report_Activity.this, "Error uploading image to storage", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // User not authenticated
            Toast.makeText(Add_Report_Activity.this, "User not authenticated.", Toast.LENGTH_SHORT).show();
        }

//    private void uploadImageToStorage(Uri imageUri, final List<String> imageUrls) {
//
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (currentUser != null) {
//            String uid = currentUser.getUid();
//            // Add data to Firestore with the user's UID
//            // ...
//
//            // Generate a unique filename for each image
//            String imageName = "image_" + System.currentTimeMillis() + ".jpg";
//
//            // Create a storage reference
//            StorageReference storageRef = storage.getReference().child("Report_images/" + uid + "/" + imageName);
//
//            // Upload the image to Firebase Storage
//            storageRef.putFile(imageUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // Image uploaded successfully
//                            // Get the download URL and add it to the list
//                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri downloadUrl) {
//                                    imageUrls.add(downloadUrl.toString());
//                                    // Check if all images are uploaded
//                                    if (imageUrls.size() == mArrayUri.size()) {
//                                        // All images uploaded, proceed to Firestore
//                                        // You might want to do this in a more robust way, such as using a counter
//                                        // or checking in a different way, depending on your use case.
//                                        // For simplicity, I'm assuming all images are uploaded when the last one is done.
//                                        // Add the document to Firestore
//                                        // Add the image URLs to the document data
//                                        // (This part is already included in uploadDataToFirestore method)
//                                    }
//                                }
//                            });
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // Handle errors
//                            Toast.makeText(Add_Report_Activity.this, "Error uploading image to storage", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//        } else {
//            // User not authenticated
//            Toast.makeText(Add_Report_Activity.this, "User not authenticated.", Toast.LENGTH_SHORT).show();
//        }
//        // ... (Your existing code)
//
//    }
    }

    public void clearData() {

        title.setText("");
        spinner.setSelection(0);
        city.setText("");
        problem.setText("");
        locationSwitch.setChecked(false);
        imageView.setImageDrawable(null);
        title.requestFocus();
        maplayout.setVisibility(View.GONE);

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

    //    map


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);

        if (checkPermissions()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
            getLastLocation();
        } else {
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }

    }


    private boolean checkPermissions() {
        boolean permission = false;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            permission = true;
        }

        return permission;
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {

//                    comment
            LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                    .setWaitForAccurateLocation(true)
                    .setMinUpdateIntervalMillis(1000)
                    .setMaxUpdateDelayMillis(1000)
                    .build();


            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {

                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                    currentLocation = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());


                    if (marker_current == null) {
                        MarkerOptions options = new MarkerOptions()
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_man))
                                .title("My Location")

                                .position(latLng);
                        marker_current = map.addMarker(options);
                    } else {
                        marker_current.setPosition(latLng);
                    }

                    moveCamera(latLng);


                }
            }, Looper.getMainLooper());


        }
    }

    public void moveCamera(LatLng latLng) {
//        CameraPosition cameraPosition = CameraPosition.builder()
//                .target(latLng)
//                .zoom(10f)
//                .build();
//
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//        map.animateCamera(cameraUpdate);

//        uncomment

//        CameraPosition cameraPosition = CameraPosition.builder()
//                .target(latLng)
//                .zoom(10f)  // Adjust the initial zoom level as needed
//                .build();

        // Set the maximum zoom level to a high value
//        float maxZoom = 10f;  // Set this to a value that suits your requirements

       // // Ensure the zoom level is within a reasonable range
//  //      float zoomLevel = Math.min(cameraPosition.zoom, maxZoom);

//        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(
//                new CameraPosition.Builder()
//                        .target(cameraPosition.target)
//                        .zoom(maxZoom)
//                        .build()
//        );
//        uncomment

//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,12);
//        map.animateCamera(cameraUpdate);
//        map.getUiSettings().setZoomControlsEnabled(true);
//        map.getUiSettings().setMyLocationButtonEnabled(true);
//        map.getUiSettings().setCompassEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Snackbar.make(findViewById(R.id.maplayout), "Location permission denied", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).show();
            }
        }
    }
    //    map


//    location picker


}

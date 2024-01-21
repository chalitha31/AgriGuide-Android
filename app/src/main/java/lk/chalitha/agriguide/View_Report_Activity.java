package lk.chalitha.agriguide;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class View_Report_Activity extends AppCompatActivity {

    public final static String TAG = View_Report_Activity.class.getName();
    ImageSwitcher imageSwitcher;
    Button problemB,commentB,next,previous;
    private int currentIndex = 0;
    String greportId,reprortUserID;
//    Marker m;
//    EditText title, category, city, problem;
//    LinearLayout locationlayout;
//    private GoogleMap mapp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reportsRef = db.collection("reports");
//
    String documentId;
    DocumentReference documentRef;
//    Map<String, Object> locationData;
//
//    SupportMapFragment mapFragment;

//    Double latitude = null;
//    Double longitude = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        documentId = getIntent().getStringExtra("docId");



        System.out.println("id1 "+documentId);
//        documentId = getIntent().getStringExtra("docId");
//        // Create a reference to the document
        documentRef = reportsRef.document(documentId);


//
//        title = findViewById(R.id.title);
//        category = findViewById(R.id.category);
//        city = findViewById(R.id.city);
//        problem = findViewById(R.id.problem);
//        locationlayout = findViewById(R.id.locationLayout);
//
//
//        //      back
        Toolbar t = findViewById(R.id.reporyviewToolbar);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


////      back

////      ReportID
////        String docId = getIntent().getStringExtra("docId");
////        Log.i(TAG,"id "+ reportId);
//
        imageSwitcher = findViewById(R.id.image);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT
                        , FrameLayout.LayoutParams.MATCH_PARENT
                ));
                return imageView;
            }
        });
//
//
//        // Check if the document exists
        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "Document exists!");


                        Map<String, Object> reportData = document.getData();
//                        System.out.println(document.getId());
                        // Access individual fields
                         reprortUserID = (String) reportData.get("userId");
                         greportId = (String) reportData.get("reportId");
//                        String gtitle = (String) reportData.get("title");
//                        String gcategory = (String) reportData.get("category");
//                        String gproblem = (String) reportData.get("problems");
//                        String gcity = (String) reportData.get("city");
//                        Double latitude = null;
//                        Double longitude = null;
                        // Access nested location data if present
//                        locationData = (Map<String, Object>) reportData.get("location");
//                        if (locationData != null) {
//
//                             mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                                    .findFragmentById(R.id.mapp);
//                            mapFragment.getMapAsync(View_Report_Activity.this);
//
//                            latitude = (Double) locationData.get("latitude");
//                            longitude = (Double) locationData.get("longitude");
//                            // Do something with latitude and longitude
//                        }

//                        title.setText(gtitle);
//                        category.setText(gcategory);
//                        problem.setText(gproblem);
//                        city.setText(gcity);


                        ArrayList<String> imageUrls;
                        imageUrls = new ArrayList<>();

                        ArrayList<String> documentImageUrls = (ArrayList<String>) reportData.get("images");
                        if (documentImageUrls != null) {
                            imageUrls.addAll(documentImageUrls);
                        }

                        ImageView imageView = (ImageView) imageSwitcher.getCurrentView();

                        Picasso.get()
                                .load(imageUrls.get(currentIndex))
                                .into(imageView);

//                        if (latitude == null && longitude == null) {
//
//                            locationlayout.setVisibility(View.GONE);
//
//                        } else {
//                            locationlayout.setVisibility(View.VISIBLE);
//
//                        }


                        next = findViewById(R.id.next);
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (currentIndex < imageUrls.size() - 1) {
                                    currentIndex = (currentIndex + 1) % imageUrls.size();
                                    Picasso.get()
                                            .load(imageUrls.get(currentIndex))
                                            .into(imageView);
                                }
                            }
                        });
                        previous = findViewById(R.id.previous);
                        previous.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (currentIndex > 0) {
                                    currentIndex = (currentIndex - 1) % imageUrls.size();
                                    Picasso.get()
                                            .load(imageUrls.get(currentIndex))
                                            .into(imageView);
                                }
                            }
                        });

                        if (imageUrls.size() == 1) {

                            next.setVisibility(View.INVISIBLE);
                            previous.setVisibility(View.INVISIBLE);
                        }


                    } else {
                        // Document does not exist
                        Log.d("TAG", "Document does not exist!");
                    }
                } else {
                    Log.e("TAG", "Failed with: ", task.getException());
                }
            }
        });

        View_Report_Fragment viewReportFragment = new View_Report_Fragment();
        Comment_Fragment commentFragment = new Comment_Fragment();

//        viewReportFragment.setDocId(documentId);

        Bundle bundle = new Bundle();
        bundle.putString("docId", documentId);
        viewReportFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, viewReportFragment)
                .commit();

        problemB = findViewById(R.id.problem_button);
        problemB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("docId", documentId);
//                System.out.println("lasy "+documentId);
                viewReportFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, viewReportFragment).commit();
            }
        });


        commentB = findViewById(R.id.comment_button);
        commentB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle Cbundle = new Bundle();
                Cbundle.putString("reportId", greportId);
                Cbundle.putString("userId", reprortUserID);
                commentFragment.setArguments(Cbundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,commentFragment).commit();
            }
        });

    }

//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mapp = googleMap;
////        LatLng location = new LatLng(latitude, longitude);
////        map.addMarker(new MarkerOptions().position(location).title("Cultivation"));
////        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
//
//
////        if (latitude == null && longitude == null) {
////
////            // Handle the case where latitude or longitude is null
////            Toast.makeText(this, "Latitude or Longitude is null", Toast.LENGTH_SHORT).show();
////        } else {
////            System.out.println(longitude);
////            LatLng location = new LatLng(7.1205383, 80.0111283);
////            mapp.addMarker(new MarkerOptions().position(location).title("Cultivation"));
////            mapp.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
////        }
//
//      reportsRef
//                .document(documentId)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        // Retrieve latitude and longitude
////                        Double latitude = documentSnapshot.getDouble("latitude");
////                        Double longitude = documentSnapshot.getDouble("longitude");
//
//                        Double    latitude = (Double) locationData.get("latitude");
//                        Double   longitude = (Double) locationData.get("longitude");
//
//                        // Check if latitude is not null before adding the marker
//                        if (latitude == null && longitude == null) {
//
//                            Toast.makeText(this, "Latitude or Longitude is null", Toast.LENGTH_SHORT).show();
//                        }else{
//                            LatLng location = new LatLng(latitude, longitude);
////                            MarkerOptions options = new MarkerOptions();
//                            System.out.println(latitude);
//                            System.out.println(longitude);
//                            mapp.addMarker(new MarkerOptions().position(location).title("Cultivation"));
//                            mapp.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
//                            mapp.getUiSettings().setZoomControlsEnabled(true);
//                        }
////                        if (latitude != null && longitude != null) {
////
////                        } else {
////                            // Handle the case where latitude or longitude is null
////
////                        }
//                    } else {
//                        // Document does not exist
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    // Handle errors
//                    Toast.makeText(this, "Error retrieving location data", Toast.LENGTH_SHORT).show();
//                });
//
//    }

}
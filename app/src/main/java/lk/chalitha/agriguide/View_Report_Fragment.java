package lk.chalitha.agriguide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class View_Report_Fragment extends Fragment implements OnMapReadyCallback {

    public final static String TAG = View_Report_Fragment.class.getName();
//    ImageSwitcher imageSwitcher;

    //    ArrayList<String> imageUrls;
//    Button next, previous;
//    private int currentIndex = 0;
    Marker m;
    EditText title, category, city, problem;
    LinearLayout locationlayout;
    private GoogleMap mapp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reportsRef = db.collection("reports");

    String documentId;
    DocumentReference documentRef;
    Map<String, Object> locationData;

    SupportMapFragment mapFragment;


    // Method to set the docId

    public static View_Report_Fragment newInstance() {
        View_Report_Fragment fragment = new View_Report_Fragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        documentId = bundle.getString("docId");
        return inflater.inflate(R.layout.fragment_view__report_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Create a reference to the document
        documentRef = reportsRef.document(documentId);

        System.out.println("id2 " + documentId);

        title = getActivity().findViewById(R.id.title);
        category = getActivity().findViewById(R.id.category);
        city = getActivity().findViewById(R.id.city);
        problem = getActivity().findViewById(R.id.problem);
        locationlayout = getActivity().findViewById(R.id.locationLayout);


//      ReportID
//        String docId = getIntent().getStringExtra("docId");
//        Log.i(TAG,"id "+ reportId);

//        imageSwitcher = getActivity().findViewById(R.id.image);
//
//        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//
//                ImageView imageView = new ImageView(getActivity());
//                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                imageView.setLayoutParams(new FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.MATCH_PARENT
//                        , FrameLayout.LayoutParams.MATCH_PARENT
//                ));
//                return imageView;
//            }
//        });


        // Check if the document exists
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
                        String guserId = (String) reportData.get("userId");
                        String greportId = (String) reportData.get("reportId");
                        String gtitle = (String) reportData.get("title");
                        String gcategory = (String) reportData.get("category");
                        String gproblem = (String) reportData.get("problems");
                        String gcity = (String) reportData.get("city");
                        Double latitude = null;
                        Double longitude = null;
                        // Access nested location data if present
                        locationData = (Map<String, Object>) reportData.get("location");
                        if (locationData != null) {

                            mapFragment = (SupportMapFragment) getChildFragmentManager()
                                    .findFragmentById(R.id.mapp);
                            mapFragment.getMapAsync(View_Report_Fragment.this);

                            latitude = (Double) locationData.get("latitude");
                            longitude = (Double) locationData.get("longitude");
                            // Do something with latitude and longitude
                        }

                        title.setText(gtitle);
                        category.setText(gcategory);
                        problem.setText(gproblem);
                        city.setText(gcity);


//                        imageUrls = new ArrayList<>();
//
//                        ArrayList<String> documentImageUrls = (ArrayList<String>) reportData.get("images");
//                        if (documentImageUrls != null) {
//                            imageUrls.addAll(documentImageUrls);
//
//                        }
//
//                        ImageView imageView = (ImageView) imageSwitcher.getCurrentView();
//
//                        Picasso.get()
//                                .load(imageUrls.get(currentIndex))
//                                .into(imageView);
//
//                        if (latitude == null && longitude == null) {
//
//                            locationlayout.setVisibility(View.GONE);
//
//                        } else {
//                            locationlayout.setVisibility(View.VISIBLE);
//
//                        }
//
//
//                        next = getActivity().findViewById(R.id.next);
//                        next.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (currentIndex < imageUrls.size() - 1) {
//                                    currentIndex = (currentIndex + 1) % imageUrls.size();
//                                    Picasso.get()
//                                            .load(imageUrls.get(currentIndex))
//                                            .into(imageView);
//                                }
//                            }
//                        });
//                        previous = getActivity().findViewById(R.id.previous);
//                        previous.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (currentIndex > 0) {
//                                    currentIndex = (currentIndex - 1) % imageUrls.size();
//                                    Picasso.get()
//                                            .load(imageUrls.get(currentIndex))
//                                            .into(imageView);
//                                }
//                            }
//                        });
//
//                        if (imageUrls.size() == 1) {
//
//                            next.setVisibility(View.INVISIBLE);
//                            previous.setVisibility(View.INVISIBLE);
//                        }


                    } else {
                        // Document does not exist
                        Log.d("TAG", "Document does not exist!");
                    }
                } else {
                    Log.e("TAG", "Failed with: ", task.getException());
                }
            }
        });

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapp = googleMap;
//        LatLng location = new LatLng(latitude, longitude);
//        map.addMarker(new MarkerOptions().position(location).title("Cultivation"));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));


//        if (latitude == null && longitude == null) {
//
//            // Handle the case where latitude or longitude is null
//            Toast.makeText(this, "Latitude or Longitude is null", Toast.LENGTH_SHORT).show();
//        } else {
//            System.out.println(longitude);
//            LatLng location = new LatLng(7.1205383, 80.0111283);
//            mapp.addMarker(new MarkerOptions().position(location).title("Cultivation"));
//            mapp.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
//        }

        reportsRef
                .document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve latitude and longitude
//                        Double latitude = documentSnapshot.getDouble("latitude");
//                        Double longitude = documentSnapshot.getDouble("longitude");

                        Double latitude = (Double) locationData.get("latitude");
                        Double longitude = (Double) locationData.get("longitude");

                        // Check if latitude is not null before adding the marker
                        if (latitude == null && longitude == null) {
                            locationlayout.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "location is not uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            LatLng location = new LatLng(latitude, longitude);
//                            MarkerOptions options = new MarkerOptions();
                            System.out.println(latitude);
                            System.out.println(longitude);
                            mapp.addMarker(new MarkerOptions().position(location).title("Cultivation"));
                            mapp.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
                            mapp.getUiSettings().setZoomControlsEnabled(true);
                        }
//                        if (latitude != null && longitude != null) {
//
//                        } else {
//                            // Handle the case where latitude or longitude is null
//
//                        }
                    } else {
                        // Document does not exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Toast.makeText(getContext(), "Error retrieving location data", Toast.LENGTH_SHORT).show();
                });

    }
}
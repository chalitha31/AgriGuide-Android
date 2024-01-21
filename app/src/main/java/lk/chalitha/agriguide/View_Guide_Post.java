package lk.chalitha.agriguide;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class View_Guide_Post extends AppCompatActivity {
    public final static String TAG = View_Guide_Post.class.getName();
    ImageSwitcher imageSwitcher;
    Button next, previous;
    private int currentIndex = 0;
    String greportId, reprortUserID;
    EditText title, category, city, problem;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reportsRef = db.collection("Guide_post");
    //
    String documentId;
    DocumentReference documentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_guide_post);

        documentId = getIntent().getStringExtra("docId");


        System.out.println("id1 " + documentId);

        documentRef = reportsRef.document(documentId);

//        //      back
        Toolbar t = findViewById(R.id.toolbarViewGuide);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


////      back


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

        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
//        city = findViewById(R.id.city);
        problem = findViewById(R.id.problem);
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
                        greportId = (String) reportData.get("postId");
                        String gtitle = (String) reportData.get("title");
                        String gcategory = (String) reportData.get("category");
                        String gproblem = (String) reportData.get("content");
//                        String gcity = (String) reportData.get("city");

                        title.setText(gtitle);
                        category.setText(gcategory);
                        problem.setText(gproblem);
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


    }
}
package lk.chalitha.agriguide.model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lk.chalitha.agriguide.CommentCallBack;

public class commentModel {

    public static final String TAG = commentModel.class.getName();

//    notification



//    notification

    private List<String> comImage;
    private String commentName;

    private  String commentUserId;
    private String commentContent;
    private static String reportID;
    private String commentDate;
//    private String reportid;
//    private  String docId;

//    public ReportData() {
//    }
//private static String fullname;

    public commentModel() {
    }



    public commentModel(List<String> comImage, String commentName, String commentContent, String commentDate,String commentUserId) {
        this.comImage = comImage;
        this.commentName = commentName;
        this.commentContent = commentContent;
        this.commentDate = commentDate;
        this.commentUserId = commentUserId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public List<String> getComImage() {
        return comImage;
    }

    public void setComImage(List<String> comImage) {
        this.comImage = comImage;
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public static String getReportID() {
        return reportID;
    }

    public static void setReportID(String reportID) {
        commentModel.reportID = reportID;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public static ArrayList<commentModel> getReportDataList(CommentCallBack callback) {

        String parentPath = "Comment";
        String documentId = "report_comment";


        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference reportsRef = db.collection("reports");


        CollectionReference commentsCollection = db.collection(parentPath).document(documentId).collection(reportID);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

//        getimage

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");




// Create a query to get comments with a specific UserID
//        Query query = commentsCollection.whereEqualTo("UserID", uid);

        ArrayList<commentModel> reportDataList = new ArrayList<>();

//        Handler handler = new Handler();
//
//        Runnable sendRequestRunnable = new Runnable() {
//            @Override
//            public void run() {
//


//        commentsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    // Iterate through the result set
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        // Access the data using the document.getData() method
//                        Map<String, Object> reportData = document.getData();
//
//                        // Access individual fields
////                        String userId = (String) reportData.get("userId");
////                        String reportId = (String) reportData.get("reportId");
////                        String title = (String) reportData.get("title");
////                        String category = (String) reportData.get("category");
////                        String problems = (String) reportData.get("problems");
////                        String city = (String) reportData.get("city");
////                        Double latitude = null;
////                        Double longitude = null;
//
//                        String comment = (String) reportData.get("comment");
//                        String commentDate = (String) reportData.get("date");
//                        String CommentUserId = (String) reportData.get("UserID");
//                        // Access nested location data if present
////                        Map<String, Object> locationData = (Map<String, Object>) reportData.get("location");
////                        if (locationData != null) {
////                            latitude = (Double) locationData.get("latitude");
////                            longitude = (Double) locationData.get("longitude");
////                            // Do something with latitude and longitude
////                        }
//
//
//                        mDatabase.child(CommentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    // Retrieve user data
//                                    User user = dataSnapshot.getValue(User.class);
//
//                                    // Now you can access the mobile number
//                                    String email = user.getEmail();
//                                    String fullName = user.getFname() + " " + user.getLname();
//
//
//                                    StorageReference profileImageRef = storageRef.child("profile-pic/" + CommentUserId + ".jpg");
//                                    List<String> imageUrls = new ArrayList<>();
//                                    profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            System.out.println("1 " + uri);
//                                            System.out.println("2 " + uri.toString());
//                                            imageUrls.add(uri.toString());
//
////                                Log.e(TAG, imageUrls.toString(), task.getException());
//                                            reportDataList.add(new commentModel(imageUrls, fullName, comment, commentDate));
//                                            callback.onCallback(reportDataList);
//
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
////                    Log.e("Error", "download Error: " + e.getMessage(), e);
//                                            if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
//                                                // The file does not exist, handle accordingly
//                                                Log.e("Error", "File does not exist at the specified location");
////                    profileImage.setImageResource(R.drawable.avatar_2388584_640);
//                                                // You might want to set a default image or show a placeholder here
//                                            } else {
//                                                // Handle other types of failures
//                                                Log.e("Error", "Download error: " + e.getMessage(), e);
//                                            }
//                                        }
//                                    });
//
//                                    // Use the mobile number as needed (e.g., display it in a TextView)
////                                    TextView profileEmail = findViewById(R.id.profileEmail);
////                                    TextView nametag = findViewById(R.id.profileName);
////                                    profileEmail.setText(email);
////                                    nametag.setText(fullname);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                Log.e(TAG, "load Data Error" + error);
//
//                            }
//                        });
//
////                        System.out.println(userId);
////                        System.out.println(reportId);
////                        System.out.println(title);
////                        System.out.println(category);
////                        System.out.println(latitude);
////                        String docID = document.getId();
////                        reportDataList.add(new ReportData(reportId, title, problems, city, category, "2023/08/26",docID));
//
//
//                        // Handle the retrieved data as needed
//                    }
//                } else {
//                    // Handle errors
//                    Log.w(TAG, "Error getting documents.", task.getException());
////                    Toast.makeText(,"Error Getting Data",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//                handler.postDelayed(this, 500);
//
//
//            }
//
//        };
//
//        handler.postDelayed(sendRequestRunnable, 500);


        commentsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed. ", e);
                    return;
                }



                    // Iterate through the result set
                    for (DocumentChange document : querySnapshot.getDocumentChanges()) {
                        // Access the data using the document.getData() method
                        switch (document.getType()) {
                            case ADDED:
                                // Document added
//                                DocumentReference addedDoc = document.getDocument().getData();
//                                Log.d("TAG", "Added: " + addedDoc.getId());

                            Map<String, Object> reportData = document.getDocument().getData();


                            String comment = (String) reportData.get("comment");
                            String commentDate = (String) reportData.get("date");
                            String CommentUserId = (String) reportData.get("UserID");


                            mDatabase.child(CommentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Retrieve user data
                                        User user = dataSnapshot.getValue(User.class);

                                        // Now you can access the mobile number
                                        String email = user.getEmail();
                                        String fullName = user.getFname() + " " + user.getLname();


                                        StorageReference profileImageRef = storageRef.child("profile-pic/" + CommentUserId + ".jpg");
                                        List<String> imageUrls = new ArrayList<>();
                                        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                System.out.println("1 " + uri);
                                                System.out.println("2 " + uri.toString());



                                                imageUrls.add(uri.toString());

                                                reportDataList.add(new commentModel(imageUrls, fullName, comment, commentDate,CommentUserId));
                                                callback.onCallback(reportDataList);

//                                Log.e(TAG, imageUrls.toString(), task.getException());

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
//                    Log.e("Error", "download Error: " + e.getMessage(), e);
                                                if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                                                    // The file does not exist, handle accordingly
                                                    Log.e("Error", "File does not exist at the specified location");
//                    profileImage.setImageResource(R.drawable.avatar_2388584_640);
                                                    imageUrls.add("https://firebasestorage.googleapis.com/v0/b/agri-guide-760a7.appspot.com/o/profile-pic%2Favatar_2388584_640.png?alt=media&token=d75854b8-d636-4f4c-aab4-2631553f40a7");
                                                    reportDataList.add(new commentModel(imageUrls, fullName, comment, commentDate,CommentUserId));
                                                    callback.onCallback(reportDataList);

                                                    // You might want to set a default image or show a placeholder here
                                                } else {
                                                    // Handle other types of failures
                                                    Log.e("Error", "Download error: " + e.getMessage(), e);
                                                }

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    Log.e(TAG, "load Data Error" + error);

                                }
                            });

//                            Notifivation

//                                NotificationCompat.Builder builder =
//                                        new NotificationCompat.Builder(Context)
//                                                .setSmallIcon(R.drawable.avatar_2388584_640) //set icon for notification
//                                                .setContentTitle("Notifications Example") //set title of notification
//                                                .setContentText("This is a notification message")//this is notification message
//                                                .setAutoCancel(true) // makes auto cancel of notification
//                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification
//
//
//                                Intent notificationIntent = new Intent(this, NotificationView.class);
//                                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                //notification message will get at NotificationView
//                                notificationIntent.putExtra("message", "This is a notification message");
//
//                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                                        PendingIntent.FLAG_UPDATE_CURRENT);
//                                builder.setContentIntent(pendingIntent);
//
//                                // Add as notification
//                                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                                manager.notify(0, builder.build());

//                            Notifivation


                                break;
                        }
                    }
                        // Handle the retrieved data as needed
                    }


        });



        return reportDataList;

//        reportDataList.add(new ReportData(1,"Lorem Ipsum is simply dummy text of the printing and typesetting industry","none","Alawwa","Vegitables","2023/08/26"));
//        reportDataList.add(new ReportData(2,"Lorem Ipsum is simply dummy text of the printing and typesetting industry","none","Alawwa","Vegitables","2023/08/26"));
//        reportDataList.add(new ReportData(3,"Lorem Ipsum is simply dummy text of the printing and typesetting industry","none","Alawwa","Vegitables","2023/08/26"));
//        reportDataList.add(new ReportData(4,"Lorem Ipsum is simply dummy text of the printing and typesetting industry","none","Alawwa","Vegitables","2023/08/26"));
//        reportDataList.add(new ReportData(5,"Lorem Ipsum is simply dummy text of the printing and typesetting industry","none","Alawwa","Vegitables","2023/08/26"));


//        return reportDataList;




    }
// handler.removeCallbacks(sendRequestRunnable);



}

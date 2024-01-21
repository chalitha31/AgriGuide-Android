package lk.chalitha.agriguide;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.chalitha.agriguide.model.User;
import lk.chalitha.agriguide.model.commentModel;


public class Comment_Fragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String reportId,reprortUserID;
  static   String commentName;
    CollectionReference collectionReference;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = currentUser.getUid();

    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getContext());
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        reportId = bundle.getString("reportId");

        reprortUserID= bundle.getString("userId");
        // Inflate the layout for this fragment0
        return inflater.inflate(R.layout.fragment_comment_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        super.onViewCreated(view, savedInstanceState);

//        upload data

        String parentPath = "Comment";
        String documentId = "report_comment";
        String reportID = reportId;
        List<String> userIdList = new ArrayList<>();
        userIdList.add("CImage");

        commentModel comment = new commentModel(userIdList, "commentName", "commentContent", "commentDate","comId");
        comment.setReportID(reportID);


        collectionReference = db.collection(parentPath).document(documentId).collection(reportID);


        EditText commentInput = view.findViewById(R.id.commentInput);
        ImageView sendComment = view.findViewById(R.id.sendComment);


        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                System.out.println(commentInput.getText().toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                // Get the current date and time
                String currentDateAndTime = sdf.format(new Date());
//                System.out.println(currentDateAndTime);
                String commentData = commentInput.getText().toString();
                if (commentData != null && !commentData.equals("")) {


                    Map<String, Object> data = new HashMap<>();
                    data.put("UserID", uid);
                    data.put("comment", commentData);
                    data.put("date", currentDateAndTime);

                    System.out.println("U id "+ uid);
                    System.out.println("r id " + reprortUserID);
                    collectionReference.add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    // Document added successfully
//                                    Log.d("Firestore", "Document added with ID: " + documentReference.getId());
                                    commentInput.setText("");

                                    mDatabase = FirebaseDatabase.getInstance().getReference("users");

                                    mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // Retrieve user data
                                                User user = dataSnapshot.getValue(User.class);

                                                // Now you can access the mobile number
//                                                String email = user.getEmail();
                                                 commentName = user.getFname()+ " " + user.getLname();

                                                // Use the mobile number as needed (e.g., display it in a TextView)

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

//                                            Log.e(TAG, "load Data Error" + error);

                                        }
                                    });

//                                    if (uid.equals(reprortUserID)) {
//                                        //Notification
//                                        // Step 1: Create notification channel
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            String channelId = "my_channel";
//                                            CharSequence channelName = "My Notifications";
//                                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//                                            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
//
//                                            NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(NotificationManager.class);
//                                            notificationManager.createNotificationChannel(channel);
//                                        }
//
//                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "my_channel")
//                                                .setSmallIcon(R.drawable.logoagri)
//                                                .setContentTitle("Report Notification")
//                                                .setContentText(commentName + " Comment Your Report")
//                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                                        // Step 3: Show the notification
//                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
//                                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                                            // TODO: Consider calling
//                                            //    ActivityCompat#requestPermissions
//                                            // here to request the missing permissions, and then overriding
//                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                            //                                          int[] grantResults)
//                                            // to handle the case where the user grants the permission. See the documentation
//                                            // for ActivityCompat#requestPermissions for more details.
//                                            return;
//                                        }
//                                        notificationManager.notify(1, builder.build());
//                                    }
                                    // Retrieve FCM token
//

//                                    if (uid.equals(reprortUserID)) {
//                                        // Notification
//                                        // Step 1: Create notification channel
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            String channelId = "my_channel";
//                                            CharSequence channelName = "My Notifications";
//                                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//                                            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
//
//                                            NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(NotificationManager.class);
//                                            notificationManager.createNotificationChannel(channel);
//                                        }
//
//                                        // Step 2: Build the notification
//                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "my_channel")
//                                                .setSmallIcon(R.drawable.logoagri)
//                                                .setContentTitle("Report Notification")
//                                                .setContentText(commentName + " commented on your report")
//                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                                        // Step 3: Show the notification
//                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
//                                                                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                                            // TODO: Consider calling
//                                            //    ActivityCompat#requestPermissions
//                                            // here to request the missing permissions, and then overriding
//                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                            //                                          int[] grantResults)
//                                            // to handle the case where the user grants the permission. See the documentation
//                                            // for ActivityCompat#requestPermissions for more details.
//                                            return;
//                                        }
////
//                                        notificationManager.notify(1, builder.build());
//                                    }

//Notification

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle errors
                                    Log.e("Firestore", "Error adding document", e);
                                    Toast.makeText(getContext(), "Error adding Comment", Toast.LENGTH_SHORT).show();
                                }
                            });

//                    documentReference.set(data)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    // Document written successfully
//                                    Log.d("Firestore", "Document added/updated successfully");
//                                    commentInput.setText("");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    // Handle errors
//                                    Log.e("Firestore", "Error adding/updating document", e);
////                                    Log.e("Firestore", "Error adding document", e);
//                                    Toast.makeText(getContext(),"Error adding Comment",Toast.LENGTH_SHORT).show();
//                                }
//                            });

                }

            }
        });

//        upload data

        RecyclerView recyclerView = view.findViewById(R.id.commentview);

        commentModel.getReportDataList(new CommentCallBack() {
            @Override
            public void onCallback(ArrayList<commentModel> reportDataList) {

                RecyclerView.Adapter adapter = new RecyclerView.Adapter<CVH>() {
                    @NonNull
                    @Override
                    public CVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View reportView = inflater.inflate(R.layout.comment_design, parent, false);
                        return new CVH(reportView);
                    }

                    @Override
                    public void onBindViewHolder(@NonNull CVH holder, int position) {
//                        holder.getCommentImage().setImageResource(R.drawable.avatar_2388584_640);
//
//                        String profileImageUrl = reportDataList.get(position).getComImage();   // image
                        List<String> profileImageUrl = reportDataList.get(position).getComImage();

                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            // Use Picasso to load the image from the URL into the ImageView
                            Picasso.get().load(profileImageUrl.get(0)).into(holder.getCommentImage());
                        } else {
                            // Handle the case when the profile image URL is empty or null
                            // You can set a placeholder or handle it according to your requirements
                            holder.getCommentImage().setImageResource(R.drawable.avatar_2388584_640);
                        }
//                        holder.getCommentImage().(Uri.parse(reportDataList.get(position).getUserID()));
                        System.out.println(reportDataList.get(position).getComImage());
                        holder.getCommentName().setText(reportDataList.get(position).getCommentName());
                        holder.getCommentCotent().setText(reportDataList.get(position).getCommentContent());
                        holder.getCommentDate().setText(reportDataList.get(position).getCommentDate());
//                        holder.getReportLocation().setText(reportDataList.get(position).getReportLocation());

//                        String reportId = reportDataList.get(position).getId();
//                        String docId = reportDataList.get(position).getdocId();

//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                // Get the clicked reportId
//
////                                System.out.println("report Id" +reportId);
//
//                                // Open ViewReportActivity and pass the reportId
//                                Intent intent = new Intent(v.getContext(), View_Report_Activity.class);
//                                intent.putExtra("docId", docId);
//                                v.getContext().startActivity(intent);
//
////                                view
////                                String reportId = getIntent().getStringExtra("reportId");
//                            }
//                        });
//
//                        String CommentUserId = reportDataList.get(position).getCommentUserId();
////Notification
//                        // Step 1: Create notification channel
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            String channelId = "my_channel";
//                            CharSequence channelName = "My Notifications";
//                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//                            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
//
//                            NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(NotificationManager.class);
//                            notificationManager.createNotificationChannel(channel);
//                        }
//
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "my_channel")
//                                .setSmallIcon(R.drawable.logoagri)
//                                .setContentTitle("Report Notification")
//                                .setContentText(reportDataList.get(position).getCommentName()+ " Comment Your Report")
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                        // Step 3: Show the notification
//                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
//                        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        notificationManager.notify(1, builder.build());

//Notification


                    }

                    @Override
                    public int getItemCount() {
                        return reportDataList.size();
                    }
                };

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });

//        Notificaton

        CollectionReference commentsCollection = db.collection(parentPath).document(documentId).collection(reportID);
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = currentUser.getUid();


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


                            break;
                    }
                }
                // Handle the retrieved data as needed
            }


        });


//        Notificaton
        retrieveFcmToken();
    }


    class CVH extends RecyclerView.ViewHolder {

        private final CircleImageView commentImage;
        private final TextView commentName;

        private final TextView commentCotent;
//        private final TextView reportLocation;
        private final TextView commentDate;


        public CVH(@NonNull View itemView) {
            super(itemView);

            commentImage = itemView.findViewById(R.id.commentuserProfile);
            commentName = itemView.findViewById(R.id.commentName);

            commentCotent = itemView.findViewById(R.id.commentContent);
//            reportLocation = itemView.findViewById(R.id.reportLocation);
            commentDate = itemView.findViewById(R.id.CommentDate);

        }

        public ImageView getCommentImage() {
            return commentImage;
        }

        public TextView getCommentName() {
            return commentName;
        }

        public TextView getCommentDate() {
            return commentDate;
        }

        public TextView getCommentCotent() {
            return commentCotent;
        }
    }

    private void retrieveFcmToken() {
        FirebaseInstallations.getInstance().getToken(/* forceRefresh= */ true)
                .addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String token = task.getResult().getToken();
                            Log.d("FCM Token", token);

                            // Send this token to your server
                            sendRegistrationToServer(token);
                        } else {
                            Log.e("FCM Token", "Error getting FCM token");
                        }
                    }
                });
    }

    private void sendRegistrationToServer(String token) {
        // Implement the logic to send the token to your server
        Log.d("CommentFragment", "Token sent to server: " + token);
    }
}
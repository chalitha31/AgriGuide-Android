package lk.chalitha.agriguide.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import lk.chalitha.agriguide.GuideCallBack;

public class Guide_post_model {

    public static final String TAG = Guide_post_model.class.getName();
    private String id;
    private String reportTitle;
    private String reportContent;

    private static String reportCategory;
    private String reportDate;

    private  String docId;

//    public ReportData() {
//    }

    public Guide_post_model(String id, String reportTitle, String reportContent,String reportCategory, String reportDate,String docId) {
        this.id = id;
        this.reportTitle = reportTitle;
        this.reportContent = reportContent;

        this.reportCategory = reportCategory;
        this.reportDate = reportDate;
        this.docId = docId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getdocId() {
        return docId;
    }

    public void setdocId(String docId) {
        this.docId = docId;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }


    public String getReportCategory() {
        return reportCategory;
    }

    public void setReportCategory(String reportCategory) {
        this.reportCategory = reportCategory;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }


    public static ArrayList<Guide_post_model> getReportDataList(GuideCallBack callback) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reportsRef = db.collection("Guide_post");

        ArrayList<Guide_post_model> reportDataList = new ArrayList<>();


        reportsRef.whereEqualTo("category",reportCategory).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Iterate through the result set
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Access the data using the document.getData() method
                        Map<String, Object> reportData = document.getData();

                        // Access individual fields
                        String userId = (String) reportData.get("userId");
                        String reportId = (String) reportData.get("postId");
                        String title = (String) reportData.get("title");
                        String category = (String) reportData.get("category");
                        String problems = (String) reportData.get("content");

//                        System.out.println(userId);
//                        System.out.println(reportId);
//                        System.out.println(title);
//                        System.out.println(category);
//                        System.out.println(latitude);
                        String docID = document.getId();
                        reportDataList.add(new Guide_post_model(reportId, title, problems,  category, "2023/12/27",docID));
                        callback.onCallback(reportDataList);
                        // Handle the retrieved data as needed
                    }
                } else {
                    // Handle errors
                    Log.w(TAG, "Error getting documents.", task.getException());
//                    Toast.makeText(,"Error Getting Data",Toast.LENGTH_SHORT).show();
                }
            }
        });


//        reportDataList.add(new ReportData(1,"Lorem Ipsum is simply dummy text of the printing and typesetting industry","none","Alawwa","Vegitables","2023/08/26"));
//        reportDataList.add(new ReportData(2,"Lorem Ipsum is simply dummy text of the printing and typesetting industry","none","Alawwa","Vegitables","2023/08/26"));
//        reportDataList.add(new ReportData(3,"Lorem Ipsum is simply dummy text of the printing and typesetting industry","none","Alawwa","Vegitables","2023/08/26"));
//        reportDataList.add(new ReportData(4,"Lorem Ipsum is simply dummy text of the printing and typesetting industry","none","Alawwa","Vegitables","2023/08/26"));
//        reportDataList.add(new ReportData(5,"Lorem Ipsum is simply dummy text of the printing and typesetting industry","none","Alawwa","Vegitables","2023/08/26"));


//        return reportDataList;

        return reportDataList;
    }


}

package lk.chalitha.agriguide;

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

public class ReportData {
    public static final String TAG = ReportData.class.getName();
    private String id;
    private String reportTitle;
    private String reportContent;
    private String reportLocation;
    private String reportCategory;
    private String reportDate;

    private  String docId;

//    public ReportData() {
//    }

    public ReportData(String id, String reportTitle, String reportContent, String reportLocation, String reportCategory, String reportDate,String docId) {
        this.id = id;
        this.reportTitle = reportTitle;
        this.reportContent = reportContent;
        this.reportLocation = reportLocation;
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

    public String getReportLocation() {
        return reportLocation;
    }

    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
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


    public static ArrayList<ReportData> getReportDataList(FirestoreCallback callback) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reportsRef = db.collection("reports");

        ArrayList<ReportData> reportDataList = new ArrayList<>();


        reportsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Iterate through the result set
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Access the data using the document.getData() method
                        Map<String, Object> reportData = document.getData();

                        // Access individual fields
                        String userId = (String) reportData.get("userId");
                        String reportId = (String) reportData.get("reportId");
                        String title = (String) reportData.get("title");
                        String category = (String) reportData.get("category");
                        String problems = (String) reportData.get("problems");
                        String city = (String) reportData.get("city");
                        Double latitude = null;
                        Double longitude = null;
                        // Access nested location data if present
                        Map<String, Object> locationData = (Map<String, Object>) reportData.get("location");
                        if (locationData != null) {
                            latitude = (Double) locationData.get("latitude");
                            longitude = (Double) locationData.get("longitude");
                            // Do something with latitude and longitude
                        }
//                        System.out.println(userId);
//                        System.out.println(reportId);
//                        System.out.println(title);
//                        System.out.println(category);
//                        System.out.println(latitude);
                        String docID = document.getId();
                        reportDataList.add(new ReportData(reportId, title, problems, city, category, "2023/12/27",docID));
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

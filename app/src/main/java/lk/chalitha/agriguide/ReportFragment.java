package lk.chalitha.agriguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ReportFragment extends Fragment {


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
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyleview);

        ReportData.getReportDataList(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<ReportData> reportDataList) {

                RecyclerView.Adapter adapter = new RecyclerView.Adapter<VH>() {
                    @NonNull
                    @Override
                    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View reportView = inflater.inflate(R.layout.report_design, parent, false);
                        return new VH(reportView);
                    }

                    @Override
                    public void onBindViewHolder(@NonNull VH holder, int position) {
                        holder.getReportImage().setImageResource(R.drawable.agr);
                        holder.getReportTitle().setText(reportDataList.get(position).getReportTitle());
                        holder.getReportCategory().setText(reportDataList.get(position).getReportCategory());
                        holder.getReportDate().setText(reportDataList.get(position).getReportDate());
                        holder.getReportLocation().setText(reportDataList.get(position).getReportLocation());

                        String reportId = reportDataList.get(position).getId();
                        String docId = reportDataList.get(position).getdocId();

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Get the clicked reportId

//                                System.out.println("report Id" +reportId);

                                // Open ViewReportActivity and pass the reportId
                                Intent intent = new Intent(v.getContext(), View_Report_Activity.class);
                                intent.putExtra("docId", docId);
                                v.getContext().startActivity(intent);

//                                view
//                                String reportId = getIntent().getStringExtra("reportId");
                            }
                        });
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


    }

}

class VH extends RecyclerView.ViewHolder {

    private final ImageView reportImage;
    private final TextView reportTitle;

    private final TextView reportCategory;
    private final TextView reportLocation;
    private final TextView reportDate;


    public VH(@NonNull View itemView) {
        super(itemView);

        reportImage = itemView.findViewById(R.id.reportImage);
        reportTitle = itemView.findViewById(R.id.reportTitle);

        reportCategory = itemView.findViewById(R.id.reportCategory);
        reportLocation = itemView.findViewById(R.id.reportLocation);
        reportDate = itemView.findViewById(R.id.reportDate);

//        itemView.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        int position = getAdapterPosition();
//        if (position != RecyclerView.NO_POSITION) {
//            // Get the clicked reportId
//            String reportId = reportDataList.get(position).getId();
//
//            // Open ViewReportActivity and pass the reportId
////            Intent intent = new Intent(v.getContext(), ViewReportActivity.class);
////            intent.putExtra("reportId", reportId);
////            v.getContext().startActivity(intent);
//            System.out.println("report Id" +reportId);
//        }
//    }

    public ImageView getReportImage() {
        return reportImage;
    }

    public TextView getReportTitle() {
        return reportTitle;
    }

    public TextView getReportCategory() {
        return reportCategory;
    }

    public TextView getReportLocation() {
        return reportLocation;
    }

    public TextView getReportDate() {
        return reportDate;
    }
}
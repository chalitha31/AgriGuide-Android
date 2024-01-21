package lk.chalitha.agriguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.chalitha.agriguide.model.Guide_post_model;

public class Guide_post_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_post);

        String getCateg = getIntent().getStringExtra("categ");

        Guide_post_model gpm = new Guide_post_model("","","",getCateg,"","");



        Toolbar toolbar = findViewById(R.id.toolbarViewGuide);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.Grecyleview);

        Guide_post_model.getReportDataList(new GuideCallBack() {
            @Override
            public void onCallback(ArrayList<Guide_post_model> reportDataList) {



                RecyclerView.Adapter adapter = new RecyclerView.Adapter<GVH>() {
                    @NonNull
                    @Override
                    public GVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                        View reportView = inflater.inflate(R.layout.guide_post_design, parent, false);
                        return new GVH(reportView);
                    }

                    @Override
                    public void onBindViewHolder(@NonNull GVH holder, int position) {
                        holder.getReportImage().setImageResource(R.drawable.agr);
                        holder.getReportTitle().setText(reportDataList.get(position).getReportTitle());
                        holder.getReportCategory().setText(reportDataList.get(position).getReportCategory());
                        holder.getReportDate().setText(reportDataList.get(position).getReportDate());


                        String reportId = reportDataList.get(position).getId();
                        String docId = reportDataList.get(position).getdocId();

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Get the clicked reportId

//                                System.out.println("report Id" +reportId);

                                // Open ViewReportActivity and pass the reportId
                                Intent intent = new Intent(getApplicationContext(), View_Guide_Post.class);
                                intent.putExtra("docId", docId);
                                startActivity(intent);

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

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
            }
        });
    }
}


class GVH extends RecyclerView.ViewHolder {

    private final ImageView reportImage;
    private final TextView reportTitle;

    private final TextView reportCategory;

    private final TextView reportDate;

    public GVH(@NonNull View itemView) {
        super(itemView);

        reportImage = itemView.findViewById(R.id.reportImage);
        reportTitle = itemView.findViewById(R.id.reportTitle);

        reportCategory = itemView.findViewById(R.id.reportCategory);

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

    public TextView getReportDate() {
        return reportDate;
    }
}
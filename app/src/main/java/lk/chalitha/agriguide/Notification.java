package lk.chalitha.agriguide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class Notification extends Fragment {



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
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ArrayList<NotificationData> notifyDataList = NotificationData.getNotifyDataList();


        RecyclerView.Adapter adapter = new RecyclerView.Adapter<NVH>() {
            @NonNull
            @Override
            public NVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View notifyView = inflater.inflate(R.layout.notification_design,parent,false);

                return new NVH(notifyView);
            }

            @Override
            public void onBindViewHolder(@NonNull NVH holder, int position) {


//                holder.getNotifyImage().setImageResource(R.drawable.agr);
                holder.getNotifyTitle().setText(notifyDataList.get(position).getNotifyTitle());
                holder.getNotifyCategory().setText(notifyDataList.get(position).getNotifyCategory());
                holder.getNotifyDate().setText(notifyDataList.get(position).getNotifyDate());
                holder.getNotifyType().setText(notifyDataList.get(position).getNotifyType());

            }

            @Override
            public int getItemCount() {
                return  notifyDataList.size();
            }
        };

        RecyclerView recyclerView =  view.findViewById(R.id.notifyrecyleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
class NVH extends RecyclerView.ViewHolder {

//    private final ImageView reportImage;
    private final TextView notifyTitle;

    private final TextView notifyCategory;
    private final TextView notifyType;
    private final TextView notifyDate;

    public NVH(@NonNull View itemView) {
        super(itemView);
//        reportImage = itemView.findViewById(R.id.reportImage);
        notifyTitle = itemView.findViewById(R.id.notifyTitle);

        notifyCategory =itemView.findViewById(R.id.notifyCategory);
        notifyType = itemView.findViewById(R.id.notifyType);
        notifyDate = itemView.findViewById(R.id.notifyDate);
    }

    public TextView getNotifyTitle() {
        return notifyTitle;
    }

    public TextView getNotifyCategory() {
        return notifyCategory;
    }

    public TextView getNotifyType() {
        return notifyType;
    }

    public TextView getNotifyDate() {
        return notifyDate;
    }
}
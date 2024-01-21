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
import android.widget.TextView;

import java.util.ArrayList;


public class BookmarkFragment extends Fragment {

    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();

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
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<NotificationData> bookmarkDataList = NotificationData.getNotifyDataList();

        RecyclerView.Adapter adapter = new RecyclerView.Adapter<BVH>() {
            @NonNull
            @Override
            public BVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View notifyView = inflater.inflate(R.layout.bookmark_design,parent,false);

                return new BVH(notifyView);
            }

            @Override
            public void onBindViewHolder(@NonNull BVH holder, int position) {


//                holder.getNotifyImage().setImageResource(R.drawable.agr);
                holder.getNotifyTitle().setText(bookmarkDataList.get(position).getNotifyTitle());
                holder.getNotifyCategory().setText(bookmarkDataList.get(position).getNotifyCategory());
                holder.getNotifyDate().setText(bookmarkDataList.get(position).getNotifyDate());
                holder.getNotifyType().setText(bookmarkDataList.get(position).getNotifyType());

            }

            @Override
            public int getItemCount() {
                return  bookmarkDataList.size();
            }
        };

        RecyclerView recyclerView =  view.findViewById(R.id.bookmark_recyleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


    }
}

class BVH extends RecyclerView.ViewHolder {

    //    private final ImageView reportImage;
    private final TextView notifyTitle;

    private final TextView notifyCategory;
    private final TextView notifyType;
    private final TextView notifyDate;

    public BVH(@NonNull View itemView) {
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


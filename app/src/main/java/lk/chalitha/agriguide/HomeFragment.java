package lk.chalitha.agriguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;


public class HomeFragment extends Fragment {




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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton viewReport = view.findViewById(R.id.viewReport);
        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Add_Guide_Activity.class));
            }
        });

        ImageButton addReport = view.findViewById(R.id.addReport);
        addReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(),Add_Report_Activity.class));

            }
        });

        ShapeableImageView vegitables = view.findViewById(R.id.vegitable);
        vegitables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(getContext(),Guide_post_Activity.class));

                Intent Vintent = new Intent(getContext(),Guide_post_Activity.class);
                Vintent.putExtra("categ","Vegetables");
                startActivity(Vintent);

            }
        });

        ShapeableImageView fruits = view.findViewById(R.id.fruits);
        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(getContext(),Guide_post_Activity.class));

                Intent Vintent = new Intent(getContext(),Guide_post_Activity.class);
                Vintent.putExtra("categ","Fruits");
                startActivity(Vintent);

            }
        });

        ShapeableImageView seed = view.findViewById(R.id.seed);
        seed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(getContext(),Guide_post_Activity.class));

                Intent Vintent = new Intent(getContext(),Guide_post_Activity.class);
                Vintent.putExtra("categ","Seeds");
                startActivity(Vintent);

            }
        });
        ShapeableImageView others = view.findViewById(R.id.others);
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(getContext(),Guide_post_Activity.class));

                Intent Vintent = new Intent(getContext(),Guide_post_Activity.class);
                Vintent.putExtra("categ","Others");
                startActivity(Vintent);

            }
        });

    }
}
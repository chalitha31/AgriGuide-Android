package lk.chalitha.agriguide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    FirebaseAuth auth;
    FirebaseUser userLoginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        userLoginData = auth.getCurrentUser();
        if (userLoginData != null) {
            ImageButton settingbutton = findViewById(R.id.settingButton);
            settingbutton.setVisibility(View.VISIBLE);
        } else {

            TextView homesignin = findViewById(R.id.homesignin);
            homesignin.setVisibility(View.VISIBLE);
            homesignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
//                    finish();
                }
            });
//            Log.d(TAG, "Clicked on Home");
        }

        ImageButton imageButton = findViewById(R.id.settingButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                String navName = getResources().getResourceEntryName(item.getItemId());

//                Log.d(TAG, " "+getResources().getResourceEntryName(item.getItemId()));

                switch (navName) {
                    case "homeNav":
                        Log.d(TAG, "Clicked on Home");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, HomeFragment.class, null).commit();


                        break;
                    case "reportNav":
                        Log.d(TAG, "Clicked on Report");

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, ReportFragment.class, null).commit();

                        break;
                    case "profileNav":
                        Log.d(TAG, "Clicked on Profile");

//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, UserprofileFragment.class,null).commit();
                        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                        startActivity(intent);

                        break;
                    case "bookmarkNav":
                        Log.d(TAG, "Clicked on Bookmark");

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, BookmarkFragment.class, null).commit();


                        break;
                    case "notifyNav":
                        Log.d(TAG, "Clicked on Notify");

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, Notification.class, null).commit();


                        break;
                }
                return true;
            }
        });


    }

}
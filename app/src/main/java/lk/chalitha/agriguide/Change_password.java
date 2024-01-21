package lk.chalitha.agriguide;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Change_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar ct = findViewById(R.id.changepasserdToolbar);
        setSupportActionBar(ct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
package greenwich.comp1786.m_hike;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import greenwich.comp1786.m_hike.database.DatabaseHelper;
import greenwich.comp1786.m_hike.ui.AddHikeActivity;
import greenwich.comp1786.m_hike.ui.ListHikeActivity;
import greenwich.comp1786.m_hike.ui.SearchActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        Button btnAdd = findViewById(R.id.btnAddHike);
        Button btnView = findViewById(R.id.btnViewAll);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnReset = findViewById(R.id.btnReset);


        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, AddHikeActivity.class)));
        btnView.setOnClickListener(v ->startActivity(new Intent(this, ListHikeActivity.class)));
        btnSearch.setOnClickListener(v -> startActivity( new Intent(this, SearchActivity.class)));
        btnReset.setOnClickListener(v -> {
            int count = db.clearAllHikes();
            Toast.makeText(this, "Database cleared (" + count + " hikes).", Toast.LENGTH_SHORT).show();
        });

    }
}
package greenwich.comp1786.m_hike.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import greenwich.comp1786.m_hike.R;
import greenwich.comp1786.m_hike.adapter.HikeAdapter;
import greenwich.comp1786.m_hike.database.DatabaseHelper;
import greenwich.comp1786.m_hike.model.Hike;

public class SearchActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etKeyword;
    private Button btnSearch;
    private RecyclerView rvSearch;
    private HikeAdapter adapter;
    private List<Hike> resultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = new DatabaseHelper(this);
        etKeyword = findViewById(R.id.etKeyword);
        btnSearch = findViewById(R.id.btnSearchGo);
        rvSearch = findViewById(R.id.rvSearch);

        rvSearch.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HikeAdapter(resultList, new HikeAdapter.HikeListener() {
            @Override
            public void onEdit(Hike h) {
                // ✅ mở AddHikeActivity để sửa thông tin
                Intent i = new Intent(SearchActivity.this, AddHikeActivity.class);
                i.putExtra("id", h.getId());
                i.putExtra("name", h.getName());
                i.putExtra("location", h.getLocation());
                i.putExtra("date", h.getDate());
                i.putExtra("length", h.getLength());
                i.putExtra("difficulty", h.getDifficulty());
                i.putExtra("parking", h.getParking());
                i.putExtra("description", h.getDescription());
                i.putExtra("terrain", h.getTerrain());
                i.putExtra("weather", h.getWeather());
                startActivity(i);
            }

            @Override
            public void onDelete(Hike h) {
                int n = db.deleteHike(h.getId());
                Toast.makeText(SearchActivity.this, "Deleted " + n + " hike(s)", Toast.LENGTH_SHORT).show();
                performSearch();
            }
            @Override
            public void onAddObservation(Hike h) {
                Intent i = new Intent(SearchActivity.this, AddObservationActivity.class);
                i.putExtra("hikeId", h.getId());
                i.putExtra("hikeName", h.getName());
                startActivity(i);
            }
        });

        rvSearch.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        resultList.clear();
        String keyword = etKeyword.getText().toString().trim();

        Cursor c = db.searchHikes(keyword, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                Hike h = new Hike();
                h.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.H_ID)));
                h.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_NAME)));
                h.setLocation(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_LOCATION)));
                h.setDate(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_DATE)));
                h.setLength(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_LENGTH)));
                h.setDifficulty(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_DIFFICULTY)));
                h.setParking(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_PARKING)));
                h.setDescription(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_DESC)));
                h.setTerrain(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_TERRAIN)));
                h.setWeather(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_WEATHER)));
                resultList.add(h);
            }
            c.close();
        }

        if (resultList.isEmpty()) {
            Toast.makeText(this, "No results found.", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }
}

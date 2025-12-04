package greenwich.comp1786.m_hike.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import greenwich.comp1786.m_hike.R;
import greenwich.comp1786.m_hike.adapter.HikeAdapter;
import greenwich.comp1786.m_hike.database.DatabaseHelper;
import greenwich.comp1786.m_hike.model.Hike;

public class ListHikeActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private List<Hike> hikes = new ArrayList<>();
    private HikeAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hike);
        db = new DatabaseHelper(this);

        RecyclerView rv = findViewById(R.id.rvHikes);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HikeAdapter(hikes, new HikeAdapter.HikeListener() {
            @Override public void onEdit(Hike h) {
                Intent i = new Intent(ListHikeActivity.this, AddHikeActivity.class);
                // chuyền dữ liệu để sửa
                i.putExtra("id", h.getId());
                i.putExtra("name", h.getName());
                i.putExtra("location", h.getLocation());
                i.putExtra("date", h.getDate());
                i.putExtra("parking", h.getParking());
                i.putExtra("length", h.getLength());
                i.putExtra("difficulty", h.getDifficulty());
                i.putExtra("description", h.getDescription());
                i.putExtra("terrain", h.getTerrain());
                i.putExtra("weather", h.getWeather());
                startActivity(i);
            }
            @Override public void onDelete(Hike h) {
                new AlertDialog.Builder(ListHikeActivity.this)
                        .setMessage("Delete this hike?")
                        .setPositiveButton("Delete", (d, w) -> {
                            int n = db.deleteHike(h.getId());
                            Toast.makeText(ListHikeActivity.this, "Deleted "+n, Toast.LENGTH_SHORT).show();
                            loadData();
                        })
                        .setNegativeButton("Cancel", null).show();
            }

            @Override
            public void onAddObservation(Hike h) {
                Intent i = new Intent(ListHikeActivity.this, AddObservationActivity.class);
                i.putExtra("hikeId", h.getId());
                i.putExtra("hikeName", h.getName());
                startActivity(i);
            }
        });
        rv.setAdapter(adapter);
    }

    @Override protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        hikes.clear();
        Cursor c = db.getAllHikes();
        if (c != null) {
            while (c.moveToNext()) {
                Hike h = new Hike();
                h.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.H_ID)));
                h.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_NAME)));
                h.setLocation(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_LOCATION)));
                h.setDate(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_DATE)));
                h.setParking(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_PARKING)));
                h.setLength(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_LENGTH)));
                h.setDifficulty(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_DIFFICULTY)));
                h.setDescription(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_DESC)));
                h.setTerrain(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_TERRAIN)));
                h.setWeather(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_WEATHER)));
                hikes.add(h);
            }
            c.close();
        }
        adapter.notifyDataSetChanged();
    }
}
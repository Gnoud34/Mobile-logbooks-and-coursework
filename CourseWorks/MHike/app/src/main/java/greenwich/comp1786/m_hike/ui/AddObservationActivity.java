package greenwich.comp1786.m_hike.ui;

import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import greenwich.comp1786.m_hike.R;
import greenwich.comp1786.m_hike.adapter.ObservationAdapter;
import greenwich.comp1786.m_hike.database.DatabaseHelper;
import greenwich.comp1786.m_hike.model.Hike;
import greenwich.comp1786.m_hike.model.Observation;

public class AddObservationActivity extends AppCompatActivity {


    private DatabaseHelper db;
    private Spinner spHike;
    private EditText etObservation, etComment;
    private Button btnSave;
    private RecyclerView rvObservations;

    private List<Hike> hikeList = new ArrayList<>();
    private List<String> hikeNames = new ArrayList<>();
    private List<Observation> obsList = new ArrayList<>();
    private ObservationAdapter obsAdapter;
    private int selectedHikeId = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        db = new DatabaseHelper(this);

        spHike = findViewById(R.id.spHike);
        etObservation = findViewById(R.id.etObs);
        etComment = findViewById(R.id.etComment);
        btnSave = findViewById(R.id.btnSaveObs);
        rvObservations = findViewById(R.id.rvObs);


        rvObservations.setLayoutManager(new LinearLayoutManager(this));
        obsAdapter = new ObservationAdapter(obsList, (Observation obs) -> {
            db.deleteObservation(obs.getId());
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            loadObservations(selectedHikeId);
        });
        rvObservations.setAdapter(obsAdapter);

        // --- Load danh sách hike vào Spinner ---
        loadHikes();
        int hikeId = getIntent().getIntExtra("hikeId", -1);
        if (hikeId != -1) {
            for (int i = 0; i < hikeList.size(); i++) {
                if (hikeList.get(i).getId() == hikeId) {
                    spHike.setSelection(i);
                    selectedHikeId = hikeId;
                    loadObservations(hikeId);
                    break;
                }
            }
        }


        spHike.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < hikeList.size()) {
                    selectedHikeId = hikeList.get(position).getId();
                    loadObservations(selectedHikeId);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedHikeId = -1;
            }
        });
        btnSave.setOnClickListener(v -> saveObservation());

    }

    private void loadHikes() {
        hikeList.clear();
        hikeNames.clear();

        Cursor c = db.getAllHikes();
        if (c != null) {
            while (c.moveToNext()) {
                Hike h = new Hike();
                h.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.H_ID)));
                h.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_NAME)));
                h.setDate(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.H_DATE)));
                hikeList.add(h);
                hikeNames.add(h.getName() + " (" + h.getDate() + ")");
            }
            c.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, hikeNames);
        spHike.setAdapter(adapter);

        if (!hikeList.isEmpty()) {
            selectedHikeId = hikeList.get(0).getId();
            loadObservations(selectedHikeId);
        }
    }

    private void loadObservations(int hikeId) {
        obsList.clear();
        if (hikeId == -1) {
            obsAdapter.notifyDataSetChanged();
            return;
        }

        Cursor c = db.getObservationsByHikeId(hikeId);
        if (c != null) {
            while (c.moveToNext()) {
                Observation o = new Observation();
                o.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.O_ID)));
                o.setHikeId(hikeId);
                o.setObservation(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.O_TEXT)));
                o.setTime(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.O_TIME)));
                o.setComment(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.O_COMMENT)));
                obsList.add(o);
            }
            c.close();
        }
        obsAdapter.notifyDataSetChanged();
    }

    private void saveObservation() {
        if (selectedHikeId == -1) {
            Toast.makeText(this, "Please select a hike.", Toast.LENGTH_SHORT).show();
            return;
        }
        String text = etObservation.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Observation cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        String comment = etComment.getText().toString().trim();
        String now = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        long id = db.insertObservation(selectedHikeId, text, now, comment);
        if (id > 0) {
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            etObservation.setText("");
            etComment.setText("");
            loadObservations(selectedHikeId);
        } else {
            Toast.makeText(this, "Save failed.", Toast.LENGTH_SHORT).show();
        }
    }
}

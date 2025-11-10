package greenwich.comp1786.m_hike.ui;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import greenwich.comp1786.m_hike.R;
import greenwich.comp1786.m_hike.database.DatabaseHelper;
import android.util.Log;
public class AddHikeActivity extends AppCompatActivity {

    private EditText etName, etLocation, etDate, etLength, etDescription, etTerrain, etWeather;
    private Spinner spDifficulty;
    private RadioGroup rgParking;
    private Button btnSave;

    private DatabaseHelper db;
    private int editId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        // === Initialize database ===
        db = new DatabaseHelper(this);

        // === Link XML views ===
        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);
        etDate = findViewById(R.id.etDate);
        etLength = findViewById(R.id.etLength);
        spDifficulty = findViewById(R.id.spDifficulty);
        rgParking = findViewById(R.id.rgParking);
        etTerrain = findViewById(R.id.etTerrain);
        etWeather = findViewById(R.id.etWeather);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);

        // === Spinner data ===
        ArrayAdapter<String> ad = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Easy", "Medium", "Hard", "Very Hard"}
        );
        spDifficulty.setAdapter(ad);

        // === Date picker ===
        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                String dd = (d < 10 ? "0" + d : "" + d);
                String mm = ((m + 1) < 10 ? "0" + (m + 1) : "" + (m + 1));
                etDate.setText(dd + "/" + mm + "/" + y);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // === Check if this is Edit mode ===
        editId = getIntent().getIntExtra("id", -1);
        if (editId != -1) {
            fillFormForEdit();
        }

        // === Save button action ===
        btnSave.setOnClickListener(v -> save());
    }

    private void fillFormForEdit() {
        etName.setText(getIntent().getStringExtra("name"));
        etLocation.setText(getIntent().getStringExtra("location"));
        etDate.setText(getIntent().getStringExtra("date"));
        etLength.setText(getIntent().getStringExtra("length"));
        etDescription.setText(getIntent().getStringExtra("description"));
        etTerrain.setText(getIntent().getStringExtra("terrain"));
        etWeather.setText(getIntent().getStringExtra("weather"));

        String parking = getIntent().getStringExtra("parking");
        if ("Yes".equals(parking)) ((RadioButton) findViewById(R.id.rbYes)).setChecked(true);
        else if ("No".equals(parking)) ((RadioButton) findViewById(R.id.rbNo)).setChecked(true);

        String difficulty = getIntent().getStringExtra("difficulty");
        if (difficulty != null) {
            ArrayAdapter adapter = (ArrayAdapter) spDifficulty.getAdapter();
            int pos = adapter.getPosition(difficulty);
            if (pos >= 0) spDifficulty.setSelection(pos);
        }
    }

    private void save() {

        Log.d("DEBUG_SAVE", "Bắt đầu lưu Hike...");

        String name = etName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String length = etLength.getText().toString().trim();
        String difficulty = spDifficulty.getSelectedItem() == null ? "" : spDifficulty.getSelectedItem().toString();
        int checked = rgParking.getCheckedRadioButtonId();
        String parking = checked == R.id.rbYes ? "Yes" : (checked == R.id.rbNo ? "No" : "");
        String desc = etDescription.getText().toString().trim();
        String terrain = etTerrain.getText().toString().trim();
        String weather = etWeather.getText().toString().trim();

        Log.d("DEBUG_SAVE", "Name=" + name + ", Location=" + location + ", Date=" + date + ", Parking=" + parking);

        // === Validate required fields ===
        if (name.isEmpty() || location.isEmpty() || date.isEmpty()
                || parking.isEmpty() || length.isEmpty() || difficulty.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        // === Insert or Update ===
        if (editId == -1) {
            long id = db.insertHike(name, location, date, parking, length, difficulty, desc, terrain, weather);
            Log.d("DEBUG_SAVE", "Insert result ID=" + id);

            Toast.makeText(this, id > 0 ? "Saved!" : "Save failed!", Toast.LENGTH_SHORT).show();
        } else {
            int n = db.updateHike(editId, name, location, date, parking, length, difficulty, desc, terrain, weather);
            Toast.makeText(this, n > 0 ? "Updated!" : "Update failed!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}

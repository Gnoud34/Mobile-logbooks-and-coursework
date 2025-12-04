package greenwich.comp1786.datastore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import android.os.Bundle;
import android.widget.Button;
import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    RecyclerView rv;
    DatabaseHelper db;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        rv = findViewById(R.id.recyclerView);
        btnBack = findViewById(R.id.btnBackList);

        db = new DatabaseHelper(this);
        ArrayList<Contact> data = db.getAllContacts();

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ContactAdapter(this, data));

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Contact> data = db.getAllContacts();
        rv.setAdapter(new ContactAdapter(this, data));
    }

}

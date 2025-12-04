package greenwich.comp1786.datastore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class DetailActivity extends AppCompatActivity {

    int contactId;
    DatabaseHelper db;
    ImageView avatar;
    TextView nameVal, dobVal, emailVal;
    Button btnDelete, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        avatar = findViewById(R.id.imgDetailAvatar);
        nameVal = findViewById(R.id.txtDetailNameValue);
        dobVal = findViewById(R.id.txtDetailDobValue);
        emailVal = findViewById(R.id.txtDetailEmailValue);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBackDetail);

        db = new DatabaseHelper(this);


        contactId = getIntent().getIntExtra("id", -1);

        for(Contact c: db.getAllContacts()){
            if(c.getId()==contactId){
                avatar.setImageResource(c.getAvatar());
                nameVal.setText(c.getName());
                dobVal.setText(c.getDob());
                emailVal.setText(c.getEmail());
            }
        }

        btnBack.setOnClickListener(v->finish());

        btnDelete.setOnClickListener(v->{
            new AlertDialog.Builder(this)
                    .setTitle("Delete Contact?")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes",(d,w)->{
                        db.deleteContact(contactId);
                        finish();
                    })
                    .setNegativeButton("No",null)
                    .show();
        });
    }
}

package greenwich.comp1786.datastore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import android.content.res.TypedArray;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    ImageView imgAvatar;
    EditText edtName, edtDob, edtEmail;
    Button btnSave, btnView;
    TypedArray avatarSet;
    int selectedAvatar;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgAvatar = findViewById(R.id.imageView);
        edtName = findViewById(R.id.edtName);
        edtDob = findViewById(R.id.edtDob);
        edtEmail = findViewById(R.id.edtEmail);
        btnSave = findViewById(R.id.btnSave);
        btnView = findViewById(R.id.btnView);

        db = new DatabaseHelper(this);
        avatarSet = getResources().obtainTypedArray(R.array.avatarSet);
        selectedAvatar = avatarSet.getResourceId(0,-1);

        imgAvatar.setImageResource(selectedAvatar);
        imgAvatar.setOnClickListener(v -> chooseAvatar());

        btnSave.setOnClickListener(v -> {
            db.insertContact(
                    edtName.getText().toString(),
                    edtDob.getText().toString(),
                    edtEmail.getText().toString(),
                    selectedAvatar
            );
            edtName.setText("");
            edtDob.setText("");
            edtEmail.setText("");
        });

        btnView.setOnClickListener(v -> {
            startActivity(new Intent(this, ContactListActivity.class));
        });
    }

    private void chooseAvatar(){
        String[] opt = {"Avatar 1","Avatar 2","Avatar 3","Avatar 4"};
        new AlertDialog.Builder(this)
                .setTitle("Choose Avatar")
                .setItems(opt, (d,w)->{
                    selectedAvatar = avatarSet.getResourceId(w,-1);
                    imgAvatar.setImageResource(selectedAvatar);
                })
                .show();
    }
}

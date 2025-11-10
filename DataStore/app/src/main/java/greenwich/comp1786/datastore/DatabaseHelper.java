package greenwich.comp1786.datastore;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "contactdb";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE contacts(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "dob TEXT," +
                "email TEXT," +
                "avatar INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldV,int newV){
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public void insertContact(String name, String dob, String email, int avatar){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("dob", dob);
        cv.put("email", email);
        cv.put("avatar", avatar);
        db.insert("contacts", null, cv);
    }

    public ArrayList<Contact> getAllContacts(){
        ArrayList<Contact> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM contacts", null);
        while(c.moveToNext()){
            list.add(new Contact(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getInt(4)
            ));
        }
        return list;
    }

    public void deleteContact(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("contacts","id=?",new String[]{String.valueOf(id)});
    }
}

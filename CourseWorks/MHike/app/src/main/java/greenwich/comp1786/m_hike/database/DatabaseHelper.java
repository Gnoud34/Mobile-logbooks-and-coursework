package greenwich.comp1786.m_hike.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "m_hike.db";
    private static final int DB_VER = 5;


    // Table Hikes
    public static final String T_HIKES = "hikes";
    public static final String H_ID = "id";
    public static final String H_NAME = "name";
    public static final String H_LOCATION = "location";
    public static final String H_DATE = "date";
    public static final String H_PARKING = "parking";
    public static final String H_LENGTH = "length";
    public static final String H_DIFFICULTY = "difficulty";
    public static final String H_DESC = "description";
    public static final String H_TERRAIN = "terrain";
    public static final String H_WEATHER = "weather";



    public static final String T_OBS = "observations";
    public static final String O_ID = "id";
    public static final String O_HIKE_ID = "hikeID";
    public static final String O_TEXT = "observation";
    public static final String O_TIME = "time";
    public static final String O_COMMENT = "comment";

    public DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VER); }


    @Override
    public void onCreate(SQLiteDatabase db) {
        android.util.Log.d("DB_CREATE", "Database created successfully!");
        db.execSQL("CREATE TABLE " + T_HIKES + " (" +
        H_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        H_NAME + " TEXT NOT NULL, " +
        H_LOCATION + " TEXT NOT NULL, " +
        H_DATE + " TEXT NOT NULL, " +
        H_PARKING + " TEXT NOT NULL, " +
        H_LENGTH + " TEXT NOT NULL, " +
        H_DIFFICULTY + " TEXT NOT NULL, " +
        H_DESC + " TEXT, " +
        H_TERRAIN + " TEXT, " +
        H_WEATHER + " TEXT" +
        ");");


        db.execSQL("CREATE TABLE " + T_OBS + " (" +
        O_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        O_HIKE_ID + " INTEGER NOT NULL, " +
        O_TEXT + " TEXT NOT NULL," +
        O_TIME + " TEXT NOT NULL," +
        O_COMMENT + " TEXT," +
        "FOREIGN KEY(" + O_HIKE_ID + ") REFERENCES " + T_HIKES + "(" + H_ID + ") ON DELETE CASCADE" +

        ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_OBS);
        db.execSQL("DROP TABLE IF EXISTS " + T_HIKES);
        onCreate(db);
    }

    //Hikes CRUD

    public long insertHike(String name, String location, String date, String parking,
                           String length, String difficulty, String desc, String terrain, String weather) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(H_NAME, name); v.put(H_LOCATION, location); v.put(H_DATE, date);
        v.put(H_PARKING, parking); v.put(H_LENGTH, length); v.put(H_DIFFICULTY, difficulty);
        v.put(H_DESC, desc); v.put(H_TERRAIN, terrain); v.put(H_WEATHER, weather);
        return db.insert(T_HIKES, null, v);
    }

    public Cursor getAllHikes() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + T_HIKES + " ORDER BY " + H_ID + " DESC", null);
    }

    public int updateHike(int id, String name, String location, String date, String parking,
                          String length, String difficulty, String desc, String terrain, String weather) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(H_NAME, name); v.put(H_LOCATION, location); v.put(H_DATE, date);
        v.put(H_PARKING, parking); v.put(H_LENGTH, length); v.put(H_DIFFICULTY, difficulty);
        v.put(H_DESC, desc); v.put(H_TERRAIN, terrain); v.put(H_WEATHER, weather);
        return db.update(T_HIKES, v, H_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteHike(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(T_HIKES, H_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int clearAllHikes() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(T_OBS, null, null); // clear obs first
        return db.delete(T_HIKES, null, null);
    }

    /* ====== OBSERVATIONS CRUD ====== */
    public long insertObservation(int hikeId, String text, String time, String comment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(O_HIKE_ID, hikeId); v.put(O_TEXT, text); v.put(O_TIME, time); v.put(O_COMMENT, comment);
        return db.insert(T_OBS, null, v);
    }

    public Cursor getObservationsByHikeId(int hikeId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + T_OBS + " WHERE " + O_HIKE_ID + "=? ORDER BY " + O_ID + " DESC",
                new String[]{String.valueOf(hikeId)});
    }

    public int deleteObservation(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(T_OBS, O_ID + "=?", new String[]{String.valueOf(id)});
    }


    /* ====== SEARCH ====== */
    public Cursor searchHikes(String nameLike, String location, String length, String date) {
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + T_HIKES + " WHERE 1=1");
        java.util.ArrayList<String> args = new java.util.ArrayList<>();

        if (nameLike != null && !nameLike.trim().isEmpty()) {
            sql.append(" AND ").append(H_NAME).append(" LIKE ?");
            args.add("%" + nameLike.trim() + "%");
        }
        if (location != null && !location.trim().isEmpty()) {
            sql.append(" AND ").append(H_LOCATION).append(" LIKE ?");
            args.add("%" + location.trim() + "%");
        }
        if (length != null && !length.trim().isEmpty()) {
            sql.append(" AND ").append(H_LENGTH).append(" LIKE ?");
            args.add("%" + length.trim() + "%");
        }
        if (date != null && !date.trim().isEmpty()) {
            sql.append(" AND ").append(H_DATE).append(" LIKE ?");
            args.add("%" + date.trim() + "%");
        }

        sql.append(" ORDER BY ").append(H_ID).append(" DESC");
        return db.rawQuery(sql.toString(), args.toArray(new String[0]));
    }
}

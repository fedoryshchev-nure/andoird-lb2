package ua.nure.fedoryshchev.lb2.utils.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "Notes";

    // Table columns
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String IMPORTANCE = "importance";
    public static final String DATE_CREATED= "dateCreated";
    public static final String IMAGE_PATH = "imagePath";

    // Database Information
    static final String DB_NAME = "Notes.DB";

    // database version
    static final int DB_VERSION = 3;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " TEXT NOT NULL, "
            + DESCRIPTION + " TEXT NOT NULL, "
            + IMPORTANCE+ " TEXT NOT NULL, "
            + DATE_CREATED + " TEXT NOT NULL, "
            + IMAGE_PATH + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(newVersion);
        if (oldVersion < 2)
            upgradeVersion2(db);
        if (oldVersion < 3)
            upgradeVersion3(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void upgradeVersion2(SQLiteDatabase database) {
        database.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + DATE_CREATED + " TEXT NOT NULL DEFAULT '';");
    }

    private void upgradeVersion3(SQLiteDatabase database) {
        database.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + IMAGE_PATH + " TEXT NOT NULL DEFAULT '';");
    }
}

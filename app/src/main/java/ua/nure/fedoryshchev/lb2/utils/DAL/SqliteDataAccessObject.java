package ua.nure.fedoryshchev.lb2.utils.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ua.nure.fedoryshchev.lb2.models.Importance;
import ua.nure.fedoryshchev.lb2.models.Note;

public class SqliteDataAccessObject implements IDataAccessObject {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public SqliteDataAccessObject(Context c) {
        context = c;
        open();
    }

    public SqliteDataAccessObject open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void add(String title, String description,
                       String importance, String dateCreated, String imagePath) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TITLE, title);
        contentValue.put(DatabaseHelper.DESCRIPTION, description);
        contentValue.put(DatabaseHelper.IMPORTANCE, importance);
        contentValue.put(DatabaseHelper.DATE_CREATED, dateCreated);
        contentValue.put(DatabaseHelper.IMAGE_PATH, imagePath);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public ArrayList<Note> Get() throws ParseException {
        ArrayList<Note> notes = new ArrayList<>();

        String[] columns = new String[] {
                DatabaseHelper._ID,
                DatabaseHelper.TITLE,
                DatabaseHelper.DESCRIPTION,
                DatabaseHelper.IMPORTANCE,
                DatabaseHelper.DATE_CREATED,
                DatabaseHelper.IMAGE_PATH
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            notes.add(new Note(
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper._ID))),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESCRIPTION)),
                    Importance.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMPORTANCE))),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGE_PATH)),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_CREATED)))));
        }

        return notes;
    }

    public void update(int _id, String title, String description,
                      String importance, String dateCreated, String imagePath) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TITLE, title);
        contentValue.put(DatabaseHelper.DESCRIPTION, description);
        contentValue.put(DatabaseHelper.IMPORTANCE, importance);
        contentValue.put(DatabaseHelper.DATE_CREATED, dateCreated);
        contentValue.put(DatabaseHelper.IMAGE_PATH, imagePath);
        database.update(DatabaseHelper.TABLE_NAME, contentValue,
                DatabaseHelper._ID + " = " + _id, null);
    }

    public void Remove(int id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + id, null);
    }

    @Override
    public void AddOrUpdate(Note note) {
        if (note.Id < 0)
            add(note.Title,
                    note.Description,
                    note.Importance.name(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(note.DateCreated),
                    note.GetImagePath());
        else
            update(note.Id,
                    note.Title,
                    note.Description,
                    note.Importance.name(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(note.DateCreated),
                    note.GetImagePath());

    }
}
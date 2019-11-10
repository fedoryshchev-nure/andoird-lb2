package ua.nure.fedoryshchev.lb2.utils.DAL;

import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;

import ua.nure.fedoryshchev.lb2.models.Note;

public class Repository {
    IDataAccessObject dataAccessObject;

    public Repository(Context context) {
        dataAccessObject = new SqliteDataAccessObject(context);
    }

    public ArrayList<Note> Get() {
        try {
            return dataAccessObject.Get();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void AddOrUpdate(Note note) {
        dataAccessObject.AddOrUpdate(note);
    }

    public void Remove(int id) {
        dataAccessObject.Remove(id);
    }
}

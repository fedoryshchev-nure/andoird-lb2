package ua.nure.fedoryshchev.lb1.utils.DAL;

import java.util.ArrayList;

import ua.nure.fedoryshchev.lb1.models.Note;

public class Repository {
    IDataAccessObject dataAccessObject;

    public Repository(String direcotry) {
        dataAccessObject = new FileDataAccessObject(direcotry);
    }

    public ArrayList<Note> Get() {
        return dataAccessObject.Get();
    }

    public Note Get(int id) {
        return dataAccessObject.Get(id);
    }

    public int GetNextId() {
        return dataAccessObject.GetNextId();
    }

    public void AddOrUpdate(Note note) {
        dataAccessObject.AddOrUpdate(note);
        dataAccessObject.SaveChanges();
    }

    public void Remove(int id) {
        dataAccessObject.Remove(id);
        dataAccessObject.SaveChanges();
    }
}

package ua.nure.fedoryshchev.lb1.utils.DAL;

import java.util.ArrayList;

import ua.nure.fedoryshchev.lb1.models.Note;

public interface IDataAccessObject {
    ArrayList<Note> Get();
    Note Get(int id);
    void AddOrUpdate(Note note);
    void Remove(int id);
    void SaveChanges();

    int GetNextId();
}

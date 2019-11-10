package ua.nure.fedoryshchev.lb2.utils.DAL;

import java.text.ParseException;
import java.util.ArrayList;

import ua.nure.fedoryshchev.lb2.models.Note;

public interface IDataAccessObject {
    ArrayList<Note> Get() throws ParseException;
    void AddOrUpdate(Note note);
    void Remove(int id);
}

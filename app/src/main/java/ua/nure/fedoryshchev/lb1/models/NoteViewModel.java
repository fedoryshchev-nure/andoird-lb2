package ua.nure.fedoryshchev.lb1.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoteViewModel extends ViewModel {
    public MutableLiveData<Note> Note = new MutableLiveData<>();

    public void setNote(Note n) {
        Note.setValue(n);
    }

    public void setImportance(Importance importance) {
        Note note = Note.getValue();
        note.Importance = importance;
        Note.setValue(note);
    }

    public void setImage(String imagePath) {
        Note note = Note.getValue();
        note.SetImagePath(imagePath);
        Note.setValue(note);
    }
}

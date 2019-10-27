package ua.nure.fedoryshchev.lb1.utils.DAL;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ua.nure.fedoryshchev.lb1.models.Note;


public class FileDataAccessObject implements IDataAccessObject {
    private String file;
    private ArrayList<Note> notes;

    public FileDataAccessObject(String direcotry) {
        file = direcotry + "/storage.json";

        try {
            File f = new File(file);
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try( JsonReader reader = new JsonReader(new FileReader(file))) {
            reader.beginArray();
            notes = new Gson().fromJson(
                    reader,
                    new TypeToken<ArrayList<Note>>(){}.getType());
            reader.endArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (notes == null) {
            notes = new ArrayList<>();
        }
    }

    public Note Get(int id) {
        return notes.get(id);
    }

    public ArrayList<Note> Get() {
        return notes;
    }

    public void AddOrUpdate(Note note) {
        boolean found = false;
        for (int i = 0; i < notes.size(); ++i) {
            if (notes.get(i).Id == note.Id) {
                found = true;
                notes.set(i, note);
                break;
            }
        }
        if (!found) {
            notes.add(note);
        }
    }

    public void Remove(int id) {
        for (int i = 0; i < notes.size(); ++i) {
            if (notes.get(i).Id == id) {
                notes.remove(i);
                break;
            }
        }
    }

    public void SaveChanges() {
        try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
            writer.beginArray();
            writer.jsonValue(new Gson().toJson(notes));
            writer.endArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int GetNextId() {
        return notes.size() > 1 ? notes.get(notes.size() - 1).Id + 1 : 2;
    }
}

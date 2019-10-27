package ua.nure.fedoryshchev.lb1.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ua.nure.fedoryshchev.lb1.R;
import ua.nure.fedoryshchev.lb1.models.Importance;
import ua.nure.fedoryshchev.lb1.models.Note;
import ua.nure.fedoryshchev.lb1.utils.DAL.Repository;
import ua.nure.fedoryshchev.lb1.utils.NotesAdapter;

public class NotesActivity extends AppCompatActivity {
    Repository repository;
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        repository = new Repository(getBaseContext().getFilesDir().getPath());
        adapter = new NotesAdapter(this, repository.Get());
        ListView lv = findViewById(R.id.lstView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            Note note = (Note) adapterView.getItemAtPosition(i);
            openDetailsPage(view, note);
        });
    }

    public void openDetailsPage(View view) {
        openDetailsPage(view, new Note(repository.GetNextId()));
    }

    public void openDetailsPage(View view, Note note) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(Note.class.getName(), note);

        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Note note = data.getExtras().getParcelable(Note.class.getName());
        if (resultCode == RESULT_OK) {
            repository.AddOrUpdate(note);
            adapter.notifyDataSetChanged();
        }
    }
}

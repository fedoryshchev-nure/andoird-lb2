package ua.nure.fedoryshchev.lb2.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ua.nure.fedoryshchev.lb2.R;
import ua.nure.fedoryshchev.lb2.models.Importance;
import ua.nure.fedoryshchev.lb2.models.Note;
import ua.nure.fedoryshchev.lb2.utils.DAL.Repository;
import ua.nure.fedoryshchev.lb2.utils.NotesAdapter;

public class NotesActivity extends AppCompatActivity {
    Repository repository;
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        repository = new Repository(this);
        adapter = new NotesAdapter(this, new ArrayList<>());

        MyAsyncTask task = new MyAsyncTask();
        task.execute();

        ListView lv = findViewById(R.id.lstView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            Note note = (Note) adapterView.getItemAtPosition(i);
            adapter.remove(note);
            openDetailsPage(view, note);
        });
        lv.setOnItemLongClickListener((adapterView, view, i, id) -> {
            Note note = (Note) adapterView.getItemAtPosition(i);
            removeNote(view, note);

            return true;
        });

        Spinner dropdown = findViewById(R.id.importance_dropdown);
        String[] dropdown_items = new String[] {
                getResources().getString(R.string.all),
                getResources().getString(R.string.low),
                getResources().getString(R.string.medium),
                getResources().getString(R.string.high)};
        ArrayAdapter dropdown_adapter = new ArrayAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                dropdown_items);
        dropdown.setAdapter(dropdown_adapter);

        ((EditText)findViewById(R.id.searchText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {
                filerNotes();
            }
        });
        ((Spinner)findViewById(R.id.importance_dropdown)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filerNotes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class MyAsyncTask extends AsyncTask {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Object doInBackground(Object[] objects) {
            setNotes(repository.Get());

            return null;
        }
    }

    private class MyFilterAsyncTask extends AsyncTask {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Object doInBackground(Object[] objects) {
            filerNotes();

            return null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("visibility", findViewById(R.id.filters_layout).getVisibility());
        savedInstanceState.putString("search_string", ((EditText)findViewById(R.id.searchText)).getText().toString());
        savedInstanceState.putInt("importance", ((Spinner)findViewById(R.id.importance_dropdown))
                .getSelectedItemPosition());

        super.onSaveInstanceState(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        findViewById(R.id.filters_layout).setVisibility(savedInstanceState.getInt("visibility"));
        ((EditText)findViewById(R.id.searchText)).setText(savedInstanceState.getString("search_string"));
        ((Spinner)findViewById(R.id.importance_dropdown)).setSelection(savedInstanceState.getInt("importance"));

        filerNotes();

        super.onSaveInstanceState(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void toggleFilersVisibility(View view) {
        LinearLayout l = findViewById(R.id.filters_layout);
        int visibility = l.getVisibility();

        if (visibility == View.GONE) {
            l.setVisibility(View.VISIBLE);
            filerNotes();
        }
        else {
            l.setVisibility(View.GONE);
            setNotes(repository.Get());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filerNotes() {
        String keyWordFilter = ((EditText)findViewById(R.id.searchText)).getText().toString();
        String selectedImportanceValue = ((Spinner)findViewById(R.id.importance_dropdown))
                .getSelectedItem()
                .toString();

        Stream<Note> stream = repository
            .Get()
            .stream();

        if (selectedImportanceValue != getResources().getString(R.string.all)) {
            Importance importanceFilter = Importance.valueOf(selectedImportanceValue);
            stream = stream.filter(x -> x.Importance == importanceFilter);
        }

        stream = stream
                .filter(x -> x.Title.contains(keyWordFilter)
                        || x.Description.contains(keyWordFilter));

        ArrayList<Note> notes = (ArrayList<Note>) stream.collect(Collectors.toList());

        setNotes(notes);
    }

    public void setNotes(ArrayList<Note> notes) {
        adapter.clear();
        adapter.addAll(notes);
        adapter.notifyDataSetChanged();
    }

    public void removeNote(View view, Note note) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.confirm_title)
            .setMessage(R.string.confirm_text)
            .setPositiveButton(R.string.yes, (dialog, whichButton) -> {
                repository.Remove(note.Id);
                adapter.remove(note);
                adapter.notifyDataSetChanged();
        })
        .setNegativeButton(R.string.no, null)
        .show();
    }

    public void openDetailsPage(View view) {
        openDetailsPage(view, new Note());
    }

    public void openDetailsPage(View view, Note note) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(Note.class.getName(), note);

        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyFilterAsyncTask t = new MyFilterAsyncTask();
        Note note = data.getExtras().getParcelable(Note.class.getName());
        if (resultCode == RESULT_OK) {
            repository.AddOrUpdate(note);
            adapter.add(note);
            adapter.notifyDataSetChanged();
        }
    }
}

package ua.nure.fedoryshchev.lb2.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ua.nure.fedoryshchev.lb2.R;
import ua.nure.fedoryshchev.lb2.models.Note;

public class NotesAdapter extends ArrayAdapter<Note> implements Filterable {
    private ArrayList<Note> originalNotes;
    private ArrayList<Note> filteredNotes;
    private NotesFilter filter;
    private Context context;

    public NotesAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
        this.context = context;
        originalNotes = notes;
        filteredNotes = notes;
        filter = new NotesFilter();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.itiem_note,
                            parent,
                            false);
        }

        ImageView imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
        TextView textViewTitle = convertView.findViewById(R.id.item_note_title);
        TextView textViewDate = convertView.findViewById(R.id.item_note_date);
        ImageView imageViewImportance = convertView.findViewById(R.id.imageViewImportance);

        imageViewIcon.setImageDrawable(context.getDrawable(R.drawable.ef_image_placeholder));
        if (note.GetImage() != null) {
            File imgFile = new  File(note.GetImagePath());
            if (imgFile.exists()) {
                imageViewIcon.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            }
        }
        textViewTitle.setText(note.Title);
        textViewDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( note.DateCreated));
        String color = "#ff99cc00";
        switch (note.Importance) {
            case Medium: color = "#ffffbb33"; break;
            case High: color = "#ffff4444"; break;
        }
        imageViewImportance.setColorFilter(Color.parseColor(color));

        return convertView;
    }

    public Filter getFilter() {
        return filter;
    }

    private class NotesFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    }
}

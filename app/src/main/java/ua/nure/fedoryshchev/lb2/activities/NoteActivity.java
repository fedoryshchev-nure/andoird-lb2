package ua.nure.fedoryshchev.lb2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import ua.nure.fedoryshchev.lb2.R;
import ua.nure.fedoryshchev.lb2.databinding.ActivityNoteBinding;
import ua.nure.fedoryshchev.lb2.models.Note;
import ua.nure.fedoryshchev.lb2.models.NoteViewModel;

public class NoteActivity extends AppCompatActivity {
    private NoteViewModel viewModel;
    private ImageView imageViewIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNoteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_note);
        viewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        Note note = getIntent().getExtras().getParcelable(Note.class.getName());
        viewModel.setNote(note);
        binding.setViewModel(viewModel);

        imageViewIcon = findViewById(R.id.imageViewIconEditor);

        if (note.GetImage() != null)
            imageViewIcon.setImageBitmap(note.GetImage());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (viewModel.Note.getValue().GetImage() != null)
            imageViewIcon.setImageBitmap(viewModel.Note.getValue().GetImage());

     super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Note.class.getName(), viewModel.Note.getValue());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void selectImage(View view) {
        ImagePicker.create(this).single().start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            viewModel.setImage(image.getPath());
            imageViewIcon.setImageBitmap(viewModel.Note.getValue().GetImage());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

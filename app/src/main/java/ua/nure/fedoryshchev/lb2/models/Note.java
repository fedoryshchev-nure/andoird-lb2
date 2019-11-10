package ua.nure.fedoryshchev.lb2.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.Date;

public class Note implements Parcelable {
    public Note(int id) {
        this(id,"", "", ua.nure.fedoryshchev.lb2.models.Importance.Low, "");
    }

    public Note(Integer id,
                String title,
                String description,
                Importance importance,
                String imagePath,
                Date dateCreated) {
        Id = id;
        Title = title;
        Description = description;
        Importance = importance;
        SetImagePath(imagePath);
        DateCreated = dateCreated;
    }

    public Note(Integer id,
                String title,
                String description,
                Importance importance,
                String imagePath
               ) {
        this(id,
                title,
                description,
                importance,
                imagePath,
                new Date(System.currentTimeMillis()));
    }

    public int Id;
    public String Title;
    public String Description;
    public Importance Importance;
    private String imagePath;

    public Note() {
        this(-1);
    }

    public String GetImagePath() { return imagePath; }
    public void SetImagePath(String imagePath) {
        this.imagePath = imagePath;
        if (this.image != null)
            this.image.recycle();
        this.image = null;
    }
    public Date DateCreated;

    private Bitmap image;

    protected Note(Parcel in) {
        Id = in.readInt();
        Title = in.readString();
        Description = in.readString();
        Importance = Importance.valueOf(in.readString());
        SetImagePath(in.readString());
        DateCreated = new Date(in.readLong());
    }

    public Bitmap GetImage() {
        if (image == null) {
            File imgFile = new  File(GetImagePath());

            if (imgFile.exists()) {
                image = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
        }

        return image;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Title);
        parcel.writeString(Description);
        parcel.writeString(Importance.name());
        parcel.writeString(GetImagePath());
        parcel.writeLong(DateCreated.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

package com.example.roomdb2;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Module {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name="image")
    private String image;
    @ColumnInfo(name="name")
    private String name;
    @ColumnInfo(name="description")
    private String description;
    @ColumnInfo(name="os")
    private String os;

    public Module(String image, String name, String description, String os) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.os = os;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}

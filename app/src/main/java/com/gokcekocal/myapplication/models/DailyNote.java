package com.gokcekocal.myapplication.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DailyNote {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "myNote")
    private String myNote;

    @ColumnInfo(name = "scoreForMySelf")
    private int scoreForMySelf;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "howMany")
    private int howMany;

    public DailyNote(String myNote, int scoreForMySelf, long date, int howMany) {
        this.myNote = myNote;
        this.scoreForMySelf = scoreForMySelf;
        this.date = date;
        this.howMany = howMany;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMyNote() {
        return myNote;
    }

    public void setMyNote(String myNote) {
        this.myNote = myNote;
    }

    public int getScoreForMySelf() {
        return scoreForMySelf;
    }

    public void setScoreForMySelf(int scoreForMySelf) {
        this.scoreForMySelf = scoreForMySelf;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getHowMany() {
        return howMany;
    }

    public void setHowMany(int howMany) {
        this.howMany = howMany;
    }
}

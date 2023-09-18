package com.gokcekocal.myapplication.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserDetails {

    @PrimaryKey(autoGenerate = false)
    private int id;

    @ColumnInfo(name = "cigarettesPerDay")
    private int cigarettesPerDay;

    @ColumnInfo(name = "cigarettesPerPack")
    private int cigarettesPerPack;

    @ColumnInfo(name = "yearsOfSmoking")
    private int yearsOfSmoking;

    @ColumnInfo(name = "pricePerPackage")
    private float pricePerPackage;

    public UserDetails(int id, int cigarettesPerDay, int cigarettesPerPack, int yearsOfSmoking, float pricePerPackage) {
        this.id = id;
        this.cigarettesPerDay = cigarettesPerDay;
        this.cigarettesPerPack = cigarettesPerPack;
        this.yearsOfSmoking = yearsOfSmoking;
        this.pricePerPackage = pricePerPackage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCigarettesPerDay() {
        return cigarettesPerDay;
    }

    public void setCigarettesPerDay(int cigarettesPerDay) {
        this.cigarettesPerDay = cigarettesPerDay;
    }

    public int getCigarettesPerPack() {
        return cigarettesPerPack;
    }

    public void setCigarettesPerPack(int cigarettesPerPack) {
        this.cigarettesPerPack = cigarettesPerPack;
    }

    public int getYearsOfSmoking() {
        return yearsOfSmoking;
    }

    public void setYearsOfSmoking(int yearsOfSmoking) {
        this.yearsOfSmoking = yearsOfSmoking;
    }

    public float getPricePerPackage() {
        return pricePerPackage;
    }

    public void setPricePerPackage(float pricePerPackage) {
        this.pricePerPackage = pricePerPackage;
    }
}

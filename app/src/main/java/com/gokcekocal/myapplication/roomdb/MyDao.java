package com.gokcekocal.myapplication.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gokcekocal.myapplication.models.DailyNote;
import com.gokcekocal.myapplication.models.Reasons;
import com.gokcekocal.myapplication.models.UserDetails;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface MyDao {

    //QUERY'S
    @Query("SELECT * FROM DailyNote")
    Flowable<List<DailyNote>> getAllDailyNotes();

    @Query("SELECT * FROM Reasons")
    Flowable<List<Reasons>> getAllReasons();

    @Query("SELECT * FROM UserDetails")
    Flowable<List<UserDetails>> getAllUserDetails();


    //INSERT TRANSACTIONS
    @Insert
    Completable insertDailyNote(DailyNote dailyNote);

    @Insert
    Completable insertReasons(Reasons reasons);

    @Insert
    Completable insertUser(UserDetails userDetails);


    //DELETE TRANSACTIONS
    @Delete
    Completable deleteDailyNote(DailyNote dailyNote);

    @Delete
    Completable deleteReason(Reasons reasons);

    //Update User Details ->
    @Update
    Completable updateUserDetails(UserDetails userDetails);


}

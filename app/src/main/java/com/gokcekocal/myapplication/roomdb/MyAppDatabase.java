package com.gokcekocal.myapplication.roomdb;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.gokcekocal.myapplication.models.DailyNote;
import com.gokcekocal.myapplication.models.Reasons;
import com.gokcekocal.myapplication.models.UserDetails;
import java.util.concurrent.Executors;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Database(entities = {DailyNote.class, Reasons.class, UserDetails.class}, version = 1)
public abstract class MyAppDatabase extends RoomDatabase {
    public abstract MyDao myDao();

    private static volatile MyAppDatabase INSTANCE;

    public static MyAppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MyAppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MyAppDatabase.class, "my_database")
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    MyDao myDao = INSTANCE.myDao();
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    compositeDisposable.add(myDao.insertReasons(new Reasons("Cildim daha sağlıklı görünecek ve ben daha genç görüneceğim."))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe());

                    compositeDisposable.add(myDao.insertReasons(new Reasons("Dişlerimde ve tırnaklarımda lekeler olmayacak."))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe());

                    compositeDisposable.add(myDao.insertReasons(new Reasons("Kanser, kalp krizi, kalp hastalığı, inme, katarakt ve başka hastalıklara yakalanma olasılığım azalacak."))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe());
                }
            });
        }

    };

}


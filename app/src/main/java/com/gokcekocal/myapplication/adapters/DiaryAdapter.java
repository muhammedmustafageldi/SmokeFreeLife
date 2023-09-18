package com.gokcekocal.myapplication.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gokcekocal.myapplication.R;
import com.gokcekocal.myapplication.databinding.RecyclerRowDiaryBinding;
import com.gokcekocal.myapplication.models.DailyNote;
import com.gokcekocal.myapplication.roomdb.MyAppDatabase;
import com.gokcekocal.myapplication.roomdb.MyDao;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {

    private final List<DailyNote> dailyNotes;
    private MediaPlayer mediaPlayer;
    private final Context context;
    private OnItemDeleteListenerDaily listener;
    private CompositeDisposable compositeDisposable;

    public DiaryAdapter(List<DailyNote> dailyNotes, Context context) {
        this.dailyNotes = dailyNotes;
        this.context = context;
    }

    public void setListener(OnItemDeleteListenerDaily listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowDiaryBinding binding = RecyclerRowDiaryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.diaryDescriptionTxt.setText(dailyNotes.get(position).getMyNote());
        holder.binding.diaryStarTxt.setText(String.valueOf(dailyNotes.get(position).getScoreForMySelf()));
        holder.binding.diaryHowManyTxt.setText(String.valueOf(dailyNotes.get(position).getHowMany()));

        Date date = new Date(dailyNotes.get(position).getDate());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(date);
        holder.binding.diaryDateTxt.setText(formattedDate);

        holder.binding.diaryDeleteButton.setOnClickListener(view -> {
            //Delete transaction
            //Init required objects of database
            MyAppDatabase database = MyAppDatabase.getInstance(context);
            MyDao dao = database.myDao();
            compositeDisposable = new CompositeDisposable();

            compositeDisposable.add(dao.deleteDailyNote(dailyNotes.get(position))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());

            //Notify fragment that item has been deleted
            if (listener != null) {
                listener.onItemDelete(dailyNotes.get(position), position);
            }

            mediaPlayer = MediaPlayer.create(context, R.raw.deletesound);
            mediaPlayer.start();
        });
    }

    @Override
    public int getItemCount() {
        return dailyNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerRowDiaryBinding binding;

        public ViewHolder(@NonNull RecyclerRowDiaryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemDeleteListenerDaily {
        void onItemDelete(DailyNote dailyNote, int position);
    }

    public void clearMemory() {
        if (mediaPlayer != null) {
            mediaPlayer = null;
        }
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

}

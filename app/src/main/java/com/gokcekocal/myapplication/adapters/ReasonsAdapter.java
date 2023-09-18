package com.gokcekocal.myapplication.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gokcekocal.myapplication.R;
import com.gokcekocal.myapplication.databinding.RecyclerRowReasonsBinding;
import com.gokcekocal.myapplication.models.Reasons;
import com.gokcekocal.myapplication.roomdb.MyAppDatabase;
import com.gokcekocal.myapplication.roomdb.MyDao;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReasonsAdapter extends RecyclerView.Adapter<ReasonsAdapter.ViewHolder> {

    private Context context;
    private final List<Reasons> reasonsList;
    private MediaPlayer mediaPlayer;
    private OnItemDeleteListenerReasons listener;
    private CompositeDisposable compositeDisposable;

    public ReasonsAdapter(Context context, List<Reasons> reasonsList) {
        this.context = context;
        this.reasonsList = reasonsList;
    }

    public void setListener(OnItemDeleteListenerReasons listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowReasonsBinding binding = RecyclerRowReasonsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.reasonTxt.setText(reasonsList.get(position).getReason());

        holder.binding.reasonDeleteButton.setOnClickListener(view -> {
            //Init required objects of database
            MyAppDatabase database = MyAppDatabase.getInstance(context);
            MyDao dao = database.myDao();
            compositeDisposable = new CompositeDisposable();

            compositeDisposable.add(dao.deleteReason(reasonsList.get(position))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());

            //Notify fragment that item has been deleted
            if (listener != null) {
                listener.onItemDelete(reasonsList.get(position), position);
            }

            mediaPlayer = MediaPlayer.create(context, R.raw.deletesound);
            mediaPlayer.start();
        });
    }

    @Override
    public int getItemCount() {
        return reasonsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerRowReasonsBinding binding;

        public ViewHolder(@NonNull RecyclerRowReasonsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemDeleteListenerReasons {
        void onItemDelete(Reasons reasons, int position);
    }

    public void clearMemory() {
        if (mediaPlayer != null) {
            mediaPlayer = null;
        }
        context = null;
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }


}

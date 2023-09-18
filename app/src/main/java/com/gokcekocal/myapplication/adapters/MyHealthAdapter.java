package com.gokcekocal.myapplication.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gokcekocal.myapplication.databinding.RecyclerRowMyHealthBinding;
import com.gokcekocal.myapplication.models.Heal;
import java.util.List;

public class MyHealthAdapter extends RecyclerView.Adapter<MyHealthAdapter.ViewHolder> {

    private final List<Heal> heals;
    private final long userTime;

    public MyHealthAdapter(List<Heal> heals, long userTime) {
        this.heals = heals;
        this.userTime = userTime;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowMyHealthBinding binding = RecyclerRowMyHealthBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.healthRecyclerTxt.setText(heals.get(position).getTitle());
        long time = heals.get(position).getTime();
        int calculatedProgress = calculatePercentage(userTime, time);
        if (calculatedProgress > 100) {
            calculatedProgress = 100;
        }
        holder.binding.healthRecyclerProgressBar.setProgress(calculatedProgress);
        holder.binding.healthProgressTxt.setText("%" + calculatedProgress);

    }

    @Override
    public int getItemCount() {
        return heals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerRowMyHealthBinding binding;

        public ViewHolder(@NonNull RecyclerRowMyHealthBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private int calculatePercentage(long userTime, long time) {
        return (int) ((userTime * 100) / time);
    }

}

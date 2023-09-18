package com.gokcekocal.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gokcekocal.myapplication.R;
import com.gokcekocal.myapplication.databinding.RecyclerRowDaySuccessBinding;
import com.gokcekocal.myapplication.models.DaySuccess;

import java.util.List;

public class DaySuccessAdapter extends RecyclerView.Adapter<DaySuccessAdapter.ViewHolder> {

    private final List<DaySuccess> daySuccesses;
    private final int userDayNumber;

    public DaySuccessAdapter(List<DaySuccess> daySuccesses, int userDayNumber) {
        this.daySuccesses = daySuccesses;
        this.userDayNumber = userDayNumber;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowDaySuccessBinding binding = RecyclerRowDaySuccessBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = daySuccesses.get(position).getTitle();
        int dayNumber = daySuccesses.get(position).getDayNumber();
        int image;
        if (userDayNumber >= dayNumber) {
            image = daySuccesses.get(position).getImageGold();
            holder.binding.dayRecyclerTitleTxt.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.secondaryColor));
        } else {
            image = daySuccesses.get(position).getImageGray();
        }
        holder.binding.dayRecyclerTitleTxt.setText(title);
        holder.binding.dayRecyclerImage.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return daySuccesses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerRowDaySuccessBinding binding;

        public ViewHolder(@NonNull RecyclerRowDaySuccessBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}

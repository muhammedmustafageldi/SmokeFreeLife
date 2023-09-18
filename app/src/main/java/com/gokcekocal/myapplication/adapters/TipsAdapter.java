package com.gokcekocal.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gokcekocal.myapplication.databinding.RecyclerRowTipsBinding;
import com.gokcekocal.myapplication.models.Tips;

import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.ViewHolder> {

    private final List<Tips> tipsList;

    public TipsAdapter(List<Tips> tipsList) {
        this.tipsList = tipsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowTipsBinding binding = RecyclerRowTipsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.recyclerTipsTxt.setText(tipsList.get(position).getTitle());
        holder.binding.recyclerTipsImage.setImageResource(tipsList.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return tipsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerRowTipsBinding binding;

        public ViewHolder(@NonNull RecyclerRowTipsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

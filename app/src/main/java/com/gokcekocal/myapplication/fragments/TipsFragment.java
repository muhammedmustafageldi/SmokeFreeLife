package com.gokcekocal.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.gokcekocal.myapplication.R;
import com.gokcekocal.myapplication.adapters.TipsAdapter;
import com.gokcekocal.myapplication.databinding.FragmentTipsBinding;
import com.gokcekocal.myapplication.models.Tips;

import java.util.ArrayList;
import java.util.List;

public class TipsFragment extends Fragment {

    private FragmentTipsBinding binding;


    public TipsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTipsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Tavsiyeler");
            toolbar.setNavigationIcon(R.drawable.ico_tips);
        }

        setTipsList();
    }

    private void setTipsList() {
        List<Tips> tipsList = new ArrayList<>();
        tipsList.add(new Tips("Bol su için.", R.drawable.ico_water));
        tipsList.add(new Tips("Sinemaya gidin.", R.drawable.ico_cinema));
        tipsList.add(new Tips("Yürüyüş yapın.", R.drawable.ico_walking));
        tipsList.add(new Tips("Sigara bırakma sebeplerinizi kaydedin ve hatırlayın.", R.drawable.ico_reason));
        tipsList.add(new Tips("Meyve ve sebze tüketin.", R.drawable.ico_fruit));
        tipsList.add(new Tips("Müzik dinleyin.", R.drawable.ico_listen_music));
        tipsList.add(new Tips("Sigara satın almayarak size kalacak para ile neler yapacağınızı düşünün.", R.drawable.ico_money));
        tipsList.add(new Tips("En önemlisi artık daha sağlıklı bir birey olacağınızı düşünün.", R.drawable.ico_save_heal));

        setRecycler(tipsList);
    }

    private void setRecycler(List<Tips> tipsList) {
        RecyclerView tipsRecyclerView = binding.tipsRecyclerView;
        tipsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        tipsRecyclerView.setHasFixedSize(true);

        TipsAdapter tipsAdapter = new TipsAdapter(tipsList);

        //Define animation for recycler
        recyclerItemAnimation(tipsRecyclerView);

        tipsRecyclerView.setAdapter(tipsAdapter);
    }

    private void recyclerItemAnimation(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }


}
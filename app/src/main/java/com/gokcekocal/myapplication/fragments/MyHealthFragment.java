package com.gokcekocal.myapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.gokcekocal.myapplication.adapters.MyHealthAdapter;
import com.gokcekocal.myapplication.databinding.FragmentMyHealtBinding;
import com.gokcekocal.myapplication.models.Heal;

import java.util.ArrayList;
import java.util.List;


public class MyHealthFragment extends Fragment {

    private FragmentMyHealtBinding binding;

    public MyHealthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyHealtBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Sağlığım");
            toolbar.setNavigationIcon(R.drawable.ico_health);
        }
        setHealList();
    }

    private void setHealList() {
        List<Heal> healList = new ArrayList<>();

        healList.add(new Heal("20 dakika sonra, kan basıncı ve nabız normale döner, el ve ayak dolaşımı düzelir.", 1200));
        healList.add(new Heal("8 saat sonra, kan oksijen düzeyi normale döner, kalp krizi geçirme riski azalır.", 28800));
        healList.add(new Heal("24 saat sonra, vücut karbonmonoksitten arınır.", 86400));
        healList.add(new Heal("48 saat sonra, kandaki nikotin düzeyi azalır, tat ve koku duyusu artar.", 172800));
        healList.add(new Heal("72 saat sonra, hava yollarının gevşemesi sonucu nefes alıp verme rahatlar, solunum yolları kendini temizlemeye başlar. Enerji düzeyi artar.", 259200));
        healList.add(new Heal("2 hafta sonra, tüm vücuttaki dolaşım düzelir, solunum yolu enfeksiyonlarına yakalanma riski azalır, yürürken yorulma ve tıkanma daha az görülür.", 1209600));
        healList.add(new Heal("3 ay sonra, öksürük, kısa aralıklarla nefes alıp verme ve hırıltılı soluk alıp verme gibi solunum problemleri düzelir, akciğer fonksiyonları düzelir.", 7776000));
        healList.add(new Heal("12 ay sonra, koroner kalp hastalığı riski yarı yarıya azalır.", 31536000));

        initRecycler(healList);
    }

    private void initRecycler(List<Heal> healList) {
        RecyclerView myHealthRecycler = binding.myHealthRecycler;
        myHealthRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        myHealthRecycler.setHasFixedSize(true);

        //Get user time for send the adapter
        long userTime = 0;
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            // User's time in seconds
            userTime = sharedPreferences.getLong("userTime", 0);
        }

        MyHealthAdapter adapter = new MyHealthAdapter(healList, calculatorSeconds(userTime));

        myHealthRecycler.setAdapter(adapter);
        recyclerItemAnimation(myHealthRecycler);
    }

    private void recyclerItemAnimation(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    private long calculatorSeconds(long userTime) {
        long currentTime = System.currentTimeMillis() / 1000;
        return currentTime - userTime;
    }

}
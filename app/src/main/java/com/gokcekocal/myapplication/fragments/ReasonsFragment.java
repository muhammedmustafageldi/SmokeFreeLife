package com.gokcekocal.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;

import com.gokcekocal.myapplication.R;
import com.gokcekocal.myapplication.adapters.ReasonsAdapter;
import com.gokcekocal.myapplication.databinding.FragmentReasonsBinding;
import com.gokcekocal.myapplication.models.Reasons;
import com.gokcekocal.myapplication.roomdb.MyAppDatabase;
import com.gokcekocal.myapplication.roomdb.MyDao;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReasonsFragment extends Fragment {

    private FragmentReasonsBinding binding;
    private MyDao myDao;
    private CompositeDisposable compositeDisposable;
    private List<Reasons> reasonsList;
    private ReasonsAdapter adapter;

    public ReasonsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReasonsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Nedenlerim");
            toolbar.setNavigationIcon(R.drawable.ico_question_marks);
        }

        MyAppDatabase database = MyAppDatabase.getInstance(requireContext());
        myDao = database.myDao();

        getData();

        binding.addReasonButton.setOnClickListener(view1 -> addReason());
    }

    private void getData() {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(myDao.getAllReasons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ReasonsFragment.this::initRecyclerView));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView(List<Reasons> dataList) {
        reasonsList = dataList;
        RecyclerView recyclerView = binding.reasonsRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);

        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        adapter = new ReasonsAdapter(requireContext(), reasonsList);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
        recyclerView.setAdapter(adapter);
        Objects.requireNonNull(adapter).notifyDataSetChanged();

        adapter.setListener((reasons, position) -> {
            reasonsList.remove(reasons);
            adapter.notifyDataSetChanged();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addReason() {
        Dialog dialog = new Dialog(requireContext());
        @SuppressLint("InflateParams") View view = LayoutInflater.from(requireContext()).inflate(R.layout.add_reason_dialog, null);
        Button addButton = view.findViewById(R.id.addReasonDialogButton);
        TextInputEditText reasonTxt = view.findViewById(R.id.addReasonTxt);

        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        addButton.setOnClickListener(view1 -> {
            String reason = Objects.requireNonNull(reasonTxt.getText()).toString();
            if (!reason.isEmpty()) {
                Reasons reasons = new Reasons(reason);
                compositeDisposable.add(myDao.insertReasons(reasons)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
                reasonsList.add(reasons);
                adapter.notifyDataSetChanged();
                dialog.cancel();
            } else {
                reasonTxt.setError("Kaydedebilmek için bir neden girmelisiniz.");
            }
        });

        dialog.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        adapter.clearMemory();
    }
}
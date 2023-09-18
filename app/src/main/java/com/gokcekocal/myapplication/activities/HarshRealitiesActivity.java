package com.gokcekocal.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.gokcekocal.myapplication.R;
import com.gokcekocal.myapplication.databinding.ActivityHarshRealitiesBinding;
import com.gokcekocal.myapplication.models.HarshRealities;

import java.util.ArrayList;
import java.util.List;

public class HarshRealitiesActivity extends AppCompatActivity {

    private ActivityHarshRealitiesBinding binding;
    private List<HarshRealities> harshRealities;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHarshRealitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setHarshRealitiesList();
        binding.harshTxt.setText(harshRealities.get(0).getTitle());
        binding.harshImage.setImageResource(harshRealities.get(0).getIcon());
        position = 1;
        binding.nextButton.setOnClickListener(view -> nextHarsh());
    }

    private void setHarshRealitiesList() {
        harshRealities = new ArrayList<>();
        harshRealities.add(new HarshRealities("Sigara kullanan her 2 kişiden biri sigara nedeniyle hayatını kaybediyor. Öyle ki her gün 14 bin kişi ve her 6,5 saniyede bir kişi sigaraya bağlı nedenlerle yaşamını yitiriyor.", R.drawable.ico_dead));
        harshRealities.add(new HarshRealities("İçtiğiniz her sigara ömrünüzü yaklaşık olarak 12 dakika kadar kısaltıyor.", R.drawable.ico_dead_time));
        harshRealities.add(new HarshRealities("Her sigarada vücut için zehirli, tahriş edici, kanser yapıcı ya da kanserin ortaya çıkmasını kolaylaştırıcı 4000'den fazla kimyasal madde bulunuyor. Yapılan araştırmalar bunlardan en az 81 tanesinin doğrudan kansere neden olduğunu gösteriyor.", R.drawable.ico_poison));
        harshRealities.add(new HarshRealities("Sigara, akciğer kanseri başta olmak üzere diğer kanser türlerinin, KOAH gibi ölümcül solunum yolu ve kalp hastalıklarının gelişiminde büyük rol oynuyor.", R.drawable.ico_tired));
        harshRealities.add(new HarshRealities("Sigara damarlardaki daraltıcı etkisiyle deride gri-esmer renklenmeye neden olduğu gibi kan akımını bozarak, yara iyileşmesini olumsuz yönde etkilemektedir. Derideki kronik oksijenlenmenin azalması, kollajen sentezini düşürerek belirgin kırışıklığa neden olmaktadır.", R.drawable.ico_skin));
    }

    private void nextHarsh() {
        if (position < 4) {
            Animation invisibleAnimation = AnimationUtils.loadAnimation(this, R.anim.invisible_anim);
            Animation visibleAnimation = AnimationUtils.loadAnimation(this, R.anim.visible_anim);

            invisibleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.harshTxt.setText(harshRealities.get(position).getTitle());
                    binding.harshImage.setImageResource(harshRealities.get(position).getIcon());
                    position++;
                    binding.harshCardView.startAnimation(visibleAnimation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            binding.harshCardView.startAnimation(invisibleAnimation);
        } else {
            Toast.makeText(HarshRealitiesActivity.this, "Şimdilik bu kadar...", Toast.LENGTH_SHORT).show();
        }
    }


}
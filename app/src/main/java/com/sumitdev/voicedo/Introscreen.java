package com.sumitdev.voicedo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class Introscreen extends AppCompatActivity {
    private ViewPager viewPager;
    private Button next,getstarted;
    private TabLayout tabLayout;
Animation btnAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        next=(Button)findViewById(R.id.next);
        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        getstarted=(Button)findViewById(R.id.getstarted);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(),Main2Activity.class );
            startActivity(mainActivity);
            finish();


        }
        getstarted.setVisibility(GONE);
        final List<IntroScreenItem> mList = new ArrayList<>();
        mList.add(new IntroScreenItem("Mic Permission","This app requires mic permission to record your voice note for todo. ",R.drawable.ic_mic_green_24dp));
        mList.add(new IntroScreenItem("Storage Permission","This app requires storage permission to store your voice note for todo. ",R.drawable.ic_storage_green_24dp));
        mList.add(new IntroScreenItem("Night Mode Feature","This mode uses darker color palette for app backgrounds.Night Mode can help you to relive eye strain when you are using this app in dark.",R.drawable.ic_night_mode));
        mList.add(new IntroScreenItem("Ready To Rock"," ",R.drawable.ic_smile));
        PagerAdapter pagerAdapter=new PagerAdapter(this,mList);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

       viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
if (position==3)
{
    tabLayout.setVisibility(GONE);
    getstarted.setVisibility(View.VISIBLE);
    getstarted.setAnimation(btnAnim);
    next.setVisibility(GONE);
}
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    viewPager.setCurrentItem(position);


                }

                if (position == mList.size()-1) { // when we rech to the last screen

                    // TODO : show the GETSTARTED Button and hide the indicator and the next button




                }
            }
        });
getstarted.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        savePrefsData();
        Intent intent=new Intent(Introscreen.this,Main2Activity.class);
        startActivity(intent);
        finish();
    }
});
    }
    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;



    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();


    }
}

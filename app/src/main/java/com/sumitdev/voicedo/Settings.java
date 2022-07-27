package com.sumitdev.voicedo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

Switch nightmodeswitch;

    Configuration configuration;
    public static final int MODE_NIGHT_NO=1;
    public static final int MODE_NIGHT_YES=2;
    UiModeManager uiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        nightmodeswitch=(Switch)findViewById(R.id.nightsw);
        uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
nightmodeswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked==true)
        {



            uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);

        }
        else {
            uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);


        }
    }
});
        if (uiManager.getNightMode()==UiModeManager.MODE_NIGHT_NO)
        {
            nightmodeswitch.setChecked(false);
        }
        else {nightmodeswitch.setChecked(true);}





    }
    public void se(String s)
    {
        Toast.makeText(this,""+s,Toast.LENGTH_LONG).show();
    }
}

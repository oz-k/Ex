package com.example.ex;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Switch;

public class SettingActivity {
    private Context context;

    public SettingActivity(Context context) {
        this.context = context;
    }

    public void callFunction() {
        final SharedPreferences sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.setting);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        final Switch alram = (Switch)dialog.findViewById(R.id.alram);
        final Switch favorite = (Switch)dialog.findViewById(R.id.favorite);
        final RadioButton rad1 = (RadioButton)dialog.findViewById(R.id.rad1);
        final RadioButton rad2 = (RadioButton)dialog.findViewById(R.id.rad2);
        final ImageView close = (ImageView)dialog.findViewById(R.id.close);
        final NumberPicker numberPicker = (NumberPicker)dialog.findViewById(R.id.minPicker);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(60);

        alram.setChecked(sp.getBoolean("alram", true));
        favorite.setChecked(sp.getBoolean("favorite", false));
        numberPicker.setValue(sp.getInt("num", 0));
        if(sp.getBoolean("rad1", true) == true) {
            rad1.setChecked(true);
            rad2.setChecked(false);
        } else {
            rad1.setChecked(false);
            rad2.setChecked(true);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("alram", alram.isChecked());
                editor.putBoolean("favorite", favorite.isChecked());
                editor.putBoolean("rad1", rad1.isChecked());
                editor.putInt("num", numberPicker.getValue());
                editor.commit();
                dialog.dismiss();
            }
        });
    }
}

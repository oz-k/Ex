package com.example.ex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Activity activity = this;
    private final String foodName[][] = {{"b1", "l1", "d1"},
                                         {"b2", "l2", "d2"},
                                         {"b3", "l3", "d3"},
                                         {"b4", "l4", "d4"},
                                         {"b5", "l5", "d5"}};

    private final String starName[][] = {{"bstar1", "lstar1", "dstar1"},
                                         {"bstar2", "lstar2", "dstar2"},
                                         {"bstar3", "lstar3", "dstar3"},
                                         {"bstar4", "lstar4", "dstar4"},
                                         {"bstar5", "lstar5", "dstar5"}};

    public String food[][] = new String[5][3];
    public boolean star[][] = new boolean[5][3];

    final String dayArray[] = {"월", "화", "수", "목", "금"};
    public int today;

    TextView breakfast, lunch, dinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        ImageView beforeButton = (ImageView)findViewById(R.id.beforeButton);
        ImageView nextButton = (ImageView)findViewById(R.id.nextButton);
        final ImageView istar[] = {(ImageView)findViewById(R.id.bStar), (ImageView)findViewById(R.id.lStar), (ImageView)findViewById(R.id.dStar)};
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        Calendar calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        final TextView day = (TextView)findViewById(R.id.day);
        breakfast = (TextView)findViewById(R.id.breakfast);
        lunch = (TextView)findViewById(R.id.lunch);
        dinner = (TextView)findViewById(R.id.dinner);

        day();
        setStar();
        setFood();

        breakfast.setText(food[today][0]);
        lunch.setText(food[today][1]);
        dinner.setText(food[today][2]);

        for(int i = 0; i < 3; i++)
            if(star[today][i] == true)
                istar[i].setImageResource(R.drawable.ic_baseline_star_24);

        istar[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(star[today][0] == true) {
                    istar[0].setImageResource(R.drawable.ic_baseline_star_border_24);
                }
                else {
                    istar[0].setImageResource(R.drawable.ic_baseline_star_24);
                }
                star[today][0] = !star[today][0];
            }
        });

        istar[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(star[today][1] == true)
                    istar[1].setImageResource(R.drawable.ic_baseline_star_border_24);
                else
                    istar[1].setImageResource(R.drawable.ic_baseline_star_24);
                star[today][1] = !star[today][1];
            }
        });
        istar[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(star[today][2] == true)
                    istar[2].setImageResource(R.drawable.ic_baseline_star_border_24);
                else
                    istar[2].setImageResource(R.drawable.ic_baseline_star_24);
                star[today][2] = !star[today][2];
            }
        });
        beforeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(today == 0) {
                    Toast.makeText(getApplicationContext(), "이번 주 까지만 지원합니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    day.setText(dayArray[--today]);

                    for (int i = 0; i < 3; i++) {
                        if (star[today][i] == true)
                            istar[i].setImageResource(R.drawable.ic_baseline_star_24);
                        else
                            istar[i].setImageResource(R.drawable.ic_baseline_star_border_24);
                    }
                    breakfast.setText(food[today][0]);
                    lunch.setText(food[today][1]);
                    dinner.setText(food[today][2]);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(today == 4) {
                    Toast.makeText(getApplicationContext(), "이번 주 까지만 지원합니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    day.setText(dayArray[++today]);

                    for (int i = 0; i < 3; i++) {
                        if (star[today][i] == true)
                            istar[i].setImageResource(R.drawable.ic_baseline_star_24);
                        else
                            istar[i].setImageResource(R.drawable.ic_baseline_star_border_24);
                    }
                    breakfast.setText(food[today][0]);
                    lunch.setText(food[today][1]);
                    dinner.setText(food[today][2]);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                PackageManager packageManager = activity.getPackageManager();
                List<PackageInfo> installList = packageManager.getInstalledPackages(0);
                for(int i = 0; i < installList.size(); i++) {
                    if(installList.get(i).packageName.equals("kr.co.haksik")) {
                        flag = true;
                    }
                }
                if(flag == true) {
                    Intent it = getApplicationContext().getPackageManager().getLaunchIntentForPackage("kr.co.haksik");
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=kr.co.haksik")));
                }
            }
        });
    }

    @Override
    protected void onPause() {
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 3; j++)
                editor.putBoolean(starName[i][j], star[i][j]);
        editor.commit();
        finish();
        super.onPause();
    }

    void setStar() {

        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);

        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 3; j++)
                star[i][j] = sp.getBoolean(starName[i][j], false);

    }
    void day() {

        final TextView day = (TextView)findViewById(R.id.day);

        if(today < 0 || today > 4) {
            day.setText(dayArray[0]);
            today = 0;
        }
        else
            day.setText(dayArray[today]);
    }

//    private void getFood() {
//        new Thread(new Runnable() {
//
//            String url = "http://www.kopo.ac.kr/semi/content.do?menu=3295";
//
//            String food[][][] = new String[5][3][];
//            String select[][] = new String[5][3];
//
//            @Override
//            public void run() {
//
//                try {
//                    Document document = Jsoup.connect(url).get();
//                    int add = 0;
//
//                    for(int i = 0; i < 5; i++)
//                        for (int j = 1; j < 4; j++)
//                            select[i][j-1] = document.select("table[class=tbl_table menu] tbody tr").eq(i)
//                                    .select("td").eq(j)
//                                    .select("span").eq(0).text();
//
//                    for(int i = 0; i < 5; i++)
//                        for(int j = 0; j < 3; j++) {
//                            food[i][j] = select[i][j].split(" , ");
//                            select[i][j] = "";
//                        }
//
//                    for(int i = 0; i < 5; i++)
//                        for(int j = 0; j < 3; j++) {
//                            for (int k = add; k < food[i][j].length/2; k++)
//                                select[i][j] += food[i][j][k] + "  ";
//                            select[i][j] += "\n\n";
//
//                            add = food[i][j].length/2;
//
//                            for (int k = add; k < food[i][j].length; k++)
//                                select[i][j] += food[i][j][k] + "  ";
//                            add = 0;
//                        }
//
//                    for(int i = 0; i < 5; i++)
//                        for(int j = 0; j < 3; j++)
//                            if(select[i][j].length() < 5)
//                                select[i][j] = "준비된 식단이 없습니다.";
//
//                } catch (IOException e) {}
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sp.edit();
//
//                        for(int i = 0; i < 5; i++)
//                            for(int j = 0; j < 3; j++)
//                                editor.putString(foodName[i][j], select[i][j]);
//                        editor.commit();
//                    }
//                });
//            }
//        }).start();
//    }

    void setFood() {
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);

        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 3; j++)
                food[i][j] = sp.getString(foodName[i][j], " ");
    }

}
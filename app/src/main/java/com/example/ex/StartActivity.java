package com.example.ex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class StartActivity extends AppCompatActivity {

    String foodName[][] = {{"b1", "l1", "d1"},
            {"b2", "l2", "d2"},
            {"b3", "l3", "d3"},
            {"b4", "l4", "d4"},
            {"b5", "l5", "d5"}};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getFood();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void getFood() {
        new Thread(new Runnable() {

            String url = "http://www.kopo.ac.kr/semi/content.do?menu=3295";
            boolean flag = true;
            String food[][][] = new String[5][3][];
            String select[][] = new String[5][3];

            @Override
            public void run() {

                try {
                    Document document = Jsoup.connect(url).get();
                    int add = 0;

                    for(int i = 0; i < 5; i++)
                        for (int j = 1; j < 4; j++)
                            select[i][j-1] = document.select("table[class=tbl_table menu] tbody tr").eq(i)
                                    .select("td").eq(j)
                                    .select("span").eq(0).text();

                    for(int i = 0; i < 5; i++)
                        for(int j = 0; j < 3; j++) {
                            food[i][j] = select[i][j].split(" , ");
                            select[i][j] = "";
                        }

                    for(int i = 0; i < 5; i++)
                        for(int j = 0; j < 3; j++) {
                            for (int k = add; k < food[i][j].length/2; k++)
                                select[i][j] += food[i][j][k] + "  ";
                            select[i][j] += "\n\n";

                            add = food[i][j].length/2;

                            for (int k = add; k < food[i][j].length; k++)
                                select[i][j] += food[i][j][k] + "  ";
                            add = 0;
                        }


                } catch (IOException e) {
                    flag = false;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(flag) {
                            SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            for (int i = 0; i < 5; i++)
                                for (int j = 0; j < 3; j++)
                                    editor.putString(foodName[i][j], select[i][j]);
                            editor.commit();
                        } else {
                            Toast toast = Toast.makeText(StartActivity.this, "학식을 불러올 수 없습니다\n 네트워크를 확인해주세요", Toast.LENGTH_SHORT);

                            toast.show();
                        }
                    }
                });
            }
        }).start();
    }
}

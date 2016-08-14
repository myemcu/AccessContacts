package com.myemcu.app_19accesscontacts;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    int cnt=0; // 默认为第一张背景图

    private LinearLayout mLayout;
    private Button mChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = (LinearLayout) findViewById(R.id.layout);
        mChange    = (Button) findViewById(R.id.change);

        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cnt++;
                if (cnt>3)
                    cnt=0;

                switch (cnt) {

                    case 0: mLayout.setBackground(getResources().getDrawable(R.drawable.blue));
                            break;
                    case 1: mLayout.setBackground(getResources().getDrawable(R.drawable.pgy));
                            break;
                    case 2: mLayout.setBackground(getResources().getDrawable(R.drawable.sky));
                            break;
                    case 3: mLayout.setBackground(getResources().getDrawable(R.drawable.heart));
                            break;

                    default:break;
                }
            }
        });
    }
}

/*
* 本程序框架涵盖：MainActivity.java，
*               activity_main.xml，result.xml
*               AndroidMainfest.xml
*               styles.xml
* */

package com.myemcu.app_19accesscontacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int cnt=0; // 默认为第一张背景图

    private LinearLayout mLayout;
    private Button mChange;
    private EditText mName;
    private EditText mNum;
    private Button mAdd;
    private Button mShow;
    private LinearLayout nLayout;
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();    // 获取所有控件对象

        // 换壁纸(此功能逻辑与本案例无关，故整成匿名类，为了方便)
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

        // 创建自定义点击监听器(内部类)
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        mAdd.setOnClickListener(myOnClickListener);
        mShow.setOnClickListener(myOnClickListener);
    }
    //==============================================================================================
    private void findViews() {
        mLayout = (LinearLayout) findViewById(R.id.layout);
        mChange    = (Button) findViewById(R.id.change);

        mName = (EditText) findViewById(R.id.name);
        mNum = (EditText) findViewById(R.id.num);
        mAdd = (Button) findViewById(R.id.add);
        mShow = (Button) findViewById(R.id.show);

        nLayout = (LinearLayout) findViewById(R.id.title);  // 标题栏布局
        nLayout.setVisibility(View.INVISIBLE);              // 标题栏隐藏
    }

    // 逻辑重点
    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:
                                addPerson();
                                break;

                case R.id.show:
                                nLayout.setVisibility(View.VISIBLE);    // 显示标题栏
                                // Map类型为ArrayList，key为String，value为String，Map中为键值对
                                ArrayList<Map<String,String>> persons = queryPerson();
                                SimpleAdapter adapter = new SimpleAdapter(
                                        MainActivity.this,
                                        persons,
                                        R.layout.result,
                                        new String[]{"id","name","num"},
                                        new int[]{R.id.personid,R.id.personname,R.id.personnum}
                                );
                                break;

                default:break;
            }
        }
    }

    // 查询联系人
    private ArrayList<Map<String,String>> queryPerson() {
        ArrayList<Map<String,String>> detail = new ArrayList<Map<String,String>>();
        return detail;
    }

    // 添加联系人
    private void addPerson() {

    }
}

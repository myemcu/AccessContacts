/*
* 本程序框架涵盖：MainActivity.java，
*               activity_main.xml，result.xml
*               AndroidMainfest.xml
*               styles.xml
* */

package com.myemcu.app_19accesscontacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int cnt = 0; // 默认为第一张背景图

    private LinearLayout mLayout;
    private Button mChange;
    private EditText mName;
    private EditText mNum;
    private Button mAdd;
    private Button mShow;
    private LinearLayout nLayout;

    private ContentResolver resolver;
    private ListView mResult;

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
                if (cnt > 3)
                    cnt = 0;

                switch (cnt) {

                    case 0:
                        mLayout.setBackground(getResources().getDrawable(R.drawable.blue));
                        break;
                    case 1:
                        mLayout.setBackground(getResources().getDrawable(R.drawable.pgy));
                        break;
                    case 2:
                        mLayout.setBackground(getResources().getDrawable(R.drawable.sky));
                        break;
                    case 3:
                        mLayout.setBackground(getResources().getDrawable(R.drawable.heart));
                        break;

                    default:
                        break;
                }
            }
        });

        // 创建自定义点击监听器(内部类)
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        mAdd.setOnClickListener(myOnClickListener);
        mShow.setOnClickListener(myOnClickListener);

        // 获取系统内容提供器
        resolver = getContentResolver();    // 若没有这句话，两个按钮就运行崩溃。
    }

    //==============================================================================================
    private void findViews() {
        mLayout = (LinearLayout) findViewById(R.id.layout);
        mChange = (Button) findViewById(R.id.change);

        mName = (EditText) findViewById(R.id.name);
        mNum = (EditText) findViewById(R.id.num);
        mAdd = (Button) findViewById(R.id.add);
        mShow = (Button) findViewById(R.id.show);

        nLayout = (LinearLayout) findViewById(R.id.title);  // 标题栏布局
        nLayout.setVisibility(View.INVISIBLE);              // 标题栏隐藏

        mResult = (ListView) findViewById(R.id.result);
    }

    // 逻辑重点
    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:
                    addPerson(); // 添加联系人
                    break;

                case R.id.show:
                    nLayout.setVisibility(View.VISIBLE);    // 显示标题栏
                    // Map类型为ArrayList，key为String，value为String，Map中为键值对
                    ArrayList<Map<String, String>> persons = queryPerson(); // 查询联系人
                    SimpleAdapter adapter = new SimpleAdapter(
                            MainActivity.this,
                            persons,
                            R.layout.result,
                            new String[]{"id", "name", "num"},
                            new int[]{R.id.personid, R.id.personname, R.id.personnum}
                    );
                    mResult.setAdapter(adapter);
                    break;

                default:break;
            }
        }
    }

    // 添加联系人
    private void addPerson() {

        // 获取联系人EditText信息
        String nameStr = mName.getText().toString();
        String numStr = mNum.getText().toString();

        ContentValues values = new ContentValues(); // 创建一个空的ContentValues

        Uri contactUri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI, values); //向CONTENT_URI插入一个空值，已得到返回的Id号
        long contactId = ContentUris.parseId(contactUri); // 得到新联系人的Id号

        values.clear(); // 清空values的内容。
        values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);   // 设置Id号
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);  // 设置类型
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, nameStr); // 设置姓名

        resolver.insert(ContactsContract.Data.CONTENT_URI, values);  // 向联系人Uri中添加姓名
        //resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);    // 设置Id号
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);   // 设置类型
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, numStr);  // 设置号码
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);    // 设置电话类型

        resolver.insert(ContactsContract.Data.CONTENT_URI, values); // 向联系人电话Uri中添加电话
        //resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);

        Toast.makeText(MainActivity.this, "添加联系人数据成功!", Toast.LENGTH_LONG).show();
    }

    // 查询联系人
    private ArrayList<Map<String, String>> queryPerson() {
        ArrayList<Map<String, String>> detail = new ArrayList<Map<String, String>>();

        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);              // 查询手机中的所有联系人

        while (cursor.moveToNext()) {                                                                           // 循环遍历每一个联系人

            Map<String, String> person = new HashMap<String, String>();  // 创建联系人存储对象(Map型)                                         // 每个联系人信息用一个Map对象存储

            // 获取手机中的联系人信息
            String personId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));           // 获取联系人Id
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));  // 获取联系人姓名

            // 将获取到的信息存入Map对象中
            person.put("id", personId);
            person.put("name", name);

            Cursor nums = resolver.query(   // 根据Id号查询手机号
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+personId,
                    null,
                    null
            );

            if (nums.moveToNext()) {
                String num = nums.getString(nums.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                person.put("num",num);  // 将手机号存入Map对象中
            }

            nums.close();       // 关闭资源
            detail.add(person); // 添加联系人
        }

        cursor.close();         // 关闭资源

        return detail;          // 返回查询列表
    }
}
package com.android.arts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private LinearLayout llEmpty;
    private FloatingActionButton btnAdd;
    private RecyclerView rvNewsList;
    private NewsAdapter mNewsAdapter;
    private List<News> mNews;
    private Banner mBanner;//轮播顶部
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_main);
        helper = new MySqliteOpenHelper(myActivity);
        rvNewsList = findViewById(R.id.rv_contacts_list);
        llEmpty = findViewById(R.id.ll_empty);
        etQuery= findViewById(R.id.et_query);
        ivSearch= findViewById(R.id.iv_search);
        btnAdd =findViewById(R.id.btn_add);
        mBanner =findViewById(R.id.banner);
        initView();
        setViewListener();
    }
    private void setViewListener() {
        //软键盘搜索
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();//加载数据
            }
        });
        //点击软键盘中的搜索
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadData();//加载数据
                    return true;
                }
                return false;
            }
        });
        //添加
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, AddNewsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        //图片资源
        int[] imageResourceID = new int[]{R.drawable.ic_1, R.drawable.ic_2, R.drawable.ic_3, R.drawable.ic_4, R.drawable.ic_5};
        List<Integer> imgeList = new ArrayList<>();
        //轮播标题
        for (int i = 0; i < imageResourceID.length; i++) {
            imgeList.add(imageResourceID[i]);//把图片资源循环放入list里面
            //设置图片加载器，通过Glide加载图片
            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(MainActivity.this).load(path).into(imageView);
                }
            });
            //设置轮播的动画效果,里面有很多种特效,可以到GitHub上查看文档。
            mBanner.setImages(imgeList);//设置图片资源
            //设置指示器位置（即图片下面的那个小圆点）
            mBanner.setDelayTime(3000);//设置轮播时间3秒切换下一图
            mBanner.start();//开始进行banner渲染
        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvNewsList.setLayoutManager(layoutManager);
        //=2.1、初始化适配器
        mNewsAdapter=new NewsAdapter();
        //=2.3、设置recyclerView的适配器
        rvNewsList.setAdapter(mNewsAdapter);
        mNewsAdapter.setItemListener(new NewsAdapter.ItemListener() {
            @Override
            public void ItemClick(News news) {
                Intent intent = new Intent(myActivity, AddNewsActivity.class);
                intent.putExtra("news",news);
                startActivity(intent);
            }

            @Override
            public void ItemLongClick(News news) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(myActivity);
                dialog.setMessage("确认要删除该数据吗");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        if (db.isOpen()) {
                            db.execSQL("delete from news where id = "+news.getId());
                            db.close();
                        }
                        Toast.makeText(myActivity,"删除成功",Toast.LENGTH_LONG).show();
                        loadData();
                    }
                });
                dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        loadData();
    }

    private void loadData() {
        mNews = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String str = etQuery.getText().toString();
        Cursor cursor ;
        if ("".equals(str)) {
            String sql = "select * from news";
            cursor = db.rawQuery(sql, null);
        }else {
            String sql = "select * from news where title like  ?";
            cursor = db.rawQuery(sql, new String[]{"%"+str+"%"});
        }

        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                String title = cursor.getString(1);
                String img = cursor.getString(2);
                String content = cursor.getString(3);
                String date = cursor.getString(4);
                News contacts = new News(dbId,title, img,content,date);
                mNews.add(contacts);
            }
        }

        db.close();
        if (mNews.size() > 0) {
            mNewsAdapter.addItem(mNews);
            llEmpty.setVisibility(View.GONE);
            rvNewsList.setVisibility(View.VISIBLE);
        }else {
            llEmpty.setVisibility(View.VISIBLE);
            rvNewsList.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
package com.android.arts;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 添加景点页面
 */
public class AddNewsActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private TextView tvTitle;
    private EditText etTitle;//标题
    private EditText etImg;//图片
    private EditText etContent;//内容
    private ImageView ivImg;//图片
    SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private News mNews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_news_add);
        tvTitle = findViewById(R.id.tv_title);
        etTitle = findViewById(R.id.title);
        etImg = findViewById(R.id.img);
        etContent = findViewById(R.id.content);
        helper = new MySqliteOpenHelper(this);
        ivImg = findViewById(R.id.iv_img);
        initView();
    }

    private void initView() {
        mNews = (News) getIntent().getSerializableExtra("news");
        if (mNews !=null){
            etTitle.setText(mNews.getTitle());
            etImg.setText(mNews.getImg());
            etContent.setText(mNews.getContent());
            Glide.with(myActivity)
                    .asBitmap()
                    .load(mNews.getImg())
                    .error(R.drawable.ic_error)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivImg);
            tvTitle.setText("修改内容");
        }
        ivImg.setVisibility(mNews ==null?View.GONE:View.VISIBLE);
    }

    public void save(View view){
        SQLiteDatabase db = helper.getWritableDatabase();
        String title = etTitle.getText().toString();
        String img = etImg.getText().toString();
        String content = etContent.getText().toString();
        if ("".equals(title)) {
            Toast.makeText(myActivity,"标题不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(img)) {
            Toast.makeText(myActivity,"图片地址不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(content)) {
            Toast.makeText(myActivity,"描述不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        if (mNews == null) {//新增
            String sql = "insert into news(title, img,content,date) values(?,?,?,?)";
            db.execSQL(sql,new Object[]{title, img,content,sf.format(new Date())});
            Toast.makeText(myActivity,"新增成功",Toast.LENGTH_SHORT).show();
        }else {//修改
            db.execSQL("update news set title = ?, img = ?, content = ?, date = ? where id=?", new Object[]{title, img,content,sf.format(new Date()), mNews.getId()});
            Toast.makeText(myActivity,"更新成功",Toast.LENGTH_SHORT).show();
        }
        db.close();
        finish();
    }
    //返回
    public void back(View view){
        finish();
    }
}

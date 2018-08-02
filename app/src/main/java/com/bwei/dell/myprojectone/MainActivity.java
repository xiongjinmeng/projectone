package com.bwei.dell.myprojectone;

import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textview2;
    private TextView tvie1;
    private String path = "https://www.zhaoapi.cn/product/getProductDetail?pid=1";
    private TextView textview3;
    private TextView textview1;
    private ViewPager viewPager;
    private ImageView imw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvie1 = findViewById(R.id.tvie1);
        textview1 = findViewById(R.id.textview1);
        textview2 = findViewById(R.id.textview2);
        textview3 = findViewById(R.id.textview3);
        viewPager = findViewById(R.id.viewPager);
        imw = findViewById(R.id.imw);
        tvie1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        textview2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute(path);
    }

    private class MyAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String json="";
            try {
                //将接口转化成url
                URL url = new URL(strings[0]);
                //打开HttpURLConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //设置请求方式
                urlConnection.setRequestMethod("GET");
                //设置超时链接
                urlConnection.setConnectTimeout(5000);
                //设置读取超时
                urlConnection.setReadTimeout(5000);
                //得到响应码 进行判断
                if (urlConnection.getResponseCode()==200){
                    //得到数据
                    InputStream inputStream = urlConnection.getInputStream();
                    //转换
                    json = streamToString(inputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Boods boods = gson.fromJson(s, Boods.class);
            double bargainPrice = boods.getData().getBargainPrice();
            double price = boods.getData().getPrice();
            String title = boods.getData().getTitle();
            String images = boods.getData().getImages();
            Log.i("bbb",images);
            textview1.setText(title);
            Log.i("aaa",title+price+bargainPrice);
            textview2.setText(price+"");
            textview3.setText(bargainPrice+"");
            final ArrayList<ImageView> list = new ArrayList<>();
            ImageView imageView = new ImageView(MainActivity.this);
            ImageView imageView2 = new ImageView(MainActivity.this);
            ImageView imageView3 = new ImageView(MainActivity.this);
            String s1 = "https://m.360buyimg.com/n0/jfs/t9004/210/1160833155/647627/ad6be059/59b4f4e1N9a2b1532.jpg!q70.jpg";
            String s2 = "https://m.360buyimg.com/n0/jfs/t7504/338/63721388/491286/f5957f53/598e95f1N7f2adb87.jpg!q70.jpg";
            String s3 = "https://m.360buyimg.com/n0/jfs/t7441/10/64242474/419246/adb30a7d/598e95fbNd989ba0a.jpg!q70.jpg";
            ImageLoader.getInstance().displayImage(s1,imageView,ImageLo.getOptions());
            ImageLoader.getInstance().displayImage(s2,imageView2,ImageLo.getOptions());
            ImageLoader.getInstance().displayImage(s3,imageView3,ImageLo.getOptions());
            list.add(imageView);
            list.add(imageView2);
            list.add(imageView3);
            MyImageAdapter adapter = new MyImageAdapter(MainActivity.this,list);
            viewPager.setAdapter(adapter);
        }
    }

    private String streamToString(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        //缓冲流
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String s = null;
        StringBuilder builder = new StringBuilder();
        try {
            while ((s=reader.readLine())!=null){
                builder.append(s);
            }
            reader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return builder.toString();
    }
}

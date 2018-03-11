package com.example.pablo.mycarpet;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Detail_Activity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().hide();
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("country_name"));
        String url = intent.getStringExtra("image_URL");
        Log.v("URL", url);
        imageView = findViewById(R.id.image_View);
        Glide.with(this).load(Uri.parse(url)).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.indev.jubicare_assistants;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewImageActivity extends AppCompatActivity {

    WebView webView1;
    String url="";
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Attached Document");
        webView1=findViewById(R.id.webView1);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        Bundle bundle=getIntent().getExtras() ;
        if (bundle!=null){
            url=bundle.getString("url");
        }
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setSupportZoom(true);
        webView1.getSettings().setBuiltInZoomControls(true);
        webView1.loadUrl(url);
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            webView1.setScaleX(mScaleFactor);
            webView1.setScaleY(mScaleFactor);
            return true;
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}

package com.safframework.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.OnClick;

/**
 * Created by Tony Shen on 2017/1/24.
 */

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Injector.injectInto(this);
    }

    @OnClick(id={R.id.text})
    void clickText() {

        Intent i = new Intent(MainActivity.this,SecondActivity.class);
        i.putExtra("param","test");
        startActivity(i);
    }
}

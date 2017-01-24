package com.safframework.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.safframework.app.dialog.CustomerDialog;
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

    @OnClick(id={R.id.text1})
    void clickText1() {

        Intent i = new Intent(MainActivity.this,WebViewActivity.class);
        startActivity(i);
    }

    @OnClick(id={R.id.text2})
    void clickText2() {

        Intent i = new Intent(MainActivity.this,FragmentActivity.class);
        startActivity(i);
    }

    @OnClick(id={R.id.text3})
    void clickText3() {

        CustomerDialog dialog = new CustomerDialog(this, R.style.simpleDialogStyle);
        dialog.show();
    }

    @OnClick(id={R.id.text4})
    void clickText4() {

        Intent i = new Intent(MainActivity.this,TestInjectViews.class);
        startActivity(i);
    }

    @OnClick(id={R.id.text5})
    void clickText5() {

        Intent i = new Intent(MainActivity.this,SecondActivity.class);
        i.putExtra("param","test");
        startActivity(i);
    }
}

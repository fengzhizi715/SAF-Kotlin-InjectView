package com.safframework.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;

/**
 * Created by Tony Shen on 2017/1/24.
 */

public class WebViewActivity extends Activity {

    @InjectView(R.id.webview)
    WebView webview;

    ProgressDialog progDailog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Injector.injectInto(this);

        progDailog = ProgressDialog.show(WebViewActivity.this, "Loading","Please wait...", true);
        progDailog.setCancelable(false);
        initViews();
    }

    private void initViews() {
        webview.loadUrl("https://github.com/fengzhizi715/SAF-Kotlin-InjectView");
        //覆盖webView默认通过系统浏览器打开网页的方式
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });
        //获取WebView类设置对象
        WebSettings settings = webview.getSettings();
        //使webView支持js
        settings.setJavaScriptEnabled(true);
        //设置webView缓存模式
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//(优先使用缓存)
        //webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//(不使用缓存)
        webview.setWebChromeClient(new WebChromeClient() {});

        webview.getSettings().setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        webview.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setUserAgentString(MyApplication.getUserAgent());
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
    }
}

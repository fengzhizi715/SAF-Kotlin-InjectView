# SAF-Kotlin-InjectView

下载安装

在根目录下的build.gradle中添加
```groovy
 buildscript {
     repositories {
         jcenter()
     }
     dependencies {
         classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
     }
 }
```
在app 模块目录下的build.gradle中添加

```groovy
apply plugin: 'com.neenbedankt.android-apt'

...

dependencies {
    compile 'com.safframework.injectview:saf-injectview:1.0.0'
    apt 'com.safframework.injectview:saf-injectview-compiler:1.0.0'
    ...
}
```

这个库原本是SAF的Dependency Injection(依赖注入)模块，简称DI。现在用`Kotlin`全部重写。

SAF中的依赖注入已经发展到第二个版本，由原先的运行时注解替换成编译时注解，底层依赖Square javapoet。
其实，ButterKnife已经足够强大了，为何还要再做一套呢？因为这个模块更加轻量级。

SAF中的DI包括以下几个方面：
* Inject View ：简化组件的查找注册
* Inject Views：支持多个相同类型组件的注入
* Inject Extra ：简化2个Activity之间Extra传递
* OnClick：简化各种组件的Click事件写法


Inject View
---
Inject View可以简化组件的查找注册，包括android自带的组件和自定义组件。在使用Inject View之前，我们会这样写代码

```Java
          public class MainActivity extends Activity {

                private ImageView imageView;

                @Override
                protected void onCreate(Bundle savedInstanceState) {
                  super.onCreate(savedInstanceState);

                  setContentView(R.layout.activity_main);
                  imageView = (ImageView) findViewById(R.id.imageview);
                }
           }
 ```


在使用Inject View之后，会这样写代码

```Java
          public class MainActivity extends Activity {

                @InjectView(R.id.imageView)
                private ImageView imageView;

                @Override
                protected void onCreate(Bundle savedInstanceState) {
                   super.onCreate(savedInstanceState);

                   setContentView(R.layout.activity_main);
                   Injector.injectInto(this);
                }
          }
```

目前，@InjectView可用于Activity、Dialog、Fragment中。在Activity和Dialog用法相似，在Fragment和Adapter中用法有一点区别。

```Java
          public class DemoFragment extends Fragment {

                   @InjectView(R.id.title)
                   private TextView titleView;

                   @InjectView(R.id.imageview)
                   private ImageView imageView;

                   @Override
                   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                          View v = inflater.inflate(R.layout.fragment_demo, container, false);

                          Injector.injectInto(this,v); // 和Activity使用的区别之处在这里

                          initViews();
                          initData();

                          return v;
                   }

                  ......
           }
```

Inject Views
---

```Java
          public class MainActivity extends Activity {

                @InjectViews(ids={R.id.imageView1,R.id.imageView2})
                private List<ImageView> imageviews;

                @Override
                protected void onCreate(Bundle savedInstanceState) {
                   super.onCreate(savedInstanceState);

                   setContentView(R.layout.activity_main);
                   Injector.injectInto(this);
                }
          }
 ```


Inject Extra
---

```Java
         /**
          * MainActivity传递数据给SecondActivity
          * Intent i = new Intent(MainActivity.this,SecondActivity.class);                                               
          * i.putExtra("test", "saf");
          * i.putExtra("test_object", hello);
          * startActivity(i);
          * 在SecondActivity可以使用@InjectExtra注解
          *
          * @author Tony Shen
          *
          */
         public class SecondActivity extends Activity{

               @InjectExtra(key="test")
               private String testStr;

               @InjectExtra(key="test_object")
               private Hello hello;

               protected void onCreate(Bundle savedInstanceState) {
                   super.onCreate(savedInstanceState);

                   Injector.injectInto(this);
                   Log.i("++++++++++++","testStr="+testStr);
                   Log.i("++++++++++++","hello="+SAFUtil.printObject(hello)); // 该方法用于打印对象
              }
          }
```

OnClick
---
@OnClick 可以在Activity、Fragment、Dialog、View中使用，支持多个组件绑定同一个方法。

```Java
     public class AddCommentFragment extends BaseFragment {

         @Override
         public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

             View v = inflater.inflate(R.layout.fragment_add_comment, container, false);

             Injector.injectInto(this, v);

             initView();

             return v;
        }

	    @OnClick(id={R.id.left_menu,R.id.btn_comment_cancel}, after = "pointClickView")
	    void clickLeftMenu() {
		    popBackStack();
	    }

      public void pointClickView(Method method, View view) {
        L.d("pointClickView");
        ....
      }

	    @OnClick(id=R.id.btn_comment_send)
	    void clickCommentSend() {
            if (StringHelper.isBlank(commentEdit.getText().toString())) {
               ToastUtil.showShort(mContext, R.string.the_comment_need_more_character);
            } else {
               AsyncTaskExecutor.executeAsyncTask(new AddCommentTask(showDialog(mContext)));
            }
	    }

	    ....
    }
```
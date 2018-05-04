package com.example.fantasy1722.example4injectdexlist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qihoo360.replugin.RePlugin;

import com.example.examplelibrary.test;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    Button mButton1;
    Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ClassLoader fcl = RePlugin.fetchClassLoader("pluginpackage");
//
        //Class<?>  clazz = null;
        //try {
        //    if (fcl != null) {
        //        clazz = fcl.loadClass("com.example.examplelibrary.test");
        //    } else {
        //        Log.d("yaopinfan", "RePlugin classloader is null!");
        //    }
        //} catch (ClassNotFoundException e) {
        //    e.printStackTrace();
        //}
        //if (clazz == null)
        //{
        //    Log.d("yaopinfan", "test is null!");
        //} else {
        //    Log.d("yaopinfan", "test is win!");
        //}


        try{

            Class obj_class1 = Class.forName("android.app.ActivityThread");
            Method method = obj_class1.getDeclaredMethod("currentActivityThread",new Class[] {});
            method.setAccessible(true);
            Object currentActivityThread = method.invoke(null, new Object[] {});

            String packageName = this.getPackageName();

            Class obj_class2 = Class.forName("android.app.ActivityThread");
            Field field1 = obj_class2.getDeclaredField("mPackages");
            field1.setAccessible(true);
            ArrayMap mPackages = (ArrayMap) field1.get(currentActivityThread);
            WeakReference wr = (WeakReference) mPackages.get(packageName);

            Object loadapk = wr.get();

            //readField("android.app.LoadedApk", loadapk, "mClassLoader");

            Class obj_class3 = Class.forName("android.app.LoadedApk");
            Field field2 = obj_class3.getDeclaredField("mClassLoader");
            field2.setAccessible(true);
            ClassLoader cl = (ClassLoader)field2.get(loadapk);
            Log.d("yaopinfan", "activitythread classloader : " + cl.toString());



//            Class obj_class4 = Class.forName("android.app.ActivityThread");
//            Field field3 = obj_class4.getDeclaredField("mResourcePackages");
//            field3.setAccessible(true);
//            ArrayMap mPackages1 = (ArrayMap) field3.get(currentActivityThread);
//            WeakReference wr1 = (WeakReference) mPackages1.get(packageName);
//
//            Object loadapk1 = wr1.get();
//
//            Class obj_class5 = Class.forName("android.app.LoadedApk");
//            Field field4 = obj_class5.getDeclaredField("mClassLoader");
//            field4.setAccessible(true);
//            ClassLoader cl1 = (ClassLoader)field4.get(loadapk1);
//            Log.d("yaopinfan", "activitythread Resource classloader : " + cl1.toString());

            //myClasz.getDeclaredMethod("test1").invoke(instance);
            //field.set(obj, filedVaule);

            //RefInvoke.setFieldOjbect("android.app.LoadedApk", "mClassLoader",
            //        wr.get(), dLoader);

            //Log.i("demo", "classloader:"+dLoader);

        }catch(Exception e){
            Log.d("yaopinfan", "load apk classloader error:"+Log.getStackTraceString(e));
        }


        mTextView = (TextView)findViewById(R.id.textView);

        mButton1 = (Button)findViewById(R.id.button1);

        mButton2 = (Button)findViewById(R.id.button2);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 刻意以“包名”来打开
                RePlugin.startActivity(MainActivity.this, RePlugin.createIntent("com.example.fantasy1722.pluginpackage", "com.example.fantasy1722.pluginpackage.MainActivity"));
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test test1 = new test();
                String ss = test1.getString();
                mTextView.setText(ss);
            }
        });

        PathClassLoader pathClassLoader = (PathClassLoader) MainActivity.this.getClassLoader();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Log.d("yaopinfan","info : " + pathClassLoader.toString());
        Log.d("yaopinfan", "info parent的类加载器:" + pathClassLoader.getParent().toString());
        Log.d("yaopinfan", "Context : " + Context.class.getClassLoader().toString());
        Log.d("yaopinfan", "thread : " + cl.toString());
        Log.d("yaopinfan", "Utils的类加载器:" + Utils.class.getClassLoader().toString());
        Log.d("yaopinfan", "String的类加载加载器:" + String.class.getClassLoader().toString());
        Log.d("yaopinfan", "MainActivity parent的类加载器:" + MainActivity.class.getClassLoader().getParent().toString());
        Log.d("yaopinfan", "MainActivity的类加载器:" + MainActivity.class.getClassLoader().toString());
        Log.d("yaopinfan", "应用程序默认加载器:" + getClassLoader().toString());
        Log.d("yaopinfan", "系统类加载器:" + ClassLoader.getSystemClassLoader().toString());
        Log.d("yaopinfan", "系统类加载器和应用程序默认加载器是否相等:" + (getClassLoader() == ClassLoader.getSystemClassLoader()));
        Log.d("yaopinfan", "自定义类和第三方类库使用的classloader和应用程序默认加载器是否相等:" + (MainActivity.class.getClassLoader() == getClassLoader()));





    }
}

package com.example.fantasy1722.example4injectdexlist;

import android.content.Context;
import android.util.Log;

import com.qihoo360.replugin.RePlugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class BootClassLoader extends ClassLoader {

    private final ClassLoader mOrig;
    private static final int BUF_SIZE = 8 * 1024;
    private static DexClassLoader mDexClassLoader;
    private ClassLoader fcl;




    public BootClassLoader(String dexPath, String optimizedDexOutputPath) throws ClassNotFoundException {
        //super(BootClassLoader.class.getClassLoader().getParent());
        mOrig = BootClassLoader.class.getClassLoader().getParent();
        //fcl = RePlugin.fetchClassLoader("pluginpackage");
        //extractAssets(context, "app-release.apk");

        //String secondDexPath = CgetDexPath(context, "app-release.apk");

        //final String optimizedDexOutputPath = CgetOptimizedDexPath(context);
        mDexClassLoader = new DexClassLoader(dexPath, optimizedDexOutputPath, null, mOrig);
        Class<?> clazz = mDexClassLoader.loadClass("com.example.examplelibrary.test");
        if (clazz == null)
        {
            Log.d("yaopinfan", "test is null!");
        } else {
            Log.d("yaopinfan", "test is win!");
        }


    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        //Log.d("yaopinfan", "find class + " + name);

        Class<?> clazz = null;

        //if (clazz == null) {
        //    Log.d("yaopinfan", "use plugin find class + " + name);
        //    clazz = fcl.loadClass(name);
        //}


        if (clazz == null) {
            Log.d("yaopinfan", "find class orgin + " + name);
            try {
                Class pcl = Class.forName("java.lang.ClassLoader");

                Method pfd = pcl.getDeclaredMethod("findClass", String.class);

                clazz = (Class<?>) pfd.invoke(mOrig, name);

            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
            } catch (InvocationTargetException e) {
                //e.printStackTrace();
            }
        }

        if (clazz ==null) {
            Log.d("yaopinfan", "use plugin find clazz null " + name);
        }

        return clazz;

        //PathClassLoader recl = (PathClassLoader) Thread.currentThread().getContextClassLoader();
        //return super.findClass(name);
    }

    //    @Override
//    protected URL findResource(String name) {
//
//        return VMClassLoader.getResource(name);
//    }
//
//
//    @Override
//    protected Enumeration<URL> findResources(String resName) throws IOException {
//        return Collections.enumeration(VMClassLoader.getResources(resName));
//    }
//
//    @Override
//    protected Package getPackage(String name) {
//        if (name != null && !name.isEmpty()) {
//            synchronized (this) {
//                Package pack = super.getPackage(name);
//
//                if (pack == null) {
//                    pack = definePackage(name, "Unknown", "0.0", "Unknown", "Unknown", "0.0",
//                            "Unknown", null);
//                }
//
//                return pack;
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public URL getResource(String resName) {
//        return findResource(resName);
//    }
//
    @Override
    protected Class<?> loadClass(String className, boolean resolve)
            throws ClassNotFoundException {
        //Log.d("yaopinfan", "find class + " + className);
        Class<?> c = null;

        if (c == null) {
            try {
                c = mOrig.loadClass(className);
                // 只有开启“详细日志”才会输出，防止“刷屏”现象
                return  c;
            } catch (Throwable e) {
                //e.printStackTrace();
            }
        }

        if (c == null && className.equals("com.example.examplelibrary.test")) {
            c = mDexClassLoader.loadClass(className);
            return c;
        }
        //return c;
        return super.loadClass(className, resolve);
    }

//    @Override
//    public Enumeration<URL> getResources(String resName) throws IOException {
//        return findResources(resName);
//    }

    public static void extractAssets(Context context, String sourceName) {
        File dexInternalStoragePath = new File(context.getDir("dex", Context.MODE_PRIVATE), sourceName);
        BufferedInputStream bis = null;
        OutputStream dexWriter = null;

        try {
            bis = new BufferedInputStream(context.getAssets().open(sourceName));
            dexWriter = new BufferedOutputStream(
                    new FileOutputStream(dexInternalStoragePath));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String CgetDexPath(Context context, String dexName) {
        return new File(context.getDir("dex", Context.MODE_PRIVATE), dexName).getAbsolutePath();
    }

    public static String CgetOptimizedDexPath(Context context) {
        return context.getDir("outdex", Context.MODE_PRIVATE).getAbsolutePath();
    }

}

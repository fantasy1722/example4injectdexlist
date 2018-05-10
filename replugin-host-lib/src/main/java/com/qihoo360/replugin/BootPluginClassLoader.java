package com.qihoo360.replugin;

import android.content.Context;
import android.util.Log;

import com.qihoo360.loader2.PMF;
import com.qihoo360.replugin.utils.ReflectUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;

import dalvik.system.DexClassLoader;

public class BootPluginClassLoader extends ClassLoader {
    private final ClassLoader mOrig;

    private static ClassLoader pCl;

    private static final int BUF_SIZE = 8 * 1024;

    private static DexClassLoader mDexClassLoader;

    private Method findClassMethod;

    private Method findResourceMethod;

    private Method findResourcesMethod;

    private Method findLibraryMethod;

    private Method getPackageMethod;


    public BootPluginClassLoader(Context context, String dexPath, String optimizedDexOutputPath) throws ClassNotFoundException {
        super(null);
        mOrig = BootPluginClassLoader.class.getClassLoader().getParent();
        initMethods(mOrig);
        pCl = RePlugin.fetchClassLoader("pluginpackage");
        extractAssets(context, "app-release.apk");

        String secondDexPath = CgetDexPath(context, "app-release.apk");

        final String optimizedDexOutputPath1 = CgetOptimizedDexPath(context);
        mDexClassLoader = new DexClassLoader(secondDexPath, optimizedDexOutputPath1, null, mOrig);
        //Class<?> clazz = mDexClassLoader.loadClass("com.example.examplelibrary.test");
        //if (clazz == null)
        //{
        //    Log.d("yaopinfan", "test is null!");
        //} else {
        //    Log.d("yaopinfan", "test is win!");
        //}



    }

    private void initMethods(ClassLoader cl) {
        Class<?> c = cl.getClass();
        findClassMethod = ReflectUtils.getMethod(c, "findClass", String.class);
        findClassMethod.setAccessible(true);
        findResourceMethod = ReflectUtils.getMethod(c, "findResource", String.class);
        findResourceMethod.setAccessible(true);
        findResourcesMethod = ReflectUtils.getMethod(c, "findResources", String.class);
        findResourcesMethod.setAccessible(true);
        findLibraryMethod = ReflectUtils.getMethod(c, "findLibrary", String.class);
        findLibraryMethod.setAccessible(true);
        getPackageMethod = ReflectUtils.getMethod(c, "getPackage", String.class);
        getPackageMethod.setAccessible(true);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        Class<?> clazz = null;

        if (clazz == null) {
            try {
                clazz = (Class<?>) findClassMethod.invoke(mOrig, name);
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
    }

    @Override
    protected URL findResource(String resName) {
        try {
            return (URL) findResourceMethod.invoke(mOrig, resName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Enumeration<URL> findResources(String resName) throws IOException {
        try {
            return (Enumeration<URL>) findResourcesMethod.invoke(mOrig, resName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Package getPackage(String name) {
        Package pack = null;
        if (name != null && !name.isEmpty()) {
            try {
                pack = (Package) getPackageMethod.invoke(mOrig, name);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return pack;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve)
            throws ClassNotFoundException {
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
            //c = mDexClassLoader.loadClass(className);
            c = pCl.loadClass(className);
            //c = PMF.loadClass(className, resolve);
        }
        return c;
        //return super.loadClass(className, resolve);
    }

    @Override
    public URL getResource(String resName) {
        return findResource(resName);
    }

    @Override
    public Enumeration<URL> getResources(String resName) throws IOException {
        return findResources(resName);
    }




    /*----------------------------------------*/
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

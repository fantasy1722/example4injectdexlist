package com.example.fantasy1722.example4injectdexlist;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;


public class Utils {

    private static final int BUF_SIZE = 8 * 1024;

    /**
     * 把Assets里面得文件复制到 /data/data/files 目录下
     *
     * @param context
     * @param sourceName
     */
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

    /**
     * 待加载插件存放dex得路径
     */
    public static String getDexPath(Context context, String dexName) {
        return new File(context.getDir("dex", Context.MODE_PRIVATE), dexName).getAbsolutePath();
    }

    /**
     * 待加载插件经过opt优化之后存放odex得路径
     */
    public static String getOptimizedDexPath(Context context) {
        return context.getDir("outdex", Context.MODE_PRIVATE).getAbsolutePath();
    }
}

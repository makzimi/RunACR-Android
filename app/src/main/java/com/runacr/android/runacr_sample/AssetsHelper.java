package com.runacr.android.runacr_sample;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetsHelper {

    private Context context;

    public interface OnFileIsReady {
        void onFileIsReady(String filePath);
    }

    public AssetsHelper (Context context) {
        this.context = context;
    }

    public void copyAssetFile(String fileName, OnFileIsReady onFileIsReady) {
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fileName);
            File outFile = new File(context.getFilesDir(), fileName);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            onFileIsReady.onFileIsReady(context.getFilesDir() + "/" + fileName);
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + fileName, e);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {

                }
            }
        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}

package cn.skyui.library.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author tianshaojie
 * @date 2018/2/28
 */
public class AssetUtils {

    public static String getStringFromAssets(Context context, String fileName) {
        String fileContent = "";
        try {
            InputStream is = context.getAssets().open(fileName);
            fileContent = inputStream2String(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}

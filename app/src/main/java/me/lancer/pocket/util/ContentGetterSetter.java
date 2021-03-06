package me.lancer.pocket.util;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by HuangFangzhi on 2016/12/14.
 */

public class ContentGetterSetter {

    public ContentGetterSetter() {
    }

    public String getContentFromHtml(String log, String url) {
        StringBuilder content = new StringBuilder();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).addHeader("Accept-Language", "zh-CN,zh;q=0.8").build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                return content.toString();
            } else {
                return "获取失败!状态码:" + response.code();
            }
        } catch (IOException e) {
            return "获取失败!捕获异常:" + e.toString();
        }
    }

    public String getContentFromFile(String path, String arg1) {
        try {
            File dir = new File(path + "/me.lancer.pocket");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir.getPath() + "/" + arg1);
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String content = new String(bytes);
            if (content == null) {
                return "加载文件失败!空文件";
            }
            if (fis != null) {
                fis.close();
            }
            return content;
        } catch (IOException e) {
            return "加载文件失败!捕获异常:" + e.toString();
        }
    }

    public void setContentToFile(String path, String arg1, String arg2, String content) {
        try {
            File dir = new File(path + "/me.lancer.pocket");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file1 = new File(dir.getPath() + "/" + arg1);
            File file2 = new File(dir.getPath() + "/" + arg2);
            if (file2.exists()) {
                file2.delete();
                file1.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file1);
            fos.write(content.getBytes());
            if (fos != null) {
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

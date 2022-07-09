package pers.zhangyang.easyguishop.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.exception.FailureDeleteFileException;

import java.io.*;
import java.net.URL;

public class ResourceUtil {
    public static void deleteFile(@NotNull File file) throws FailureDeleteFileException {
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File deleteFile : files) {
            if (deleteFile.isDirectory()) {
                //判断如果是文件夹，则递归删除下面的文件后再删除该文件夹
                deleteFile(deleteFile);
            } else {
                //文件直接删除
                deleteFile(deleteFile);
            }
        }
        if (!file.delete()) {
            throw new FailureDeleteFileException();
        }
    }

    @Nullable
    public static String readFirstLine(URL url) throws IOException {
        if (url == null) {
            throw new NullPointerException();
        }
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.readLine();
    }

}

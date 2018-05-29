package com.microdev.automation.restng.util.basic;

import java.io.*;

/**
 * Created by wuchao on 17/7/26.
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * 获得excel文件的路径
     *
     * @return
     * @throws IOException
     */
    public static String getPath(String fileName) throws IOException {
        return getPath(fileName, ".xls");
    }

    /**
     * 获得任意文件的路径
     *
     * @return
     * @throws IOException
     */
    public static String getPath(String fileName, String postfix) throws IOException {
        File directory = new File(".");
        return directory.getCanonicalPath() + "/src/test/resources/data/"
                + fileName + postfix;
    }

    public static InputStream getInputStream(String fileName, String postfix) {
        InputStream is;

        String className = FileUtils.class.getName().replace('.', '/');
        String classJar =
                FileUtils.class.getResource("/" + className + ".class").toString();
        //从jar包执行
        if (classJar.startsWith("jar:")) {
            is = FileUtils.class.getResourceAsStream("/data/" + fileName + postfix);
        } else {
            //优先test，test没有再读main
            String path = "src/test/resources/data/" + fileName + postfix;
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                path = "src/main/resources/data/" + fileName + postfix;
            }
            file = new File(path);
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                is = null;
            }
        }
        return is;
    }

}

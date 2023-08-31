package com.ruanyi.mifish.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.ruanyi.mifish.common.model.FilePathName;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 18:40
 */
public final class FileUtils {

    /**
     * listAllPaths
     * 
     * @param dir
     * @return
     */
    public static List<FilePathName> listAllPaths(String dir) {
        File file = new File(dir);
        if (file.isFile()) {
            FilePathName filePathName = new FilePathName();
            filePathName.setName(file.getName());
            filePathName.setPath(file.getPath());
            return Lists.newArrayList(filePathName);
        } else {
            File[] fs = file.listFiles();
            List<FilePathName> filePaths = new ArrayList<>(fs.length);
            for (File f : fs) {
                if (f.isDirectory()) {
                    filePaths.addAll(listAllPaths(f.getPath()));
                } else {
                    FilePathName filePathName = new FilePathName();
                    filePathName.setName(f.getName());
                    filePathName.setPath(f.getPath());
                    filePaths.add(filePathName);
                }
            }
            return filePaths;
        }
    }

    /** FileUtils */
    private FileUtils() {

    }
}

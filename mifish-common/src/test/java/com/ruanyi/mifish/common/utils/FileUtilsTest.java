package com.ruanyi.mifish.common.utils;

import java.util.List;

import org.junit.Test;

import com.ruanyi.mifish.common.model.FilePathName;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 18:48
 */
public class FileUtilsTest {

    @Test
    public void test1() {
        String dir = "/Users/rls/Documents/tmp/v19/素材";
        List<FilePathName> paths = FileUtils.listAllPaths(dir);
        System.out.println(paths);
    }
}

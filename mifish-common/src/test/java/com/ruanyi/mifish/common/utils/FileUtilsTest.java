package com.ruanyi.mifish.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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

    @Test
    public void test2() {
        String fromVideoDir = "/Users/rls/Documents/tmp/v19/素材";
        String toVideoDir = "/Users/rls/Documents/tmp/v19/video_rs";
        List<FilePathName> fromPaths = FileUtils.listAllPaths(fromVideoDir);
        List<FilePathName> toPaths = FileUtils.listAllPaths(toVideoDir);
        List<FilePathName> diffs = new ArrayList<>();
        for (FilePathName ffp : fromPaths) {
            String ffpK = StringUtils.substringBefore(ffp.getName(), ".");
            Optional<FilePathName> optional = toPaths.stream().filter((tfp) -> {
                String key = StringUtils.substringBefore(tfp.getName(), ".");
                return StringUtils.contains(key, ffpK);
            }).findFirst();
            if (!optional.isPresent()) {
                diffs.add(ffp);
            }
        }
        List<String> diffNames = diffs.stream().map((fp) -> fp.getName()).collect(Collectors.toList());
        System.out.println(diffNames.toString());
    }

}

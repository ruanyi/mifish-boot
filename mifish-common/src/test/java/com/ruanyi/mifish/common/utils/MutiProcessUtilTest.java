package com.ruanyi.mifish.common.utils;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.ruanyi.mifish.common.model.ProcessResult;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-23 17:15
 */
public class MutiProcessUtilTest {

    @Test
    public void testRunSubProcess() {
        String pid = UUIDUtil.obtainUUID();
        String ffmpeg =
            "/usr/local/bin/ffmpeg -i /Users/ruanyi/Documents/tmp/watermark/f343da361cc3d1f57a960632bf208e06.mov -f lavfi -i \"color=black@0:s=300x300,format=yuva420p\" -filter_complex \"[1]trim=end_frame=1,drawtext=fontfile='PingFang SC':text='美图内部课程':fontsize=36:fontcolor=white:alpha=0.2,split[text][alpha];[text][alpha]alphamerge,rotate=(-30*PI/180):ow=rotw(-30*PI/180):oh=roth(-30*PI/180):c=black@0,loop=-1:1:0,tile=30x9,trim=end_frame=1[wm];[0][wm]overlay=0:0\" -y /Users/ruanyi/Documents/tmp/watermark/output_9.mp4";
        ProcessResult pr = MutiProcessUtil.runSubProcess(pid, 1, TimeUnit.MINUTES, "sh", "-c", ffmpeg);
        System.out.println(pr.getExitValue());
    }

    @Test
    public void testRunSubProcess2() {
        String pid = UUIDUtil.obtainUUID();
        ProcessResult pr = MutiProcessUtil.runShellProcess(pid, "ls -lah");
        System.out.println(pr.getExitValue());
    }
}

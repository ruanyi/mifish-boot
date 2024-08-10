package com.ruanyi.mifish.kernel.model.storage;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * 
 * 唯一键：cloud + bucket;
 * 
 * 不同云下的名字，不建议：一模一样
 *
 * @author: ruanyi
 * @Date: 2024-05-03 15:23
 */
@Getter
@Setter
public class BucketMeta {

    /** cloud */
    private String cloud;

    /** supportS3 */
    private boolean supportS3;

    /** bucket */
    private String bucket;

    /** hosts */
    private List<BucketHost> hosts;

    /** auths */
    private List<BucketAuth> auths;

}

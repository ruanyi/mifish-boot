package com.ruanyi.mifish.model.storage;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * 
 * host地址，全局唯一，不允许出现：不同云之间的host地址是一模一样的
 *
 * @author: ruanyi
 * @Date: 2024-05-03 15:31
 */
@Getter
@Setter
public class BucketHost {

    /** host */
    private String host;

    /** tags */
    private List<String> tags;
}

package com.ruanyi.mifish.model.storage;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-03 15:28
 */
@Getter
@Setter
public class BucketAuth {

    /** ak */
    private String ak;

    /** sk */
    private String sk;

    /** tags */
    private List<String> tags;
}

package com.ruanyi.mifish.x8583.context;

import org.junit.Test;

import com.ruanyi.mifish.x8583.model.X8583Model;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-18 23:50
 */
public class ModelAnnotationsTest {

    @Test
    public void test1() {
        ModelAnnotations modelAnnotations = ModelAnnotations.of(X8583Model.class);
        System.out.println(modelAnnotations);
    }
}

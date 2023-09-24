package com.ruanyi.mifish.x8583.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ruanyi.mifish.common.utils.ClassUtil;
import com.ruanyi.mifish.x8583.annotation.BitMapField;
import com.ruanyi.mifish.x8583.annotation.FixedLenField;
import com.ruanyi.mifish.x8583.annotation.UnsizedField;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-18 23:16
 */
public class X8583ModelTest {

    @Test
    public void test1() {
        Field[] fields = ClassUtil.getAllFields(X8583Model.class);
        Map<Integer, Annotation> annotations = new HashMap<>();
        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            for (Annotation an : fieldAnnotations) {
                if (an instanceof BitMapField) {
                    BitMapField bitMapField = (BitMapField)an;
                    annotations.put(bitMapField.order(), bitMapField);
                } else if (an instanceof FixedLenField) {
                    FixedLenField fixedLenField = (FixedLenField)an;
                    annotations.put(fixedLenField.order(), fixedLenField);
                } else if (an instanceof UnsizedField) {
                    UnsizedField unsizedField = (UnsizedField)an;
                    annotations.put(unsizedField.order(), unsizedField);
                }
            }
        }
        if (annotations.size() != 129 && annotations.size() != 65) {
            throw new ISOX8583Exception("cannot be support");
        }
        System.out.println(annotations);
    }
}

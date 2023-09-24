package com.ruanyi.mifish.x8583.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ruanyi.mifish.common.utils.ClassUtil;
import com.ruanyi.mifish.x8583.annotation.BitMapField;
import com.ruanyi.mifish.x8583.annotation.FixedLenField;
import com.ruanyi.mifish.x8583.annotation.UnsizedField;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-18 23:34
 */
public final class ModelAnnotations {

    private Map<Integer, Annotation> annotationContainer = new HashMap<>(129);

    private Map<Integer, Field> fieldContainer = new HashMap<>(129);

    /**
     * getBitMapField
     *
     * @return
     */
    public BitMapField getBitMapField() {
        return (BitMapField)this.annotationContainer.get(1);
    }

    /**
     * getField
     * 
     * @param order
     * @return
     */
    public Field getField(Integer order) {
        return this.fieldContainer.get(order);
    }

    /**
     * getAnnotation
     * 
     * @param order
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> T getAnnotation(Integer order, Class<T> clazz) {
        if (!this.annotationContainer.containsKey(order)) {
            return null;
        }
        Annotation annotation = annotationContainer.get(order);
        return clazz.cast(annotation);
    }

    /**
     * getAnnotation
     * 
     * @param order
     * @return
     */
    public Annotation getAnnotation(Integer order) {
        return this.annotationContainer.get(order);
    }

    /**
     * getAnnotationIterator
     * 
     * @return
     */
    public Iterator<Map.Entry<Integer, Annotation>> getAnnotationIterator() {
        return this.annotationContainer.entrySet().iterator();
    }

    /**
     * getFieldIterator
     * 
     * @return
     */
    public Iterator<Map.Entry<Integer, Field>> getFieldIterator() {
        return this.fieldContainer.entrySet().iterator();
    }

    /**
     * getFixedLenField
     * 
     * @param order
     * @return
     */
    public FixedLenField getFixedLenField(Integer order) {
        Annotation annotation = this.annotationContainer.get(order);
        return (FixedLenField)annotation;
    }

    /**
     * getUnsizedField
     * 
     * @param order
     * @return
     */
    public UnsizedField getUnsizedField(Integer order) {
        Annotation annotation = this.annotationContainer.get(order);
        return (UnsizedField)annotation;
    }

    /**
     * register
     * 
     * @param bitMapField
     * @param field
     * @return
     */
    public ModelAnnotations register(BitMapField bitMapField, Field field) {
        annotationContainer.put(bitMapField.order(), bitMapField);
        fieldContainer.put(bitMapField.order(), field);
        return this;
    }

    /**
     * register
     *
     * @param fixedLenField
     * @param field
     * @return
     */
    public ModelAnnotations register(FixedLenField fixedLenField, Field field) {
        annotationContainer.put(fixedLenField.order(), fixedLenField);
        fieldContainer.put(fixedLenField.order(), field);
        return this;
    }

    /**
     * register
     * 
     * @param unsizedField
     * @param field
     * @return
     */
    public ModelAnnotations register(UnsizedField unsizedField, Field field) {
        annotationContainer.put(unsizedField.order(), unsizedField);
        fieldContainer.put(unsizedField.order(), field);
        return this;
    }

    /**
     * getAnnotationSize
     * 
     * @return
     */
    public int getAnnotationSize() {
        return annotationContainer.size();
    }

    /** forbit instance */
    private ModelAnnotations() {

    }

    /**
     * of
     * 
     * @param x8583Clazz
     * @return
     */
    public static ModelAnnotations of(Class<?> x8583Clazz) {
        if (x8583Clazz == null) {
            throw new ISOX8583Exception("x8583Model cnanot be null");
        }
        ModelAnnotations modelAnnotations = new ModelAnnotations();
        Field[] fields = ClassUtil.getAllFields(x8583Clazz);
        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            for (Annotation an : fieldAnnotations) {
                if (an instanceof BitMapField) {
                    BitMapField bitMapField = (BitMapField)an;
                    modelAnnotations.register(bitMapField, field);
                } else if (an instanceof FixedLenField) {
                    FixedLenField fixedLenField = (FixedLenField)an;
                    modelAnnotations.register(fixedLenField, field);
                } else if (an instanceof UnsizedField) {
                    UnsizedField unsizedField = (UnsizedField)an;
                    modelAnnotations.register(unsizedField, field);
                }
            }
        }
        // mti，并非所有8583报文都有的
        if (modelAnnotations.getAnnotationSize() != 129 && modelAnnotations.getAnnotationSize() != 128
            && modelAnnotations.getAnnotationSize() != 65 && modelAnnotations.getAnnotationSize() != 64) {
            throw new ISOX8583Exception("8583Model's fields is illegal");
        }
        return modelAnnotations;
    }
}

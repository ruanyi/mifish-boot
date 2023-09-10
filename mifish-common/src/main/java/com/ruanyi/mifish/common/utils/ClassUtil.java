package com.ruanyi.mifish.common.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ClassUtil
 * 
 * @Creator paladin.xiehai
 * @CreateTime 2008-11-5 $Author$ ${date} $Revision$ $Date$
 */
public abstract class ClassUtil {

    public static Class<?>[] loadClassesFromPackage(String packageName, Class<? extends Annotation> cls)
        throws IOException, ClassNotFoundException {
        Class<?>[] clss = loadClassesFromPackage(packageName);
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (int i = 0; i < clss.length; i++) {
            if (clss[i].isAnnotationPresent(cls)) {
                set.add(clss[i]);
            }
        }
        return set.toArray(new Class<?>[] {});
    }

    public static Class<?>[] loadClassesFromPackage(String name) throws IOException, ClassNotFoundException {
        if (name == null) {
            return new Class[] {};
        }
        Enumeration<URL> resources =
            Thread.currentThread().getContextClassLoader().getResources(name.replace('.', '/'));
        Set<Class<?>> cls = new HashSet<Class<?>>();
        for (; resources.hasMoreElements();) {
            URL url = resources.nextElement();

            String protocol = url.getProtocol();

            if ("file".equals(protocol)) {
                cls.addAll(
                    Arrays.asList(loadClassesFromFilePath(name, java.net.URLDecoder.decode(url.getFile(), "UTF-8"))));
            } else if ("jar".equals(protocol)) {
                JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
                cls.addAll(Arrays.asList(loadClassesFromJar(name, jar)));
            }
        }
        return cls.toArray(new Class<?>[] {});
    }

    public static Class<?>[] loadClassesFromJar(String pkgname, JarFile jarFile) throws ClassNotFoundException {
        Enumeration<JarEntry> entries = jarFile.entries();

        Set<Class<?>> cls = new HashSet<Class<?>>();
        for (; entries.hasMoreElements();) {
            JarEntry je = entries.nextElement();
            String name = je.getName();

            if (name.startsWith("/")) {
                name = name.substring(1);
            }

            name = name.replace('/', '.');

            if (name.startsWith(pkgname)) {
                if (je.isDirectory()) {
                    continue;
                } else if (name.endsWith(".class")) {
                    String clsname = name.substring(0, name.length() - 6);
                    cls.add(forName(clsname));
                }

            }
        }
        return cls.toArray(new Class<?>[] {});
    }

    public static Class<?>[] loadClassesFromFilePath(String pkgname, String path) throws ClassNotFoundException {
        File file = new File(path);

        if (!file.exists() || !file.isDirectory()) {
            return new Class<?>[] {};
        }

        Set<Class<?>> cls = new HashSet<>();
        File[] children =
            file.listFiles((pathname) -> (pathname.isDirectory()) || (pathname.getName().endsWith("class")));

        for (File child : children) {
            if (child.isFile()) {
                String clsname = pkgname + "." + child.getName().substring(0, child.getName().length() - 6);
                cls.add(forName(clsname));
            } else {
                String newPkg = pkgname + "." + child.getName();
                cls.addAll(Arrays.asList(loadClassesFromFilePath(newPkg, child.getAbsolutePath())));
            }
        }

        return cls.toArray(new Class<?>[] {});
    }

    /**
     * 
     * @param cls
     * @param annoCls
     * @return
     */
    public static Field[] getField(Class<?> cls, Class<? extends Annotation> annoCls) {
        List<Field> ls = new ArrayList<Field>();
        Class<?> temp = cls;

        if (!temp.getClass().getName().equals("java.lang.Object")) {
            ls.addAll(Arrays.asList(getDeclaredField(temp, annoCls)));
            temp = temp.getSuperclass();
        }

        return ls.toArray(new Field[] {});

    }

    /**
     * 
     * @param cls
     * @param annoCls
     * @return
     */
    public static Field[] getDeclaredField(Class<?> cls, Class<? extends Annotation> annoCls) {
        Field[] fields = cls.getDeclaredFields();

        List<Field> ls = new ArrayList<Field>();

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getAnnotation(annoCls) == null) {
                continue;
            }
            fields[i].setAccessible(true);
            ls.add(fields[i]);
        }

        return ls.toArray(new Field[] {});
    }

    public static Field[] getDeclaredField(Class<?> cls, Class<? extends Annotation> annoCls, String[] fieldNames) {
        Set<String> set = new HashSet<String>(Arrays.asList(fieldNames));
        List<Field> fs = new ArrayList<Field>();

        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (set.contains(fields[i].getName())) {
                if (fields[i].getAnnotation(annoCls) == null) {
                    continue;
                }
                fields[i].setAccessible(true);
                fs.add(fields[i]);
            }
        }
        return fs.toArray(new Field[] {});
    }

    public static Field[] getDeclaredField(Class<?> cls, String[] fieldNames) {
        Set<String> set = new HashSet<String>(Arrays.asList(fieldNames));
        List<Field> fs = new ArrayList<Field>();

        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (set.contains(fields[i].getName())) {
                fields[i].setAccessible(true);
                fs.add(fields[i]);
            }
        }
        return fs.toArray(new Field[] {});
    }

    /**
     * 
     * @param cls
     * @return
     */
    public static Field[] getDeclaredField(Class<?> cls) {
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
        }
        return fields;
    }

    /**
     * 获得一个类（包括父类）中所有带有annoCLs的域
     * 
     * @param cls
     * @param annoCls
     * @return Field[]
     */
    public static Field[] getAllFields(Class<?> cls, Class<? extends Annotation> annoCls) {
        Field[] fields = getDeclaredField(cls, annoCls);
        if (cls.getSuperclass() == null) {
            return fields;
        } else {
            // 假如有父类的话，接着寻找
            fields = join(fields, getAllFields(cls.getSuperclass(), annoCls));
        }
        return fields;
    }

    /**
     * 
     * @param cls
     * @return field[]
     */
    public static Field[] getAllFields(Class<?> cls) {
        // 获得当前类的所有属性
        Field[] fields = getDeclaredField(cls);
        if (cls.getSuperclass() == null) {
            return fields;
        } else {
            // 假如有父类的话，接着寻找
            fields = join(fields, getAllFields(cls.getSuperclass()));
        }
        return fields;
    }

    /**
     * join
     * 
     * @param left
     * @param right
     * @return Field[]
     */
    public static Field[] join(Field[] left, Field[] right) {
        // 假设right的长度为0，则直接返回left
        if (right.length == 0) {
            return left;
        }
        Field[] temp = new Field[left.length + right.length];
        System.arraycopy(left, 0, temp, 0, left.length);
        System.arraycopy(right, 0, temp, left.length, right.length);
        return temp;
    }

    /**
     * 
     * @param className
     * @return
     */
    public static String getPackageName(String className) {
        int index = className.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        String packageName = className.substring(0, index);

        return packageName;
    }

    /**
     * 
     * @param objBean
     * @return
     */
    public static String getShortName(Object objBean) {
        if (objBean == null) {
            return "";
        }
        return objBean.getClass().getSimpleName();
    }

    /**
     * 
     * @param <T>
     * @param cls
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> T instantiateClass(Class<T> cls) throws InstantiationException, IllegalAccessException {
        return cls.newInstance();
    }

    /**
     * 
     * @param <T>
     * @param constructor
     * @param args
     * @return
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> T instantiateClass(Constructor<T> constructor, Object[] args)
        throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return constructor.newInstance(args);
    }

    /**
     * 
     * @param className
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, getDefaultClassLoader());
    }

    /**
     * 
     * @param className
     * @param classLoader
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> forName(String className, ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> cls = null;

        try {
            cls = classLoader.loadClass(className);
            return cls;
        } catch (ClassNotFoundException e) {
            try {
                cls = getDefaultClassLoader().loadClass(className);
                return cls;
            } catch (ClassNotFoundException e1) {
                throw e;
            }
        }
    }

    /**
     * 
     * @param cls
     * @param argTypes
     * @return
     * @throws NoSuchMethodException
     */
    public static <T> Constructor<T> getConstructorIfAvailable(Class<T> cls, Class<?>[] argTypes)
        throws NoSuchMethodException {
        Constructor<T> constructor = null;

        try {
            constructor = cls.getConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            constructor = cls.getDeclaredConstructor(argTypes);
            constructor.setAccessible(true);
        }

        return constructor;
    }

    /**
     * 
     * @param cls
     * @param methodName
     * @param argTypes
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getMethodIfAvailable(Class<?> cls, String methodName, Class<?>[] argTypes)
        throws NoSuchMethodException {
        Method method = null;

        try {
            method = cls.getDeclaredMethod(methodName, argTypes);
        } catch (NoSuchMethodException e) {
            if (cls.getSuperclass() != null) {
                return getMethodIfAvailable(cls.getSuperclass(), methodName, argTypes);
            } else {
                throw e;
            }
        }
        method.setAccessible(true);

        return method;
    }

    /**
     * 
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 
     * @return
     */
    public static ClassLoader getSystemClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    public static Object invoke(Object obj, String methodName, Object[] args)
        throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class<?>[] argTypes = new Class<?>[args.length];

        for (int i = 0; i < argTypes.length; i++) {
            argTypes[i] = args[i].getClass();
        }

        Method method = getMethodIfAvailable(obj.getClass(), methodName, argTypes);

        return method.invoke(obj, args);
    }

    public static <T extends Annotation> T getAnnotationIfAvailable(Class<?> cls, Class<T> clazz) {
        return clazz.cast(cls.getAnnotation(clazz));
    }

    public static Object instance(String className)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> cls = forName(className);
        return instantiateClass(cls);
    }

    public static Object instance(String className, Object[] args)
        throws IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException,
        InvocationTargetException, ClassNotFoundException {
        Class<?> cls = forName(className);
        return instantiateClass(cls, args);
    }

    public static <T> T instantiateClass(Class<T> cls, Object[] args) throws InstantiationException,
        IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        Constructor<T> constructor = getConstructorIfAvailable(cls, types);
        return instantiateClass(constructor, args);
    }

    public static Class<?>[] getExtends(Class<?> cls) {
        Set<Class<?>> retVal = new HashSet<Class<?>>();
        retVal.add(cls);

        Class<?>[] inters = cls.getInterfaces();
        retVal.addAll(Arrays.asList(inters));

        Class<?> scls = cls.getSuperclass();
        if (!scls.getName().equals("java.lang.Object")) {
            retVal.addAll(Arrays.asList(getExtends(scls)));
        }

        return retVal.toArray(new Class<?>[] {});
    }

    public static Class<?>[] getInterfaces(Class<?> cls) {
        List<Class<?>> retVal = new ArrayList<Class<?>>();
        if (cls.isInterface()) {
            retVal.add(cls);
        } else {
            Class<?>[] inters = cls.getInterfaces();
            retVal.addAll(Arrays.asList(inters));

            Class<?> scls = cls.getSuperclass();
            if (!scls.getName().equals("java.lang.Object")) {
                retVal.addAll(Arrays.asList(getInterfaces(scls)));
            }
        }
        return retVal.toArray(new Class<?>[] {});

    }

    public static Method findMethodByAnnotation(Class<?> cls, Class<? extends Annotation> acls) {
        Method[] methods = cls.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Annotation a = methods[i].getAnnotation(acls);
            if (a == null) {
                continue;
            } else {
                return methods[i];
            }
        }
        return null;
    }

    public static Method[] findMethodsByAnnotation(Class<?> cls, Class<? extends Annotation> acls) {
        List<Method> ms = new ArrayList<Method>();
        Method[] methods = cls.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Annotation a = methods[i].getAnnotation(acls);
            if (a == null) {
                continue;
            } else {
                ms.add(methods[i]);
            }
        }
        return ms.toArray(new Method[] {});
    }

}

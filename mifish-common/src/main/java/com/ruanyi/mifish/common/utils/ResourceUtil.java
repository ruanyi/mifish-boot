package com.ruanyi.mifish.common.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @Creator paladin.xiehai
 * @CreateTime 2008-10-17 $Author$ ${date} $Revision$ $Date$
 */
public final class ResourceUtil {

    public static Set<String> URL_PREFIX = new HashSet<String>();

    public static String CLASS_PATH_PREFIX = "classpath:";

    static {
        URL_PREFIX.add("http:");
        URL_PREFIX.add("https:");
        URL_PREFIX.add("file:");
    }

    /**
     * @see URL#openStream()
     * @param url
     * @return
     * @throws IOException
     */
    public static InputStream loadAsStream(URL url) throws IOException {
        if (url == null) {
            throw new NullPointerException("the url cannot be null!");
        }
        return url.openStream();
    }

    /**
     * @see #loadAsStream(String, Class)
     * @param resource
     * @return InputStream
     */
    public static InputStream loadAsStream(String resource) {
        if (resource == null || "".equals(resource)) {
            throw new IllegalArgumentException("the argument of resource cannot be empty!!!");
        }
        if (resource != null) {
            resource = resource.trim();
        }
        return loadAsStream(resource, ResourceUtil.class);
    }

    /**
     * @see #loadAsURL(String, Class)
     * @param resource
     * @return
     */
    public static URL loadAsURL(String resource) {
        if (resource == null || "".equals(resource)) {
            throw new IllegalArgumentException("the argument of resource cannot be empty!!!");
        }
        if (resource != null) {
            resource = resource.trim();
        }
        return loadAsURL(resource, ResourceUtil.class);
    }

    /**
     * loadAsURL
     * 
     * @param resource
     * @param callingClass
     * @return
     */
    public static URL loadAsURL(String resource, Class<?> callingClass) {
        if (resource == null || "".equals(resource)) {
            throw new IllegalArgumentException("the argument of resource cannot be empty!!!");
        }
        if (resource != null) {
            resource = resource.trim();
        }
        if (resource.startsWith(CLASS_PATH_PREFIX)) {
            resource = resource.substring(CLASS_PATH_PREFIX.length());
        }
        URL url = null;

        url = ClassUtil.getDefaultClassLoader().getResource(resource);

        if (url == null) {
            url = callingClass.getResource(resource);
        } else {
            ClassLoader loader = ClassUtil.getDefaultClassLoader().getParent();
            while (url == null && loader != null) {
                url = loader.getResource(resource);
                if (url == null) {
                    loader = loader.getParent();
                } else {
                    break;
                }
            }
        }
        if (url == null) {
            url = ClassLoader.getSystemResource(resource);
        }
        if (url == null) {
            try {
                url = new URL(resource);
            } catch (MalformedURLException e) {
                return null;
            }
        }
        return url;
    }

    /**
     * 
     * @param resource
     * @param callingClass
     * @return InputStream
     */
    public static InputStream loadAsStream(String resource, Class<?> callingClass) {
        if (resource == null || "".equals(resource)) {
            throw new IllegalArgumentException("the argument of resource cannot be empty!!!");
        }
        if (resource != null) {
            resource = resource.trim();
        }
        if (resource.startsWith(CLASS_PATH_PREFIX)) {
            resource = resource.substring(CLASS_PATH_PREFIX.length());
        }
        InputStream is = null;
        is = ClassUtil.getDefaultClassLoader().getResourceAsStream(resource);
        if (is == null) {
            is = callingClass.getResourceAsStream(resource);
        }
        return is;
    }

    /**
     * loadAsFile
     *
     * @param resource
     * @return
     */
    public static File loadAsFile(String resource) {
        if (resource == null || "".equals(resource)) {
            throw new IllegalArgumentException("the argument of resource cannot be empty!!!");
        }
        URL url = loadAsURL(resource);
        if (url == null) {
            return null;
        }
        try {
            File file = new File(url.toURI());
            return file;
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Not throw Exception
     * 
     * @param closeable
     * @throws IOException
     */
    public static final void closeStream(Closeable closeable) throws IOException {
        try {
            closeable.close();
        } catch (IOException e) {
            // ingore
        }
    }
}

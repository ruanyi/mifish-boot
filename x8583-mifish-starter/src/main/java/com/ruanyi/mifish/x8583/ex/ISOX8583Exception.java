package com.ruanyi.mifish.x8583.ex;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 
 * ISOX8583Exception
 * 
 * @author ruanyi
 * @time:2013-06-01
 */
public class ISOX8583Exception extends RuntimeException {

    /***/
    private static final long serialVersionUID = 5785668605194524322L;

    private Throwable rootCause;

    public ISOX8583Exception() {
        super();
    }

    public ISOX8583Exception(String msg) {
        super(msg);
    }

    public ISOX8583Exception(Throwable rootCause) {
        this.rootCause = rootCause;
    }

    public ISOX8583Exception(String msg, Throwable rootCause) {
        super(msg);
        this.rootCause = rootCause;
    }

    @Override
    public Throwable getCause() {
        return rootCause;
    }

    @Override
    public String getMessage() {
        StringBuffer sb = new StringBuffer();
        String msg = super.getMessage();

        if (msg != null) {
            sb.append(msg);

            if (rootCause != null) {
                sb.append(": ");
            }
        }

        if (rootCause != null) {
            sb.append(
                "root cause: " + ((rootCause.getMessage() == null) ? rootCause.toString() : rootCause.getMessage()));
        }

        return sb.toString();
    }

    public Throwable getRootCause() {
        return rootCause;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();

        if (rootCause != null) {
            synchronized (System.err) {
                System.err.println("\nRoot cause:");
                rootCause.printStackTrace();
            }
        }
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (rootCause != null) {
            synchronized (s) {
                s.println("\nRoot cause:");
                rootCause.printStackTrace(s);
            }
        }
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (rootCause != null) {
            synchronized (s) {
                s.println("\nRoot cause:");
                rootCause.printStackTrace(s);
            }
        }
    }
}

package com.remainder.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectUtil {
    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.fillInStackTrace();
            return null;
        }
    }

    public static Object getField(Class<?> clazz, Object object, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            e.fillInStackTrace();
            return null;
        }
    }

    /**
     * @return false if the field is not found
     */
    public static boolean getBool(Class<?> clazz, Object object, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getBoolean(object);
        } catch (Exception e) {
            e.fillInStackTrace();
            return false;
        }
    }

    public static int getInt(Class<?> clazz, Object object, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(object);
        } catch (Exception e) {
            e.fillInStackTrace();
            return 0;
        }
    }

    public static float getFloat(Class<?> clazz, Object object, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getFloat(object);
        } catch (Exception e) {
            e.fillInStackTrace();
            return 0.0f;
        }
    }

    public static double getDouble(Class<?> clazz, Object object, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getDouble(object);
        } catch (Exception e) {
            e.fillInStackTrace();
            return 0.0;
        }
    }

    public static long getLong(Class<?> clazz, Object object, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getLong(object);
        } catch (Exception e) {
            e.fillInStackTrace();
            return 0;
        }
    }

    public static short getShort(Class<?> clazz, Object object, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getShort(object);
        } catch (Exception e) {
            e.fillInStackTrace();
            return 0;
        }
    }

    public static byte getByte(Class<?> clazz, Object object, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getByte(object);
        } catch (Exception e) {
            e.fillInStackTrace();
            return 0;
        }
    }

    public static char getChar(Class<?> clazz, Object object, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getChar(object);
        } catch (Exception e) {
            e.fillInStackTrace();
            return 0;
        }
    }

    /**
     * @param parameterTypes The method's parameter types.
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            e.fillInStackTrace();
            return null;
        }
    }

    /**
     * @param parameterTypes The method's parameter types.
     * @param args The method's arguments, or {@code null} if the method does not take any arguments.
     * @return The method's return value, or {@code null} if the method does not exist.
     */
    public static Object getAndInvoke(Class<?> clazz, Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            e.fillInStackTrace();
            return null;
        }
    }

    /**
     * The method should not take any arguments.
     * @return The method's return value, or {@code null} if the method does not exist.
     */
    public static Object getAndInvoke(Class<?> clazz, Object object, String methodName) {
        return getAndInvoke(clazz, object, methodName, null);
    }

    public static void setField(Class<?> clazz, Object object, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void setBool(Class<?> clazz, Object object, String fieldName, boolean value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setBoolean(object, value);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void setInt(Class<?> clazz, Object object, String fieldName, int value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setInt(object, value);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void setFloat(Class<?> clazz, Object object, String fieldName, float value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setFloat(object, value);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void setDouble(Class<?> clazz, Object object, String fieldName, double value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setDouble(object, value);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void setLong(Class<?> clazz, Object object, String fieldName, long value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setLong(object, value);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void setShort(Class<?> clazz, Object object, String fieldName, short value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setShort(object, value);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void setByte(Class<?> clazz, Object object, String fieldName, byte value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setByte(object, value);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void setChar(Class<?> clazz, Object object, String fieldName, char value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setChar(object, value);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}

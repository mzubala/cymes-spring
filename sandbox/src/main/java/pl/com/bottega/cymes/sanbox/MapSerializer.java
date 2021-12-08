package pl.com.bottega.cymes.sanbox;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MapSerializer {

    private static final Pattern GETTER = Pattern.compile("get([A-Z].*)");

    Map<String, Object> serialize(Object input) {
        if(input == null) {
            throw new IllegalArgumentException();
        }
        if(isSimpleType(input)) {
            throw new IllegalArgumentException();
        }
        var resultMap = new HashMap<String, Object>();
        for(var field : input.getClass().getDeclaredFields()) {
            if(!field.getName().startsWith("this")) {
                Object value = readValue(input, field);
                resultMap.put(field.getName(), value);
            }
        }
        for(var method : input.getClass().getDeclaredMethods()) {
            var matcher = GETTER.matcher(method.getName());
            if(matcher.matches() && method.getParameterCount() == 0 && !method.getReturnType().equals(Void.class)) {
                Object value = readValue(input, method);
                resultMap.put(propertyName(matcher.group(1)), value);
            }
        }
        return resultMap;
    }

    private String propertyName(String getterName) {
        String firstChar = "" + getterName.charAt(0);
        return getterName.replaceFirst(firstChar, firstChar.toLowerCase());
    }

    private Object readValue(Object input, Field field) {
        try {
            var accessible = field.canAccess(input);
            field.setAccessible(true);
            var value = field.get(input);
            field.setAccessible(accessible);
            return value;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Object readValue(Object input, Method method) {
        try {
            var accessible = method.canAccess(input);
            method.setAccessible(true);
            var value = method.invoke(input);
            method.setAccessible(accessible);
            return value;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean isSimpleType(Object input) {
        return input.getClass().getPackage() == null ||
            input.getClass().getPackage().getName().startsWith("java") ||
            input.getClass().isEnum();
    }
}

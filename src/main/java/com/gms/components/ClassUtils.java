package com.gms.components;

import com.gms.entity.db.IBaseEntity;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;

/**
 *
 * @author vvthanh
 */
public class ClassUtils {

    // returns the class (without the package if any)
    public static String getClassName(Class c) {
        String FQClassName = c.getName();
        int firstChar;
        firstChar = FQClassName.lastIndexOf('.') + 1;
        if (firstChar > 0) {
            FQClassName = FQClassName.substring(firstChar);
        }
        return FQClassName;
    }

    // returns package and class name
    public static String getFullClassName(Class c) {
        return c.getName();
    }

    // returns the package without the classname, empty string if
    // there is no package
    public static String getPackageName(Class c) {
        String fullyQualifiedName = c.getName();
        int lastDot = fullyQualifiedName.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        }
        return fullyQualifiedName.substring(0, lastDot);
    }

    /**
     * Get Attributes
     *
     * @param items
     * @param methodName
     * @return
     */
    public static Collection getAttribute(List items, String methodName) {
        return CollectionUtils.collect(items, TransformerUtils.invokerTransformer(methodName));
    }

    /**
     * Chuyển object về map options
     *
     * @param items
     * @param methodKey
     * @param methodValue
     * @return
     */
    public static Map<String, String> toOptions(List<IBaseEntity> items, String methodKey, String methodValue) {
        Map<String, String> map = new HashMap<>();
        for (IBaseEntity item : items) {
            try {
                map.put(item.get(methodKey).toString(), item.get(methodValue).toString());
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
                Logger.getLogger(ClassUtils.class.getName()).log(Level.WARNING, null, ex);
            }
        }
        return map;
    }
}

package com.gms.entity.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vvthanh
 */
public abstract class BaseEntity implements IBaseEntity {

    protected static HashMap<String, String> attributeLabels;
    protected static List<String> ignoreAttributes;

    /**
     * Label thuộc tính
     *
     * @auth vvThành
     */
    @Override
    public void setAttributeLabels() {
        attributeLabels = new HashMap<>();
    }

    public String getAttributeLabel(String attributeName, String defaultValue) {
        setAttributeLabels();
        return attributeLabels == null ? null : attributeLabels.getOrDefault(attributeName, defaultValue);
    }

    public String getAttributeLabel(String attributeName) {
        return getAttributeLabel(attributeName, null);
    }

    public HashMap<String, String> getAttributeLabels() {
        setAttributeLabels();
        return attributeLabels;
    }

    @Override
    public void setIgnoreSet() {
        ignoreAttributes = new ArrayList<>();
        ignoreAttributes.add("ID");
    }

    public List<String> getIgnoreSet() {
        setIgnoreSet();
        return ignoreAttributes;
    }

    @Override
    public boolean set(String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(this, fieldValue);
        return true;
    }

    @Override
    public boolean set(Object object) throws IllegalAccessException, NoSuchFieldException {
        return set(object, null);
    }

    @Override
    public boolean set(Object object, List<String> ignoreAttributes) throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (ignoreAttributes != null && ignoreAttributes.contains(field.getName())) {
                continue;
            }

            if (getIgnoreSet() != null && getIgnoreSet().contains(field.getName())) {
                continue;
            }
            try {
                set(field.getName(), field.get(object));
            } catch (Exception ex) {
            }
        }
        return true;
    }

    public List<String> getFields() {
        final List<String> lines = new ArrayList<>();
        for (final Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            lines.add(field.getName());
        }
        return lines;
    }

    @Override
    public Object get(String fieldName) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append("[");
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                sb.append(field.getName());
                sb.append("=");
                sb.append(this.get(field.getName()));
                sb.append(" ");
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * track change object
     *
     * @param oldObject
     * @return
     */
    public HashMap<String, List<Object>> change(Object oldObject) {
        return change(oldObject, true);
    }

    /**
     * track change object
     *
     * @param oldObject
     * @param ignore
     * @return
     */
    public HashMap<String, List<Object>> change(Object oldObject, boolean ignore) {
        HashMap<String, List<Object>> map = new HashMap<>();

        Field[] fields = oldObject.getClass().getDeclaredFields();
        List<String> fCurrent = getFields();
        List<Object> cItem = null;
        for (Field field : fields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (ignore && ignoreAttributes != null && ignoreAttributes.contains(field.getName())) {
                continue;
            }
            if (ignore && getIgnoreSet() != null && getIgnoreSet().contains(field.getName())) {
                continue;
            }
            for (String f : fCurrent) {
                try {
                    if (!field.getName().equals(f)) {
                        continue;
                    }
                    if (!get(f).toString().equals(field.get(oldObject).toString())) {
                        cItem = new ArrayList<>();
                        cItem.add(get(f));
                        cItem.add(field.get(oldObject));
                        map.put(f, cItem);
                    }
                } catch (Exception ex) {
                }
            }
        }
        return map;
    }

    /**
     *
     * @param oldObject
     * @param html
     * @return
     */
    public String changeToString(Object oldObject, boolean html) {
        HashMap<String, List<Object>> map = change(oldObject);
        if (map == null || map.isEmpty()) {
            return "";
        }
        HashMap<String, String> labels = getAttributeLabels();
        List<String> ignoreSet = getIgnoreSet();
        StringBuilder sb = new StringBuilder();
        sb.append(tag("<ul>", html, "["));

        for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
            if (ignoreSet.contains(entry.getKey())) {
                continue;
            }
            sb.append(tag("<li>", html, ""))
                    .append(labels.getOrDefault(entry.getKey(), entry.getKey()))
                    .append(tag("[", html, "("))
                    .append(entry.getValue().get(0))
                    .append("=>")
                    .append(entry.getValue().get(1))
                    .append(tag("]", html, ")"))
                    .append(tag("</li>", html, ""));
        }
        sb.append(tag("</ul>", html, "]"));
        return sb.toString();
    }

    private String tag(String tag, boolean html, String def) {
        if (html) {
            return tag;
        }
        return def;
    }

}

package com.gms.components.annotation.interf;

/**
 *
 * @author vvthanh
 */
public interface FieldValueUnique {

    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param value The value to check for
     * @param fieldName The name of the field for which to check if the value
     * exists
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    boolean fieldValueUnique(Object value, String fieldName) throws UnsupportedOperationException;
}

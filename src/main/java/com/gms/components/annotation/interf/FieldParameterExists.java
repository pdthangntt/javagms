package com.gms.components.annotation.interf;

/**
 *
 * @author vvthanh
 */
public interface FieldParameterExists {

    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param code The value to check for
     * @param type
     * @param mutiple
     * @param fieldName The name of the field for which to check if the value
     * exists
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    boolean fieldParameterExists(Object code, String type, boolean mutiple, String fieldName) throws UnsupportedOperationException;
}

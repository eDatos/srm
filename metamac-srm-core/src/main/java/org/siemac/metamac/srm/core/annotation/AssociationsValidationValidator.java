package org.siemac.metamac.srm.core.annotation;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.validator.Validator;

public class AssociationsValidationValidator implements Validator<AssociationsValidation>, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1145459807886665529L;

    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        }

        if (!(value instanceof Collection<?>)) {
            return false;
        }

        Collection<?> coleccion = (Collection<?>) value;

        if (coleccion.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public void initialize(AssociationsValidation parameters) {
        // nothing
    }
}
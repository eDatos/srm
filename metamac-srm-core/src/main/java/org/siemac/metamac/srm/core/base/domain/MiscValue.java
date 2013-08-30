package org.siemac.metamac.srm.core.base.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity representing MiscValue.
 * <p>
 * This class is responsible for the domain object related business logic for MiscValue. Properties and associations are implemented in the generated base class
 * {@link org.siemac.metamac.srm.core.base.domain.MiscValueBase}.
 */
@Entity
@Table(name = "TB_MISC_VALUES")
public class MiscValue extends MiscValueBase {

    private static final long serialVersionUID = 1L;

    public void clearAllValues() {
        setStringValue(null);
        setDateValue(null);
    }
}

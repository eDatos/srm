package org.siemac.metamac.core.common.ent.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The International String is a collection of Localised Strings and supports the representation of text in multiple locales.
 */
@Entity
@Table(name = "TB_INTERNATIONAL_STRINGS")
public class InternationalString extends InternationalStringBase {

    private static final long serialVersionUID = 1L;

    public InternationalString() {
    }

    public String getLocalisedLabel(String locale) {
        if (locale == null) {
            return null;
        }
        for (LocalisedString localstr : getTexts()) {
            if (locale.equalsIgnoreCase(localstr.getLocale())) {
                return localstr.getLabel();
            }
        }
        return null;
    }

}

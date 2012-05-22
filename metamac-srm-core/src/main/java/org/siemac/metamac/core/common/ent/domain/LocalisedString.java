package org.siemac.metamac.core.common.ent.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Localised String supports the representation of text in one locale (locale is similar to language but includes geographic
 * variations such as Canadian French, US English etc.).
 */
@Entity
@Table(name = "TB_LOCALISED_STRINGS")
public class LocalisedString extends LocalisedStringBase {
    private static final long serialVersionUID = 1L;

    public LocalisedString() {
    }
}

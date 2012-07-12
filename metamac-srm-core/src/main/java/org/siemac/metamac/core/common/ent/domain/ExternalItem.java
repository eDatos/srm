package org.siemac.metamac.core.common.ent.domain;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity for store information about ExternalItems.
 */
@Entity
@Table(name = "TB_EXTERNAL_ITEMS")
public class ExternalItem extends ExternalItemBase {
    private static final long serialVersionUID = 1L;

    protected ExternalItem() {
    }

    public ExternalItem(String uri, String urn, TypeExternalArtefactsEnum type) {
        super(uri, urn, type);
    }
}

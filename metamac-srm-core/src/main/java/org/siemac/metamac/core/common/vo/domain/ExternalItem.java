package org.siemac.metamac.core.common.vo.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.siemac.metamac.core.common.bt.domain.ExternalItemBt;

/**
 * Entity for ExternalItem. It's necessary Sets
 */
@Entity
@Table(name = "TB_EXTERNAL_ITEMS")
public class ExternalItem extends ExternalItemBase {
    private static final long serialVersionUID = 1L;

    protected ExternalItem() {
    }

    public ExternalItem(ExternalItemBt ext) {
        super(ext);
    }
}

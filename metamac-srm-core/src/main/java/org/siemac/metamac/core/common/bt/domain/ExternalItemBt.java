package org.siemac.metamac.core.common.bt.domain;

import javax.persistence.Embeddable;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;

/**
 * External codeList items
 */
@Embeddable
public class ExternalItemBt extends ExternalItemBtBase {
    private static final long serialVersionUID = 1L;

    protected ExternalItemBt() {
    }

    public ExternalItemBt(String uriInt, String codeId,
        TypeExternalArtefactsEnum type) {
        super(uriInt, codeId, type);
    }
}

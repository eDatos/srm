package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.web.common.shared.criteria.base.HasItemSchemeCriteria;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

public class RelatedResourceItemWebCriteria extends RelatedResourceBaseWebCriteria implements HasItemSchemeCriteria {

    private static final long serialVersionUID = 1L;

    private String            itemSchemeUrn;
    private boolean           isItemSchemeExternallyPublished;
    private boolean           isItemSchemeLastVersion;

    public RelatedResourceItemWebCriteria() {
    }

    public RelatedResourceItemWebCriteria(RelatedResourceTypeEnum type) {
        super(type);
    }

    @Override
    public String getItemSchemeUrn() {
        return itemSchemeUrn;
    }

    @Override
    public void setItemSchemeUrn(String itemSchemUrn) {
        this.itemSchemeUrn = itemSchemUrn;
    }

    @Override
    public boolean isItemSchemeExternallyPublished() {
        return isItemSchemeExternallyPublished;
    }

    @Override
    public void setItemSchemeExternallyPublished(boolean isItemSchemeExternallyPublished) {
        this.isItemSchemeExternallyPublished = isItemSchemeExternallyPublished;
    }

    @Override
    public boolean isItemSchemeLastVersion() {
        return isItemSchemeLastVersion;
    }

    @Override
    public void setItemSchemeLastVersion(boolean isItemSchemeLastVersion) {
        this.isItemSchemeLastVersion = isItemSchemeLastVersion;
    }
}

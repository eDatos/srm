package org.siemac.metamac.srm.web.category.model.record;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.client.model.record.VersionableResourceRecord;

import static org.siemac.metamac.srm.web.shared.CommonSharedUtils.getPublicationStreamStatusIconUrl;

public class CategorySchemeRecord extends VersionableResourceRecord {

    public CategorySchemeRecord() {
    }

    public void setCategorySchemeBasicDto(CategorySchemeMetamacBasicDto categorySchemeMetamacDto) {
        setAttribute(CategorySchemeDS.DTO, categorySchemeMetamacDto);
    }

    public void setStreamMessageStatus(StreamMessageStatusEnum value) {
        setAttribute(CategorySchemeDS.PUBLICATION_STREAM_STATUS, getPublicationStreamStatusIconUrl(value));
    }

    public ProcStatusEnum getProcStatus() {
        return ((CategorySchemeMetamacBasicDto) getAttributeAsObject(CategorySchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public CategorySchemeMetamacBasicDto getCategorySchemeBasicDto() {
        return (CategorySchemeMetamacBasicDto) getAttributeAsObject(CategorySchemeDS.DTO);
    }
}

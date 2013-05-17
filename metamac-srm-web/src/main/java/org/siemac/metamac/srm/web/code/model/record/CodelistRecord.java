package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.record.VersionableResourceRecord;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CodelistRecord extends VersionableResourceRecord {

    public CodelistRecord() {
    }

    public void setIsRecommended(String value) {
        setAttribute(CodelistDS.IS_RECOMMENDED, value);
    }

    public void setVariable(RelatedResourceDto value) {
        setRelatedResource(CodelistDS.VARIABLE, value);
    }

    public void setCodelistBasicDto(CodelistMetamacBasicDto codelistMetamacDto) {
        setAttribute(CodelistDS.DTO, codelistMetamacDto);
    }

    public ProcStatusEnum getProcStatus() {
        return ((CodelistMetamacBasicDto) getAttributeAsObject(CodelistDS.DTO)).getLifeCycle().getProcStatus();
    }

    public CodelistMetamacBasicDto getCodelistMetamacBasicDto() {
        return (CodelistMetamacBasicDto) getAttributeAsObject(CodelistDS.DTO);
    }
}

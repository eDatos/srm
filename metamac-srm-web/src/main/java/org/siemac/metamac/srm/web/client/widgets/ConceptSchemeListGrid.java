package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;

public class ConceptSchemeListGrid extends VersionableResourceListGrid {

    public ConceptSchemeListGrid() {
        setFields(ResourceFieldUtils.getConceptSchemeListGridFields());
    }

    public void setConceptSchemes(List<ConceptSchemeMetamacBasicDto> conceptSchemeDtos) {
        removeAllData();
        if (conceptSchemeDtos != null) {
            ConceptSchemeRecord[] schemeRecords = new ConceptSchemeRecord[conceptSchemeDtos.size()];
            for (int i = 0; i < conceptSchemeDtos.size(); i++) {
                schemeRecords[i] = RecordUtils.getConceptSchemeRecord(conceptSchemeDtos.get(i));
            }
            this.setData(schemeRecords);
        }
    }
}

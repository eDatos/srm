package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdRecordUtils;

public class DsdListGrid extends NavigableListGrid {

    public DsdListGrid() {
        super();
        setFields(ResourceFieldUtils.getDsdListGridFields());
    }

    public void setDsds(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDtos) {
        removeAllData();
        if (dataStructureDefinitionMetamacDtos != null) {
            DsdRecord[] dsdRecords = new DsdRecord[dataStructureDefinitionMetamacDtos.size()];
            for (int i = 0; i < dataStructureDefinitionMetamacDtos.size(); i++) {
                dsdRecords[i] = DsdRecordUtils.getDsdRecord(dataStructureDefinitionMetamacDtos.get(i));
            }
            setData(dsdRecords);
        }
    }
}

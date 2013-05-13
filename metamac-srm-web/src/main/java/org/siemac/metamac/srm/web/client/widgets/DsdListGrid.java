package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;

public class DsdListGrid extends VersionableResourceListGrid {

    public DsdListGrid() {
        super();
    }

    public void setDsds(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDtos) {
        removeAllData();
        if (dataStructureDefinitionMetamacDtos != null) {
            DsdRecord[] dsdRecords = new DsdRecord[dataStructureDefinitionMetamacDtos.size()];
            for (int i = 0; i < dataStructureDefinitionMetamacDtos.size(); i++) {
                dsdRecords[i] = RecordUtils.getDsdRecord(dataStructureDefinitionMetamacDtos.get(i));
            }
            setData(dsdRecords);
        }
    }
}

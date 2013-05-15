package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

public class DsdListGrid extends BaseCustomListGrid {

    public DsdListGrid() {
        super();
        setFields(ResourceFieldUtils.getDsdListGridFields());
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

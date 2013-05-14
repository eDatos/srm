package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.smartgwt.client.types.Autofit;

public class DsdPaginatedListGrid extends PaginatedCheckListGrid {

    public DsdPaginatedListGrid(int maxResults, PaginatedAction action) {
        super(maxResults, action);

        getListGrid().setAutoFitMaxRecords(DsdListPresenter.DSD_LIST_MAX_RESULTS);
        getListGrid().setAutoFitData(Autofit.VERTICAL);
        getListGrid().setShowAllRecords(true);

        getListGrid().setFields(ResourceFieldUtils.getDsdListGridFields());
    }

    public void setDsds(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDtos, int firstResult, int totalResults) {
        DsdRecord[] dsdRecords = new DsdRecord[dataStructureDefinitionMetamacDtos.size()];
        for (int i = 0; i < dataStructureDefinitionMetamacDtos.size(); i++) {
            dsdRecords[i] = RecordUtils.getDsdRecord(dataStructureDefinitionMetamacDtos.get(i));
        }
        // Populate the List Grid
        getListGrid().setData(dsdRecords);
        refreshPaginationInfo(firstResult, dataStructureDefinitionMetamacDtos.size(), totalResults);
    }
}

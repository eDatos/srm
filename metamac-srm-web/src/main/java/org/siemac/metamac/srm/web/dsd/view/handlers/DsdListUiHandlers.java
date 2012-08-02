package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdListUiHandlers extends UiHandlers {

    void retrieveDsdList(int firstResult, int maxResults, String dsd);
    void goToDsd(String urn);
    void saveDsd(DataStructureDefinitionDto dataStructureDefinitionDto);
    void deleteDsds(List<DataStructureDefinitionDto> dataStructureDefinitionDtos);

    void dsdSuccessfullyImported(String fileName);
    void dsdImportFailed(String fileName);

    void exportDsd(DataStructureDefinitionDto dsd);

}

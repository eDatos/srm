package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdListUiHandlers extends UiHandlers {

    void retrieveDsdList(int firstResult, int maxResults, String dsd);
    void goToDsd(String urn);
    void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto);
    void deleteDsds(List<String> urns);

    void dsdSuccessfullyImported(String fileName);
    void dsdImportFailed(String fileName);

    void exportDsd(DataStructureDefinitionMetamacDto dsd);

}

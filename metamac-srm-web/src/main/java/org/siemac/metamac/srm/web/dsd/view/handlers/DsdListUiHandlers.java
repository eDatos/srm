package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdListUiHandlers extends UiHandlers {

    void retrieveDsds();
    void goToDsd(String urn);
    void saveDsd(DataStructureDefinitionDto dataStructureDefinitionDto);
    void deleteDsd(DataStructureDefinitionDto dataStructureDefinitionDto);
    void deleteDsds(List<DataStructureDefinitionDto> dataStructureDefinitionDtos);

    void dsdSuccessfullyImported(String fileName);
    void dsdImportFailed(String fileName);

    void exportDsd(DataStructureDefinitionDto dsd);

}

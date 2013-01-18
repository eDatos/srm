package org.siemac.metamac.srm.web.client.code.view.handlers;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface BaseCodeUiHandlers extends UiHandlers {

    void saveCode(CodeMetamacDto codeDto);
    void deleteCode(ItemDto itemDto);
    void goToCode(String urn);

    void updateCodeInOrder(String codeUrn, String codelistOrderIdentifier, Long newCodeIndex);
    void updateCodeParent(String codeUrn, String newParentUrn, String codelistOrderIdentifier);
}

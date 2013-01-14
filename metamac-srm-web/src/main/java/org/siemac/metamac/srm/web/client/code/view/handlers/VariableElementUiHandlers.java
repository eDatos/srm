package org.siemac.metamac.srm.web.client.code.view.handlers;

import org.siemac.metamac.srm.core.code.dto.VariableElementDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface VariableElementUiHandlers extends UiHandlers {

    void saveVariableElement(VariableElementDto variableElementDto);
}

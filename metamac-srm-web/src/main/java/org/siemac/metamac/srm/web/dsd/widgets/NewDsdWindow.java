package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewDsdWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String FIELD_SAVE             = "save-dsd";

    private CustomDynamicForm   form;

    private DsdListUiHandlers   uiHandlers;

    public NewDsdWindow() {
        super(getConstants().dsdCreate());
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(DataStructureDefinitionDS.CODE, MetamacSrmWeb.getConstants().identifiableArtefactCode());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        codeItem.setValidators(SemanticIdentifiersUtils.getDsdIdentifierCustomValidator());

        RequiredTextItem nameItem = new RequiredTextItem(DataStructureDefinitionDS.NAME, MetamacSrmWeb.getConstants().nameableArtefactName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().dsdCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, nameItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public DataStructureDefinitionMetamacDto getNewDsd() {
        DataStructureDefinitionMetamacDto dsd = new DataStructureDefinitionMetamacDto();
        dsd.setCode(form.getValueAsString(DataStructureDefinitionDS.CODE));
        dsd.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(DataStructureDefinitionDS.NAME)));
        dsd.setMaintainer(RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto());
        dsd.setFinalLogic(false);
        dsd.setIsExternalReference(false);
        return dsd;
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public void setUiHandlers(DsdListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}

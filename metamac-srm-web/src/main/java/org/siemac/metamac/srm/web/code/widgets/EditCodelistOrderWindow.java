package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.code.model.ds.CodelistOrderDS;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.TranslateToolStripButton;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class EditCodelistOrderWindow extends CustomWindow {

    private static final String           FORM_ITEM_CUSTOM_WIDTH = FormItemUtils.FORM_ITEM_WIDTH;
    private static final String           FIELD_SAVE             = "save-order";

    private CustomDynamicForm             form;

    private CodelistOrderVisualisationDto codelistOrderVisualisationDto;

    public EditCodelistOrderWindow(String title) {
        super(title);
        setAutoSize(true);

        // ToolStrip
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth(getWidth());
        final TranslateToolStripButton translateToolStripButton = new TranslateToolStripButton();
        toolStrip.addButton(translateToolStripButton);
        translateToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                form.setTranslationsShowed(translateToolStripButton.isSelected());
            }
        });

        // Create form
        RequiredTextItem codeItem = new RequiredTextItem(CodelistOrderDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getCodelistOrderVisualisationIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        codeItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return canCodeBeEdited();
            }
        });

        ViewTextItem codeItemView = new ViewTextItem(CodelistOrderDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        codeItemView.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !canCodeBeEdited();
            }
        });

        MultiLanguageTextItem nameItem = new MultiLanguageTextItem(CodelistOrderDS.NAME, getConstants().nameableArtefactName(), FORM_ITEM_CUSTOM_WIDTH);
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        nameItem.setRequired(true);
        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());
        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, codeItemView, nameItem, saveItem);

        VLayout layout = new VLayout();
        layout.addMember(toolStrip);
        layout.addMember(form);
        layout.setMargin(10);

        addItem(layout);
        show();
    }

    public void setCodelistOrder(CodelistOrderVisualisationDto codelistOrderVisualisationDto) {
        this.codelistOrderVisualisationDto = codelistOrderVisualisationDto;
        form.setValue(CodelistOrderDS.CODE, codelistOrderVisualisationDto.getCode());
        form.setValue(CodelistOrderDS.CODE_VIEW, codelistOrderVisualisationDto.getCode());
        form.setValue(CodelistOrderDS.NAME, RecordUtils.getInternationalStringRecord(codelistOrderVisualisationDto.getName()));
        form.markForRedraw();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public CodelistOrderVisualisationDto getCodelistOrderDto() {
        codelistOrderVisualisationDto.setCode(form.getValueAsString(CodelistOrderDS.CODE));
        codelistOrderVisualisationDto.setName((InternationalStringDto) form.getValue(CodelistOrderDS.NAME));
        return codelistOrderVisualisationDto;
    }

    private boolean canCodeBeEdited() {
        // The code of an order can be edited only when the order is not the alphabetical one
        return codelistOrderVisualisationDto != null ? !SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(codelistOrderVisualisationDto.getCode()) : true;
    }
}

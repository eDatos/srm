package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.code.model.ds.CodelistVisualisationDS;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
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

public abstract class EditCodelistVisualisationWindow extends CustomWindow {

    private static final int           FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String        FIELD_SAVE             = "save-visualisation";

    private CustomDynamicForm          form;

    protected CodelistVisualisationDto codelistVisualisationDto;

    public EditCodelistVisualisationWindow(String title) {
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
        RequiredTextItem codeItem = new RequiredTextItem(CodelistVisualisationDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getCodelistVisualisationIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        codeItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return canCodeBeEdited();
            }
        });

        ViewTextItem codeItemView = new ViewTextItem(CodelistVisualisationDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        codeItemView.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !canCodeBeEdited();
            }
        });

        MultiLanguageTextItem nameItem = new MultiLanguageTextItem(CodelistVisualisationDS.NAME, getConstants().nameableArtefactName(), String.valueOf(FORM_ITEM_CUSTOM_WIDTH - 28));
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH - 28);
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

    public void setCodelistVisualisation(CodelistVisualisationDto codelistVisualisationDto) {
        this.codelistVisualisationDto = codelistVisualisationDto;
        form.setValue(CodelistVisualisationDS.CODE, codelistVisualisationDto.getCode());
        form.setValue(CodelistVisualisationDS.CODE_VIEW, codelistVisualisationDto.getCode());
        form.setValue(CodelistVisualisationDS.NAME, RecordUtils.getInternationalStringRecord(codelistVisualisationDto.getName()));
        form.markForRedraw();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public CodelistVisualisationDto getCodelistVisualisationDto() {
        codelistVisualisationDto.setCode(form.getValueAsString(CodelistVisualisationDS.CODE));
        codelistVisualisationDto.setName((InternationalStringDto) form.getValue(CodelistVisualisationDS.NAME));
        return codelistVisualisationDto;
    }

    protected abstract boolean canCodeBeEdited();
}

package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.code.view.handlers.BaseVariableUiHandlers;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;

public class NewVariableElementWindow extends CustomWindow {

    private static final int       FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String    FIELD_SAVE             = "save-ele";

    private CustomDynamicForm      form;

    private BaseVariableUiHandlers uiHandlers;

    private VariableDto            variableDto;

    public NewVariableElementWindow(String title, BaseVariableUiHandlers uiHandlers, final VariableDto variableDto) {
        super(title);
        this.uiHandlers = uiHandlers;
        this.variableDto = variableDto;

        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getVariableElementIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem shortNameItem = new RequiredTextItem(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName());
        shortNameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();
        lengthRangeValidator.setMin(0);
        lengthRangeValidator.setMax(SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH);
        shortNameItem.setValidators(lengthRangeValidator);

        SearchCodeForVariableElementGeographicalGranularity geographicalGranularity = createGeographicalGranularityItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY, getConstants()
                .variableElementGeographicalGranularity());
        geographicalGranularity.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return VariableTypeEnum.GEOGRAPHICAL.equals(variableDto.getType());
            }
        });

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().variableElementCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, shortNameItem, geographicalGranularity, saveItem);

        addItem(form);
        show();
    }

    private SearchCodeForVariableElementGeographicalGranularity createGeographicalGranularityItem(final String name, String title) {
        final SearchCodeForVariableElementGeographicalGranularity item = new SearchCodeForVariableElementGeographicalGranularity(name, title, getCustomLinkItemNavigationClickHandler());
        item.setUiHandlers(getUiHandlers());
        item.setSaveClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                RelatedResourceDto code = item.getSelectedItem();
                item.markSearchWindowForDestroy();
                item.setRelatedResource(code);
                form.validate(false);
            }
        });
        return item;
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public VariableElementDto getNewVariableElementDto() {
        VariableElementDto variableElementDto = new VariableElementDto();
        variableElementDto.setCode(form.getValueAsString(VariableElementDS.CODE));
        variableElementDto.setVariable(RelatedResourceUtils.createVariableRelatedResourceDto(variableDto.getUrn()));
        variableElementDto.setShortName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(VariableElementDS.SHORT_NAME)));
        if (form.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY).isVisible()) {
            variableElementDto.setGeographicalGranularity(((SearchCodeForVariableElementGeographicalGranularity) form.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).getRelatedResourceDto());
        }
        return variableElementDto;
    }

    public BaseVariableUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setItemSchemes(GetRelatedResourcesResult result) {
        ((SearchCodeForVariableElementGeographicalGranularity) form.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).setItemSchemes(result.getRelatedResourceDtos(), result.getFirstResultOut(),
                result.getTotalResults());
    }

    public void setItems(GetRelatedResourcesResult result) {
        ((SearchCodeForVariableElementGeographicalGranularity) form.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).setItems(result.getRelatedResourceDtos(), result.getFirstResultOut(),
                result.getTotalResults());
    }

    // ------------------------------------------------------------------------------------------------------------
    // CLICK HANDLERS
    // ------------------------------------------------------------------------------------------------------------

    private CustomLinkItemNavigationClickHandler getCustomLinkItemNavigationClickHandler() {
        return new CustomLinkItemNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }
}

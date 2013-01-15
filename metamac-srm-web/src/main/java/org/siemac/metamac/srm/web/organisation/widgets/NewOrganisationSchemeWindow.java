package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.MaintainerUtils;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewOrganisationSchemeWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String FIELD_SAVE             = "save-sch";

    private CustomDynamicForm   form;

    public NewOrganisationSchemeWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredSelectItem typeItem = new RequiredSelectItem(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());
        typeItem.setValueMap(CommonUtils.getOrganisationSchemeTypeHashMap());
        typeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        typeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                form.markForRedraw();
            }
        });

        RequiredTextItem codeItem = new RequiredTextItem(OrganisationSchemeDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        codeItem.setShowIfCondition(getCodeShowIfFunction());

        RequiredTextItem nameItem = new RequiredTextItem(OrganisationSchemeDS.NAME, getConstants().nameableArtefactName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().organisationSchemeCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(typeItem, codeItem, nameItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public OrganisationSchemeMetamacDto getNewOrganisationSchemeDto() {
        OrganisationSchemeMetamacDto organisationSchemeDto = new OrganisationSchemeMetamacDto();

        // TODO agency
        RelatedResourceDto maintainer = RelatedResourceUtils.createMaintainerAsRelatedResourceDto("SDMX_AGENCY", MaintainerUtils.getCurrentMaintainer());
        organisationSchemeDto.setMaintainer(maintainer);

        organisationSchemeDto.setType(OrganisationSchemeTypeEnum.valueOf(form.getValueAsString(OrganisationSchemeDS.TYPE)));
        organisationSchemeDto.setCode(form.getItem(OrganisationSchemeDS.CODE).isVisible() ? form.getValueAsString(OrganisationSchemeDS.CODE) : null);
        organisationSchemeDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(OrganisationSchemeDS.NAME)));
        return organisationSchemeDto;
    }

    private FormItemIfFunction getCodeShowIfFunction() {
        FormItemIfFunction function = new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                // The code of AgencySchemes, DataConsumerSchemes and DataProviderSchemes is fixed
                String type = form.getValueAsString(OrganisationSchemeDS.TYPE);
                if (OrganisationSchemeTypeEnum.AGENCY_SCHEME.getName().equals(type) || OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME.getName().equals(type)
                        || OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME.getName().equals(type)) {
                    return false;
                }
                return true;
            }
        };
        return function;
    }
}

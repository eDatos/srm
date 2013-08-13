package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.organisation.model.ds.ContactDS;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationContactWebCriteria;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class OrganisationContactSearchSectionStack extends BaseAdvancedSearchSectionStack {

    private OrganisationUiHandlers uiHandlers;
    private OrganisationMetamacDto organisationMetamacDto;

    public OrganisationContactSearchSectionStack() {
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);

        TextItem name = new TextItem(ContactDS.NAME, getConstants().organisationContactName());
        name.setWidth("*");
        TextItem organisationUnit = new TextItem(ContactDS.ORGANISATION_UNIT, getConstants().organisationContactOrganisationUnit());
        organisationUnit.setWidth("*");
        TextItem responsibility = new TextItem(ContactDS.RESPONSIBILITY, getConstants().organisationContactResponsibility());
        responsibility.setWidth("*");
        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(2);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });

        FormItem[] advancedSearchFormItems = new FormItem[]{name, organisationUnit, responsibility, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    public OrganisationContactWebCriteria getOrganisationWebCriteria() {
        OrganisationContactWebCriteria contactWebCriteria = new OrganisationContactWebCriteria();
        contactWebCriteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        contactWebCriteria.setName(advancedSearchForm.getValueAsString(ContactDS.NAME));
        contactWebCriteria.setOrganisationUrn(organisationMetamacDto.getUrn());
        contactWebCriteria.setOrganisationUnit(advancedSearchForm.getValueAsString(ContactDS.ORGANISATION_UNIT));
        contactWebCriteria.setResponsibility(advancedSearchForm.getValueAsString(ContactDS.RESPONSIBILITY));
        return contactWebCriteria;
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveContacts(getOrganisationWebCriteria());
    }

    public void setUiHandlers(OrganisationUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public OrganisationUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setOrganisationMetamacDto(OrganisationMetamacDto organisationMetamacDto) {
        this.organisationMetamacDto = organisationMetamacDto;
    }
}

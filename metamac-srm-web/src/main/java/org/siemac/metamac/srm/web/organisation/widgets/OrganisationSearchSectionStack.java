package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.widgets.ItemSearchSectionStack;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationsPresenter;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationsUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class OrganisationSearchSectionStack extends ItemSearchSectionStack {

    private OrganisationsUiHandlers uiHandlers;

    public OrganisationSearchSectionStack() {
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveOrganisations(OrganisationsPresenter.ITEM_LIST_FIRST_RESULT, OrganisationsPresenter.ITEM_LIST_MAX_RESULTS, getOrganisationWebCriteria());
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {
        SelectItem organisationType = new SelectItem(OrganisationDS.TYPE, getConstants().organisationType());
        organisationType.setValueMap(CommonUtils.getOrganisationTypeHashMap());

        // Add replaceTo and accessType items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] organisationFields = new FormItem[advancedSearchFormItems.length + 1];
        System.arraycopy(advancedSearchFormItems, 0, organisationFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, organisationFields, organisationFields.length - 1, 1);
        organisationFields[organisationFields.length - 2] = organisationType;
        advancedSearchForm.setFields(organisationFields);
    }

    public OrganisationWebCriteria getOrganisationWebCriteria() {
        OrganisationWebCriteria organisationWebCriteria = (OrganisationWebCriteria) getItemWebCriteria(new OrganisationWebCriteria());

        OrganisationTypeEnum selectedOrganisationType = !StringUtils.isBlank(advancedSearchForm.getValueAsString(OrganisationDS.TYPE)) ? OrganisationTypeEnum.valueOf(advancedSearchForm
                .getValueAsString(OrganisationDS.TYPE)) : null;
        if (selectedOrganisationType != null) {
            organisationWebCriteria.setOrganisationSchemeType(CommonUtils.getOrganisationSchemeTypeEnum(selectedOrganisationType));
        }

        return organisationWebCriteria;
    }

    public void setUiHandlers(OrganisationsUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public OrganisationsUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}

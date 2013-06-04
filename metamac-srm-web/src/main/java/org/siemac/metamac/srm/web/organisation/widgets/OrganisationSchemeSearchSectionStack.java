package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeListUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationSchemeWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class OrganisationSchemeSearchSectionStack extends VersionableResourceSearchSectionStack {

    private OrganisationSchemeListUiHandlers uiHandlers;

    public OrganisationSchemeSearchSectionStack() {
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {
        SelectItem type = new SelectItem(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());
        type.setValueMap(CommonUtils.getOrganisationSchemeTypeHashMap());

        // Add type item to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] organisationSchemeFields = new FormItem[advancedSearchFormItems.length + 1];
        System.arraycopy(advancedSearchFormItems, 0, organisationSchemeFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, organisationSchemeFields, organisationSchemeFields.length - 1, 1);
        organisationSchemeFields[organisationSchemeFields.length - 2] = type;
        advancedSearchForm.setFields(organisationSchemeFields);
    }

    public OrganisationSchemeWebCriteria getOrganisationSchemeWebCriteria() {
        OrganisationSchemeWebCriteria organisationSchemeWebCriteria = (OrganisationSchemeWebCriteria) getVersionableResourceWebCriteria(new OrganisationSchemeWebCriteria());
        organisationSchemeWebCriteria.setType(!StringUtils.isBlank(advancedSearchForm.getValueAsString(OrganisationSchemeDS.TYPE)) ? OrganisationSchemeTypeEnum.valueOf(advancedSearchForm
                .getValueAsString(OrganisationSchemeDS.TYPE)) : null);
        return organisationSchemeWebCriteria;
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveOrganisationSchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS, getOrganisationSchemeWebCriteria());
    }

    public void setUiHandlers(OrganisationSchemeListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public OrganisationSchemeListUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}

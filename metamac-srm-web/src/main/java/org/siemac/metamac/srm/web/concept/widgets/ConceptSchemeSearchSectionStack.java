package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.widgets.SearchStatisticalOperationLinkItem;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class ConceptSchemeSearchSectionStack extends VersionableResourceSearchSectionStack {

    private ConceptSchemeListUiHandlers        uiHandlers;

    private SearchStatisticalOperationLinkItem statisticalOperationItem;

    public ConceptSchemeSearchSectionStack() {
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {

        // Statistical operation
        statisticalOperationItem = createStatisticalOperationItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());

        // Type
        SelectItem conceptSchemeType = new SelectItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        conceptSchemeType.setValueMap(CommonUtils.getConceptSchemeTypeHashMap());

        // Add replaceTo and accessType items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] conceptFields = new FormItem[advancedSearchFormItems.length + 2];
        System.arraycopy(advancedSearchFormItems, 0, conceptFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, conceptFields, conceptFields.length - 1, 1);
        conceptFields[conceptFields.length - 3] = statisticalOperationItem;
        conceptFields[conceptFields.length - 2] = conceptSchemeType;
        advancedSearchForm.setFields(conceptFields);
    }

    public ConceptSchemeWebCriteria getConceptSchemeWebCriteria() {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) getVersionableResourceWebCriteria(new ConceptSchemeWebCriteria());
        ExternalItemDto statisticalOperation = ((SearchExternalItemLinkItem) advancedSearchForm.getItem(ConceptSchemeDS.RELATED_OPERATION)).getExternalItemDto();
        conceptSchemeWebCriteria.setStatisticalOperationUrn(statisticalOperation != null ? statisticalOperation.getUrn() : null);
        conceptSchemeWebCriteria.setConceptSchemeTypeEnum(!StringUtils.isBlank(advancedSearchForm.getValueAsString(ConceptSchemeDS.TYPE)) ? ConceptSchemeTypeEnum.valueOf(advancedSearchForm
                .getValueAsString(ConceptSchemeDS.TYPE)) : null);
        return conceptSchemeWebCriteria;
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveConceptSchemes(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS, getConceptSchemeWebCriteria());
    }

    public void setOperations(GetStatisticalOperationsResult result) {
        statisticalOperationItem.setOperations(result.getOperations(), result.getFirstResultOut(), result.getTotalResults());
    }

    public void setUiHandlers(ConceptSchemeListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public ConceptSchemeListUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private SearchStatisticalOperationLinkItem createStatisticalOperationItem(String name, String title) {
        SearchStatisticalOperationLinkItem operationItem = new SearchStatisticalOperationLinkItem(name, title) {

            @Override
            protected void retrieveStatisticalOperations(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveStatisticalOperationsForSearchSection(firstResult, maxResults, webCriteria != null ? webCriteria.getCriteria() : null);
            }
        };
        return operationItem;
    }

}

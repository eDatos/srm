package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.widgets.ItemSearchSectionStack;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptsUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class ConceptSearchSectionStack extends ItemSearchSectionStack {

    private ConceptsUiHandlers uiHandlers;

    public ConceptSearchSectionStack() {
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveConcepts(SrmWebConstants.ITEM_LIST_FIRST_RESULT, SrmWebConstants.ITEM_LIST_MAX_RESULTS, getConceptWebCriteria());
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {
        TextItem acronym = new TextItem(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        TextItem descriptionSource = new TextItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());
        SelectItem conceptSchemeType = new SelectItem(ConceptDS.CONCEPT_SCHEME_TYPE, getConstants().conceptConceptSchemeType());
        conceptSchemeType.setValueMap(CommonUtils.getConceptSchemeTypeHashMap());

        // Add replaceTo and accessType items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] conceptFields = new FormItem[advancedSearchFormItems.length + 3];
        System.arraycopy(advancedSearchFormItems, 0, conceptFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, conceptFields, conceptFields.length - 1, 1);
        conceptFields[conceptFields.length - 4] = acronym;
        conceptFields[conceptFields.length - 3] = descriptionSource;
        conceptFields[conceptFields.length - 2] = conceptSchemeType;
        advancedSearchForm.setFields(conceptFields);
    }

    public ConceptWebCriteria getConceptWebCriteria() {
        ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) getItemWebCriteria(new ConceptWebCriteria());
        conceptWebCriteria.setAcronym(advancedSearchForm.getValueAsString(ConceptDS.ACRONYM));
        conceptWebCriteria.setDescriptionSource(advancedSearchForm.getValueAsString(ConceptDS.DESCRIPTION_SOURCE));
        conceptWebCriteria.setConceptSchemeTypeEnum(!StringUtils.isBlank(advancedSearchForm.getValueAsString(ConceptDS.CONCEPT_SCHEME_TYPE)) ? ConceptSchemeTypeEnum.valueOf(advancedSearchForm
                .getValueAsString(ConceptDS.CONCEPT_SCHEME_TYPE)) : null);
        return conceptWebCriteria;
    }

    public void setUiHandlers(ConceptsUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public ConceptsUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}

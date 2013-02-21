package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.widgets.ItemSearchSectionStack;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.presenter.ConceptsPresenter;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptsUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class ConceptSearchSectionStack extends ItemSearchSectionStack {

    private ConceptsUiHandlers uiHandlers;

    public ConceptSearchSectionStack() {
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveConcepts(ConceptsPresenter.ITEM_LIST_FIRST_RESULT, ConceptsPresenter.ITEM_LIST_MAX_RESULTS, getConceptWebCriteria());
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {
        TextItem acronym = new TextItem(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        TextItem descriptionSource = new TextItem(ConceptDS.DESCRIPTION_SOURCE, getConstants().conceptDescriptionSource());

        // Add replaceTo and accessType items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] conceptFields = new FormItem[advancedSearchFormItems.length + 2];
        System.arraycopy(advancedSearchFormItems, 0, conceptFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, conceptFields, conceptFields.length - 1, 1);
        conceptFields[conceptFields.length - 3] = acronym;
        conceptFields[conceptFields.length - 2] = descriptionSource;
        advancedSearchForm.setFields(conceptFields);
    }

    public ConceptWebCriteria getConceptWebCriteria() {
        ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) getItemWebCriteria(new ConceptWebCriteria());
        conceptWebCriteria.setAcronym(advancedSearchForm.getValueAsString(ConceptDS.ACRONYM));
        conceptWebCriteria.setDescriptionSource(advancedSearchForm.getValueAsString(ConceptDS.DESCRIPTION_SOURCE));
        return conceptWebCriteria;
    }

    public void setUiHandlers(ConceptsUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public ConceptsUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}

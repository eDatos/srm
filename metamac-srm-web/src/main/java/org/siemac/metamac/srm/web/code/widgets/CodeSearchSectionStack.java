package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.widgets.ItemSearchSectionStack;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.presenter.CodesPresenter;
import org.siemac.metamac.srm.web.code.view.handlers.CodesUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class CodeSearchSectionStack extends ItemSearchSectionStack {

    private CodesUiHandlers uiHandlers;

    public CodeSearchSectionStack() {
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveCodes(CodesPresenter.ITEM_LIST_FIRST_RESULT, CodesPresenter.ITEM_LIST_MAX_RESULTS, getCodeWebCriteria());
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {
        TextItem shortName = new TextItem(CodeDS.SHORT_NAME, getConstants().codeShortName());

        // Add replaceTo and accessType items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] codeFields = new FormItem[advancedSearchFormItems.length + 1];
        System.arraycopy(advancedSearchFormItems, 0, codeFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, codeFields, codeFields.length - 1, 1);
        codeFields[codeFields.length - 2] = shortName;
        advancedSearchForm.setFields(codeFields);
    }

    public CodeWebCriteria getCodeWebCriteria() {
        CodeWebCriteria codeWebCriteria = (CodeWebCriteria) getItemWebCriteria(new CodeWebCriteria());
        codeWebCriteria.setShortName(advancedSearchForm.getValueAsString(CodeDS.SHORT_NAME));
        return codeWebCriteria;
    }

    public void setUiHandlers(CodesUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CodesUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}

package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.presenter.CodelistListPresenter;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistListUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class CodelistSearchSectionStack extends VersionableResourceSearchSectionStack {

    private CodelistListUiHandlers uiHandlers;

    public CodelistSearchSectionStack() {
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {
        SelectItem accessType = new SelectItem(CodelistDS.ACCESS_TYPE, getConstants().codelistAccessType());
        accessType.setValueMap(CommonUtils.getAccessTypeHashMap());

        // Add replaceTo and accessType items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] codelistFields = new FormItem[advancedSearchFormItems.length + 1];
        System.arraycopy(advancedSearchFormItems, 0, codelistFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, codelistFields, codelistFields.length - 1, 1);
        codelistFields[codelistFields.length - 2] = accessType;
        advancedSearchForm.setFields(codelistFields);
    }

    public CodelistWebCriteria getCodelistWebCriteria() {
        CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) getVersionableResourceWebCriteria(new CodelistWebCriteria());
        codelistWebCriteria.setAccessType(!StringUtils.isBlank(advancedSearchForm.getValueAsString(CodelistDS.ACCESS_TYPE)) ? AccessTypeEnum.valueOf(advancedSearchForm
                .getValueAsString(CodelistDS.ACCESS_TYPE)) : null);
        return codelistWebCriteria;
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveCodelists(CodelistListPresenter.SCHEME_LIST_FIRST_RESULT, CodelistListPresenter.SCHEME_LIST_MAX_RESULTS, getCodelistWebCriteria());
    }

    public void setUiHandlers(CodelistListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CodelistListUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}

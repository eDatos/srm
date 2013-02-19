package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
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
        RelatedResourceListItem replaceTo = createReplaceToItem(CodelistDS.REPLACE_TO_CODELISTS, getConstants().codelistReplaceToCodelists());
        replaceTo.setColSpan(2);

        // Add replaceTo and accessType items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] codelistFields = new FormItem[advancedSearchFormItems.length + 2];
        System.arraycopy(advancedSearchFormItems, 0, codelistFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, codelistFields, codelistFields.length - 1, 1);
        codelistFields[codelistFields.length - 3] = accessType;
        codelistFields[codelistFields.length - 2] = replaceTo;
        advancedSearchForm.setFields(codelistFields);
    }

    public CodelistWebCriteria getCodelistWebCriteria() {
        CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) getVersionableResourceWebCriteria(new CodelistWebCriteria());
        codelistWebCriteria.setAccessType(!StringUtils.isBlank(advancedSearchForm.getValueAsString(CodelistDS.ACCESS_TYPE)) ? AccessTypeEnum.valueOf(advancedSearchForm
                .getValueAsString(CodelistDS.ACCESS_TYPE)) : null);
        codelistWebCriteria.setReplaceToCodelistUrns(((RelatedResourceListItem) advancedSearchForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).getSelectedRelatedResourceUrns());
        return codelistWebCriteria;
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveCodelists(CodelistListPresenter.SCHEME_LIST_FIRST_RESULT, CodelistListPresenter.SCHEME_LIST_MAX_RESULTS, getCodelistWebCriteria());
    }

    @Override
    protected void clearAdvancedSearchSection() {
        super.clearAdvancedSearchSection();
        ((RelatedResourceListItem) advancedSearchForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).clearRelatedResourceList();
    }

    public void setUiHandlers(CodelistListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CodelistListUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private RelatedResourceListItem createReplaceToItem(String name, String title) {
        RelatedResourceListItem replaceTo = new RelatedResourceListItem(name, title, true);
        // TODO
        return replaceTo;
    }
}

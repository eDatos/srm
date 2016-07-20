package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.code.model.ds.VariableDS;
import org.siemac.metamac.srm.web.code.view.handlers.VariableListUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class VariableSearchSectionStack extends BaseAdvancedSearchSectionStack {

    private VariableListUiHandlers uiHandlers;

    public VariableSearchSectionStack() {
    }

    @Override
    protected void clearAdvancedSearchSection() {
        super.clearAdvancedSearchSection();
        ((CustomCheckboxItem) advancedSearchForm.getItem(VariableDS.IS_GEOGRAPHICAL)).setValue(false);
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);
        CustomCheckboxItem isGeographical = new CustomCheckboxItem(VariableDS.IS_GEOGRAPHICAL, getConstants().variableIsGeographical());
        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });

        FormItem[] advancedSearchFormItems = new FormItem[]{isGeographical, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveVariables(SrmWebConstants.ITEM_LIST_FIRST_RESULT, SrmWebConstants.ITEM_LIST_MAX_RESULTS, getVariableWebCriteria());
    }

    public VariableWebCriteria getVariableWebCriteria() {
        VariableWebCriteria variableWebCriteria = new VariableWebCriteria();
        variableWebCriteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        boolean isGeographical = ((CustomCheckboxItem) advancedSearchForm.getItem(VariableDS.IS_GEOGRAPHICAL)).getValueAsBoolean();
        variableWebCriteria.setVariableType(isGeographical ? VariableTypeEnum.GEOGRAPHICAL : null);
        return variableWebCriteria;
    }

    public void setUiHandlers(VariableListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public VariableListUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}

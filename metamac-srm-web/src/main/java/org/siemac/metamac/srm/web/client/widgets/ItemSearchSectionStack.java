package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.shared.criteria.ItemWebCriteria;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public abstract class ItemSearchSectionStack extends BaseAdvancedSearchSectionStack {

    public ItemSearchSectionStack() {
    }

    @Override
    protected void clearAdvancedSearchSection() {
        super.clearAdvancedSearchSection();
        // Search last versions by default
        ((BooleanSelectItem) advancedSearchForm.getItem(ItemDS.IS_LAST_VERSION)).setBooleanValue(true);
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisibility(Visibility.HIDDEN);
        TextItem code = new TextItem(ItemDS.CODE, getConstants().identifiableArtefactCode());
        TextItem name = new TextItem(ItemDS.NAME, getConstants().nameableArtefactName());
        TextItem urn = new TextItem(ItemDS.URN, getConstants().identifiableArtefactUrn());
        TextItem description = new TextItem(ItemDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        BooleanSelectItem isLastVersion = new BooleanSelectItem(ItemDS.IS_LAST_VERSION, getConstants().maintainableArtefactIsLastVersion());
        isLastVersion.setBooleanValue(true);
        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });

        FormItem[] advancedSearchFormItems = new FormItem[]{code, name, urn, description, isLastVersion, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    public ItemWebCriteria getItemWebCriteria(ItemWebCriteria itemWebCriteria) {
        itemWebCriteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        itemWebCriteria.setCode(advancedSearchForm.getValueAsString(ItemDS.CODE));
        itemWebCriteria.setName(advancedSearchForm.getValueAsString(ItemDS.NAME));
        itemWebCriteria.setUrn(advancedSearchForm.getValueAsString(ItemDS.URN));
        itemWebCriteria.setDescription(advancedSearchForm.getValueAsString(ItemDS.DESCRIPTION));
        itemWebCriteria.setIsLastVersion(((BooleanSelectItem) advancedSearchForm.getItem(ItemDS.IS_LAST_VERSION)).getBooleanValue());
        return itemWebCriteria;
    }
}

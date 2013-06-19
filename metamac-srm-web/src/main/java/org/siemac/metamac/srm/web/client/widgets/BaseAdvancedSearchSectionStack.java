package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.constants.CommonWebConstants;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.BaseSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public abstract class BaseAdvancedSearchSectionStack extends BaseSearchSectionStack {

    protected static String     SEARCH_ITEM_NAME               = "search-item";
    protected static String     ADVANCED_SEARCH_ITEM_NAME      = "adv-search-item";
    protected static String     HIDE_ADVANCED_SEARCH_ITEM_NAME = "hide-adv-search-item";

    protected CustomDynamicForm searchForm;
    protected FormItemIcon      searchIcon;

    protected GroupDynamicForm  advancedSearchForm;

    public BaseAdvancedSearchSectionStack() {
        setHeight(26);

        createSearchForm();
        createAdvancedSearchForm();

        HLayout hLayout = new HLayout();
        hLayout.addMember(searchForm);
        hLayout.addMember(advancedSearchForm);

        section.setItems(hLayout);
    }

    public void clearSearchSection() {
        searchForm.clearValues();
        hideAdvancedSearchSection();
    }

    protected void clearAdvancedSearchSection() {
        advancedSearchForm.clearValues();
        advancedSearchForm.hide();
    }

    protected void showAdvancedSearchSection() {
        advancedSearchForm.show();
        searchForm.getItem(SEARCH_ITEM_NAME).setShowIcons(false); // Hide search icon in search section when advanced search section is shown
        searchForm.markForRedraw();
    }

    protected void hideAdvancedSearchSection() {
        clearAdvancedSearchSection();
        searchForm.getItem(SEARCH_ITEM_NAME).setShowIcons(true);
        searchForm.markForRedraw();
    }

    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {
        advancedSearchForm.setFields(advancedSearchFormItems);
    }

    protected void createSearchForm() {
        searchForm = new CustomDynamicForm();
        searchForm.setMargin(5);
        searchForm.setAutoWidth();
        searchForm.setNumCols(4);

        CustomTextItem searchItem = new CustomTextItem(SEARCH_ITEM_NAME, "");
        searchItem.setShowTitle(false);
        searchItem.setWidth(300);
        searchItem.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (StringUtils.equalsIgnoreCase(event.getKeyName(), CommonWebConstants.ENTER_KEY)) {
                    retrieveResources();
                }
            }
        });

        searchIcon = new FormItemIcon();
        searchIcon.setSrc(GlobalResources.RESOURCE.search().getURL());
        searchIcon.addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                retrieveResources();
            }
        });
        searchItem.setIcons(searchIcon);

        // Show advanced section item
        LinkItem advancedSearchItem = new LinkItem(ADVANCED_SEARCH_ITEM_NAME);
        advancedSearchItem.setShowTitle(false);
        advancedSearchItem.setLinkTitle(MetamacWebCommon.getConstants().advancedSearch());
        advancedSearchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showAdvancedSearchSection();
            }
        });
        advancedSearchItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !advancedSearchForm.isVisible();
            }
        });

        // Hide advanced section item
        LinkItem hideAdvancedSearchItem = new LinkItem(HIDE_ADVANCED_SEARCH_ITEM_NAME);
        hideAdvancedSearchItem.setShowTitle(false);
        hideAdvancedSearchItem.setLinkTitle(MetamacWebCommon.getConstants().actionHideAdvancedSearch());
        hideAdvancedSearchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hideAdvancedSearchSection();
            }
        });
        hideAdvancedSearchItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return advancedSearchForm.isVisible();
            }
        });

        searchForm.setFields(searchItem, advancedSearchItem, hideAdvancedSearchItem);
    }

    public GroupDynamicForm getAdvancedSearchForm() {
        return advancedSearchForm;
    }

    protected abstract void createAdvancedSearchForm();
    protected abstract void retrieveResources();
}

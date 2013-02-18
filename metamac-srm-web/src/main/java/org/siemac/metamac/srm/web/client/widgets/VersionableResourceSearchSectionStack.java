package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.Date;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.shared.criteria.VersionableResourceWebCriteria;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.BaseSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;

public abstract class VersionableResourceSearchSectionStack extends BaseSearchSectionStack {

    protected static String     SEARCH_ITEM_NAME               = "search-item";
    protected static String     ADVANCED_SEARCH_ITEM_NAME      = "adv-search-item";
    protected static String     HIDE_ADVANCED_SEARCH_ITEM_NAME = "hide-adv-search-item";

    protected CustomDynamicForm searchForm;
    protected GroupDynamicForm  advancedSearchForm;
    protected FormItemIcon      searchIcon;

    public VersionableResourceSearchSectionStack() {
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
        clearAdvancedSearchSection();
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

    public VersionableResourceWebCriteria getVersionableResourceWebCriteria(VersionableResourceWebCriteria versionableResourceWebCriteria) {
        versionableResourceWebCriteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        versionableResourceWebCriteria.setCode(advancedSearchForm.getValueAsString(VersionableResourceDS.CODE));
        versionableResourceWebCriteria.setName(advancedSearchForm.getValueAsString(VersionableResourceDS.NAME));
        versionableResourceWebCriteria.setUrn(advancedSearchForm.getValueAsString(VersionableResourceDS.URN));
        versionableResourceWebCriteria.setDescription(advancedSearchForm.getValueAsString(VersionableResourceDS.DESCRIPTION));
        versionableResourceWebCriteria.setProcStatus(!StringUtils.isBlank(advancedSearchForm.getValueAsString(VersionableResourceDS.PROC_STATUS)) ? ProcStatusEnum.valueOf(advancedSearchForm
                .getValueAsString(VersionableResourceDS.PROC_STATUS)) : null);
        versionableResourceWebCriteria.setInternalPublicationDate(advancedSearchForm.getValue(VersionableResourceDS.INTERNAL_PUBLICATION_DATE) != null ? (Date) advancedSearchForm
                .getValue(VersionableResourceDS.INTERNAL_PUBLICATION_DATE) : null);
        versionableResourceWebCriteria.setInternalPublicationUser(advancedSearchForm.getValueAsString(VersionableResourceDS.INTERNAL_PUBLICATION_USER));
        versionableResourceWebCriteria.setExternalPublicationDate(advancedSearchForm.getValue(VersionableResourceDS.EXTERNAL_PUBLICATION_DATE) != null ? (Date) advancedSearchForm
                .getValue(VersionableResourceDS.EXTERNAL_PUBLICATION_DATE) : null);
        versionableResourceWebCriteria.setExternalPublicationUser(advancedSearchForm.getValueAsString(VersionableResourceDS.EXTERNAL_PUBLICATION_USER));
        versionableResourceWebCriteria.setIsLastVersion(advancedSearchForm.getValue(VersionableResourceDS.IS_LAST_VERSION) != null ? (Boolean) advancedSearchForm
                .getValue(VersionableResourceDS.IS_LAST_VERSION) : null);
        return versionableResourceWebCriteria;
    }

    public abstract void retrieveResources();

    private void createSearchForm() {
        searchForm = new CustomDynamicForm();
        searchForm.setMargin(5);
        searchForm.setAutoWidth();
        searchForm.setNumCols(4);

        CustomTextItem searchItem = new CustomTextItem(SEARCH_ITEM_NAME, "");
        searchItem.setShowTitle(false);
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

    private void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisibility(Visibility.HIDDEN);
        TextItem code = new TextItem(VersionableResourceDS.CODE, getConstants().identifiableArtefactCode());
        TextItem name = new TextItem(VersionableResourceDS.NAME, getConstants().nameableArtefactName());
        TextItem urn = new TextItem(VersionableResourceDS.URN, getConstants().identifiableArtefactUrn());
        TextItem description = new TextItem(VersionableResourceDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        SelectItem procStatus = new SelectItem(VersionableResourceDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        procStatus.setValueMap(CommonUtils.getProcStatusHashMap());
        CustomDateItem internalPublicationDate = new CustomDateItem(VersionableResourceDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        TextItem internalPublicationUser = new TextItem(VersionableResourceDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        CustomDateItem externalPublicationDate = new CustomDateItem(VersionableResourceDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        TextItem externalPublicationUser = new TextItem(VersionableResourceDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        CustomCheckboxItem isLastVersion = new CustomCheckboxItem(VersionableResourceDS.IS_LAST_VERSION, getConstants().maintainableArtefactIsLastVersion());
        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });
        advancedSearchForm.setFields(code, internalPublicationDate, name, internalPublicationUser, urn, externalPublicationDate, description, externalPublicationUser, procStatus, isLastVersion,
                searchItem);
    }
}

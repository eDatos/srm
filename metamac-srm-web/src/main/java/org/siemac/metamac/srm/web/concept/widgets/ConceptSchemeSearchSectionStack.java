package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.Date;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
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

public class ConceptSchemeSearchSectionStack extends BaseSearchSectionStack {

    private static String               SEARCH_ITEM_NAME               = "search-item";
    private static String               ADVANCED_SEARCH_ITEM_NAME      = "adv-search-item";
    private static String               HIDE_ADVANCED_SEARCH_ITEM_NAME = "hide-adv-search-item";

    private ConceptSchemeListUiHandlers uiHandlers;

    private CustomDynamicForm           searchForm;
    private GroupDynamicForm            advancedSearchForm;
    private FormItemIcon                searchIcon;

    public ConceptSchemeSearchSectionStack() {
        setHeight(26);

        createSearchForm();
        createAdvancedSearchForm();

        HLayout hLayout = new HLayout();
        hLayout.addMember(searchForm);
        hLayout.addMember(advancedSearchForm);

        section.setItems(hLayout);
    }

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
                getUiHandlers().retrieveConceptSchemes(ConceptSchemeListPresenter.SCHEME_LIST_FIRST_RESULT, ConceptSchemeListPresenter.SCHEME_LIST_MAX_RESULTS, getConceptSchemeWebCriteria());
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
        TextItem code = new TextItem(ConceptSchemeDS.CODE, getConstants().identifiableArtefactCode());
        TextItem name = new TextItem(ConceptSchemeDS.NAME, getConstants().nameableArtefactName());
        TextItem urn = new TextItem(ConceptSchemeDS.URN, getConstants().identifiableArtefactUrn());
        TextItem description = new TextItem(ConceptSchemeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        SelectItem procStatus = new SelectItem(ConceptSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        procStatus.setValueMap(CommonUtils.getProcStatusHashMap());
        CustomDateItem internalPublicationDate = new CustomDateItem(ConceptSchemeDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        TextItem internalPublicationUser = new TextItem(ConceptSchemeDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        CustomDateItem externalPublicationDate = new CustomDateItem(ConceptSchemeDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        TextItem externalPublicationUser = new TextItem(ConceptSchemeDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        CustomCheckboxItem isLastVersion = new CustomCheckboxItem(ConceptSchemeDS.IS_LAST_VERSION, getConstants().maintainableArtefactIsLastVersion());
        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().retrieveConceptSchemes(ConceptSchemeListPresenter.SCHEME_LIST_FIRST_RESULT, ConceptSchemeListPresenter.SCHEME_LIST_MAX_RESULTS, getConceptSchemeWebCriteria());
            }
        });
        advancedSearchForm.setFields(code, internalPublicationDate, name, internalPublicationUser, urn, externalPublicationDate, description, externalPublicationUser, procStatus, isLastVersion,
                searchItem);
    }

    public void clearSearchSection() {
        searchForm.clearValues();
        clearAdvancedSearchSection();
    }

    private void clearAdvancedSearchSection() {
        advancedSearchForm.clearValues();
        advancedSearchForm.hide();
    }

    public void setUiHandlers(ConceptSchemeListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public ConceptSchemeListUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public ConceptSchemeWebCriteria getConceptSchemeWebCriteria() {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria();
        conceptSchemeWebCriteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        conceptSchemeWebCriteria.setCode(advancedSearchForm.getValueAsString(ConceptSchemeDS.CODE));
        conceptSchemeWebCriteria.setName(advancedSearchForm.getValueAsString(ConceptSchemeDS.NAME));
        conceptSchemeWebCriteria.setUrn(advancedSearchForm.getValueAsString(ConceptSchemeDS.URN));
        conceptSchemeWebCriteria.setDescription(advancedSearchForm.getValueAsString(ConceptSchemeDS.DESCRIPTION));
        conceptSchemeWebCriteria.setProcStatusEnum(!StringUtils.isBlank(advancedSearchForm.getValueAsString(ConceptSchemeDS.PROC_STATUS)) ? ProcStatusEnum.valueOf(advancedSearchForm
                .getValueAsString(ConceptSchemeDS.PROC_STATUS)) : null);
        conceptSchemeWebCriteria.setInternalPublicationDate(advancedSearchForm.getValue(ConceptSchemeDS.INTERNAL_PUBLICATION_DATE) != null ? (Date) advancedSearchForm
                .getValue(ConceptSchemeDS.INTERNAL_PUBLICATION_DATE) : null);
        conceptSchemeWebCriteria.setInternalPublicationUser(advancedSearchForm.getValueAsString(ConceptSchemeDS.INTERNAL_PUBLICATION_USER));
        conceptSchemeWebCriteria.setExternalPublicationDate(advancedSearchForm.getValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_DATE) != null ? (Date) advancedSearchForm
                .getValue(ConceptSchemeDS.EXTERNAL_PUBLICATION_DATE) : null);
        conceptSchemeWebCriteria.setExternalPublicationUser(advancedSearchForm.getValueAsString(ConceptSchemeDS.EXTERNAL_PUBLICATION_USER));
        conceptSchemeWebCriteria.setIsLastVersion(advancedSearchForm.getValue(ConceptSchemeDS.IS_LAST_VERSION) != null ? (Boolean) advancedSearchForm.getValue(ConceptSchemeDS.IS_LAST_VERSION) : null);
        return conceptSchemeWebCriteria;
    }

    private void showAdvancedSearchSection() {
        advancedSearchForm.show();
        searchForm.getItem(SEARCH_ITEM_NAME).setShowIcons(false); // Hide search icon in search section when advanced search section is shown
        searchForm.markForRedraw();
    }

    private void hideAdvancedSearchSection() {
        clearAdvancedSearchSection();
        searchForm.getItem(SEARCH_ITEM_NAME).setShowIcons(true);
        searchForm.markForRedraw();
    }
}

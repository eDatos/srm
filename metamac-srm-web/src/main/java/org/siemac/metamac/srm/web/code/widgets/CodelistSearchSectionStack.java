package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.presenter.CodelistListPresenter;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistListUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class CodelistSearchSectionStack extends VersionableResourceSearchSectionStack {

    private CodelistListUiHandlers                       uiHandlers;

    private SearchMultipleRelatedResourcePaginatedWindow searchReplaceToCodelistsWindow;

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

    public void setCodelistsForReplaceTo(GetCodelistsResult result) {
        if (searchReplaceToCodelistsWindow != null) {
            List<RelatedResourceDto> codelists = RelatedResourceUtils.getCodelistDtosAsRelatedResourceDtos(result.getCodelists());
            searchReplaceToCodelistsWindow.setSourceRelatedResources(codelists);
            searchReplaceToCodelistsWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), codelists.size(), result.getTotalResults());
        }
    }

    public void setUiHandlers(CodelistListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CodelistListUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private RelatedResourceListItem createReplaceToItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        RelatedResourceListItem replaceToItem = new RelatedResourceListItem(name, title, true);
        replaceToItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent arg0) {
                searchReplaceToCodelistsWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().codelistsSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveCodelistsForReplaceToInAdvancedSearch(firstResult, maxResults,
                                getCodelistWebCriteriaForReplaceTo(searchReplaceToCodelistsWindow.getRelatedResourceCriteria()));
                    }
                });

                // Load the list codelists that can be replaced
                getUiHandlers().retrieveCodelistsForReplaceToInAdvancedSearch(FIRST_RESULST, MAX_RESULTS, getCodelistWebCriteriaForReplaceTo(null));

                // Set the selected codelists
                List<RelatedResourceDto> selectedCodelists = ((RelatedResourceListItem) advancedSearchForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).getRelatedResourceDtos();
                searchReplaceToCodelistsWindow.setTargetRelatedResources(selectedCodelists);

                searchReplaceToCodelistsWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveCodelistsForReplaceToInAdvancedSearch(firstResult, maxResults, getCodelistWebCriteriaForReplaceTo(criteria));
                    }
                });
                searchReplaceToCodelistsWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        List<RelatedResourceDto> selectedCodelists = searchReplaceToCodelistsWindow.getSelectedRelatedResources();
                        searchReplaceToCodelistsWindow.markForDestroy();
                        // Set selected codelists in form
                        ((RelatedResourceListItem) advancedSearchForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).setRelatedResources(selectedCodelists);
                    }
                });
            }
        });
        return replaceToItem;
    }

    private CodelistWebCriteria getCodelistWebCriteriaForReplaceTo(String criteria) {
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria(criteria);
        codelistWebCriteria = MetamacWebCriteriaClientUtils.addCanBeReplacedConditionToCodelistWebCriteria(codelistWebCriteria);
        return codelistWebCriteria;
    }
}

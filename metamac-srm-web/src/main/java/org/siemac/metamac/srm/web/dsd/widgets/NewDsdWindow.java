package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchExternalPaginatedItem;

import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewDsdWindow extends CustomWindow {

    private final static int            OPERATION_LIST_FIRST_RESULT = 0;
    private final static int            OPERATION_LIST_MAX_RESULTS  = 6;

    private static final int            FORM_ITEM_CUSTOM_WIDTH      = 350;
    private static final String         FIELD_SAVE                  = "save-dsd";

    private CustomDynamicForm           form;
    private SearchExternalPaginatedItem searchOperationItem;

    public NewDsdWindow(final DsdListUiHandlers uiHandlers) {
        super(getConstants().dsdCreate());
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(DataStructureDefinitionDS.CODE, MetamacSrmWeb.getConstants().identifiableArtefactCode());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        codeItem.setValidators(SemanticIdentifiersUtils.getDsdIdentifierCustomValidator());

        RequiredTextItem nameItem = new RequiredTextItem(DataStructureDefinitionDS.NAME, MetamacSrmWeb.getConstants().nameableArtefactName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        searchOperationItem = new SearchExternalPaginatedItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation(), FormItemUtils.FORM_ITEM_WIDTH,
                OPERATION_LIST_MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        uiHandlers.retrieveStatisticalOperations(firstResult, maxResults, searchOperationItem.getSearchCriteria());
                    }
                });
        searchOperationItem.setRequired(true);
        searchOperationItem.getListGrid().setSelectionType(SelectionStyle.SINGLE);
        searchOperationItem.setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                uiHandlers.retrieveStatisticalOperations(firstResult, maxResults, criteria);
            }
        });
        searchOperationItem.getSearchItem().setWidth(FORM_ITEM_CUSTOM_WIDTH);
        searchOperationItem.getPaginatedCheckListGrid().setWidth(FORM_ITEM_CUSTOM_WIDTH);
        searchOperationItem.setWidth(FORM_ITEM_CUSTOM_WIDTH + 50);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().dsdCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, nameItem, searchOperationItem, saveItem);

        addItem(form);

        // Load the statistical operations to populate the widget once is created
        uiHandlers.retrieveStatisticalOperations(OPERATION_LIST_FIRST_RESULT, OPERATION_LIST_MAX_RESULTS, null);

        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public void setOperations(GetStatisticalOperationsResult result) {
        ((SearchExternalPaginatedItem) form.getItem(DataStructureDefinitionDS.STATISTICAL_OPERATION)).setExternalItems(result.getOperations());
        ((SearchExternalPaginatedItem) form.getItem(DataStructureDefinitionDS.STATISTICAL_OPERATION)).refreshPaginationInfo(result.getFirstResultOut(), result.getOperations().size(),
                result.getTotalResults());
    }

    public DataStructureDefinitionMetamacDto getNewDsd() {
        DataStructureDefinitionMetamacDto dsd = new DataStructureDefinitionMetamacDto();
        dsd.setCode(form.getValueAsString(DataStructureDefinitionDS.CODE));
        dsd.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(DataStructureDefinitionDS.NAME)));
        dsd.setMaintainer(RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto());
        dsd.setFinalLogic(false);
        dsd.setIsExternalReference(false);
        dsd.setStatisticalOperation(ExternalItemUtils.removeTitle(((SearchExternalPaginatedItem) form.getItem(DataStructureDefinitionDS.STATISTICAL_OPERATION)).getSelectedExternalItem()));
        return dsd;
    }

    public boolean validateForm() {
        return form.validate(false);
    }
}

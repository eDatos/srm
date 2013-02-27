package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedItem;
import org.siemac.metamac.srm.web.code.view.handlers.BaseCodeUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

/**
 * Window with a {@link CodesCheckboxTreeGrid} and a {@link SearchRelatedResourcePaginatedItem} as the codelist filter.
 */
public class SearchMultipleCodeHierarchyWindow extends CustomWindow {

    private static final int                     FIRST_RESULTS          = 0;
    private static final int                     MAX_RESULTS            = 6;
    private static final int                     FORM_ITEM_CUSTOM_WIDTH = 500;
    private static final String                  FIELD_SAVE             = "save-cod";

    protected CustomDynamicForm                  filterForm;
    protected CustomDynamicForm                  form;

    protected SearchRelatedResourcePaginatedItem filterListItem;
    protected ViewTextItem                       filterTextItem;

    protected CodesCheckboxTreeGrid              codesTreeGrid;

    private CodelistMetamacDto                   complexCodelistToAddCodes;          // Codelist where the selected codes will be inserted
    private BaseCodeUiHandlers                   uiHandlers;

    public SearchMultipleCodeHierarchyWindow(CodelistMetamacDto complexCodelistToAddCodes, BaseCodeUiHandlers uiHandlers) {
        super(getConstants().codeAdd());
        this.setUiHandlers(uiHandlers);
        this.complexCodelistToAddCodes = complexCodelistToAddCodes;
        setAutoSize(true);

        // FILTER SECTION

        filterListItem = new SearchRelatedResourcePaginatedItem("filterList", getConstants().codelistSelection(), FORM_ITEM_CUSTOM_WIDTH, MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCodelistsForCreateComplexCodelists(firstResult, maxResults, getCodelistWebCriteriaForCreateComplexCodelist(filterListItem.getSearchCriteria()));
            }
        });
        filterListItem.setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                getUiHandlers().retrieveCodelistsForCreateComplexCodelists(firstResult, maxResults, getCodelistWebCriteriaForCreateComplexCodelist(criteria));
            }
        });
        filterListItem.setTitleStyle("staticFormItemTitle");
        filterListItem.getListGrid().setSelectionType(SelectionStyle.SINGLE);
        filterListItem.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {

                RelatedResourceDto selectedRelatedResourceDto = filterListItem.getSelectedRelatedResource();
                if (selectedRelatedResourceDto != null) {
                    // Load codes of the codelist
                    getUiHandlers().retrieveCodesForCreateComplexCodelists(selectedRelatedResourceDto.getUrn());
                    filterTextItem.setValue(RelatedResourceUtils.getRelatedResourceName(selectedRelatedResourceDto));
                } else {
                    filterTextItem.clearValue();
                }
                filterForm.markForRedraw();
            }
        });

        filterTextItem = new ViewTextItem("filterText", getConstants().codelistSelected());
        filterTextItem.setWidth(200);
        filterTextItem.setTitleColSpan(2);
        filterTextItem.setShowIfCondition(getFilterTextFormItemIfFunction());

        filterForm = new CustomDynamicForm();
        filterForm.setTitleOrientation(TitleOrientation.TOP);
        filterForm.setMargin(15);
        filterForm.setNumCols(3);
        filterForm.setFields(filterListItem, filterTextItem);

        // TREEGRID TITLE

        Label title = new Label(getConstants().codesSelectionToAdd() + ":");
        title.setMargin(15);
        title.setHeight(20);
        title.setWidth100();
        title.setStyleName("staticFormItemTitle");

        // TREE SELECTION TYPE

        final CheckboxItem partialSelectionItem = new CheckboxItem("partialSelect", getConstants().actionTreeAllowPartialSelection());
        partialSelectionItem.setDefaultValue(true);
        partialSelectionItem.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                boolean selected = partialSelectionItem.getValueAsBoolean();
                codesTreeGrid.setShowPartialSelection(!selected);
                codesTreeGrid.redraw();
            }
        });

        DynamicForm treeSelectionTypeForm = new DynamicForm();
        treeSelectionTypeForm.setFields(partialSelectionItem);

        // CODES TREEGRID

        codesTreeGrid = new CodesCheckboxTreeGrid();
        codesTreeGrid.setMargin(15);
        codesTreeGrid.setWidth(760);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().accept());

        form = new CustomDynamicForm();
        form.setMargin(15);
        form.setWidth(760);
        form.setFields(saveItem);

        addItem(filterForm);
        addItem(title);
        addItem(treeSelectionTypeForm);
        addItem(codesTreeGrid);
        addItem(form);

        // Load codelists
        getUiHandlers().retrieveCodelistsForCreateComplexCodelists(FIRST_RESULTS, MAX_RESULTS, getCodelistWebCriteriaForCreateComplexCodelist(null));

        show();
    }

    public void setCodelists(GetCodelistsResult result) {
        List<RelatedResourceDto> codelists = RelatedResourceUtils.getCodelistDtosAsRelatedResourceDtos(result.getCodelists());
        filterListItem.setRelatedResources(codelists);
        filterListItem.refreshPaginationInfo(result.getFirstResultOut(), codelists.size(), result.getTotalResults());
    }

    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes) {
        codesTreeGrid.setItems(codelistMetamacDto, codes);
    }

    public void setUiHandlers(BaseCodeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public BaseCodeUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private FormItemIfFunction getFilterTextFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return filterTextItem.getValue() != null && !StringUtils.isBlank(filterTextItem.getValue().toString());
            }
        };
    }

    private CodelistWebCriteria getCodelistWebCriteriaForCreateComplexCodelist(String criteria) {
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria();
        codelistWebCriteria.setCriteria(criteria);
        // Do not include the codes of the own codelist
        codelistWebCriteria.setIsNotCodelistUrn(complexCodelistToAddCodes.getUrn());
        return codelistWebCriteria;
    }
}

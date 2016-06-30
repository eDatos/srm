package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.model.ds.CategorisationDS;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.utils.RecordUtils;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.ExportSdmxResourceWindow;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceItemWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.CustomLinkListGridField;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public abstract class CategorisationsPanel extends VLayout {

    protected ToolStripButton                                                newCategorisationButton;
    protected ToolStripButton                                                deleteCategorisationButton;
    protected ToolStripButton                                                exportCategorisationButton;
    protected ToolStripButton                                                cancelCategorisationValidityButton;
    protected NavigableListGrid                                              categorisationListGrid;

    private SearchMultipleRelatedResourceItemWithSchemeFilterPaginatedWindow window;
    private DeleteConfirmationWindow                                         deleteConfirmationWindow;

    private CategorisationUiHandlers                                         uiHandlers;

    protected ProcStatusEnum                                                 categorisedArtefactProcStatus;

    protected DateWindow                                                     dateWindow;

    public CategorisationsPanel() {
        setMargin(15);

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newCategorisationButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newCategorisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showSearchCategoriesWindow();
            }
        });

        deleteCategorisationButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteCategorisationButton.setVisible(false);
        deleteCategorisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        exportCategorisationButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionExportSdmxMl(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        exportCategorisationButton.setVisible(false);
        exportCategorisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<String> urns = getSelectedCategorisationUrns();
                if (!urns.isEmpty()) {
                    showExportationWindow(urns);
                }
            }

            protected void showExportationWindow(final List<String> urns) {
                new ExportSdmxResourceWindow() {

                    @Override
                    protected void startExportation(ExportDetailEnum infoAmount, ExportReferencesEnum references) {
                        getUiHandlers().exportCategorisations(urns, infoAmount, references);
                    }
                };
            }
        });

        cancelCategorisationValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), RESOURCE.disable().getURL());
        cancelCategorisationValidityButton.setVisible(false);
        cancelCategorisationValidityButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dateWindow = new DateWindow(getConstants().categorisationCancelValidityDate());
                dateWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (dateWindow.validateForm()) {
                            Date endValidityDate = dateWindow.getSelectedDate();
                            dateWindow.markForDestroy();
                            ListGridRecord[] records = categorisationListGrid.getSelectedRecords();
                            getUiHandlers().cancelCategorisationValidity(getUrnsFromSelectedRecords(records), endValidityDate);
                        }
                    }
                });
            }
        });

        toolStrip.addButton(newCategorisationButton);
        toolStrip.addButton(deleteCategorisationButton);
        toolStrip.addButton(cancelCategorisationValidityButton);
        toolStrip.addButton(exportCategorisationButton);

        // Deletion window

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().categorisationDeleteConfirmationTitle(), getConstants().categorisationDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCategorisations(getSelectedCategorisationUrns());
                deleteConfirmationWindow.hide();
            }
        });

        // ListGrid

        categorisationListGrid = new NavigableListGrid();
        categorisationListGrid.setOverflow(Overflow.VISIBLE);
        categorisationListGrid.setAutoFitData(Autofit.VERTICAL);
        ListGridUtils.setCheckBoxSelectionType(categorisationListGrid);
        CustomListGridField codeField = new CustomListGridField(CategorisationDS.CODE, getConstants().identifiableArtefactCode());
        CustomLinkListGridField categoryField = new CustomLinkListGridField(CategorisationDS.CATEGORY, getConstants().category());
        CustomListGridField validFrom = new CustomListGridField(CategorisationDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        CustomListGridField validTo = new CustomListGridField(CategorisationDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        categorisationListGrid.setFields(codeField, categoryField, validFrom, validTo);

        categorisationListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (categorisationListGrid.getSelectedRecords().length > 0) {
                    showDeleteCategorisationButton(categorisationListGrid.getSelectedRecords());
                    showCancelCategorisationValidity(categorisationListGrid.getSelectedRecords());
                    showExportCategorisationButton(categorisationListGrid.getSelectedRecords());
                } else {
                    deleteCategorisationButton.hide();
                    cancelCategorisationValidityButton.hide();
                    exportCategorisationButton.hide();
                }
            }
        });

        addMember(toolStrip);
        addMember(categorisationListGrid);
    }

    public void setCategorisations(List<CategorisationDto> categorisationDtos) {
        categorisationListGrid.setData(RecordUtils.getCategorisationRecords(categorisationDtos));
    }

    public void setCategorySchemes(GetRelatedResourcesResult result) {
        if (window != null) {
            List<RelatedResourceDto> categorySchemes = result.getRelatedResourceDtos();
            window.setFilterResources(categorySchemes);
            window.refreshFilterSourcePaginationInfo(result.getFirstResultOut(), categorySchemes.size(), result.getTotalResults());
        }
    }

    public void setCategories(GetRelatedResourcesResult result) {
        if (window != null) {
            List<RelatedResourceDto> categories = result.getRelatedResourceDtos();
            window.setResources(categories);
            window.refreshSourcePaginationInfo(result.getFirstResultOut(), categories.size(), result.getTotalResults());
        }
    }

    public void setUiHandlers(CategorisationUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        categorisationListGrid.setUiHandlers(uiHandlers);
    }

    public CategorisationUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public ProcStatusEnum getCategorisedArtefactProcStatus() {
        return categorisedArtefactProcStatus;
    }

    public void setCategorisedArtefactProcStatus(ProcStatusEnum procStatus) {
        this.categorisedArtefactProcStatus = procStatus;
    }

    private List<String> getSelectedCategorisationUrns() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : categorisationListGrid.getSelectedRecords()) {
            CategorisationRecord categorisationRecord = (CategorisationRecord) record;
            urns.add(categorisationRecord.getUrn());
        }
        return urns;
    }

    private void showSearchCategoriesWindow() {
        org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction<RelatedResourceWebCriteria> filterSearchAction = new org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction<RelatedResourceWebCriteria>() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, RelatedResourceWebCriteria webCriteria) {
                webCriteria.setOnlyLastVersion(window.getFilter().getSearchCriteria().isItemSchemeLastVersion());
                retrieveCategorySchemesForCategorisations(firstResult, maxResults, webCriteria.getCriteria(), webCriteria.isOnlyLastVersion());
            }
        };

        window = new SearchMultipleRelatedResourceItemWithSchemeFilterPaginatedWindow(MetamacWebCommon.getConstants().resourceSelection(), SrmWebConstants.FORM_LIST_MAX_RESULTS, filterSearchAction,
                new org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction<RelatedResourceItemWebCriteria>() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, RelatedResourceItemWebCriteria webCriteria) {
                        retrieveCategoriesForCategorisations(firstResult, maxResults, webCriteria.getCriteria(), webCriteria.getItemSchemeUrn(), webCriteria.isItemSchemeLastVersion());
                    }
                });

        window.retrieveItems();

        window.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                List<RelatedResourceDto> categories = window.getSelectedResources();
                window.markForDestroy();
                if (categories != null && !categories.isEmpty()) {
                    getUiHandlers().createCategorisations(RelatedResourceUtils.getUrnsFromRelatedResourceDtos(categories));
                }
            }
        });
    }

    private void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria, boolean isLastVersion) {
        CategorySchemeWebCriteria categorySchemeWebCriteria = new CategorySchemeWebCriteria();
        categorySchemeWebCriteria.setCriteria(criteria);
        categorySchemeWebCriteria.setIsLastVersion(isLastVersion);
        getUiHandlers().retrieveCategorySchemesForCategorisations(firstResult, maxResults, categorySchemeWebCriteria);
    }

    private void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String itemSchemeUrn, boolean isLastVersion) {
        CategoryWebCriteria categoryWebCriteria = new CategoryWebCriteria();
        categoryWebCriteria.setCriteria(criteria);
        categoryWebCriteria.setItemSchemeUrn(itemSchemeUrn);
        categoryWebCriteria.setIsLastVersion(isLastVersion);
        getUiHandlers().retrieveCategoriesForCategorisations(firstResult, maxResults, categoryWebCriteria);
    }

    private void showDeleteCategorisationButton(ListGridRecord[] selectedRecords) {
        if (canAllCategorisationsBeDeleted(selectedRecords)) {
            deleteCategorisationButton.show();
        } else {
            deleteCategorisationButton.hide();
        }
    }

    private void showCancelCategorisationValidity(ListGridRecord[] selectedRecords) {
        if (canCancelAllCategorisationsValidity(selectedRecords)) {
            cancelCategorisationValidityButton.show();
        } else {
            cancelCategorisationValidityButton.hide();
        }
    }

    private void showExportCategorisationButton(ListGridRecord[] selectedRecords) {
        if (canExportAllCategorisations(selectedRecords)) {
            exportCategorisationButton.show();
        } else {
            exportCategorisationButton.hide();
        }
    }

    private List<String> getUrnsFromSelectedRecords(ListGridRecord[] records) {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : records) {
            urns.add(((CategorisationRecord) record).getUrn());
        }
        return urns;
    }

    public abstract void updateNewButtonVisibility();
    public abstract boolean canAllCategorisationsBeDeleted(ListGridRecord[] records);
    public abstract boolean canCancelAllCategorisationsValidity(ListGridRecord[] records);
    public abstract boolean canExportAllCategorisations(ListGridRecord[] records);
}

package org.siemac.metamac.srm.web.client.view;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.presenter.StructuralResourcesPresenter;
import org.siemac.metamac.srm.web.client.view.handlers.StructuralResourcesUiHandlers;
import org.siemac.metamac.srm.web.client.widgets.CategorySchemeListGrid;
import org.siemac.metamac.srm.web.client.widgets.CodelistListGrid;
import org.siemac.metamac.srm.web.client.widgets.ConceptSchemeListGrid;
import org.siemac.metamac.srm.web.client.widgets.DsdListGrid;
import org.siemac.metamac.srm.web.client.widgets.OrganisationSchemeListGrid;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class StructuralResourcesViewImpl extends ViewWithUiHandlers<StructuralResourcesUiHandlers> implements StructuralResourcesPresenter.StructuralResourcesView {

    private static final String              CONTEXT_AREA_WIDTH       = "*";

    private static final String              DSDS_SECTION_ID          = "dsds";
    private static final String              CONCEPTS_SECTION_ID      = "concepts";
    private static final String              ORGANISATIONS_SECTION_ID = "organisations";
    private static final String              CATEGORIES_SECTION_ID    = "categories";
    private static final String              CODELISTS_SECTION_ID     = "codelists";

    private final DsdListGrid                dsdListGrid;
    private final ConceptSchemeListGrid      conceptSchemeListGrid;
    private final OrganisationSchemeListGrid organisationSchemeListGrid;
    private final CategorySchemeListGrid     categorySchemeListGrid;
    private final CodelistListGrid           codelistListGrid;

    private final SectionStack               lastModifiedArtefactsSectionStack;

    private VLayout                          panel;

    @Inject
    public StructuralResourcesViewImpl(DsdListGrid dsdListGrid, ConceptSchemeListGrid conceptSchemeListGrid, OrganisationSchemeListGrid organisationSchemeListGrid,
            CategorySchemeListGrid categorySchemeListGrid, CodelistListGrid codelistListGrid) {
        super();

        this.dsdListGrid = dsdListGrid;
        this.dsdListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                DsdRecord record = (DsdRecord) event.getRecord();
                getUiHandlers().goToDsd(record.getUrn());
            }
        });

        this.conceptSchemeListGrid = conceptSchemeListGrid;
        this.conceptSchemeListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                ConceptSchemeRecord record = (ConceptSchemeRecord) event.getRecord();
                getUiHandlers().goToConceptScheme(record.getUrn());

            }
        });

        this.organisationSchemeListGrid = organisationSchemeListGrid;
        this.organisationSchemeListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                OrganisationSchemeRecord record = (OrganisationSchemeRecord) event.getRecord();
                getUiHandlers().goToOrganisationScheme(record.getUrn(), record.getOrganisationSchemeBasicDto().getType());
            }
        });

        this.categorySchemeListGrid = categorySchemeListGrid;
        this.categorySchemeListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                CategorySchemeRecord record = (CategorySchemeRecord) event.getRecord();
                getUiHandlers().goToCategoryScheme(record.getUrn());
            }
        });

        this.codelistListGrid = codelistListGrid;
        this.codelistListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                CodelistRecord record = (CodelistRecord) event.getRecord();
                getUiHandlers().goToCodelist(record.getUrn());
            }
        });

        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        // Initialize the OperationsList View layout container
        panel.setStyleName("metamac-ContextArea");
        panel.setWidth(CONTEXT_AREA_WIDTH);

        // Section Stack
        lastModifiedArtefactsSectionStack = new SectionStack();
        lastModifiedArtefactsSectionStack.setWidth100();
        lastModifiedArtefactsSectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        lastModifiedArtefactsSectionStack.setAnimateSections(true);
        lastModifiedArtefactsSectionStack.setOverflow(Overflow.HIDDEN);

        SectionStackSection lastConceptSchemesModifiedSection = new SectionStackSection();
        lastConceptSchemesModifiedSection.setID(CONCEPTS_SECTION_ID);
        lastConceptSchemesModifiedSection.setTitle(MetamacSrmWeb.getConstants().conceptSchemeLastModified());
        lastConceptSchemesModifiedSection.setExpanded(true);
        lastConceptSchemesModifiedSection.setItems(this.conceptSchemeListGrid);

        SectionStackSection lastCodelistModifiedSection = new SectionStackSection();
        lastCodelistModifiedSection.setID(CODELISTS_SECTION_ID);
        lastCodelistModifiedSection.setTitle(MetamacSrmWeb.getConstants().codelistLastModified());
        lastCodelistModifiedSection.setExpanded(true);
        lastCodelistModifiedSection.setItems(this.codelistListGrid);

        SectionStackSection lastDsdModifiedSection = new SectionStackSection();
        lastDsdModifiedSection.setID(DSDS_SECTION_ID);
        lastDsdModifiedSection.setTitle(MetamacSrmWeb.getConstants().dsdLastModified());
        lastDsdModifiedSection.setExpanded(true);
        lastDsdModifiedSection.setItems(this.dsdListGrid);

        SectionStackSection lastOrganisationSchemesModifiedSection = new SectionStackSection();
        lastOrganisationSchemesModifiedSection.setID(ORGANISATIONS_SECTION_ID);
        lastOrganisationSchemesModifiedSection.setTitle(MetamacSrmWeb.getConstants().organisationSchemeLastModified());
        lastOrganisationSchemesModifiedSection.setExpanded(true);
        lastOrganisationSchemesModifiedSection.setItems(this.organisationSchemeListGrid);

        SectionStackSection lastCatSchemesModifiedSection = new SectionStackSection();
        lastCatSchemesModifiedSection.setID(CATEGORIES_SECTION_ID);
        lastCatSchemesModifiedSection.setTitle(MetamacSrmWeb.getConstants().categorySchemeLastModified());
        lastCatSchemesModifiedSection.setExpanded(true);
        lastCatSchemesModifiedSection.setItems(this.categorySchemeListGrid);

        lastModifiedArtefactsSectionStack.setSections(lastConceptSchemesModifiedSection, lastCodelistModifiedSection, lastDsdModifiedSection, lastOrganisationSchemesModifiedSection,
                lastCatSchemesModifiedSection);

        // Add the ToolStrip to the Operation View layout container
        panel.addMember(this.lastModifiedArtefactsSectionStack);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(StructuralResourcesUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        conceptSchemeListGrid.setUiHandlers(uiHandlers);
        dsdListGrid.setUiHandlers(uiHandlers);
        codelistListGrid.setUiHandlers(uiHandlers);
        organisationSchemeListGrid.setUiHandlers(uiHandlers);
        categorySchemeListGrid.setUiHandlers(uiHandlers);
    }

    @Override
    public void resetView() {
        for (int i = 0; i < lastModifiedArtefactsSectionStack.getSections().length; i++) {
            lastModifiedArtefactsSectionStack.collapseSection(i);
        }
    }

    @Override
    public void setDsdList(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDtos) {
        dsdListGrid.setDsds(dataStructureDefinitionMetamacDtos);
        lastModifiedArtefactsSectionStack.expandSection(DSDS_SECTION_ID);
    }

    @Override
    public void setConceptSchemeList(List<ConceptSchemeMetamacBasicDto> conceptSchemeDtos) {
        conceptSchemeListGrid.setConceptSchemes(conceptSchemeDtos);
        lastModifiedArtefactsSectionStack.expandSection(CONCEPTS_SECTION_ID);
    }

    @Override
    public void setOrganisationSchemeList(List<OrganisationSchemeMetamacBasicDto> organisationSchemeMetamacDtos) {
        organisationSchemeListGrid.setOrganisationSchemes(organisationSchemeMetamacDtos);
        lastModifiedArtefactsSectionStack.expandSection(ORGANISATIONS_SECTION_ID);
    }

    @Override
    public void setCategorySchemesList(List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDtos) {
        categorySchemeListGrid.setCategorySchemes(categorySchemeMetamacDtos);
        lastModifiedArtefactsSectionStack.expandSection(CATEGORIES_SECTION_ID);
    }

    @Override
    public void setCodelistList(List<CodelistMetamacBasicDto> codelistMetamacDtos) {
        codelistListGrid.setCodelist(codelistMetamacDtos);
        lastModifiedArtefactsSectionStack.expandSection(CODELISTS_SECTION_ID);
    }
}

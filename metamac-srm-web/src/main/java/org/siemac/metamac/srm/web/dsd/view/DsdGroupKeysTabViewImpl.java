package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.dsd.model.ds.GroupKeysDS;
import org.siemac.metamac.srm.web.dsd.model.record.GroupKeysRecord;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGroupKeysTabPresenter;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGroupKeysTabUiHandlers;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class DsdGroupKeysTabViewImpl extends ViewWithUiHandlers<DsdGroupKeysTabUiHandlers> implements DsdGroupKeysTabPresenter.DsdGroupKeysTabView {

    private ProcStatusEnum              procStatus;

    private List<DimensionComponentDto> dimensionComponentDtos;
    private DescriptorDto               descriptorDto;

    private VLayout                     panel;
    private VLayout                     selectedDescriptorLayout;
    private ListGrid                    groupKeysGrid;

    private InternationalMainFormLayout mainFormLayout;

    private AnnotationsPanel            viewAnnotationsPanel;
    private AnnotationsPanel            editionAnnotationsPanel;

    // VIEW FORM

    private GroupDynamicForm            staticForm;
    private ViewTextItem                staticCode;
    private ViewTextItem                staticDimensionsItem;

    // EDITION FORM

    private GroupDynamicForm            editionForm;
    private RequiredTextItem            code;
    private CustomSelectItem            dimensionsItem;

    private ToolStripButton             newToolStripButton;
    private ToolStripButton             deleteToolStripButton;

    private DeleteConfirmationWindow    deleteConfirmationWindow;

    public DsdGroupKeysTabViewImpl() {
        super();
        panel = new VLayout();

        // ··················
        // List of group keys
        // ··················

        // ToolStrip

        newToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionNew(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.newListGrid().getURL());
        newToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                selectGroupKeys(new DescriptorDto());
            }
        });

        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().dsdDeleteConfirmationTitle(), MetamacSrmWeb.getConstants().dsdGroupKeysDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);

        deleteToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionDelete(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteToolStripButton.setVisibility(Visibility.HIDDEN);
        deleteToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        ToolStrip groupKeysGridToolStrip = new ToolStrip();
        groupKeysGridToolStrip.setWidth100();
        groupKeysGridToolStrip.addButton(newToolStripButton);
        groupKeysGridToolStrip.addSeparator();
        groupKeysGridToolStrip.addButton(deleteToolStripButton);

        // Grid

        groupKeysGrid = new ListGrid();
        groupKeysGrid.setWidth100();
        groupKeysGrid.setHeight(150);
        groupKeysGrid.setSelectionType(SelectionStyle.SIMPLE);
        groupKeysGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        ListGridField idLogicField = new ListGridField(GroupKeysRecord.CODE, MetamacSrmWeb.getConstants().dsdGroupKeysIdLogic());
        groupKeysGrid.setFields(idLogicField);
        // ToolTip
        idLogicField.setShowHover(true);
        idLogicField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                GroupKeysRecord groupKeysRecord = (GroupKeysRecord) record;
                return groupKeysRecord.getCode();
            }
        });
        // Show attribute details when record clicked
        groupKeysGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (groupKeysGrid.getSelectedRecords() != null && groupKeysGrid.getSelectedRecords().length == 1) {
                    GroupKeysRecord record = (GroupKeysRecord) groupKeysGrid.getSelectedRecord();
                    DescriptorDto groupKeysSelected = record.getDescriptorDto();
                    selectGroupKeys(groupKeysSelected);
                } else {
                    // No group keys selected
                    deselectGroupKeys();
                    if (groupKeysGrid.getSelectedRecords().length > 1) {
                        // Delete more than one group keys with one click
                        showDeleteToolStripButton();
                    }
                }
            }
        });
        groupKeysGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // CheckBox is not clicked
                    groupKeysGrid.deselectAllRecords();
                    groupKeysGrid.selectRecord(event.getRecord());
                }
            }
        });

        VLayout gridLayout = new VLayout();
        gridLayout.setAutoHeight();
        gridLayout.setMargin(10);
        gridLayout.addMember(groupKeysGridToolStrip);
        gridLayout.addMember(groupKeysGrid);

        // ··············
        // Dimension Form
        // ··············

        mainFormLayout = new InternationalMainFormLayout();
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setTranslationsShowed(mainFormLayout.getTranslateToolStripButton().isSelected());
            }
        });
        mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // If it is a new dimension, hide mainFormLayout
                if (descriptorDto.getId() == null) {
                    selectedDescriptorLayout.hide();
                }
            }
        });

        createViewForm();
        createEditionForm();

        selectedDescriptorLayout = new VLayout(10);
        selectedDescriptorLayout.addMember(mainFormLayout);
        selectedDescriptorLayout.setVisibility(Visibility.HIDDEN);

        panel.addMember(gridLayout);
        panel.addMember(selectedDescriptorLayout);
    }

    /**
     * Creates and returns the view layout
     * 
     * @return
     */
    private void createViewForm() {
        staticForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdGroupKeysDetails());
        staticCode = new ViewTextItem(GroupKeysDS.CODE, MetamacSrmWeb.getConstants().dsdGroupKeysId());
        ViewTextItem urn = new ViewTextItem(GroupKeysDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(GroupKeysDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        staticDimensionsItem = new ViewTextItem(GroupKeysDS.DIMENSIONS, MetamacSrmWeb.getConstants().dsdDimensions());
        staticForm.setFields(staticCode, urn, urnProvider, staticDimensionsItem);

        // Annotations
        viewAnnotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(staticForm);
        mainFormLayout.addViewCanvas(viewAnnotationsPanel);
    }

    /**
     * Creates and returns the edition layout
     * 
     * @return
     */
    private void createEditionForm() {
        editionForm = new GroupDynamicForm(MetamacSrmWeb.getConstants().dsdDimensionDetails());

        // Id
        code = new RequiredTextItem(GroupKeysDS.CODE, MetamacSrmWeb.getConstants().dsdGroupKeysId());
        code.setValidators(SemanticIdentifiersUtils.getGroupDescriptorIdentifierCustomValidator());

        ViewTextItem urn = new ViewTextItem(GroupKeysDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(GroupKeysDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());

        // Dimensions
        dimensionsItem = new CustomSelectItem(GroupKeysDS.DIMENSIONS, MetamacSrmWeb.getConstants().dsdDimensions());
        dimensionsItem.setMultiple(true);
        dimensionsItem.setPickListWidth(350);

        editionForm.setFields(code, urn, urnProvider, dimensionsItem);

        // Annotations
        editionAnnotationsPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(editionForm);
        mainFormLayout.addEditionCanvas(editionAnnotationsPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setDsdGroupKeys(ProcStatusEnum procStatus, List<DimensionComponentDto> dimensionComponentDtos, List<DescriptorDto> descriptorDtos) {
        this.procStatus = procStatus;

        // Security
        newToolStripButton.setVisibility(DsdClientSecurityUtils.canUpdateGroupKeys(procStatus) ? Visibility.VISIBLE : Visibility.HIDDEN);
        mainFormLayout.setCanEdit(DsdClientSecurityUtils.canUpdateGroupKeys(procStatus));

        deselectGroupKeys();
        this.dimensionComponentDtos = dimensionComponentDtos;
        dimensionsItem.setValueMap(CommonUtils.getDimensionComponentDtoHashMap(dimensionComponentDtos));

        groupKeysGrid.selectAllRecords();
        groupKeysGrid.removeSelectedData();
        groupKeysGrid.deselectAllRecords();
        for (DescriptorDto descriptorDto : descriptorDtos) {
            GroupKeysRecord record = RecordUtils.getGroupKeysRecord(descriptorDto);
            groupKeysGrid.addData(record);
        }
    }

    @Override
    public HasClickHandlers getSave() {
        return mainFormLayout.getSave();
    }

    @Override
    public HasClickHandlers getDelete() {
        return deleteConfirmationWindow.getYesButton();
    }

    @Override
    public DescriptorDto getGroupKeys() {
        descriptorDto.setCode(code.getValueAsString());

        descriptorDto.getComponents().clear();
        List<DimensionComponentDto> dimensions = getDimensions(dimensionsItem.getValues());
        descriptorDto.getComponents().addAll(dimensions);

        // If it is a new component, specify type
        if (descriptorDto.getId() == null) {
            descriptorDto.setTypeComponentList(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR);
        }

        // Annotations
        descriptorDto.getAnnotations().clear();
        descriptorDto.getAnnotations().addAll(editionAnnotationsPanel.getAnnotations());

        return descriptorDto;
    }

    @Override
    public List<DescriptorDto> getSelectedGroupKeys() {
        if (groupKeysGrid.getSelectedRecords() != null) {
            List<DescriptorDto> selectedGroupKeys = new ArrayList<DescriptorDto>();
            ListGridRecord[] records = groupKeysGrid.getSelectedRecords();
            for (int i = 0; i < records.length; i++) {
                GroupKeysRecord record = (GroupKeysRecord) records[i];
                selectedGroupKeys.add(record.getDescriptorDto());
            }
            return selectedGroupKeys;
        }
        return null;
    }

    @Override
    public void setGroupKeys(DescriptorDto descriptorDto) {
        setGroupKeysViewMode(descriptorDto);
        setGroupKeysEditionMode(descriptorDto);
    }

    @Override
    public boolean validate() {
        return editionForm.validate();
    }

    @Override
    public void onGroupKeysSaved(DescriptorDto descriptorDto) {
        this.descriptorDto = descriptorDto;
        groupKeysGrid.removeSelectedData();
        GroupKeysRecord record = RecordUtils.getGroupKeysRecord(descriptorDto);
        groupKeysGrid.addData(record);
        groupKeysGrid.selectRecord(record);
        mainFormLayout.setViewMode();
    }

    private void setGroupKeysViewMode(DescriptorDto descriptorDto) {
        this.descriptorDto = descriptorDto;

        staticCode.setValue(descriptorDto.getCode());

        staticForm.setValue(GroupKeysDS.URN, descriptorDto.getUrn());
        staticForm.setValue(GroupKeysDS.URN_PROVIDER, descriptorDto.getUrnProvider());

        staticDimensionsItem.clearValue();
        List<ComponentDto> dimensionComponentDtos = new ArrayList<ComponentDto>(descriptorDto.getComponents());

        StringBuilder dimensionBuilder = new StringBuilder();
        for (int i = 0; i < dimensionComponentDtos.size(); i++) {
            dimensionBuilder.append(i != 0 ? ",  " : "");
            dimensionBuilder.append(dimensionComponentDtos.get(i).getCode());
        }
        staticDimensionsItem.setValue(dimensionBuilder.toString());

        // Annotations
        viewAnnotationsPanel.setAnnotations(descriptorDto.getAnnotations());
    }

    private void setGroupKeysEditionMode(DescriptorDto descriptorDto) {
        this.descriptorDto = descriptorDto;

        code.setValue(descriptorDto.getCode());

        editionForm.setValue(GroupKeysDS.URN, descriptorDto.getUrn());
        editionForm.setValue(GroupKeysDS.URN_PROVIDER, descriptorDto.getUrnProvider());

        dimensionsItem.clearValue();
        Set<ComponentDto> dimensionComponentDtos = descriptorDto.getComponents();
        List<String> dimensions = new ArrayList<String>();
        for (ComponentDto c : dimensionComponentDtos) {
            dimensions.add(c.getId().toString());
        }
        dimensionsItem.setValues(dimensions.toArray(new String[0]));

        // Annotations
        editionAnnotationsPanel.setAnnotations(descriptorDto.getAnnotations());
    }

    /**
     * Select dimension in ListGrid
     * 
     * @param dimensionSelected
     */
    private void selectGroupKeys(DescriptorDto groupKeysSelected) {
        if (groupKeysSelected.getId() == null) {
            // New group keys
            mainFormLayout.setTitleLabelContents(new String());
            deleteToolStripButton.hide();
            groupKeysGrid.deselectAllRecords();
            setGroupKeysEditionMode(groupKeysSelected);
            mainFormLayout.setEditionMode();
        } else {
            mainFormLayout.setTitleLabelContents(groupKeysSelected.getCode());
            showDeleteToolStripButton();
            setGroupKeys(groupKeysSelected);
            mainFormLayout.setViewMode();
        }

        // Clear errors
        editionForm.clearErrors(true);

        selectedDescriptorLayout.show();
        selectedDescriptorLayout.redraw();
    }

    /**
     * DeSelect dimension in ListGrid
     */
    private void deselectGroupKeys() {
        selectedDescriptorLayout.hide();
        deleteToolStripButton.hide();
    }

    private List<DimensionComponentDto> getDimensions(String[] selectedDimensions) {
        List<DimensionComponentDto> dimensionComponentDtos = new ArrayList<DimensionComponentDto>();
        for (String d : selectedDimensions) {
            dimensionComponentDtos.add(getDimensionComponentDto(d));
        }
        return dimensionComponentDtos;
    }

    private DimensionComponentDto getDimensionComponentDto(String id) {
        if (id != null) {
            Long idDimension = Long.valueOf(id);
            for (DimensionComponentDto d : dimensionComponentDtos) {
                if (d.getId().compareTo(idDimension) == 0) {
                    return d;
                }
            }
        }
        return null;
    }

    private void setTranslationsShowed(boolean translationsShowed) {
        viewAnnotationsPanel.setTranslationsShowed(translationsShowed);
        editionAnnotationsPanel.setTranslationsShowed(translationsShowed);
    }

    private void showDeleteToolStripButton() {
        if (DsdClientSecurityUtils.canUpdateGroupKeys(procStatus)) {
            deleteToolStripButton.show();
        }
    }

}

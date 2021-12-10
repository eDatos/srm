package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.search.SearchMultiRelatedResourceSimpleItem;
import org.siemac.metamac.srm.web.client.widgets.search.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.code.presenter.VariableElementPresenter;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.VariableElementUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.SearchCodeForVariableElementGeographicalGranularity;
import org.siemac.metamac.srm.web.code.widgets.VariableElementMainFormLayout;
import org.siemac.metamac.srm.web.code.widgets.VariableElementOperationLayout;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.CustomColorPicker;
import org.siemac.metamac.web.common.client.widgets.CustomSectionStack;
import org.siemac.metamac.web.common.client.widgets.DoubleItem;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;
import com.smartgwt.client.widgets.layout.VLayout;

public class VariableElementViewImpl extends ViewWithUiHandlers<VariableElementUiHandlers> implements VariableElementPresenter.VariableElementView {

    private VLayout                                      panel;
    private VariableElementMainFormLayout                mainFormLayout;

    // View forms
    private GroupDynamicForm                             identifiersForm;
    private GroupDynamicForm                             contentDescriptorsForm;
    private GroupDynamicForm                             diffusionDescriptorsForm;
    private GroupDynamicForm                             geographicalInformationForm;
    private GroupDynamicForm                             annotationsForm;

    // Edition forms
    private GroupDynamicForm                             identifiersEditionForm;
    private GroupDynamicForm                             contentDescriptorsEditionForm;
    private GroupDynamicForm                             diffusionDescriptorsEditionForm;
    private GroupDynamicForm                             geographicalInformationEditionForm;
    private GroupDynamicForm                             annotationsEditionForm;

    private SearchMultipleRelatedResourcePaginatedWindow createSegregationWindow;

    // Variable element operations
    private CustomSectionStack                           operationsSectionStack;
    private VariableElementOperationLayout               variableElementOperationsLayout;

    private VariableElementDto                           variableElementDto;

    @Inject
    public VariableElementViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();

        //
        // VARIABLE ELEMENT
        //

        mainFormLayout = new VariableElementMainFormLayout();
        mainFormLayout.getSegregate().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showCreateSegregationWindow();
            }
        });
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        // VARIABLE ELEMENT OPERATIONS

        variableElementOperationsLayout = new VariableElementOperationLayout();
        operationsSectionStack = new CustomSectionStack(getConstants().variableOperationsBetweenElements());
        operationsSectionStack.setMargin(15);
        operationsSectionStack.getDefaultSection().addItem(variableElementOperationsLayout);

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(mainFormLayout);
        subPanel.addMember(operationsSectionStack);

        panel.addMember(subPanel);
    }

    @Override
    public void setUiHandlers(VariableElementUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        variableElementOperationsLayout.setUiHandlers(uiHandlers);
        ((SearchCodeForVariableElementGeographicalGranularity) geographicalInformationEditionForm.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).setUiHandlers(uiHandlers);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                geographicalInformationForm.setTranslationsShowed(translationsShowed);
                geographicalInformationEditionForm.setTranslationsShowed(translationsShowed);

                diffusionDescriptorsForm.setTranslationsShowed(translationsShowed);
                diffusionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsForm.setTranslationsShowed(translationsShowed);
                annotationsEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && diffusionDescriptorsEditionForm.validate(false) && annotationsEditionForm.validate(false)
                        && (geographicalInformationEditionForm.isVisible() ? geographicalInformationEditionForm.validate(false) : true)) {
                    // See: METAMAC-2516
                    // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                    getVariableElementDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            // See: METAMAC-2516
                            // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                            getVariableElementDto();
                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                                @Override
                                public void execute() {
                                    getUiHandlers().saveVariableElement(getVariableElementDto());
                                }
                            });
                        }
                    });

                }
            }
        });

        // Delete
        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteVariableElement(variableElementDto.getUrn());
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == VariableElementPresenter.TYPE_SetContextAreaContentCodesToolBar) {
            if (content != null) {
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    @Override
    public void setVariableElement(VariableElementDto variableElementDto) {
        this.variableElementDto = variableElementDto;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(variableElementDto.getShortName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);
        mainFormLayout.setVariableElement(variableElementDto);

        mainFormLayout.setViewMode();

        setVariableElementViewMode(variableElementDto);
        setVariableElementEditionMode(variableElementDto);

        // Show the geographical information form only if the variable element belongs to a geographical variable
        if (variableElementDto.getVariable() != null && VariableTypeEnum.GEOGRAPHICAL.equals(variableElementDto.getVariable().getVariableType())) {
            geographicalInformationForm.show();
            geographicalInformationEditionForm.show();
        } else {
            geographicalInformationForm.hide();
            geographicalInformationEditionForm.hide();
        }
    }

    @Override
    public void setVariableElementOperations(List<VariableElementOperationDto> variableElementOperationDtos) {
        variableElementOperationsLayout.setVariableElementOperations(variableElementOperationDtos);

        // Do not show the operations is the list if is empty
        if (variableElementOperationDtos.isEmpty()) {
            operationsSectionStack.hide();
        } else {
            operationsSectionStack.show();
        }
    }

    @Override
    public void setVariableElementsForReplaceTo(GetVariableElementsResult result) {
        ((SearchMultiRelatedResourceSimpleItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.REPLACE_TO_ELEMENTS))
                .setResources(RelatedResourceUtils.getVariableElementBasicDtosAsRelatedResourceDtos(result.getVariableElements()), result.getFirstResultOut(), result.getTotalResults());
    }

    @Override
    public void setVariableElementsForSegregation(GetVariableElementsResult result) {
        if (createSegregationWindow != null) {
            createSegregationWindow.setResources(RelatedResourceUtils.getVariableElementBasicDtosAsRelatedResourceDtos(result.getVariableElements()));
            createSegregationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
        }
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem shortName = new ViewMultiLanguageTextItem(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName());
        ViewTextItem urn = new ViewTextItem(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, shortName, urn);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        RelatedResourceLinkItem variable = new RelatedResourceLinkItem(VariableElementDS.VARIABLE, getConstants().variable(), getCustomLinkItemNavigationClickHandler());
        CustomColorPicker customColorPicker = new CustomColorPicker(VariableElementDS.RENDERING_COLOR, getConstants().variableElementRenderingColor());
        contentDescriptorsForm.setFields(variable, customColorPicker);

        // Geographical information
        geographicalInformationForm = new GroupDynamicForm(getConstants().formGeographicalInformation());
        RelatedResourceLinkItem geographicalGranularity = new RelatedResourceLinkItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY, getConstants().variableElementGeographicalGranularity(),
                getCustomLinkItemNavigationClickHandler());
        ViewTextItem latitude = new ViewTextItem(VariableElementDS.LATITUDE, getConstants().variableElementLatitude());
        latitude.setStartRow(true);
        ViewTextItem longitude = new ViewTextItem(VariableElementDS.LONGITUDE, getConstants().variableElementLongitude());
        ViewTextItem hasShape = new ViewTextItem(VariableElementDS.SHAPE_WKT, getConstants().variableElementHasPolygonShape());
        geographicalInformationForm.setFields(geographicalGranularity, latitude, longitude, hasShape);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem validFrom = new ViewTextItem(VariableElementDS.VALID_FROM, getConstants().variableElementValidFrom());
        ViewTextItem validTo = new ViewTextItem(VariableElementDS.VALID_TO, getConstants().variableElementValidTo());
        RelatedResourceListItem replaceToElements = new RelatedResourceListItem(VariableElementDS.REPLACE_TO_ELEMENTS, getConstants().variableElementReplaceToVariableElements(), false,
                getListRecordNavigationClickHandler());
        ViewTextItem replacedByElement = new ViewTextItem(VariableElementDS.REPLACED_BY_ELEMENT, getConstants().variableElementReplacedByVariableElement());
        diffusionDescriptorsForm.setFields(validFrom, validTo, replaceToElements, replacedByElement);

        // Annotations
        annotationsForm = new GroupDynamicForm(getConstants().formAnnotations());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(VariableElementDS.COMMENTS, getConstants().variableElementComment());
        annotationsForm.setFields(comments);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(geographicalInformationForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
        mainFormLayout.addViewCanvas(annotationsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName(), SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH);
        shortName.setRequired(true);
        ViewTextItem urn = new ViewTextItem(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, shortName, urn);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        RelatedResourceLinkItem variable = new RelatedResourceLinkItem(VariableElementDS.VARIABLE, getConstants().variable(), getCustomLinkItemNavigationClickHandler());
        CustomColorPicker customColorPicker = new CustomColorPicker(VariableElementDS.RENDERING_COLOR, getConstants().variableElementRenderingColor(), Boolean.TRUE);
        contentDescriptorsEditionForm.setFields(variable, customColorPicker);

        // Geographical information
        geographicalInformationEditionForm = new GroupDynamicForm(getConstants().formGeographicalInformation());
        SearchCodeForVariableElementGeographicalGranularity geographicalGranularity = createGeographicalGranularityItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY,
                getConstants().variableElementGeographicalGranularity());
        DoubleItem latitude = new DoubleItem(VariableElementDS.LATITUDE, getConstants().variableElementLatitude());
        latitude.setStartRow(true);
        DoubleItem longitude = new DoubleItem(VariableElementDS.LONGITUDE, getConstants().variableElementLongitude());
        ViewTextItem hasShape = new ViewTextItem(VariableElementDS.SHAPE_WKT, getConstants().variableElementHasPolygonShape());
        geographicalInformationEditionForm.setFields(geographicalGranularity, latitude, longitude, hasShape);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        CustomDateItem validFrom = new CustomDateItem(VariableElementDS.VALID_FROM, getConstants().variableElementValidFrom());
        CustomDateItem validTo = new CustomDateItem(VariableElementDS.VALID_TO, getConstants().variableElementValidTo());
        RelatedResourceListItem replaceToElements = createReplaceToElementsItem();
        ViewTextItem replacedByElement = new ViewTextItem(VariableElementDS.REPLACED_BY_ELEMENT, getConstants().variableElementReplacedByVariableElement());
        diffusionDescriptorsEditionForm.setFields(validFrom, validTo, replaceToElements, replacedByElement);

        // Annotations
        annotationsEditionForm = new GroupDynamicForm(getConstants().formAnnotations());
        MultiLanguageRichTextEditorItem comments = new MultiLanguageRichTextEditorItem(VariableElementDS.COMMENTS, getConstants().variableElementComment());
        annotationsEditionForm.setFields(comments);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(geographicalInformationEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setVariableElementViewMode(VariableElementDto variableElementDto) {
        // Identifiers
        identifiersForm.setValue(VariableElementDS.CODE, variableElementDto.getCode());
        identifiersForm.setValue(VariableElementDS.URN, variableElementDto.getUrn());
        identifiersForm.setValue(VariableElementDS.SHORT_NAME, variableElementDto.getShortName());

        // Content descriptors
        ((RelatedResourceLinkItem) contentDescriptorsForm.getItem(VariableElementDS.VARIABLE)).setRelatedResource(variableElementDto.getVariable());
        ((CustomColorPicker) contentDescriptorsForm.getItem(VariableElementDS.RENDERING_COLOR)).setColor(variableElementDto.getRenderingColor());

        // Geographical information
        ((RelatedResourceLinkItem) geographicalInformationForm.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).setRelatedResource(variableElementDto.getGeographicalGranularity());
        geographicalInformationForm.setValue(VariableElementDS.LATITUDE, variableElementDto.getLatitude() != null ? variableElementDto.getLatitude().toString() : StringUtils.EMPTY);
        geographicalInformationForm.setValue(VariableElementDS.LONGITUDE, variableElementDto.getLongitude() != null ? variableElementDto.getLongitude().toString() : StringUtils.EMPTY);
        geographicalInformationForm.setValue(VariableElementDS.SHAPE_WKT,
                StringUtils.isBlank(variableElementDto.getShapeWkt()) ? MetamacWebCommon.getConstants().no() : MetamacWebCommon.getConstants().yes());

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(VariableElementDS.VALID_FROM, DateUtils.getFormattedDate(variableElementDto.getValidFrom()));
        diffusionDescriptorsForm.setValue(VariableElementDS.VALID_TO, DateUtils.getFormattedDate(variableElementDto.getValidTo()));
        ((RelatedResourceListItem) diffusionDescriptorsForm.getItem(VariableElementDS.REPLACE_TO_ELEMENTS)).setRelatedResources(variableElementDto.getReplaceToVariableElements());
        diffusionDescriptorsForm.setValue(VariableElementDS.REPLACED_BY_ELEMENT, RelatedResourceUtils.getRelatedResourceName(variableElementDto.getReplacedByVariableElement()));

        // Annotations
        annotationsForm.setValue(VariableElementDS.COMMENTS, variableElementDto.getComment());
    }

    public void setVariableElementEditionMode(VariableElementDto variableElementDto) {
        // Identifiers
        identifiersEditionForm.setValue(VariableElementDS.CODE, variableElementDto.getCode());
        identifiersEditionForm.setValue(VariableElementDS.URN, variableElementDto.getUrn());
        identifiersEditionForm.setValue(VariableElementDS.SHORT_NAME, variableElementDto.getShortName());

        // Content descriptors
        ((RelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(VariableElementDS.VARIABLE)).setRelatedResource(variableElementDto.getVariable());
        ((CustomColorPicker) contentDescriptorsEditionForm.getItem(VariableElementDS.RENDERING_COLOR)).setColor(variableElementDto.getRenderingColor());

        // Geographical information
        ((SearchCodeForVariableElementGeographicalGranularity) geographicalInformationEditionForm.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY))
                .setRelatedResource(variableElementDto.getGeographicalGranularity());
        geographicalInformationEditionForm.setValue(VariableElementDS.LATITUDE, variableElementDto.getLatitude() != null ? variableElementDto.getLatitude().toString() : StringUtils.EMPTY);
        geographicalInformationEditionForm.setValue(VariableElementDS.LONGITUDE, variableElementDto.getLongitude() != null ? variableElementDto.getLongitude().toString() : StringUtils.EMPTY);
        geographicalInformationEditionForm.setValue(VariableElementDS.SHAPE_WKT,
                StringUtils.isBlank(variableElementDto.getShapeWkt()) ? MetamacWebCommon.getConstants().no() : MetamacWebCommon.getConstants().yes());

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(VariableElementDS.VALID_FROM, variableElementDto.getValidFrom());
        diffusionDescriptorsEditionForm.setValue(VariableElementDS.VALID_TO, variableElementDto.getValidTo());
        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.REPLACE_TO_ELEMENTS)).setRelatedResources(variableElementDto.getReplaceToVariableElements());
        diffusionDescriptorsEditionForm.setValue(VariableElementDS.REPLACED_BY_ELEMENT, RelatedResourceUtils.getRelatedResourceName(variableElementDto.getReplacedByVariableElement()));

        // Annotations
        annotationsEditionForm.setValue(VariableElementDS.COMMENTS, variableElementDto.getComment());
    }

    private VariableElementDto getVariableElementDto() {
        // Identifiers
        variableElementDto.setShortName(identifiersEditionForm.getValueAsInternationalStringDto(VariableElementDS.SHORT_NAME));

        // Content descriptors
        variableElementDto.setRenderingColor(((CustomColorPicker) contentDescriptorsEditionForm.getItem(VariableElementDS.RENDERING_COLOR)).getColor());

        // Geographical information
        variableElementDto.setGeographicalGranularity(
                ((SearchCodeForVariableElementGeographicalGranularity) geographicalInformationEditionForm.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).getRelatedResourceDto());
        variableElementDto.setLatitude(((DoubleItem) geographicalInformationEditionForm.getItem(VariableElementDS.LATITUDE)).getValueAsDouble());
        variableElementDto.setLongitude(((DoubleItem) geographicalInformationEditionForm.getItem(VariableElementDS.LONGITUDE)).getValueAsDouble());

        // Diffusion descriptors
        variableElementDto.setValidFrom(((CustomDateItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.VALID_FROM)).getValueAsDate());
        variableElementDto.setValidTo(((CustomDateItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.VALID_TO)).getValueAsDate());
        variableElementDto.getReplaceToVariableElements().clear();
        variableElementDto.getReplaceToVariableElements()
                .addAll(((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(VariableElementDS.REPLACE_TO_ELEMENTS)).getSelectedRelatedResources());

        // Annotations
        variableElementDto.setComment(annotationsEditionForm.getValueAsInternationalStringDto(VariableElementDS.COMMENTS));

        return variableElementDto;
    }

    private RelatedResourceListItem createReplaceToElementsItem() {
        return new SearchMultiRelatedResourceSimpleItem(VariableElementDS.REPLACE_TO_ELEMENTS, getConstants().variableElementReplaceToVariableElements(), SrmWebConstants.FORM_LIST_MAX_RESULTS,
                getListRecordNavigationClickHandler()) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveVariableElementsByVariableForReplaceTo(firstResult, maxResults, webCriteria.getCriteria(), variableElementDto.getVariable().getUrn());
            }
        };
    }

    private SearchCodeForVariableElementGeographicalGranularity createGeographicalGranularityItem(final String name, String title) {
        final SearchCodeForVariableElementGeographicalGranularity item = new SearchCodeForVariableElementGeographicalGranularity(name, title, getCustomLinkItemNavigationClickHandler());
        item.setValidators(new RequiredIfValidator(new RequiredIfFunction() {

            @Override
            public boolean execute(FormItem formItem, Object value) {
                return !CommonUtils.isVariableElementWorld(variableElementDto);
            }
        }));
        item.setSaveClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                RelatedResourceDto code = item.getSelectedItem();
                item.markSearchWindowForDestroy();
                item.setRelatedResource(code);
                geographicalInformationEditionForm.validate(false);
            }
        });
        return item;
    }

    private void showCreateSegregationWindow() {
        createSegregationWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().actionSegregate(), SrmWebConstants.FORM_LIST_MAX_RESULTS,
                new org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction<MetamacWebCriteria>() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                        getUiHandlers().retrieveVariableElementsByVariableForSegregationOperation(firstResult, maxResults, webCriteria.getCriteria(), variableElementDto.getVariable().getUrn());
                    }
                });

        createSegregationWindow.retrieveItems();

        createSegregationWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                List<RelatedResourceDto> selectedVariableElements = createSegregationWindow.getSelectedResources();
                createSegregationWindow.markForDestroy();
                // Segregate the selected variable elements
                String sourceUrn = variableElementDto.getUrn();
                List<String> targetUrns = RelatedResourceUtils.getUrnsFromRelatedResourceDtos(selectedVariableElements);
                getUiHandlers().createSegregation(sourceUrn, targetUrns);
            }
        });
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void setCodelistsForVariableElementGeographicalGranularity(GetRelatedResourcesResult result) {
        ((SearchCodeForVariableElementGeographicalGranularity) geographicalInformationEditionForm.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).setItemSchemes(result.getRelatedResourceDtos(),
                result.getFirstResultOut(), result.getTotalResults());
    }

    @Override
    public void setCodesForVariableElementGeographicalGranularity(GetRelatedResourcesResult result) {
        ((SearchCodeForVariableElementGeographicalGranularity) geographicalInformationEditionForm.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).setItems(result.getRelatedResourceDtos(),
                result.getFirstResultOut(), result.getTotalResults());
    }

    // ------------------------------------------------------------------------------------------------------------
    // CLICK HANDLERS
    // ------------------------------------------------------------------------------------------------------------

    private ListRecordNavigationClickHandler getListRecordNavigationClickHandler() {
        return new ListRecordNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }

    private CustomLinkItemNavigationClickHandler getCustomLinkItemNavigationClickHandler() {
        return new CustomLinkItemNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }
}

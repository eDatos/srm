package org.siemac.metamac.internal.web.dsd.view;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.domain_dto.ComponentDto;
import org.siemac.metamac.domain_dto.FacetDto;
import org.siemac.metamac.domain_dto.RepresentationDto;
import org.siemac.metamac.domain_enum.domain.TypeRepresentationEnum;
import org.siemac.metamac.internal.web.client.MetamacInternalWeb;
import org.siemac.metamac.internal.web.dsd.enums.RepresentationTypeEnum;
import org.siemac.metamac.internal.web.dsd.presenter.DsdPrimaryMeasureTabPresenter;
import org.siemac.metamac.internal.web.dsd.utils.CommonUtils;
import org.siemac.metamac.internal.web.dsd.utils.FacetFormUtils;
import org.siemac.metamac.internal.web.dsd.view.handlers.DsdPrimaryMeasureTabUiHandlers;
import org.siemac.metamac.internal.web.dsd.widgets.AnnotationsPanel;
import org.siemac.metamac.internal.web.dsd.widgets.FacetForm;
import org.siemac.metamac.internal.web.dsd.widgets.StaticFacetForm;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.HasChangeHandlers;
import com.smartgwt.client.widgets.layout.VLayout;

public class DsdPrimaryMeasureTabViewImpl extends ViewWithUiHandlers<DsdPrimaryMeasureTabUiHandlers> implements DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabView {

    private List<ExternalItemBtDto>     concepts;
    private List<ExternalItemBtDto>     codeLists;

    private VLayout                     panel;

    private InternationalMainFormLayout mainFormLayout;

    private AnnotationsPanel            viewAnnotationsPanel;
    private AnnotationsPanel            editionAnnotationsPanel;

    // VIEW FORM

    private ViewTextItem                staticConceptItem;
    private ViewTextItem                staticRepresentationTypeItem;
    private ViewTextItem                staticCodeListItem;
    private StaticFacetForm             staticFacetForm;

    // EDITION FORM

    private GroupDynamicForm            form;
    private ExternalSelectItem          conceptItem;
    private SelectItem                  representationTypeItem;
    private SelectItem                  codeListItem;
    private FacetForm                   facetForm;

    @Inject
    public DsdPrimaryMeasureTabViewImpl() {
        super();
        panel = new VLayout();

        mainFormLayout = new InternationalMainFormLayout();
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setTranslationsShowed(mainFormLayout.getTranslateToolStripButton().isSelected());
            }
        });

        createViewForm();
        createEditionForm();

        panel.addMember(mainFormLayout);
    }

    /**
     * Creates and returns the view layout
     * 
     * @return
     */
    private void createViewForm() {
        staticConceptItem = new ViewTextItem("concept-pmeas-view", MetamacInternalWeb.getConstants().concept());
        staticRepresentationTypeItem = new ViewTextItem("repr-pmeas", MetamacInternalWeb.getConstants().dsdRepresentation());
        staticCodeListItem = new ViewTextItem("repr-enum-code-list-view", MetamacInternalWeb.getConstants().dsdCodeList());

        GroupDynamicForm staticForm = new GroupDynamicForm(MetamacInternalWeb.getConstants().dsdPrimaryMeasureDetails());
        staticForm.setFields(staticConceptItem, staticRepresentationTypeItem, staticCodeListItem);

        staticFacetForm = new StaticFacetForm();

        // Annotations
        viewAnnotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(staticForm);
        mainFormLayout.addViewCanvas(staticFacetForm);
        mainFormLayout.addViewCanvas(viewAnnotationsPanel);
    }

    /**
     * Creates and returns the edition layout
     * 
     * @return
     */
    private void createEditionForm() {
        conceptItem = new ExternalSelectItem("concept-pmeas", MetamacInternalWeb.getConstants().concept());
        conceptItem.setRequired(true);

        representationTypeItem = new SelectItem("repr-pmeas", MetamacInternalWeb.getConstants().dsdRepresentation());
        representationTypeItem.setType("comboBox");
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (RepresentationTypeEnum r : RepresentationTypeEnum.values()) {
            String value = MetamacInternalWeb.getCoreMessages().getString(MetamacInternalWeb.getCoreMessages().representationTypeEnum() + r.getName());
            valueMap.put(r.toString(), value);
        }
        representationTypeItem.setValueMap(valueMap);
        representationTypeItem.setRedrawOnChange(true);
        // Show FacetForm if RepresentationTypeEnum = NON_NUMERATED
        representationTypeItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
            }
        });

        codeListItem = new SelectItem("repr-enum-code-list", MetamacInternalWeb.getConstants().dsdCodeList());
        codeListItem.setType("comboBox");
        // Show CodeList if RepresentationTypeEnum = ENUMERATED
        codeListItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CommonUtils.isRepresentationTypeEnumerated(representationTypeItem.getValueAsString());
            }
        });

        form = new GroupDynamicForm(MetamacInternalWeb.getConstants().dsdPrimaryMeasureDetails());
        form.setFields(conceptItem, representationTypeItem, codeListItem);

        facetForm = new FacetForm();

        // Annotations
        editionAnnotationsPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(form);
        mainFormLayout.addEditionCanvas(facetForm);
        mainFormLayout.addEditionCanvas(editionAnnotationsPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setConceptSchemes(List<ExternalItemBtDto> conceptSchemes) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (ExternalItemBtDto scheme : conceptSchemes) {
            map.put(scheme.getCodeId(), scheme.getCodeId());
        }
        conceptItem.setSchemesValueMap(map);
    }

    @Override
    public void setConcepts(List<ExternalItemBtDto> concepts) {
        this.concepts = concepts;
        LinkedHashMap<String, String> conceptsMap = new LinkedHashMap<String, String>();
        for (ExternalItemBtDto concept : concepts) {
            conceptsMap.put(concept.getCodeId(), concept.getCodeId());
        }
        conceptItem.setItemsValueMap(conceptsMap);
    }

    @Override
    public void setCodeLists(List<ExternalItemBtDto> codeLists) {
        this.codeLists = codeLists;
        LinkedHashMap<String, String> codeListsMap = new LinkedHashMap<String, String>();
        for (ExternalItemBtDto codeList : codeLists) {
            codeListsMap.put(codeList.getCodeId(), codeList.getCodeId());
        }
        codeListItem.setValueMap(codeListsMap);
    }

    @Override
    public void setDsdPrimaryMeasure(ComponentDto componentDto) {
        mainFormLayout.setViewMode();

        setPrimaryMeasureViewMode(componentDto);
        setPrimaryMeasureEditionMode(componentDto);
    }

    private void setPrimaryMeasureViewMode(ComponentDto componentDto) {
        // Concept
        staticConceptItem.setValue(componentDto.getCptIdRef() == null ? null : componentDto.getCptIdRef().getCodeId());

        // Representation
        staticFacetForm.hide();
        staticCodeListItem.hide();
        staticCodeListItem.clearValue();
        staticRepresentationTypeItem.clearValue();
        staticFacetForm.clearValues();
        if (componentDto.getLocalRepresentation() != null) {
            if (RepresentationTypeEnum.ENUMERATED.equals(componentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                staticCodeListItem.setValue(componentDto.getLocalRepresentation().getEnumerated().getCodeId());
                staticRepresentationTypeItem.setValue(MetamacInternalWeb.getCoreMessages().representationTypeEnumENUMERATED());
                staticCodeListItem.show();
                // Facet
            } else if (RepresentationTypeEnum.NON_NUMERATED.equals(componentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                staticRepresentationTypeItem.setValue(MetamacInternalWeb.getCoreMessages().representationTypeEnumNON_NUMERATED());
                FacetDto facetDto = componentDto.getLocalRepresentation().getNonEnumerated();
                staticFacetForm.setFacet(facetDto);
                staticFacetForm.show();
            }
        }

        // Annotations
        viewAnnotationsPanel.setAnnotations(componentDto.getAnnotations());
    }

    private void setPrimaryMeasureEditionMode(ComponentDto componentDto) {
        // Concept
        conceptItem.clearValue(); // Clear concept value: which is the scheme of a concept?

        // Representation
        codeListItem.clearValue();
        representationTypeItem.clearValue();
        facetForm.clearValues();
        facetForm.clearValues();
        if (componentDto.getLocalRepresentation() != null) {
            // Code List
            if (RepresentationTypeEnum.ENUMERATED.equals(componentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                // codeListItem.setValue(componentDto.getLocalRepresentation().getEnumCodeList().getCodeId());
                codeListItem.clearValue(); // don´t know the concept (which is the scheme?), so code list neither
                representationTypeItem.setValue(RepresentationTypeEnum.ENUMERATED.toString());
                // Facet
            } else if (RepresentationTypeEnum.NON_NUMERATED.equals(componentDto.getLocalRepresentation().getTypeRepresentationEnum())) {
                representationTypeItem.setValue(RepresentationTypeEnum.NON_NUMERATED.toString());
                FacetDto facetDto = componentDto.getLocalRepresentation().getNonEnumerated();
                facetForm.setFacet(facetDto);
            }
        }
        FacetFormUtils.setFacetFormVisibility(facetForm, representationTypeItem.getValueAsString());
        form.redraw();

        // Clear errors
        form.clearErrors(true);
        facetForm.clearErrors(true);

        // Annotations
        editionAnnotationsPanel.setAnnotations(componentDto.getAnnotations());
    }

    @Override
    public ComponentDto getDsdPrimaryMeasure(ComponentDto componentDto) {
        // Concept
        componentDto.setCptIdRef(conceptItem.getSelectedExternalItem(concepts));

        // Representation
        if (representationTypeItem.getValue() != null && !representationTypeItem.getValue().toString().isEmpty()) {
            RepresentationTypeEnum representationType = RepresentationTypeEnum.valueOf(representationTypeItem.getValue().toString());

            if (componentDto.getLocalRepresentation() == null) {
                componentDto.setLocalRepresentation(new RepresentationDto());
            }

            // Code List
            if (RepresentationTypeEnum.ENUMERATED.equals(representationType)) {
                componentDto.getLocalRepresentation().setTypeRepresentationEnum(TypeRepresentationEnum.ENUMERATED);
                componentDto.getLocalRepresentation().setEnumerated(ExternalItemUtils.getExternalItemBtDtoFromCodeId(codeLists, codeListItem.getValueAsString()));
                componentDto.getLocalRepresentation().setNonEnumerated(null);
                // Facet
            } else if (RepresentationTypeEnum.NON_NUMERATED.equals(representationType)) {
                componentDto.getLocalRepresentation().setTypeRepresentationEnum(TypeRepresentationEnum.TEXT_FORMAT);
                FacetDto facetDto = facetForm.getFacet(componentDto.getLocalRepresentation().getNonEnumerated() == null ? new FacetDto() : componentDto.getLocalRepresentation().getNonEnumerated());
                componentDto.getLocalRepresentation().setNonEnumerated(facetDto);
                componentDto.getLocalRepresentation().setEnumerated(null);
            }
        } else {
            // No representation
            componentDto.setLocalRepresentation(null);
        }

        // Annotations
        componentDto.getAnnotations().clear();
        componentDto.getAnnotations().addAll(editionAnnotationsPanel.getAnnotations());
        return componentDto;
    }

    @Override
    public boolean validate() {
        return Visibility.HIDDEN.equals(facetForm.getVisibility()) ? form.validate(false) && conceptItem.validateItem() : (form.validate(false) && facetForm.validate(false) && conceptItem
                .validateItem());
    }

    @Override
    public void onPrimaryMeasureSaved(ComponentDto componentDto) {
        setDsdPrimaryMeasure(componentDto);
        mainFormLayout.setViewMode();
    }

    @Override
    public HasClickHandlers getSave() {
        return mainFormLayout.getSave();
    }

    @Override
    public HasChangeHandlers onConceptSchemeChange() {
        return conceptItem.getSchemeItem();
    }

    @Override
    public HasChangeHandlers onConceptChange() {
        return conceptItem.getItem();
    }

    @Override
    public HasChangeHandlers onRepresentationTypeChange() {
        return representationTypeItem;
    }

    public void setTranslationsShowed(boolean translationsShowed) {
        viewAnnotationsPanel.setTranslationsShowed(translationsShowed);
        editionAnnotationsPanel.setTranslationsShowed(translationsShowed);
    }

}

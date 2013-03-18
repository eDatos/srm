package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.model.ds.AnnotationDS;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.model.record.AnnotationRecord;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.utils.UrlUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationDto;
import com.google.gwt.resources.client.ImageResource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class AnnotationsPanel extends VLayout {

    private AnnotationsListGrid listGrid;
    private DynamicForm         form;
    private SelectItem          selectItem;
    private boolean             translationsShowed;

    private boolean             viewMode;

    private Img                 annotationImg;
    private Img                 addAnnotationImg;

    private RelatedResourceDto  maintainer;

    public AnnotationsPanel(boolean viewMode) {
        super();
        this.viewMode = viewMode;

        HLayout imgLayout = new HLayout();
        imgLayout.setMembersMargin(10);
        imgLayout.setAutoHeight();
        imgLayout.setBackgroundColor("#eaf1fb");
        imgLayout.setLayoutMargin(5);
        imgLayout.setStyleName("annotationPanel");

        // Annotation icon

        annotationImg = new Img(GlobalResources.RESOURCE.annotations().getURL());
        annotationImg.setTooltip(getConstants().annotations());
        annotationImg.setSize(32);
        annotationImg.setAlign(Alignment.LEFT);
        imgLayout.addMember(annotationImg);

        // Add annotation icon

        addAnnotationImg = new Img(GlobalResources.RESOURCE.addAnnotation().getURL());
        addAnnotationImg.setTooltip(getConstants().addAnnotation());
        addAnnotationImg.setCursor(Cursor.POINTER);
        addAnnotationImg.setName("note-img");
        addAnnotationImg.setSize(32);
        addAnnotationImg.setAlign(Alignment.LEFT);
        addAnnotationImg.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                listGrid.startEditingNew();
            }
        });
        imgLayout.addMember(addAnnotationImg);

        // Show translations form

        form = new DynamicForm();
        form.setAutoHeight();
        selectItem = new SelectItem("lang");
        selectItem.setShowTitle(false);
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> valueIcons = new LinkedHashMap<String, String>();
        for (final String locale : ApplicationEditionLanguages.getLocales()) {
            String iconSrc = ((ImageResource) org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.getResource(locale)).getURL();
            valueMap.put(locale, MetamacWebCommon.getEnumConstants().getString(MetamacWebCommon.getEnumConstants().locale() + locale)); // Set locale name (English, Spanish...)
            valueIcons.put(locale, iconSrc);
        }
        selectItem.setValueMap(valueMap);
        selectItem.setValueIcons(valueIcons);
        selectItem.setValue(ApplicationEditionLanguages.getCurrentLocale());
        selectItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return translationsShowed;
            }
        });
        selectItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                if (event.getValue() != null && event.getValue() instanceof String) {
                    String selectedLocale = (String) event.getValue();
                    changeAnnotationsLanguage(selectedLocale);
                }
            }
        });
        form.setFields(selectItem);
        imgLayout.addMember(form);

        // Annotations list

        listGrid = new AnnotationsListGrid();
        listGrid.setCellHeight(40);
        listGrid.setAutoFitMaxRecords(10);
        listGrid.setAutoFitData(Autofit.VERTICAL);
        listGrid.setShowRowNumbers(true);
        listGrid.setLeaveScrollbarGap(false);
        listGrid.setAlternateRecordStyles(false);
        listGrid.setAnimateRollUnder(true);
        listGrid.setSelectionType(SelectionStyle.SIMPLE);
        listGrid.setShowSelectionCanvas(true);
        listGrid.setAnimateSelectionUnder(true);
        listGrid.setWrapCells(true);
        listGrid.setBorder("1px solid #A7ABB4");
        listGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
        listGrid.setCanEdit(!viewMode);
        listGrid.setCanRemoveRecords(!viewMode);
        listGrid.setRemoveFieldTitle(getConstants().actionDelete());
        listGrid.setRemoveIcon(org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        listGrid.setRemoveIconSize(14);
        listGrid.addEditCompleteHandler(new EditCompleteHandler() {

            @Override
            public void onEditComplete(EditCompleteEvent event) {
                if (event.getNewValues() != null && event.getNewValues().size() > 0) {
                    Record record = listGrid.getRecord(event.getRowNum());
                    AnnotationDto annotationDto = new AnnotationDto();
                    if (record.getAttributeAsObject(AnnotationDS.ANNOTATION_DTO) != null && record.getAttributeAsObject(AnnotationDS.ANNOTATION_DTO) instanceof AnnotationDto) {
                        annotationDto = (AnnotationDto) record.getAttributeAsObject(AnnotationDS.ANNOTATION_DTO);
                    }
                    // Title
                    if (event.getNewValues().containsKey(AnnotationDS.TITLE)) {
                        annotationDto.setTitle(event.getNewValues().get(AnnotationDS.TITLE) != null ? (String) event.getNewValues().get(AnnotationDS.TITLE) : null);
                    }
                    // Text
                    if (event.getNewValues().containsKey(AnnotationDS.TEXT)) {
                        String locale = translationsShowed ? selectItem.getValueAsString() : ApplicationEditionLanguages.getCurrentLocale();
                        String text = event.getNewValues().get(AnnotationDS.TEXT) != null ? (String) event.getNewValues().get(AnnotationDS.TEXT) : null;
                        annotationDto.setText(InternationalStringUtils.updateInternationalString(locale, annotationDto.getText(), text));
                    }
                    // URL
                    if (event.getNewValues().containsKey(AnnotationDS.URL)) {
                        String url = event.getNewValues().get(AnnotationDS.URL) != null ? UrlUtils.addHttpPrefixIfNeeded((String) event.getNewValues().get(AnnotationDS.URL)) : null;
                        annotationDto.setUrl(url);
                        listGrid.getRecord(event.getRowNum()).setAttribute(AnnotationDS.URL, url);
                    }
                    listGrid.getRecord(event.getRowNum()).setAttribute(AnnotationDS.ANNOTATION_DTO, annotationDto);
                }
            }
        });

        // ListGrid fields

        ListGridField textField = new ListGridField(AnnotationDS.TEXT, getConstants().annotationText());
        textField.setShowHover(true);
        textField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return record.getAttribute(AnnotationDS.TEXT);
            }
        });
        textField.setWidth("75%");

        ListGridField urlField = new ListGridField(AnnotationDS.URL, getConstants().annotationUrl());
        urlField.setType(ListGridFieldType.LINK);

        ListGridField isTextEditable = new ListGridField(AnnotationDS.IS_TEXT_EDITABLE, "is-editable");
        isTextEditable.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        listGrid.setFields(textField, urlField, isTextEditable);

        // ListGrid style

        Canvas rollUnderCanvasProperties = new Canvas();
        rollUnderCanvasProperties.setAnimateFadeTime(600);
        rollUnderCanvasProperties.setAnimateShowEffect(AnimationEffect.FADE);
        rollUnderCanvasProperties.setBackgroundColor("#ffe973");
        rollUnderCanvasProperties.setOpacity(50);
        listGrid.setRollUnderCanvasProperties(rollUnderCanvasProperties);
        Canvas background = new Canvas();
        background.setBackgroundColor("#FFFFE0");
        listGrid.setBackgroundComponent(background);

        addMember(imgLayout);
        addMember(listGrid);
    }
    public void setAnnotations(Set<AnnotationDto> annotations, RelatedResourceDto maintainer) {
        this.maintainer = maintainer;

        // Clear annotations
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        listGrid.deselectAllRecords();

        String selectedLocale = ApplicationEditionLanguages.getCurrentLocale();
        if (selectItem.getValueAsString() != null && !selectItem.getValueAsString().isEmpty()) {
            selectedLocale = selectItem.getValueAsString();
        }

        // Set annotations in the selected locale
        for (AnnotationDto annotationDto : annotations) {
            AnnotationRecord record = RecordUtils.getAnnotationRecord(annotationDto, selectedLocale);
            listGrid.addData(record);
        }

        // Show/hide Add and Remove icons
        setCanAddOrRemoveAnnotations(viewMode, maintainer);
    }

    public Set<AnnotationDto> getAnnotations() {
        Set<AnnotationDto> annotations = new HashSet<AnnotationDto>();
        ListGridRecord[] records = listGrid.getRecords();
        for (int i = 0; i < records.length; i++) {
            AnnotationDto annotationDto = (AnnotationDto) records[i].getAttributeAsObject(AnnotationDS.ANNOTATION_DTO);
            annotations.add(annotationDto);
        }
        return annotations;
    }

    public void setTranslationsShowed(boolean translationsShowed) {
        this.translationsShowed = translationsShowed;
        form.markForRedraw();
        // Show annotations in current locale
        if (!ApplicationEditionLanguages.getCurrentLocale().equals(selectItem.getValueAsString())) {
            changeAnnotationsLanguage(ApplicationEditionLanguages.getCurrentLocale());
        }
        if (translationsShowed) {
            selectItem.setValue(ApplicationEditionLanguages.getCurrentLocale());
        }
    }

    private void changeAnnotationsLanguage(String locale) {
        for (int i = 0; i < listGrid.getRecords().length; i++) {
            if (listGrid.getRecord(i).getAttribute(AnnotationDS.ANNOTATION_DTO) != null) {
                AnnotationDto annotationDto = (AnnotationDto) listGrid.getRecord(i).getAttributeAsObject(AnnotationDS.ANNOTATION_DTO);
                listGrid.getRecord(i).setAttribute(AnnotationDS.TEXT, InternationalStringUtils.getLocalisedString(annotationDto.getText(), locale));
                listGrid.getRecord(i).setAttribute(AnnotationDS.IS_TEXT_EDITABLE, InternationalStringUtils.isLocalisedStringModifiable(annotationDto.getText(), locale));
            }
        }
        listGrid.redraw();
    }

    private void setCanAddOrRemoveAnnotations(boolean viewMode, RelatedResourceDto maintainer) {
        annotationImg.hide();
        addAnnotationImg.hide();
        if (viewMode) {
            annotationImg.show();
        } else {
            if (CommonUtils.isDefaultMaintainer(maintainer)) {
                addAnnotationImg.show();
            } else {
                annotationImg.show();
            }
        }
    }

    private class AnnotationsListGrid extends ListGrid {

        @Override
        protected boolean canEditCell(int rowNum, int colNum) {
            if (viewMode) {

                // In view mode, NEVER edit cell values
                return false;

            } else {

                // In edition mode, if the maintainer is not the default one:
                // - do not edit the URL
                // - do not edit the text value if it is marked as unmodifiable

                if (!CommonUtils.isDefaultMaintainer(maintainer)) {
                    String fieldName = listGrid.getField(colNum) != null ? listGrid.getField(colNum).getName() : null;

                    // URL cell: never update annotations URL if the annotation maintainer is not the default one
                    if (StringUtils.equals(AnnotationDS.URL, fieldName)) {
                        return false;
                    }

                    // Text cell: update text only if the LocalisedString is modifiable
                    Record record = listGrid.getRecord(rowNum);
                    if (record != null) {
                        if (record instanceof AnnotationRecord) {
                            return ((AnnotationRecord) record).getIsTextEditable();
                        } else {
                            // Nothing happens. If the record is not instance of AnnotationRecord is because has been recently created.
                        }
                    }
                }
                return true;
            }
        }
    }
}

package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.siemac.metamac.srm.web.client.model.ds.AnnotationDS;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.dsd.model.record.AnnotationRecord;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.UrlUtils;

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

    private ListGrid    listGrid;
    private DynamicForm form;
    private SelectItem  selectItem;
    private boolean     translationsShowed;

    public AnnotationsPanel(boolean viewOnly) {
        super();

        HLayout imgLayout = new HLayout();
        imgLayout.setMembersMargin(10);
        imgLayout.setAutoHeight();
        imgLayout.setBackgroundColor("#eaf1fb");
        imgLayout.setLayoutMargin(5);
        imgLayout.setStyleName("annotationPanel");

        if (viewOnly) {
            Img annotationImg = new Img(GlobalResources.RESOURCE.annotations().getURL());
            annotationImg.setTooltip(getConstants().annotations());
            annotationImg.setSize(32);
            annotationImg.setAlign(Alignment.LEFT);
            imgLayout.addMember(annotationImg);
        } else {
            Img addAnnotationImg = new Img(GlobalResources.RESOURCE.addAnnotation().getURL());
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
        }

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
        selectItem.setValue(InternationalStringUtils.getCurrentLocale());
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

        listGrid = new ListGrid();
        listGrid.setCellHeight(40);
        listGrid.setAutoFitMaxRecords(10);
        listGrid.setAutoFitData(Autofit.VERTICAL);
        listGrid.setShowRowNumbers(true);
        // grid.setStyleName("annotationGrid");
        listGrid.setLeaveScrollbarGap(false);
        // grid.setCellPadding(4);
        // grid.setCellHeight(40);
        // grid.setNormalCellHeight(40);
        listGrid.setAlternateRecordStyles(false);
        // grid.setShowRollOverCanvas(true);
        listGrid.setAnimateRollUnder(true);
        listGrid.setSelectionType(SelectionStyle.SIMPLE);
        // grid.setBaseStyle("annotationCell");
        listGrid.setShowSelectionCanvas(true);
        listGrid.setAnimateSelectionUnder(true);
        listGrid.setWrapCells(true);
        listGrid.setBorder("1px solid #A7ABB4");
        listGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
        listGrid.setCanEdit(!viewOnly);
        listGrid.setCanRemoveRecords(!viewOnly);
        listGrid.setRemoveFieldTitle(getConstants().actionDelete());
        listGrid.setRemoveIcon(org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        listGrid.setRemoveIconSize(14);
        listGrid.addEditCompleteHandler(new EditCompleteHandler() {

            @Override
            public void onEditComplete(EditCompleteEvent event) {
                // If title is empty or does not exist, delete the new record
                // if (event.getOldRecord() == null) {
                // if (((event.getNewValues().containsKey(AnnotationRecord.TITLE) && ((String) event.getNewValues().get(AnnotationRecord.TITLE) == null)) ||
                // (!event.getNewValues().containsKey(AnnotationRecord.TITLE)))
                // || ((event.getNewValues().containsKey(AnnotationRecord.TEXT) && ((String) event.getNewValues().get(AnnotationRecord.TEXT) == null)) ||
                // (!event.getNewValues().containsKey(AnnotationRecord.TEXT)))) {
                // Record record = grid.getRecord(event.getRowNum());
                // grid.removeData(record);
                // }
                // } else {
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
                        String locale = translationsShowed ? selectItem.getValueAsString() : InternationalStringUtils.getCurrentLocale();
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
                // }
            }
        });

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
        listGrid.setFields(textField, urlField);

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

    public void setAnnotations(Set<AnnotationDto> annotations) {
        // Clear annotations
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        listGrid.deselectAllRecords();
        String selectedLocale = InternationalStringUtils.getCurrentLocale();
        if (selectItem.getValueAsString() != null && !selectItem.getValueAsString().isEmpty()) {
            selectedLocale = selectItem.getValueAsString();
        }
        for (AnnotationDto annotationDto : annotations) {
            AnnotationRecord record = RecordUtils.getAnnotationRecord(annotationDto, selectedLocale);
            listGrid.addData(record);
        }
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
        if (!InternationalStringUtils.getCurrentLocale().equals(selectItem.getValueAsString())) {
            changeAnnotationsLanguage(InternationalStringUtils.getCurrentLocale());
        }
        if (translationsShowed) {
            selectItem.setValue(InternationalStringUtils.getCurrentLocale());
        }
    }

    private void changeAnnotationsLanguage(String locale) {
        for (int i = 0; i < listGrid.getRecords().length; i++) {
            if (listGrid.getRecord(i).getAttribute(AnnotationDS.ANNOTATION_DTO) != null) {
                AnnotationDto annotationDto = (AnnotationDto) listGrid.getRecord(i).getAttributeAsObject(AnnotationDS.ANNOTATION_DTO);
                listGrid.getRecord(i).setAttribute(AnnotationDS.TEXT, InternationalStringUtils.getLocalisedString(annotationDto.getText(), locale));
            }
        }
        listGrid.redraw();
    }

}

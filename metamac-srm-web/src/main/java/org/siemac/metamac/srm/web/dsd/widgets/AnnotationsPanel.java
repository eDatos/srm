package org.siemac.metamac.srm.web.dsd.widgets;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.siemac.metamac.domain.srm.dto.AnnotationDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.dsd.model.record.AnnotationRecord;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.LocaleMock;

import com.google.gwt.resources.client.ImageResource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
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
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class AnnotationsPanel extends VLayout {

    private ListGrid    grid;
    private DynamicForm form;
    private SelectItem  selectItem;
    private boolean     translationsShowed;

    public AnnotationsPanel(boolean viewOnly) {
        super();
        setHeight(200);
        setOverflow(Overflow.AUTO);

        HLayout imgLayout = new HLayout();
        imgLayout.setMembersMargin(10);
        imgLayout.setAutoHeight();
        imgLayout.setBackgroundColor("#eaf1fb");
        imgLayout.setLayoutMargin(5);
        imgLayout.addStyleName("annotationPanel");

        if (viewOnly) {
            Img annotationImg = new Img(GlobalResources.RESOURCE.annotations().getURL());
            annotationImg.setTooltip(MetamacSrmWeb.getConstants().annotations());
            annotationImg.setSize(32);
            annotationImg.setAlign(Alignment.LEFT);
            imgLayout.addMember(annotationImg);
        } else {
            Img addAnnotationImg = new Img(GlobalResources.RESOURCE.addAnnotation().getURL());
            addAnnotationImg.setTooltip(MetamacSrmWeb.getConstants().addAnnotation());
            addAnnotationImg.setCursor(Cursor.POINTER);
            addAnnotationImg.setName("note-img");
            addAnnotationImg.setSize(32);
            addAnnotationImg.setAlign(Alignment.LEFT);
            addAnnotationImg.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    grid.startEditingNew();
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
        for (final String locale : LocaleMock.getLocales()) {
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

        grid = new ListGrid();
        grid.addStyleName("annotationGrid");
        grid.setLeaveScrollbarGap(false);
        grid.setCellPadding(4);
        grid.setCellHeight(40);
        grid.setNormalCellHeight(40);
        grid.setAlternateRecordStyles(false);
        grid.setShowRollOverCanvas(true);
        grid.setAnimateRollUnder(true);
        grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setBaseStyle("annotationCell");
        grid.setShowSelectionCanvas(true);
        grid.setAnimateSelectionUnder(true);
        grid.setWrapCells(true);
        grid.setShowHeader(false);
        grid.setBorder("1px solid #A7ABB4");
        grid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
        grid.setCanEdit(!viewOnly);
        grid.setCanRemoveRecords(!viewOnly);
        grid.setRemoveFieldTitle(MetamacSrmWeb.getConstants().actionDelete());
        grid.setRemoveIcon(org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        grid.setRemoveIconSize(14);
        grid.addEditCompleteHandler(new EditCompleteHandler() {

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
                    Record record = grid.getRecord(event.getRowNum());
                    AnnotationDto annotationDto = new AnnotationDto();
                    if (record.getAttributeAsObject(AnnotationRecord.ANNOTATION_DTO) != null && record.getAttributeAsObject(AnnotationRecord.ANNOTATION_DTO) instanceof AnnotationDto) {
                        annotationDto = (AnnotationDto) record.getAttributeAsObject(AnnotationRecord.ANNOTATION_DTO);
                    }
                    if (event.getNewValues().containsKey(AnnotationRecord.TITLE)) {
                        annotationDto.setTitle(event.getNewValues().get(AnnotationRecord.TITLE) != null ? (String) event.getNewValues().get(AnnotationRecord.TITLE) : new String());
                    }
                    if (event.getNewValues().containsKey(AnnotationRecord.TEXT)) {
                        String locale = translationsShowed ? selectItem.getValueAsString() : InternationalStringUtils.getCurrentLocale();
                        String text = event.getNewValues().get(AnnotationRecord.TEXT) != null ? (String) event.getNewValues().get(AnnotationRecord.TEXT) : new String();
                        annotationDto.setText(InternationalStringUtils.updateInternationalString(locale, annotationDto.getText(), text));
                    }
                    grid.getRecord(event.getRowNum()).setAttribute(AnnotationRecord.ANNOTATION_DTO, annotationDto);
                }
                // }
            }
        });

        ListGridField titleField = new ListGridField(AnnotationRecord.TITLE, "Title");
        titleField.setWidth("15%");
        ListGridField textField = new ListGridField(AnnotationRecord.TEXT, "Annotation");
        grid.setFields(titleField, textField);

        Canvas rollUnderCanvasProperties = new Canvas();
        rollUnderCanvasProperties.setAnimateFadeTime(600);
        rollUnderCanvasProperties.setAnimateShowEffect(AnimationEffect.FADE);
        rollUnderCanvasProperties.setBackgroundColor("#ffe973");
        rollUnderCanvasProperties.setOpacity(50);
        grid.setRollUnderCanvasProperties(rollUnderCanvasProperties);

        Canvas background = new Canvas();
        background.setBackgroundColor("#FFFFE0");
        grid.setBackgroundComponent(background);

        addMember(imgLayout);
        addMember(grid);
    }

    public void setAnnotations(Set<AnnotationDto> annotations) {
        // Clear annotations
        grid.selectAllRecords();
        grid.removeSelectedData();
        grid.deselectAllRecords();
        String selectedLocale = InternationalStringUtils.getCurrentLocale();
        if (selectItem.getValueAsString() != null && !selectItem.getValueAsString().isEmpty()) {
            selectedLocale = selectItem.getValueAsString();
        }
        for (AnnotationDto annotationDto : annotations) {
            AnnotationRecord record = RecordUtils.getAnnotationRecord(annotationDto, selectedLocale);
            grid.addData(record);
        }
    }

    public Set<AnnotationDto> getAnnotations() {
        Set<AnnotationDto> annotations = new HashSet<AnnotationDto>();
        ListGridRecord[] records = grid.getRecords();
        for (int i = 0; i < records.length; i++) {
            AnnotationDto annotationDto = (AnnotationDto) records[i].getAttributeAsObject(AnnotationRecord.ANNOTATION_DTO);
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
        for (int i = 0; i < grid.getRecords().length; i++) {
            if (grid.getRecord(i).getAttribute(AnnotationRecord.ANNOTATION_DTO) != null) {
                AnnotationDto annotationDto = (AnnotationDto) grid.getRecord(i).getAttributeAsObject(AnnotationRecord.ANNOTATION_DTO);
                grid.getRecord(i).setAttribute(AnnotationRecord.TEXT, InternationalStringUtils.getLocalisedString(annotationDto.getText(), locale));
            }
        }
        grid.redraw();
    }

}

package org.siemac.metamac.srm.web.dsd.widgets;

import java.util.HashSet;
import java.util.Set;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.dsd.model.ds.InternationalAnnotationDS;
import org.siemac.metamac.srm.web.dsd.model.record.InternationalAnnotationRecord;
import org.siemac.metamac.srm.web.dsd.utils.DsdRecordUtils;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationDto;
import com.google.gwt.resources.client.ImageResource;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.GroupNode;
import com.smartgwt.client.widgets.grid.GroupTitleRenderer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridFieldIfFunction;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.EditFailedEvent;
import com.smartgwt.client.widgets.grid.events.EditFailedHandler;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class InternationalAnnotationsPanel extends VLayout {

    private final static String NEW_RECORD_START_CHARACTER = "___";

    private BaseCustomListGrid  grid;
    private Img                 annotationImg;
    private Img                 addAnnotationImg;

    private boolean             translationsShowed         = false;
    private boolean             editionMode                = false;

    private Set<Record>         annotationRecords;

    public InternationalAnnotationsPanel() {
        super();
        setHeight(250);
        setOverflow(Overflow.AUTO);

        HLayout imgLayout = new HLayout();
        imgLayout.setAutoHeight();
        imgLayout.setBackgroundColor("#eaf1fb");
        imgLayout.setLayoutMargin(5);
        imgLayout.setStyleName("annotationPanel");

        grid = new BaseCustomListGrid();
        grid.setDataSource(new InternationalAnnotationDS());
        grid.setAutoFetchData(true);
        grid.setAutoSaveEdits(true);
        grid.setStyleName("annotationGrid");
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

        annotationImg = new Img();
        annotationImg.setSize(32);
        annotationImg.setAlign(Alignment.LEFT);
        annotationImg.setSrc(GlobalResources.RESOURCE.annotations().getURL());
        annotationImg.setTooltip(MetamacSrmWeb.getConstants().annotations());

        addAnnotationImg = new Img();
        addAnnotationImg.setVisible(false);
        addAnnotationImg.setSize(32);
        addAnnotationImg.setAlign(Alignment.LEFT);
        addAnnotationImg.setSrc(GlobalResources.RESOURCE.addAnnotation().getURL());
        addAnnotationImg.setTooltip(MetamacSrmWeb.getConstants().addAnnotation());
        addAnnotationImg.setCursor(Cursor.POINTER);
        addAnnotationImg.setName("note-img");
        addAnnotationImg.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String id = NEW_RECORD_START_CHARACTER + String.valueOf(Math.random() * 1000);
                InternationalAnnotationRecord record = new InternationalAnnotationRecord();
                record.setId(id);
                record.setLocale(ApplicationEditionLanguages.getCurrentLocale());
                record.setFlag(((ImageResource) org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.getResource(ApplicationEditionLanguages.getCurrentLocale())).getURL());
                // record.setAnnotationDto(new AnnotationDto());
                grid.startEditingNew(record); // Record is added to the grid as a Record, but should be an InternationalAnnotationRecord
            }
        });
        imgLayout.addMember(annotationImg);
        imgLayout.addMember(addAnnotationImg);

        grid.addEditCompleteHandler(new EditCompleteHandler() {

            @Override
            public void onEditComplete(EditCompleteEvent event) {
                // If translations are not showed, every time a record is saved, others records must be saved too (one record for each locale)
                if (!translationsShowed) {
                    // If title is empty or does not exist, delete the record
                    if ((event.getNewValues().containsKey(InternationalAnnotationRecord.TITLE) && ((String) event.getNewValues().get(InternationalAnnotationRecord.TITLE) == null))
                            || (!event.getNewValues().containsKey(InternationalAnnotationRecord.TITLE))) {
                        Record record = grid.getRecord(event.getRowNum());
                        if (record != null) {
                            grid.removeData(record);
                        }
                        // If title is not empty or null, save record
                    } else {
                        for (String locale : ApplicationEditionLanguages.getLocales()) {
                            if (!ApplicationEditionLanguages.getCurrentLocale().equals(locale)) {
                                if (event.getNewValues().containsKey(InternationalAnnotationRecord.ID)) {
                                    String id = (String) event.getNewValues().get(InternationalAnnotationRecord.ID);
                                    InternationalAnnotationRecord record = new InternationalAnnotationRecord();
                                    record.setId(id);
                                    record.setLocale(locale);
                                    record.setIdDs(id + locale);
                                    record.setFlag(((ImageResource) org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.getResource(locale)).getURL());
                                    String title = (String) (event.getNewValues().containsKey(InternationalAnnotationRecord.TITLE) ? event.getNewValues().get(InternationalAnnotationRecord.TITLE) : "");
                                    record.setTitle(title);
                                    grid.addData(record);
                                }
                            }
                        }
                    }
                }
            }
        });

        grid.addEditorExitHandler(new EditorExitHandler() {

            @Override
            public void onEditorExit(EditorExitEvent event) {
                System.out.println();
            }
        });

        grid.addEditFailedHandler(new EditFailedHandler() {

            @Override
            public void onEditFailed(EditFailedEvent event) {
                // Remove record failed
                System.out.println();
                // grid.removeData(event.getRecord());
            }
        });

        grid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
        grid.setCanEdit(true);
        grid.setCanRemoveRecords(true);
        grid.setRemoveFieldTitle(MetamacSrmWeb.getConstants().actionDelete());
        grid.setRemoveIcon(org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        grid.setRemoveIconSize(14);

        Canvas rollUnderCanvasProperties = new Canvas();
        rollUnderCanvasProperties.setAnimateFadeTime(600);
        rollUnderCanvasProperties.setAnimateShowEffect(AnimationEffect.FADE);
        rollUnderCanvasProperties.setBackgroundColor("#ffe973");
        rollUnderCanvasProperties.setOpacity(50);
        grid.setRollUnderCanvasProperties(rollUnderCanvasProperties);
        Canvas background = new Canvas();
        background.setBackgroundColor("#FFFFE0");
        grid.setBackgroundComponent(background);

        ListGridField id = new ListGridField(InternationalAnnotationRecord.ID, "Id");
        id.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        id.setGroupTitleRenderer(new GroupTitleRenderer() {

            @Override
            public String getGroupTitle(Object groupValue, GroupNode groupNode, ListGridField field, String fieldName, ListGrid grid) {
                return groupNode.getGroupMembers()[0].getAttributeAsString(InternationalAnnotationRecord.TITLE);
                // return ((InternationalAnnotationRecord)groupNode.getGroupMembers()[0]).getTitle();
            }
        });
        ListGridField idDs = new ListGridField(InternationalAnnotationRecord.ID_DS, "Id Ds");
        idDs.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        ListGridField title = new ListGridField(InternationalAnnotationRecord.TITLE, "Title");
        title.setWidth("15%");
        title.setShowIfCondition(new ListGridFieldIfFunction() {

            @Override
            public boolean execute(ListGrid grid, ListGridField field, int fieldNum) {
                return !(grid.getGroupByFields() != null); // If is grouped, translations are being showed
            }
        });
        ListGridField flag = new ListGridField(InternationalAnnotationRecord.FLAG, "Flag", 30);
        flag.setType(ListGridFieldType.IMAGE);
        flag.setCanEdit(false);
        flag.setShowIfCondition(new ListGridFieldIfFunction() {

            @Override
            public boolean execute(ListGrid grid, ListGridField field, int fieldNum) {
                return grid.getGroupByFields() != null; // If is grouped, translations are being showed
            }
        });
        ListGridField locale = new ListGridField(InternationalAnnotationRecord.LOCALE, "Locale");
        locale.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        ListGridField text = new ListGridField(InternationalAnnotationRecord.TEXT, "Annotation");
        ListGridField removeField = new ListGridField(InternationalAnnotationRecord.REMOVE_FIELD, "Remove");
        removeField.setIsRemoveField(true);
        removeField.setShowIfCondition(new ListGridFieldIfFunction() {

            @Override
            public boolean execute(ListGrid grid, ListGridField field, int fieldNum) {
                return editionMode && !translationsShowed;
            }
        });

        grid.setFields(id, idDs, title, locale, flag, text, removeField);
        grid.setGroupStartOpen(GroupStartOpen.ALL);

        addMember(imgLayout);
        addMember(grid);
        setViewMode();
    }

    public void setAnnotations(Set<AnnotationDto> annotations) {
        setViewMode();
        hideTranslations();
        // Clear annotations
        ListGridRecord[] gridRecords = grid.getRecords();
        for (int i = 0; i < gridRecords.length; i++) {
            grid.removeData(gridRecords[i]);
        }

        annotationRecords = new HashSet<Record>();

        int numRows = annotations.size() * ApplicationEditionLanguages.getLocales().size();
        ListGridRecord listGridRecords[] = new ListGridRecord[numRows];

        int indice = 0;
        for (AnnotationDto annotationDto : annotations) {
            for (String locale : ApplicationEditionLanguages.getLocales()) {

                InternationalAnnotationRecord record = DsdRecordUtils.getInternationalAnnotationRecord(annotationDto, locale);

                System.out.println("------------------------------");
                System.out.println(record.getId());
                System.out.println(record.getIdDs());
                System.out.println(record.getCode());
                System.out.println(record.getTitle());
                System.out.println(record.getText());
                System.out.println(record.getLocale());
                System.out.println("------------------------------");

                // grid.addData(record);
                listGridRecords[indice] = record;

                annotationRecords.add(record);
                indice = indice + 1;
            }
        }

        grid.setData(listGridRecords);

        // If translations are not showed, filter records to show only those that are written in current locale
        if (!translationsShowed) {
            grid.clearCriteria();
            Criteria criteria = new Criteria();
            criteria.addCriteria(InternationalAnnotationRecord.LOCALE, ApplicationEditionLanguages.getCurrentLocale());
            grid.fetchData(criteria);
        }
    }

    public Set<AnnotationDto> getAnnotations() {
        Set<AnnotationDto> annotations = new HashSet<AnnotationDto>();

        if (translationsShowed) {
            ListGridRecord[] records = grid.getRecords();
            for (int i = 0; i < records.length; i++) {
                String recordLocale = records[i].getAttributeAsString(InternationalAnnotationRecord.LOCALE);
                if (ApplicationEditionLanguages.getCurrentLocale().equals(recordLocale)) {
                    String recordId = records[i].getAttributeAsString(InternationalAnnotationRecord.ID);
                    Set<ListGridRecord> allRecordsWithId = getRecordsById(records, recordId);
                    AnnotationDto annotationDto = records[i].getAttributeAsObject(InternationalAnnotationRecord.ANNOTATION_DTO) != null ? ((AnnotationDto) records[i]
                            .getAttributeAsObject(InternationalAnnotationRecord.ANNOTATION_DTO)) : new AnnotationDto();
                    annotationDto.setTitle(records[i].getAttributeAsString(InternationalAnnotationRecord.TITLE));
                    annotations.add(updateAnnotationDto(annotationDto, allRecordsWithId));
                }
            }
        } else {
            // ListGridRecord[] records = grid.getRecords();
        }

        return annotations;
    }

    public void showTranslations() {
        translationsShowed = true;
        grid.clearCriteria();
        grid.groupBy(InternationalAnnotationRecord.ID);
        grid.refreshFields(); // Refresh fields to hide record title

        if (editionMode) {
            grid.hideField(InternationalAnnotationRecord.REMOVE_FIELD);
            annotationImg.show();
            addAnnotationImg.hide();
        }
    }

    public void hideTranslations() {
        // Save all records before filter records in grid
        annotationRecords.clear();
        ListGridRecord[] records = grid.getRecords();
        for (int i = 0; i < records.length; i++) {
            annotationRecords.add(records[i]);
        }

        translationsShowed = false;
        Criteria criteria = new Criteria();
        criteria.addCriteria(InternationalAnnotationRecord.LOCALE, ApplicationEditionLanguages.getCurrentLocale());
        grid.fetchData(criteria);
        grid.ungroup();
        grid.refreshFields(); // Refresh fields to hide record title

        if (editionMode) {
            grid.showField(InternationalAnnotationRecord.REMOVE_FIELD);
            annotationImg.hide();
            addAnnotationImg.show();
        }
    }

    public void setViewMode() {
        this.editionMode = false;
        annotationImg.show();
        addAnnotationImg.hide();
        grid.disable();
        grid.hideField(InternationalAnnotationRecord.REMOVE_FIELD);
    }

    public void setEditionMode() {
        this.editionMode = true;
        if (!translationsShowed) {
            annotationImg.hide();
            addAnnotationImg.show();
            grid.showField(InternationalAnnotationRecord.REMOVE_FIELD);
        } else {
            grid.hideField(InternationalAnnotationRecord.REMOVE_FIELD);
        }
        grid.enable();
    }

    /**
     * Returns records with attribute ID with the same value that id parameter
     * 
     * @param records
     * @param id
     * @return
     */
    private Set<ListGridRecord> getRecordsById(ListGridRecord[] records, String id) {
        Set<ListGridRecord> recordSet = new HashSet<ListGridRecord>();
        if (id != null) {
            for (int i = 0; i < records.length; i++) {
                if (id.equals(records[i].getAttributeAsString(InternationalAnnotationRecord.ID))) {
                    recordSet.add(records[i]);
                }
            }
        }
        return recordSet;
    }

    private AnnotationDto updateAnnotationDto(AnnotationDto annotationDto, Set<ListGridRecord> records) {
        InternationalStringDto texts = annotationDto.getText();
        // Texts
        for (ListGridRecord record : records) {
            String locale = record.getAttributeAsString(InternationalAnnotationRecord.LOCALE);
            String annotationText = record.getAttributeAsString(InternationalAnnotationRecord.TEXT);
            texts = InternationalStringUtils.updateInternationalString(locale, texts, annotationText);
        }
        annotationDto.setText(texts);
        return annotationDto;
    }

}

package org.siemac.metamac.srm.web.client.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.client.code.presenter.CodePresenter;
import org.siemac.metamac.srm.web.client.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.code.view.handlers.CodeUiHandlers;
import org.siemac.metamac.srm.web.client.code.widgets.CodesTreeGrid;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.CustomVLayout;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class CodeViewImpl extends ViewWithUiHandlers<CodeUiHandlers> implements CodePresenter.CodeView {

    private VLayout                     panel;
    private InternationalMainFormLayout mainFormLayout;

    private CodesTreeGrid               codesTreeGrid;

    // View forms
    private GroupDynamicForm            identifiersForm;
    private GroupDynamicForm            contentDescriptorsForm;
    private GroupDynamicForm            commentsForm;
    private AnnotationsPanel            annotationsPanel;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            contentDescriptorsEditionForm;
    private GroupDynamicForm            commentsEditionForm;
    private AnnotationsPanel            annotationsEditionPanel;

    private CodeMetamacDto              codeDto;

    @Inject
    public CodeViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // CODES HIERARCHY
        //

        codesTreeGrid = new CodesTreeGrid();

        CustomVLayout codesListGridLayout = new CustomVLayout();
        codesListGridLayout.addMember(new TitleLabel(getConstants().codelistCodes()));
        codesListGridLayout.addMember(codesTreeGrid);

        //
        // CODE
        //

        mainFormLayout = new InternationalMainFormLayout();

        // Translations
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                commentsForm.setTranslationsShowed(translationsShowed);
                commentsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validateEditionForms()) {
                    getUiHandlers().saveCode(getCodeDto());
                }
            }
        });

        createViewForm();
        createEditionForm();

        panel.addMember(codesListGridLayout);
        panel.addMember(mainFormLayout);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(CodeUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        codesTreeGrid.setUiHandlers(uiHandlers);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().codeIdentifiers());
        ViewTextItem code = new ViewTextItem(CodeDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(CodeDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem uri = new ViewTextItem(CodeDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CodeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CodeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersForm.setFields(code, name, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().codeContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(CodeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsForm.setFields(description);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(CodeDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().codeIdentifiers());
        RequiredTextItem code = new RequiredTextItem(CodeDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(CodeDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem uri = new ViewTextItem(CodeDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CodeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CodeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        identifiersEditionForm.setFields(code, name, uri, urn, urnProvider);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().codeContentDescriptors());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(CodeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        contentDescriptorsEditionForm.setFields(description);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(CodeDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        codesTreeGrid.setItems(codelistMetamacDto, itemHierarchyDtos);
        codesTreeGrid.selectItem(codeDto);

        // Security
        mainFormLayout.setCanEdit(CodesClientSecurityUtils.canUpdateCode(codelistMetamacDto.getLifeCycle().getProcStatus()));
    }

    @Override
    public void setCode(CodeMetamacDto codeDto) {
        this.codeDto = codeDto;

        getUiHandlers().retrieveCodesByCodelist(codeDto.getItemSchemeVersionUrn());

        // Set title
        String defaultLocalized = InternationalStringUtils.getLocalisedString(codeDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setCodeViewMode(codeDto);
        setCodeEditionMode(codeDto);
    }

    private void setCodeViewMode(CodeMetamacDto codeDto) {
        // Identifiers Form
        identifiersForm.setValue(CodeDS.CODE, codeDto.getCode());
        identifiersForm.setValue(CodeDS.NAME, RecordUtils.getInternationalStringRecord(codeDto.getName()));
        identifiersForm.setValue(CodeDS.URI, codeDto.getUriProvider());
        identifiersForm.setValue(CodeDS.URN, codeDto.getUrn());
        identifiersForm.setValue(CodeDS.URN_PROVIDER, codeDto.getUrnProvider());

        // Content descriptors
        contentDescriptorsForm.setValue(CodeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(codeDto.getDescription()));
        contentDescriptorsForm.markForRedraw();

        // Comments
        commentsForm.setValue(CodeDS.COMMENTS, RecordUtils.getInternationalStringRecord(codeDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(codeDto.getAnnotations());
    }

    private void setCodeEditionMode(CodeMetamacDto codeDto) {
        // Identifiers Form
        identifiersEditionForm.setValue(CodeDS.CODE, codeDto.getCode());
        identifiersEditionForm.setValue(CodeDS.NAME, RecordUtils.getInternationalStringRecord(codeDto.getName()));
        identifiersEditionForm.setValue(CodeDS.URI, codeDto.getUriProvider());
        identifiersEditionForm.setValue(CodeDS.URN, codeDto.getUrn());
        identifiersEditionForm.setValue(CodeDS.URN_PROVIDER, codeDto.getUrnProvider());

        // Content descriptors
        contentDescriptorsEditionForm.setValue(CodeDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(codeDto.getDescription()));

        // Comments
        commentsEditionForm.setValue(CodeDS.COMMENTS, RecordUtils.getInternationalStringRecord(codeDto.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(codeDto.getAnnotations());
    }

    private CodeMetamacDto getCodeDto() {
        // Identifiers Form
        codeDto.setCode(identifiersEditionForm.getValueAsString(CodeDS.CODE));
        codeDto.setName((InternationalStringDto) identifiersEditionForm.getValue(CodeDS.NAME));

        // Content descriptors
        codeDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(CodeDS.DESCRIPTION));

        // Comments
        codeDto.setComment((InternationalStringDto) commentsEditionForm.getValue(CodeDS.COMMENTS));

        // Annotations
        codeDto.getAnnotations().clear();
        codeDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return codeDto;
    }

    private boolean validateEditionForms() {
        return identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false);
    }
}

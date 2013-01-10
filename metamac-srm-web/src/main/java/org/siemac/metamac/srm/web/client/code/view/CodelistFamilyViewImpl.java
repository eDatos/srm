package org.siemac.metamac.srm.web.client.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.web.client.code.model.ds.CodelistFamilyDS;
import org.siemac.metamac.srm.web.client.code.presenter.CodelistFamilyPresenter;
import org.siemac.metamac.srm.web.client.code.view.handlers.CodelistFamilyUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class CodelistFamilyViewImpl extends ViewWithUiHandlers<CodelistFamilyUiHandlers> implements CodelistFamilyPresenter.CodelistFamilyView {

    private VLayout                     panel;
    private InternationalMainFormLayout mainFormLayout;

    // View forms
    private GroupDynamicForm            identifiersForm;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;

    private CodelistFamilyDto           codelistFamilyDto;

    @Inject
    public CodelistFamilyViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // CODELIST FAMILY
        //

        mainFormLayout = new InternationalMainFormLayout(); // TODO Security
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        panel.addMember(mainFormLayout);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false)) {
                    getUiHandlers().saveCodelistFamily(getCodelistFamilyDto());
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CodelistFamilyPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setCodelistFamily(CodelistFamilyDto family) {
        this.codelistFamilyDto = family;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(family.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setCodelistFamilyViewMode(family);
        setCodelistFamilyEditionMode(family);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().codelistFamilyIdentifiers());
        ViewTextItem code = new ViewTextItem(CodelistFamilyDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(CodelistFamilyDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem urn = new ViewTextItem(CodelistFamilyDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, name, urn);

        mainFormLayout.addViewCanvas(identifiersForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().codelistFamilyIdentifiers());
        RequiredTextItem code = new RequiredTextItem(CodelistFamilyDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(CodelistFamilyDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem urn = new ViewTextItem(CodelistFamilyDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, name, urn);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setCodelistFamilyViewMode(CodelistFamilyDto codelistFamilyDto) {
        // Identifiers
        identifiersForm.setValue(CodelistFamilyDS.CODE, codelistFamilyDto.getCode());
        identifiersForm.setValue(CodelistFamilyDS.URN, codelistFamilyDto.getUrn());
        identifiersForm.setValue(CodelistFamilyDS.NAME, RecordUtils.getInternationalStringRecord(codelistFamilyDto.getName()));
    }

    public void setCodelistFamilyEditionMode(CodelistFamilyDto codelistFamilyDto) {
        // Identifiers
        identifiersEditionForm.setValue(CodelistFamilyDS.CODE, codelistFamilyDto.getCode());
        identifiersEditionForm.setValue(CodelistFamilyDS.URN, codelistFamilyDto.getUrn());
        identifiersEditionForm.setValue(CodelistFamilyDS.NAME, RecordUtils.getInternationalStringRecord(codelistFamilyDto.getName()));
    }

    public CodelistFamilyDto getCodelistFamilyDto() {
        // Identifiers
        codelistFamilyDto.setCode(identifiersEditionForm.getValueAsString(CodelistFamilyDS.CODE));
        codelistFamilyDto.setName((InternationalStringDto) identifiersEditionForm.getValue(CodelistFamilyDS.NAME));

        return codelistFamilyDto;
    }

    @Override
    public void setCodelists(GetCodelistsResult result) {
        // TODO Auto-generated method stub

    }
}

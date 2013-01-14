package org.siemac.metamac.srm.web.client.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.web.client.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.client.code.presenter.VariableElementPresenter;
import org.siemac.metamac.srm.web.client.code.view.handlers.VariableElementUiHandlers;
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

public class VariableElementViewImpl extends ViewWithUiHandlers<VariableElementUiHandlers> implements VariableElementPresenter.VariableElementView {

    private VLayout                     panel;
    private InternationalMainFormLayout mainFormLayout;

    // View forms
    private GroupDynamicForm            identifiersForm;
    private GroupDynamicForm            diffusionDescriptorsForm;

    // Edition forms
    private GroupDynamicForm            identifiersEditionForm;
    private GroupDynamicForm            diffusionDescriptorsEditionForm;

    private VariableElementDto          variableElementDto;

    @Inject
    public VariableElementViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // VARIABLE ELEMENT
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

                diffusionDescriptorsForm.setTranslationsShowed(translationsShowed);
                diffusionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && diffusionDescriptorsEditionForm.validate(false)) {
                    getUiHandlers().saveVariableElement(getVariableElementDto());
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

        String defaultLocalized = InternationalStringUtils.getLocalisedString(variableElementDto.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setVariableElementViewMode(variableElementDto);
        setVariableElementEditionMode(variableElementDto);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(VariableElementDS.NAME, getConstants().nameableArtefactName());
        ViewMultiLanguageTextItem shortName = new ViewMultiLanguageTextItem(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName());
        ViewTextItem urn = new ViewTextItem(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, name, shortName, urn);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        // TODO

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        RequiredTextItem code = new RequiredTextItem(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        MultiLanguageTextItem name = new MultiLanguageTextItem(VariableElementDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName());
        name.setRequired(true);
        ViewTextItem urn = new ViewTextItem(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, name, shortName, urn);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        // TODO

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setVariableElementViewMode(VariableElementDto variableElementDto) {
        // Identifiers
        identifiersForm.setValue(VariableElementDS.CODE, variableElementDto.getCode());
        identifiersForm.setValue(VariableElementDS.URN, variableElementDto.getUrn());
        identifiersForm.setValue(VariableElementDS.NAME, RecordUtils.getInternationalStringRecord(variableElementDto.getName()));
        identifiersForm.setValue(VariableElementDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(variableElementDto.getShortName()));

        // Diffusion descriptors
        // TODO
    }

    public void setVariableElementEditionMode(VariableElementDto variableElementDto) {
        // Identifiers
        identifiersEditionForm.setValue(VariableElementDS.CODE, variableElementDto.getCode());
        identifiersEditionForm.setValue(VariableElementDS.URN, variableElementDto.getUrn());
        identifiersEditionForm.setValue(VariableElementDS.NAME, RecordUtils.getInternationalStringRecord(variableElementDto.getName()));
        identifiersEditionForm.setValue(VariableElementDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(variableElementDto.getShortName()));

        // Diffusion descriptors
        // TODO
    }

    public VariableElementDto getVariableElementDto() {
        // Identifiers
        variableElementDto.setCode(identifiersEditionForm.getValueAsString(VariableElementDS.CODE));
        variableElementDto.setName((InternationalStringDto) identifiersEditionForm.getValue(VariableElementDS.NAME));
        variableElementDto.setShortName((InternationalStringDto) identifiersEditionForm.getValue(VariableElementDS.SHORT_NAME));

        // Diffusion descriptors
        // TODO

        return variableElementDto;
    }
}

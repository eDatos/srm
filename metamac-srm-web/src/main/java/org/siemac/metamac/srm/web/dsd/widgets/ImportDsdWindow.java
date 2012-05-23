package org.siemac.metamac.internal.web.dsd.widgets;

import org.siemac.metamac.internal.web.client.MetamacInternalWeb;
import org.siemac.metamac.internal.web.dsd.listener.UploadListener;
import org.siemac.metamac.internal.web.shared.utils.SharedTokens;

import com.google.gwt.user.client.ui.NamedFrame;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormErrorOrientation;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.VisibilityChangedEvent;
import com.smartgwt.client.widgets.events.VisibilityChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ImportDsdWindow extends Window {

    private static final String TARGET = "uploadTarget";

    private IButton             uploadButton;
    private DynamicForm         form;
    private UploadItem          uploadItem;

    private UploadListener      listener;

    public ImportDsdWindow() {
        super();
        initComplete(this);
        initUploadFailed(this);
        setWidth(360);
        setHeight(130);
        setTitle(MetamacInternalWeb.getMessages().dsdImport());
        setShowMinimizeButton(false);
        setIsModal(true);
        setShowModalMask(true);
        centerInPage();
        addCloseClickHandler(new CloseClickHandler() {

            @Override
            public void onCloseClick(CloseClickEvent event) {
                hide();
            }
        });
        addVisibilityChangedHandler(new VisibilityChangedHandler() {

            @Override
            public void onVisibilityChanged(VisibilityChangedEvent event) {
                if (event.getIsVisible()) {
                    uploadItem.clearValue();
                }
            }
        });

        VLayout body = new VLayout();
        body.setWidth100();
        body.setHeight100();

        // Initialize the form
        form = new DynamicForm();
        form.setErrorOrientation(FormErrorOrientation.RIGHT);
        form.setValidateOnChange(true);
        form.setAutoHeight();
        form.setCanSubmit(true);
        form.setWidth100();
        form.setMargin(8);
        form.setNumCols(2);
        form.setCellPadding(2);
        form.setWrapItemTitles(false);
        form.setTitleSuffix(" ");
        form.setRequiredTitleSuffix(" ");

        // Initialize the hidden frame
        NamedFrame frame = new NamedFrame(TARGET);
        frame.setWidth("1px");
        frame.setHeight("1px");
        frame.setVisible(false);

        form.setEncoding(Encoding.MULTIPART);
        form.setMethod(FormMethod.POST);
        form.setTarget(TARGET);

        StringBuilder url = new StringBuilder();
        url.append(SharedTokens.FILE_UPLOAD_DIR_PATH);
        form.setAction(MetamacInternalWeb.getRelativeURL(url.toString()));

        uploadItem = new UploadItem("filename");
        uploadItem.setTitle(MetamacInternalWeb.getConstants().dsdFileName());
        uploadItem.setWidth(300);
        uploadItem.setRequired(true);

        // Set the fields into the form
        form.setFields(uploadItem);

        // Add the Upload Form and the (hidden) Frame to the main layout container
        body.addMember(form);
        body.addMember(frame);

        HLayout buttonsLayout = new HLayout(2);
        buttonsLayout.setAlign(Alignment.CENTER);

        uploadButton = new IButton(MetamacInternalWeb.getConstants().actionImport());
        uploadButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Object obj = uploadItem.getDisplayValue();
                if (ImportDsdWindow.this.form.validate() && obj != null) {
                    form.submitForm();
                    hide();
                }
            }
        });

        buttonsLayout.addMember(uploadButton);

        VLayout layout = new VLayout();
        layout.setMembersMargin(20);
        layout.setMargin(10);
        layout.addMember(body);
        layout.addMember(buttonsLayout);

        addItem(layout);

    }

    public IButton getUploadButton() {
        return uploadButton;
    }

    public DynamicForm getUploadForm() {
        return form;
    }

    public void setUploadListener(UploadListener listener) {
        this.listener = listener;
    }

    public void uploadComplete(String fileName) {
        if (listener != null)
            listener.uploadComplete(fileName);
    }

    public void uploadFailed(String fileName) {
        if (listener != null) {
            listener.uploadFailed(fileName);
        }
    }

    private native void initComplete(ImportDsdWindow upload) /*-{
                                                             $wnd.uploadComplete = function(fileName) {
                                                             upload.@org.siemac.metamac.internal.web.dsd.widgets.ImportDsdWindow::uploadComplete(Ljava/lang/String;)(fileName);
                                                             };
                                                             }-*/;

    private native void initUploadFailed(ImportDsdWindow upload) /*-{
                                                                 $wnd.uploadFailed = function(fileName) {
                                                                 upload.@org.siemac.metamac.internal.web.dsd.widgets.ImportDsdWindow::uploadFailed(Ljava/lang/String;)(fileName);
                                                                 }
                                                                 }-*/;

}

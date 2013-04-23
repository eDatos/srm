package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.dsd.listener.UploadListener;
import org.siemac.metamac.srm.web.shared.utils.SharedTokens;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.google.gwt.user.client.ui.NamedFrame;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormErrorOrientation;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.VisibilityChangedEvent;
import com.smartgwt.client.widgets.events.VisibilityChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class ImportResourceWindow extends Window {

    private static final String TARGET = "uploadTarget";

    private CustomButtonItem    uploadButton;
    private DynamicForm         form;
    private UploadItem          uploadItem;

    private UploadListener      listener;

    public ImportResourceWindow(String title, String uploadItemTitle) {
        super();

        setHeight(130);
        setWidth(350);
        setTitle(title);
        setShowMinimizeButton(false);

        initComplete(this);
        initUploadFailed(this);
        setAutoSize(true);
        setShowModalMask(true);
        setAutoCenter(true);
        setIsModal(true);
        setShowModalMask(true);
        setAutoCenter(true);
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
        form.setCellPadding(4);
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
        form.setAction(MetamacSrmWeb.getRelativeURL(url.toString()));

        uploadItem = new UploadItem("filename");
        uploadItem.setTitle(uploadItemTitle);
        uploadItem.setWidth(300);
        uploadItem.setRequired(true);

        uploadButton = new CustomButtonItem("button-import", MetamacWebCommon.getConstants().accept());
        uploadButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                Object obj = uploadItem.getDisplayValue();
                if (ImportResourceWindow.this.form.validate() && obj != null) {
                    form.submitForm();
                    hide();
                }
            }
        });

        form.setFields(uploadItem, uploadButton);

        // Add the Upload Form and the (hidden) Frame to the main layout container
        body.addMember(form);
        body.addMember(frame);

        VLayout layout = new VLayout();
        layout.setMembersMargin(20);
        layout.setMargin(10);
        layout.addMember(body);

        addItem(layout);
    }

    public ButtonItem getUploadButton() {
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

    private native void initComplete(ImportResourceWindow upload) /*-{
		$wnd.uploadComplete = function(fileName) {
			upload.@org.siemac.metamac.srm.web.client.widgets.ImportResourceWindow::uploadComplete(Ljava/lang/String;)(fileName);
		};
    }-*/;

    private native void initUploadFailed(ImportResourceWindow upload) /*-{
		$wnd.uploadFailed = function(fileName) {
			upload.@org.siemac.metamac.srm.web.client.widgets.ImportResourceWindow::uploadFailed(Ljava/lang/String;)(fileName);
		}
    }-*/;
}

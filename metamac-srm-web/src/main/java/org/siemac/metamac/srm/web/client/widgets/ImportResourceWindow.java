package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.dsd.listener.UploadListener;
import org.siemac.metamac.srm.web.shared.utils.SharedTokens;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.google.gwt.user.client.ui.NamedFrame;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.VisibilityChangedEvent;
import com.smartgwt.client.widgets.events.VisibilityChangedHandler;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class ImportResourceWindow extends Window {

    protected static final String TARGET = "uploadTarget";

    protected VLayout             body;

    protected UploadForm          form;

    private UploadListener        listener;

    public ImportResourceWindow(String title) {
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
                    form.clearValues(); // TODO do not clear hidden fields
                }
            }
        });

        body = new VLayout();
        body.setWidth100();
        body.setHeight100();

        // Initialize the hidden frame
        NamedFrame frame = new NamedFrame(TARGET);
        frame.setWidth("1px");
        frame.setHeight("1px");
        frame.setVisible(false);

        // Add the Upload Form and the (hidden) Frame to the main layout container
        body.addMember(frame);

        VLayout layout = new VLayout();
        layout.setMembersMargin(20);
        layout.setMargin(10);
        layout.addMember(body);

        addItem(layout);
    }

    public void setForm(UploadForm form) {
        this.form = form;
        form.setTarget(TARGET);
        form.getUploadButton().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                Object obj = ImportResourceWindow.this.form.getUploadItem().getDisplayValue();
                if (ImportResourceWindow.this.form.validate() && obj != null) {
                    ImportResourceWindow.this.form.submitForm();
                    hide();
                }
            }
        });
        body.addMember(form);
    }

    public CustomDynamicForm getForm() {
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

    protected class UploadForm extends CustomDynamicForm {

        protected UploadItem       uploadItem;

        protected CustomButtonItem uploadButton;

        public UploadForm(String uploadItemTitle) {
            setAutoHeight();
            setCanSubmit(true);
            setWidth100();
            setMargin(8);
            setNumCols(2);
            setCellPadding(4);
            setWrapItemTitles(false);

            setEncoding(Encoding.MULTIPART);
            setMethod(FormMethod.POST);

            StringBuilder url = new StringBuilder();
            url.append(SharedTokens.FILE_UPLOAD_DIR_PATH);
            setAction(MetamacSrmWeb.getRelativeURL(url.toString()));

            uploadItem = new UploadItem("file-name");
            uploadItem.setTitle(uploadItemTitle);
            uploadItem.setWidth(300);
            uploadItem.setRequired(true);

            uploadButton = new CustomButtonItem("button-import", MetamacWebCommon.getConstants().accept());

            setFields(uploadItem, uploadButton);
        }

        public UploadItem getUploadItem() {
            return uploadItem;
        }

        public CustomButtonItem getUploadButton() {
            return uploadButton;
        }
    }
}

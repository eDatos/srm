package org.siemac.metamac.srm.web.server.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.lang.shared.LocaleConstants;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.code.domain.TaskImportationInfo;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.WebMessageExceptionsConstants;
import org.siemac.metamac.srm.web.shared.code.enums.VariableElementShapeTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.server.utils.WebTranslateExceptions;
import org.siemac.metamac.web.common.server.utils.ZipUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.siemac.metamac.web.common.shared.exception.MetamacWebExceptionItem;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.v2_1.domain.dto.task.ContentInputDto;
import com.google.inject.Singleton;

@Singleton
@SuppressWarnings("serial")
public class ResourceImportationServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(ResourceImportationServlet.class.getName());

    private File          tmpDir;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger.info("FileUpload Servlet");
        tmpDir = new File(((File) getServletContext().getAttribute("javax.servlet.context.tempdir")).toString());
        logger.info("tmpDir: " + tmpDir.toString());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.process(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check that we have a file upload request
        if (ServletFileUpload.isMultipartContent(request)) {
            processFiles(request, response);
        } else {
            processQuery(request, response);
        }
    }

    @SuppressWarnings("rawtypes")
    private void processFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HashMap<String, String> args = new HashMap<String, String>();

        String fileName = new String();
        InputStream inputStream = null;

        try {
            SrmCoreServiceFacade srmCoreServiceFacade = (SrmCoreServiceFacade) ApplicationContextProvider.getApplicationContext().getBean("srmCoreServiceFacade");

            DiskFileItemFactory factory = new DiskFileItemFactory();
            // Get the temporary directory (this is where files that exceed the threshold will be stored)
            factory.setRepository(tmpDir);

            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            List items = upload.parseRequest(request);

            // Process the uploaded items
            Iterator itr = items.iterator();

            while (itr.hasNext()) {
                DiskFileItem item = (DiskFileItem) itr.next();
                if (item.isFormField()) {
                    args.put(item.getFieldName(), item.getString());
                } else {
                    fileName = item.getName();
                    inputStream = item.getInputStream();
                }
            }

            String successMessage = StringUtils.EMPTY;

            if (ImportableResourceTypeEnum.SDMX_STRUCTURE.name().equals(args.get(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                TaskInfo taskInfo = importSDMXStructure(srmCoreServiceFacade, fileName, inputStream);
                successMessage = updateSuccessMessage(successMessage, taskInfo);

            } else if (ImportableResourceTypeEnum.CODES.name().equals(args.get(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                TaskImportationInfo taskImportationInfo = importCodes(srmCoreServiceFacade, fileName, inputStream, args);
                successMessage = updateSuccessMessage(successMessage, taskImportationInfo);

            } else if (ImportableResourceTypeEnum.CODES_ORDER.name().equals(args.get(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                TaskImportationInfo taskImportationInfo = importCodeOrders(srmCoreServiceFacade, fileName, inputStream, args);
                successMessage = updateSuccessMessage(successMessage, taskImportationInfo);

            } else if (ImportableResourceTypeEnum.VARIABLE_ELEMENTS.name().equals(args.get(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                TaskImportationInfo taskImportationInfo = importVariableElements(srmCoreServiceFacade, fileName, inputStream, args);
                successMessage = updateSuccessMessage(successMessage, taskImportationInfo);

            } else if (ImportableResourceTypeEnum.VARIABLE_ELEMENT_SHAPE.name().equals(args.get(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                TaskImportationInfo taskImportationInfo = importVariableElementShape(srmCoreServiceFacade, fileName, inputStream, args);
                successMessage = updateSuccessMessage(successMessage, taskImportationInfo);
            }

            sendSuccessImportationResponse(response, successMessage);

        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof MetamacException) {
                errorMessage = WebExceptionUtils.serializeToJson((MetamacException) e);
            } else if (e instanceof MetamacWebException) {
                errorMessage = getMessageFromMetamacWebException((MetamacWebException) e);
                errorMessage = StringEscapeUtils.escapeJavaScript(errorMessage);
            } else {
                errorMessage = e.getMessage();
                errorMessage = StringEscapeUtils.escapeJavaScript(errorMessage);
            }
            logger.log(Level.SEVERE, "Error importing file = " + fileName + ". " + e.getMessage());
            logger.log(Level.SEVERE, e.getMessage());
            sendFailedImportationResponse(response, errorMessage);
        }
    }

    private String updateSuccessMessage(String successMessage, TaskInfo taskInfo) {
        return updateSuccessMessage(successMessage, taskInfo.getIsPlannedInBackground());
    }

    private String updateSuccessMessage(String successMessage, TaskImportationInfo taskImportationInfo) {
        if (BooleanUtils.isFalse(taskImportationInfo.getIsPlannedInBackground())) {
            if (taskImportationInfo.getInformationItems() != null && !taskImportationInfo.getInformationItems().isEmpty()) {
                // The importation was executed synchronously
                // There may be information items that have to be shown... that's why these information items (MetamacExceptionItem) are serialized in JSON
                List<MetamacExceptionItem> infoItems = taskImportationInfo.getInformationItems();
                MetamacException infoItemsContainer = new MetamacException();
                infoItemsContainer.getExceptionItems().addAll(infoItems);
                return WebExceptionUtils.serializeToJson(infoItemsContainer);
            }
        }
        return updateSuccessMessage(successMessage, taskImportationInfo.getIsPlannedInBackground());
    }

    private String updateSuccessMessage(String successMessage, Boolean isPlannedInBackground) {
        if (BooleanUtils.isFalse(isPlannedInBackground)) {
            // Synchronous importation
            successMessage = getTranslatedMessage(WebMessageExceptionsConstants.RESOURCE_SUCCESSFUL_IMPORTATION);
        } else {
            // Asynchronous importation
            successMessage = getTranslatedMessage(WebMessageExceptionsConstants.RESOURCE_IMPORTATION_PLANNED_IN_BACKGROUND);
        }
        return successMessage;
    }

    private void processQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    //
    // IMPORTATION METHODS
    //

    // SDMX Structure

    private TaskInfo importSDMXStructure(SrmCoreServiceFacade srmCoreServiceFacade, String fileName, InputStream inputStream) throws MetamacException {

        ContentInputDto contentInputDto = new ContentInputDto();
        contentInputDto.setName(fileName);
        contentInputDto.setInput(inputStream);

        return srmCoreServiceFacade.importSDMXStructureMsg(ServiceContextHolder.getCurrentServiceContext(), contentInputDto);
    }

    // Codes

    private TaskImportationInfo importCodes(SrmCoreServiceFacade srmCoreServiceFacade, String fileName, InputStream inputStream, HashMap<String, String> args) throws MetamacException {

        String codelistUrn = args.get(SrmSharedTokens.UPLOAD_PARAM_CODELIST_URN);
        Boolean updateAlreadyExisting = Boolean.parseBoolean(args.get(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING));

        return srmCoreServiceFacade.importCodesTsv(ServiceContextHolder.getCurrentServiceContext(), codelistUrn, inputStream, fileName, updateAlreadyExisting);
    }

    // Code orders

    private TaskImportationInfo importCodeOrders(SrmCoreServiceFacade srmCoreServiceFacade, String fileName, InputStream inputStream, HashMap<String, String> args) throws MetamacException {

        String codelistUrn = args.get(SrmSharedTokens.UPLOAD_PARAM_CODELIST_URN);

        return srmCoreServiceFacade.importCodeOrdersTsv(ServiceContextHolder.getCurrentServiceContext(), codelistUrn, inputStream, fileName);
    }

    // Variable elements

    private TaskImportationInfo importVariableElements(SrmCoreServiceFacade srmCoreServiceFacade, String fileName, InputStream inputStream, HashMap<String, String> args) throws MetamacException {

        String variableUrn = args.get(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_URN);
        Boolean updateAlreadyExisting = Boolean.parseBoolean(args.get(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING));

        return srmCoreServiceFacade.importVariableElementsTsv(ServiceContextHolder.getCurrentServiceContext(), variableUrn, inputStream, fileName, updateAlreadyExisting);
    }

    // Variable element shape

    private TaskImportationInfo importVariableElementShape(SrmCoreServiceFacade srmCoreServiceFacade, String fileName, InputStream inputStream, HashMap<String, String> args) throws MetamacException,
            IOException, MetamacWebException {

        VariableElementShapeTypeEnum shapeType = VariableElementShapeTypeEnum.valueOf(args.get(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_ELEMENT_SHAPE_TYPE));
        String variableUrn = args.get(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_URN);

        String tempZipFilePathName = inputStreamToTempFile(fileName, inputStream);
        URL shapeFileUrl = unZipCompressedShapefile(tempZipFilePathName);

        if (VariableElementShapeTypeEnum.POLYGON.equals(shapeType)) {
            return srmCoreServiceFacade.importVariableElementsShape(ServiceContextHolder.getCurrentServiceContext(), variableUrn, shapeFileUrl);
        } else if (VariableElementShapeTypeEnum.POINT.equals(shapeType)) {
            return srmCoreServiceFacade.importVariableElementsPoints(ServiceContextHolder.getCurrentServiceContext(), variableUrn, shapeFileUrl);
        }
        return null;
    }

    //
    // UTILITY METHODS
    //

    /**
     * Given an {@link InputStream}, creates a file in the temporal directory
     * 
     * @param fileName
     * @param inputStream
     * @return
     * @throws IOException
     */
    private String inputStreamToTempFile(String fileName, InputStream inputStream) throws IOException {

        OutputStream outputStream = null;
        File outputFile = null;

        try {

            File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");

            outputFile = new File(tempDir, fileName);
            outputStream = new FileOutputStream(outputFile);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (IOException e) {
            throw e;

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }

        return outputFile.getPath();
    }

    /**
     * UnZip a compressed file that should contains at least three kind of files: SHP, SHX and DBF.
     * 
     * @param zipFile
     * @return the URL of the SHP file
     * @throws IOException
     * @throws MetamacWebException
     */
    public URL unZipCompressedShapefile(String zipFile) throws IOException, MetamacWebException {

        File outputFolder = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");

        List<File> unzipFiles = ZipUtils.unzipArchive(new File(zipFile), outputFolder);

        return getShapeUrl(unzipFiles);
    }

    private URL getShapeUrl(List<File> files) throws MalformedURLException, MetamacWebException {
        for (File file : files) {
            String fileName = file.getName();
            if (StringUtils.endsWith(fileName, ".shp")) {
                return file.toURI().toURL();
            }
        }
        throwMetamacWebException(WebMessageExceptionsConstants.IMPORTATION_SHAPE_NOT_FOUND_IN_ZIP);
        return null;
    }

    private void sendSuccessImportationResponse(HttpServletResponse response, String message) throws IOException {
        String action = "if (parent.uploadComplete) parent.uploadComplete('" + message + "');";
        sendResponse(response, action);
    }

    private void sendFailedImportationResponse(HttpServletResponse response, String errorMessage) throws IOException {
        String action = "if (parent.uploadFailed) parent.uploadFailed('" + errorMessage + "');";
        sendResponse(response, action);
    }

    private void sendResponse(HttpServletResponse response, String action) throws IOException {
        response.setContentType("text/html");
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<script type=\"text/javascript\">");
        out.println(action);
        out.println("</script>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
    }

    private String getMessageFromMetamacWebException(MetamacWebException e) {
        List<MetamacWebExceptionItem> items = e.getWebExceptionItems();
        if (items != null && !items.isEmpty()) {
            return items.get(0).getMessage(); // only return the first message error
        }
        return null;
    }

    private void throwMetamacWebException(String exceptionCode) throws MetamacWebException {
        String exceptionMessage = getTranslatedMessage(exceptionCode);
        throw new MetamacWebException(exceptionCode, exceptionMessage);
    }

    private String getTranslatedMessage(String messageCode) {

        WebTranslateExceptions webTranslateExceptions = (WebTranslateExceptions) ApplicationContextProvider.getApplicationContext().getBean("webTranslateExceptions");

        Locale locale = (Locale) ServiceContextHolder.getCurrentServiceContext().getProperty(LocaleConstants.locale);
        return webTranslateExceptions.getTranslatedMessage(messageCode, locale);
    }
}

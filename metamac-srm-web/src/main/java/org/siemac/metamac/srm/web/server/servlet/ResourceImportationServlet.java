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
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.lang.shared.LocaleConstants;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.WebMessageExceptionsConstants;
import org.siemac.metamac.srm.web.shared.code.enums.VariableElementShapeTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
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

            String successMessage = "";

            if (ImportableResourceTypeEnum.SDMX_STRUCTURE.name().equals(args.get(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                TaskInfo taskInfo = importSDMXStructure(srmCoreServiceFacade, fileName, inputStream);
                if (BooleanUtils.isFalse(taskInfo.getIsPlannedInBackground())) { // It was a synchronous importation
                    successMessage = getTranslatedMessage(WebMessageExceptionsConstants.SDMX_RESOURCE_SUCCESSFUL_IMPORTATION);
                }

            } else if (ImportableResourceTypeEnum.CODES.name().equals(args.get(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                importCodes(srmCoreServiceFacade, fileName, inputStream, args);

            } else if (ImportableResourceTypeEnum.CODES_ORDER.name().equals(args.get(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                importCodeOrders(srmCoreServiceFacade, fileName, inputStream, args);

            } else if (ImportableResourceTypeEnum.VARIABLE_ELEMENTS.name().equals(args.get(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                importVariableElements(srmCoreServiceFacade, fileName, inputStream, args);

            } else if (ImportableResourceTypeEnum.VARIABLE_ELEMENT_SHAPE.name().equals(args.get(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                importVariableElementShape(srmCoreServiceFacade, fileName, inputStream, args);
            }

            sendSuccessImportationResponse(response, successMessage);

        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof MetamacException) {
                errorMessage = getMessageFromMetamacException((MetamacException) e);
            } else if (e instanceof MetamacWebException) {
                errorMessage = getMessageFromMetamacWebException((MetamacWebException) e);
            } else {
                errorMessage = e.getMessage();
            }
            logger.log(Level.SEVERE, "Error importing file = " + fileName + ". " + e.getMessage());
            logger.log(Level.SEVERE, e.getMessage());
            sendFailedImportationResponse(response, errorMessage);
        }
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

    private void importCodes(SrmCoreServiceFacade srmCoreServiceFacade, String fileName, InputStream inputStream, HashMap<String, String> args) throws MetamacException {

        String codelistUrn = args.get(SrmSharedTokens.UPLOAD_PARAM_CODELIST_URN);
        Boolean updateAlreadyExisting = Boolean.parseBoolean(args.get(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING));

        srmCoreServiceFacade.importCodesTsvInBackground(ServiceContextHolder.getCurrentServiceContext(), codelistUrn, inputStream, fileName, updateAlreadyExisting);
    }

    // Code orders

    private void importCodeOrders(SrmCoreServiceFacade srmCoreServiceFacade, String fileName, InputStream inputStream, HashMap<String, String> args) throws MetamacException {

        String codelistUrn = args.get(SrmSharedTokens.UPLOAD_PARAM_CODELIST_URN);

        srmCoreServiceFacade.importCodeOrdersTsvInBackground(ServiceContextHolder.getCurrentServiceContext(), codelistUrn, inputStream, fileName);
    }

    // Variable elements

    private void importVariableElements(SrmCoreServiceFacade srmCoreServiceFacade, String fileName, InputStream inputStream, HashMap<String, String> args) throws MetamacException {

        String variableUrn = args.get(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_URN);
        Boolean updateAlreadyExisting = Boolean.parseBoolean(args.get(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING));

        srmCoreServiceFacade.importVariableElementsTsvInBackground(ServiceContextHolder.getCurrentServiceContext(), variableUrn, inputStream, fileName, updateAlreadyExisting);
    }

    // Variable element shape

    private void importVariableElementShape(SrmCoreServiceFacade srmCoreServiceFacade, String fileName, InputStream inputStream, HashMap<String, String> args) throws MetamacException, IOException,
            MetamacWebException {

        VariableElementShapeTypeEnum shapeType = VariableElementShapeTypeEnum.valueOf(args.get(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_ELEMENT_SHAPE_TYPE));
        String variableUrn = args.get(SrmSharedTokens.UPLOAD_PARAM_VARIABLE_URN);

        String tempZipFilePathName = inputStreamToTempFile(fileName, inputStream);
        URL shapeFileUrl = unZipCompressedShapefile(tempZipFilePathName);

        if (VariableElementShapeTypeEnum.POLYGON.equals(shapeType)) {
            srmCoreServiceFacade.importVariableElementsShapeInBackground(ServiceContextHolder.getCurrentServiceContext(), variableUrn, shapeFileUrl);
        } else if (VariableElementShapeTypeEnum.POINT.equals(shapeType)) {
            srmCoreServiceFacade.importVariableElementsPointsInBackground(ServiceContextHolder.getCurrentServiceContext(), variableUrn, shapeFileUrl);
        }
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

        String processedErrorMessage = escapeUnsupportedCharacters(errorMessage);

        String action = "if (parent.uploadFailed) parent.uploadFailed('" + processedErrorMessage + "');";
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

    private String getMessageFromMetamacException(MetamacException e) {
        if (e.getPrincipalException() != null) {
            return e.getPrincipalException().getMessage();
        }
        List<MetamacExceptionItem> items = e.getExceptionItems();
        if (items != null && !items.isEmpty()) {
            return items.get(0).getMessage(); // only return the first message error
        }
        return null;
    }

    private String getMessageFromMetamacWebException(MetamacWebException e) {
        List<MetamacWebExceptionItem> items = e.getWebExceptionItems();
        if (items != null && !items.isEmpty()) {
            return items.get(0).getMessage(); // only return the first message error
        }
        return null;
    }

    private String escapeUnsupportedCharacters(String message) {
        if (!StringUtils.isBlank(message)) {
            return message.replace("'", "");
        }
        return message;
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

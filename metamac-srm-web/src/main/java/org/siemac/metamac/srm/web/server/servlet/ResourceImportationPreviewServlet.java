package org.siemac.metamac.srm.web.server.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.lang.shared.LocaleConstants;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.WebMessageExceptionsConstants;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.server.utils.WebTranslateExceptions;
import org.siemac.metamac.web.common.server.utils.ZipUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.siemac.metamac.web.common.shared.exception.MetamacWebExceptionItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.task.ContentInputDto;
import com.google.inject.Singleton;

@Singleton
@SuppressWarnings("serial")
public class ResourceImportationPreviewServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(ResourceImportationPreviewServlet.class.getName());

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

            List<RelatedResourceDto> resources = previewSDMXStructures(srmCoreServiceFacade, fileName, inputStream);

            String jsonMessage = serializeResourcesJson(resources);

            sendSuccessImportationJsonResponse(response, jsonMessage);

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
            logger.log(Level.SEVERE, "Error previewing file = " + fileName + ". " + e.getMessage());
            logger.log(Level.SEVERE, e.getMessage());
            sendFailedImportationResponse(response, errorMessage);
        }
    }

    private List<RelatedResourceDto> previewSDMXStructures(SrmCoreServiceFacade srmCoreServiceFacade, String fileName, InputStream inputStream) throws MetamacException {

        ContentInputDto contentInputDto = new ContentInputDto();
        contentInputDto.setName(fileName);
        contentInputDto.setInput(inputStream);

        return srmCoreServiceFacade.previewImportSDMXStructure(ServiceContextHolder.getCurrentServiceContext(), contentInputDto);
    }
    //
    // UTILITY METHODS
    //

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

    private void sendSuccessImportationJsonResponse(HttpServletResponse response, String message) throws IOException {
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
        response.setCharacterEncoding("UTF-8");
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

    private String serializeResourcesJson(List<RelatedResourceDto> resources) {
        JSONArray list = new JSONArray();
        for (RelatedResourceDto resource : resources) {
            list.add(serializeResourceJson(resource));
        }
        return list.toJSONString();
    }

    // CAUTION: The values names must be equal to RelatedResourceBaseDS
    // That class can't be used here because it's client only
    private JSONObject serializeResourceJson(RelatedResourceDto resource) {
        JSONObject obj = new JSONObject();
        obj.put("urn-provider", resource.getUrnProvider());
        obj.put("urn", resource.getUrn());
        obj.put("code", resource.getCode());
        obj.put("title", serializeInternationalString(resource.getTitle()));
        return obj;
    }
    private JSONObject serializeInternationalString(InternationalStringDto title) {
        JSONObject obj = new JSONObject();
        for (LocalisedStringDto localised : title.getTexts()) {
            String clean = JSONObject.escape(localised.getLabel());
            obj.put(localised.getLocale(), clean);
        }
        return obj;
    }

}

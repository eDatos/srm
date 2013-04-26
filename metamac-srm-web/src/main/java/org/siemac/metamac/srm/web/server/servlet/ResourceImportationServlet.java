package org.siemac.metamac.srm.web.server.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SharedTokens;
import org.siemac.metamac.web.common.server.ServiceContextHolder;

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

            if (ImportableResourceTypeEnum.SDMX_STRUCTURE.name().equals(args.get(SharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                ContentInputDto contentInputDto = new ContentInputDto();
                contentInputDto.setName(fileName);
                contentInputDto.setInput(inputStream);

                srmCoreServiceFacade.importSDMXStructureMsgInBackground(ServiceContextHolder.getCurrentServiceContext(), contentInputDto);

            } else if (ImportableResourceTypeEnum.CODES.name().equals(args.get(SharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                String codelistUrn = args.get(SharedTokens.UPLOAD_PARAM_CODELIST_URN);
                Boolean updateAlreadyExisting = Boolean.parseBoolean(args.get(SharedTokens.UPDATE_PARAM_UPDATE_EXISTING));

                srmCoreServiceFacade.importCodesCsvInBackground(ServiceContextHolder.getCurrentServiceContext(), codelistUrn, inputStream, fileName, updateAlreadyExisting);

            } else if (ImportableResourceTypeEnum.CODES_ORDER.name().equals(args.get(SharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                String codelistUrn = args.get(SharedTokens.UPLOAD_PARAM_CODELIST_URN);

                srmCoreServiceFacade.importCodeOrdersCsvInBackground(ServiceContextHolder.getCurrentServiceContext(), codelistUrn, inputStream, fileName);

            } else if (ImportableResourceTypeEnum.VARIABLE_ELEMENTS.name().equals(args.get(SharedTokens.UPLOAD_PARAM_FILE_TYPE))) {

                String variableUrn = args.get(SharedTokens.UPLOAD_PARAM_VARIABLE_URN);
                Boolean updateAlreadyExisting = Boolean.parseBoolean(args.get(SharedTokens.UPDATE_PARAM_UPDATE_EXISTING));

                srmCoreServiceFacade.importVariableElementsCsvInBackground(ServiceContextHolder.getCurrentServiceContext(), variableUrn, inputStream, fileName, updateAlreadyExisting);
            }

            sendSuccessImportationResponse(response, fileName);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error importing file = " + fileName + ". " + e.getMessage());
            sendFailedImportationResponse(response, fileName);
        }
    }

    private void processQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private void sendSuccessImportationResponse(HttpServletResponse response, String fileName) throws IOException {
        String action = "if (parent.uploadComplete) parent.uploadComplete('" + fileName + "');";
        sendResponse(response, action);
    }

    private void sendFailedImportationResponse(HttpServletResponse response, String fileName) throws IOException {
        String action = "if (parent.uploadFailed) parent.uploadFailed('" + fileName + "');";
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
}
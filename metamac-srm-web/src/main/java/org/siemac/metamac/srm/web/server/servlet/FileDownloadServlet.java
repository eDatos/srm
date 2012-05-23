package org.siemac.metamac.internal.web.server.servlet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.siemac.metamac.internal.web.shared.utils.SharedTokens;

import com.google.inject.Singleton;

@Singleton
@SuppressWarnings("serial")
public class FileDownloadServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(FileDownloadServlet.class.getName());

    private File          tmpDir;
    private File          destinationDir;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger.info("FileDownload Servlet");
        tmpDir = new File(((File) getServletContext().getAttribute("javax.servlet.context.tempdir")).toString());
        if (!tmpDir.isDirectory()) {
            throw new ServletException(tmpDir.toString() + " is not a directory");
        }
        logger.info("tmpDir: " + tmpDir.toString());
        String realPath = getServletContext().getRealPath("/" + SharedTokens.FILE_DOWNLOAD_DIR_PATH);
        destinationDir = new File(realPath);
        if (!destinationDir.isDirectory()) {
            throw new ServletException(SharedTokens.FILE_DOWNLOAD_DIR_PATH + " is not a directory");
        }
        logger.info("destinationDir: " + destinationDir.toString());
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

    private void processFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private void processQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter(SharedTokens.PARAM_FILE_NAME);
        File file = new File(fileName);
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType(request.getContentType());
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());

        byte[] buffer = new byte[8 * 1024]; // 8k buffer
        DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
        int length = 0;

        try {
            while ((inputStream != null) && ((length = inputStream.read(buffer)) != -1)) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            response.getOutputStream().print(stringWriter.toString());
        } finally {
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        }

    }

}

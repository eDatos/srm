package org.siemac.metamac.srm.web.server.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.sdmx.resources.sdmxml.rest.schemas.v2_1.types.StructureParameterDetailEnum;
import org.sdmx.resources.sdmxml.rest.schemas.v2_1.types.StructureParameterReferencesEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.lang.shared.LocaleConstants;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceResult;
import org.siemac.metamac.srm.web.shared.WebMessageExceptionsConstants;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.server.utils.WebTranslateExceptions;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.siemac.metamac.web.common.shared.exception.MetamacWebExceptionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.ReferenceResourceDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportSDMXResourceActionHandler extends SecurityActionHandler<ExportSDMXResourceAction, ExportSDMXResourceResult> {

    @Autowired
    private SrmCoreServiceFacade   srmCoreServiceFacade;

    @Autowired
    private WebTranslateExceptions webTranslateExceptions;

    public ExportSDMXResourceActionHandler() {
        super(ExportSDMXResourceAction.class);
    }

    @Override
    public ExportSDMXResourceResult executeSecurityAction(ExportSDMXResourceAction action) throws ActionException {

        StructureParameterDetailEnum detail = StructureParameterDetailEnum.valueOf(action.getDetail().name());
        StructureParameterReferencesEnum references = StructureParameterReferencesEnum.valueOf(action.getReferences().name());

        Map<String, String> filesPathNoErrors = new HashMap<String, String>();
        List<MetamacWebExceptionItem> exceptionItems = new LinkedList<MetamacWebExceptionItem>();

        for (String urn : action.getUrns()) {
            try {
                ReferenceResourceDto resourceToExport = new ReferenceResourceDto(urn, action.getResourcesType());
                String file = srmCoreServiceFacade.exportSDMXStructureMsg(ServiceContextHolder.getCurrentServiceContext(), resourceToExport, detail, references);
                filesPathNoErrors.put(urn, file);
            } catch (MetamacException e) {
                exceptionItems.addAll(WebExceptionUtils.getMetamacWebExceptionItems(e.getPrincipalException(), e.getExceptionItems()));
            }
        }

        String resultFilePath = null;
        try {
            resultFilePath = getPathToDownloadableFile(filesPathNoErrors);
        } catch (MetamacWebException e) {
            exceptionItems.addAll(e.getWebExceptionItems());
        }

        String downloadableFileName = getFileNameIfExists(resultFilePath);
        MetamacWebException exception = buildWebExceptionIfItems(exceptionItems);
        return new ExportSDMXResourceResult(downloadableFileName, exception);
    }

    private String getFileNameIfExists(String resultFilePath) {
        if (resultFilePath != null) {
            return new File(resultFilePath).getName();
        }
        return null;
    }

    private MetamacWebException buildWebExceptionIfItems(List<MetamacWebExceptionItem> items) {
        if (!items.isEmpty()) {
            return new MetamacWebException(items);
        }
        return null;
    }

    private String getPathToDownloadableFile(Map<String, String> filesPathNoErrors) throws MetamacWebException {
        String resultFilePath = null;
        if (!filesPathNoErrors.isEmpty()) {
            if (filesPathNoErrors.size() > 1) {
                try {
                    resultFilePath = zipFiles(filesPathNoErrors);
                } catch (Exception e) {
                    MetamacWebExceptionItem exceptionItem = new MetamacWebExceptionItem(ServiceExceptionType.UNKNOWN.getCode(),
                            getTranslatedMessage(WebMessageExceptionsConstants.RESOURCE_EXPORTATION_ZIP_CREATION_ERROR));
                    throw new MetamacWebException(Arrays.asList(exceptionItem));
                }
            } else {
                try {
                    Entry<String, String> entry = filesPathNoErrors.entrySet().iterator().next();
                    String urn = entry.getKey();
                    String path = entry.getValue();

                    File file = renameFileUrnLikeName(path, urn);
                    resultFilePath = file.getAbsolutePath();
                } catch (Exception e) {
                    MetamacWebExceptionItem exceptionItem = new MetamacWebExceptionItem(ServiceExceptionType.UNKNOWN.getCode(),
                            getTranslatedMessage(WebMessageExceptionsConstants.RESOURCE_EXPORTATION_FILE_PREPARATION_ERROR));
                    throw new MetamacWebException(Arrays.asList(exceptionItem));
                }
            }
        }
        return resultFilePath;
    }

    private File renameFileUrnLikeName(String path, String urn) throws IOException {
        String finalName = cleanFileNameFormUrn(urn + ".xml");
        File file = new File(FileUtils.getTempDirectoryPath() + File.separatorChar + finalName);
        if (file.exists()) {
            file.delete();
        }
        File originalTempFile = new File(path);
        FileUtils.copyFile(originalTempFile, file);
        FileUtils.deleteQuietly(originalTempFile);
        return file;
    }

    private String zipFiles(Map<String, String> filePaths) throws Exception {

        byte[] buffer = new byte[1024];
        ZipOutputStream zos = null;
        FileInputStream in = null;

        File zipFile = File.createTempFile("temp-", ".zip");

        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            for (Entry<String, String> filePathEntry : filePaths.entrySet()) {
                String urn = filePathEntry.getKey();
                String filePath = filePathEntry.getValue();
                File file = new File(filePath);
                ZipEntry ze = new ZipEntry(cleanFileNameFormUrn(urn + ".xml"));
                zos.putNextEntry(ze);

                in = new FileInputStream(file);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (zos != null) {
                zos.closeEntry();
                zos.close();
            }
        }
        return zipFile.getAbsolutePath();
    }

    private String cleanFileNameFormUrn(String urn) {
        return urn.replaceAll(":", "_");
    }

    private String getTranslatedMessage(String messageCode) {
        Locale locale = (Locale) ServiceContextHolder.getCurrentServiceContext().getProperty(LocaleConstants.locale);
        return webTranslateExceptions.getTranslatedMessage(messageCode, locale);
    }
}

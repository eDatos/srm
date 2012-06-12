package org.siemac.metamac.data.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.lang.RandomStringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.bt.domain.ExternalItemBt;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.domain.util.dto.ContentInputDto;

public class Mocks {

    private static final ServiceContext serviceContext = new ServiceContext("system", "123456", "junit");

    protected static ServiceContext getServiceContext() {
        return serviceContext;
    }

    public static ContentInputDto createContentInput(File file) throws FileNotFoundException {
        ContentInputDto contentDto = new ContentInputDto();
        contentDto.setInput(new FileInputStream(file));
        contentDto.setName(file.getName());
        return contentDto;
    }

}

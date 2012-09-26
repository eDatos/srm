package org.siemac.metamac.srm.core.base.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class BaseDo2DtoMapperTest {

    @Autowired
    @Qualifier("baseDo2DtoMapper")
    private BaseDo2DtoMapper do2DtoMapper;

    @Test
    public void testLifeCycleMetadata() {
        SrmLifeCycleMetadata source = BaseDoMocks.mockLifeCycle();
        LifeCycleDto target = do2DtoMapper.lifeCycleDoToDto(source);
        assertEquals(source.getProcStatus(), target.getProcStatus());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(source.getProductionValidationDate()), target.getProductionValidationDate());
        assertEquals(source.getProductionValidationUser(), target.getProductionValidationUser());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(source.getDiffusionValidationDate()), target.getDiffusionValidationDate());
        assertEquals(source.getDiffusionValidationUser(), target.getDiffusionValidationUser());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(source.getInternalPublicationDate()), target.getInternalPublicationDate());
        assertEquals(source.getInternalPublicationUser(), target.getInternalPublicationUser());
        assertEquals(CoreCommonUtil.transformDateTimeToDate(source.getExternalPublicationDate()), target.getExternalPublicationDate());
        assertEquals(source.getExternalPublicationUser(), target.getExternalPublicationUser());
    }

}

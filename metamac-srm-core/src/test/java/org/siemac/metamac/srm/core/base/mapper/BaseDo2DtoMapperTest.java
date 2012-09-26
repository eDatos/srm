package org.siemac.metamac.srm.core.base.mapper;

import static org.siemac.metamac.srm.core.base.utils.BaseAsserts.assertEqualsLifeCycle;

import org.junit.Test;
import org.junit.runner.RunWith;
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
        SrmLifeCycleMetadata entity = BaseDoMocks.mockLifeCycle();
        LifeCycleDto dto = do2DtoMapper.lifeCycleDoToDto(entity);
        assertEqualsLifeCycle(entity, dto);
    }

}

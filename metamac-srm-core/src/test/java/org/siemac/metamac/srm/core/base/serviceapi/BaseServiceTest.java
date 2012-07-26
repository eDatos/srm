package org.siemac.metamac.srm.core.base.serviceapi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.base.serviceapi.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class BaseServiceTest extends SrmBaseTest implements BaseServiceTestBase {

    @Autowired
    protected BaseService        baseService;

    private final ServiceContext serviceContext = new ServiceContext("system", "123456", "junit");

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }

    /**************************************************************************
     * Component Tests
     **************************************************************************/

    @Test
    public void testSaveComponent() throws Exception {
        Component component = baseService.saveComponent(getServiceContext(), BaseDoMocks.createDimension().get(0));
        assertNotNull(component);
    }

    @Test
    public void testDeleteComponent() throws Exception {
        testSaveComponent();

        List<Component> components = baseService.findAllComponent(getServiceContext());
        Component component = components.get(components.size() - 1);

        baseService.deleteComponent(getServiceContext(), component);

        assertTrue(baseService.findAllComponent(getServiceContext()).size() < components.size());
    }

    @Override
    @Test
    public void testFindAllComponent() throws Exception {
        testSaveComponent();

        List<Component> components = baseService.findAllComponent(getServiceContext());
        assertTrue(!components.isEmpty());
    }

    /**************************************************************************
     * ComponentList Tests
     **************************************************************************/
    @Test
    public void testSaveComponentList() throws Exception {
        ComponentList componentList = baseService.saveComponentList(getServiceContext(), BaseDoMocks.createDimensionDescriptor());

        assertNotNull(componentList);
    }

    @Override
    @Test
    public void testDeleteComponentList() throws Exception {
        testSaveComponentList();

        List<ComponentList> componentLists = baseService.findAllComponentList(getServiceContext());
        ComponentList componentList = componentLists.get(componentLists.size() - 1);

        baseService.deleteComponentList(getServiceContext(), componentList);

        assertTrue(baseService.findAllComponentList(getServiceContext()).size() < componentLists.size());
    }

    @Override
    @Test
    public void testFindAllComponentList() throws Exception {
        testSaveComponentList();

        List<ComponentList> componentLists = baseService.findAllComponentList(getServiceContext());
        assertTrue(!componentLists.isEmpty());
    }

    @Override
    public void testFindComponentListById() throws Exception {

    }

    @Override
    public void testPopulateAssociationsComponentList() throws Exception {

    }
}

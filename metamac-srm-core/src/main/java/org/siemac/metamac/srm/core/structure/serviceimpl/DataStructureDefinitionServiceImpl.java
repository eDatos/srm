package org.siemac.metamac.srm.core.structure.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.structure.domain.AttributeDescriptor;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.DimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.GroupDimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.MeasureDescriptor;
import org.siemac.metamac.srm.core.structure.domain.MeasureDimension;
import org.siemac.metamac.srm.core.structure.domain.TimeDimension;
import org.springframework.stereotype.Service;

/**
 * Implementation of DataStructureDefinitionService.
 */
@Service("dataStructureDefinitionService")
public class DataStructureDefinitionServiceImpl extends DataStructureDefinitionServiceImplBase {

    public DataStructureDefinitionServiceImpl() {
    }

    @Override
    public DataStructureDefinition saveDsd(ServiceContext ctx, DataStructureDefinition dataStructureDefinition) throws MetamacException {

        // Validation of parameters

        // Save DataStructureDefinition
        return getDataStructureDefinitionRepository().save(dataStructureDefinition);
    }

    @Override
    public void deleteDsd(ServiceContext ctx, DataStructureDefinition dataStructureDefinition) throws MetamacException {
        // Validation of parameters

        // Remove associations: Grouping (no cascade)
        MeasureDescriptor measureDescriptor = null;
        AttributeDescriptor attributeDescriptor = null;
        Set<GroupDimensionDescriptor> groupDimensionDescriptor = new HashSet<GroupDimensionDescriptor>();
        DimensionDescriptor dimensionDescriptor = null;

        for (ComponentList componentList : dataStructureDefinition.getGrouping()) {
            if (componentList instanceof MeasureDescriptor) {
                measureDescriptor = (MeasureDescriptor) componentList;
            } else if (componentList instanceof AttributeDescriptor) {
                attributeDescriptor = (AttributeDescriptor) componentList;
            } else if (componentList instanceof GroupDimensionDescriptor) {
                groupDimensionDescriptor.add((GroupDimensionDescriptor) componentList);
            } else if (componentList instanceof DimensionDescriptor) {
                dimensionDescriptor = (DimensionDescriptor) componentList;
            }
        }

        // 1 - Remove MeasureDescriptor
        if (measureDescriptor != null) {
            getSdmxBaseService().deleteComponentList(ctx, measureDescriptor);
        }

        // 2 - Remove AttributeDescriptor
        if (attributeDescriptor != null) {
            getSdmxBaseService().deleteComponentList(ctx, attributeDescriptor);
        }

        // 3 - Remove GroupDimensionDescriptor
        for (ComponentList componentList : groupDimensionDescriptor) {
            // First remove association with components for evict cascade
            componentList.removeAllComponents();
            // getSdmxBaseService().saveComponentList(ctx, componentList);
            getSdmxBaseService().deleteComponentList(ctx, componentList);
        }

        // 4 - Remove DimensionDescriptor
        if (dimensionDescriptor != null) {
            getSdmxBaseService().deleteComponentList(ctx, dimensionDescriptor);
        }

        // Remove DSD
        getDataStructureDefinitionRepository().delete(dataStructureDefinition);
    }

    @Override
    public ComponentList saveDescriptorForDsd(ServiceContext ctx, DataStructureDefinition dataStructureDefinition, ComponentList componentList) throws MetamacException {

        // Check type componentlist
        if (!(componentList instanceof AttributeDescriptor) && !(componentList instanceof DimensionDescriptor) && !(componentList instanceof GroupDimensionDescriptor)
                && !(componentList instanceof MeasureDescriptor)) {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "typeComponentList");
            throw metamacException;
        }

        // Check cardinals constraints in the relationship "grouping"
        List<ComponentList> groupDimDescFound = new ArrayList<ComponentList>();
        ComponentList dimensionDescFound = null;
        ComponentList measureDescFound = null;
        ComponentList attributeDescFound = null;

        for (ComponentList currentComponentList : dataStructureDefinition.getGrouping()) {
            if ((currentComponentList instanceof AttributeDescriptor) && (componentList instanceof AttributeDescriptor)) {
                attributeDescFound = currentComponentList;
            } else if ((currentComponentList instanceof DimensionDescriptor) && (componentList instanceof DimensionDescriptor)) {
                dimensionDescFound = currentComponentList;
            } else if ((currentComponentList instanceof GroupDimensionDescriptor) && (componentList instanceof GroupDimensionDescriptor)) {
                groupDimDescFound.add(currentComponentList);
            } else if ((currentComponentList instanceof MeasureDescriptor) && (componentList instanceof MeasureDescriptor)) {
                measureDescFound = currentComponentList;
            }
        }

        // Minimum cardinality not checked in this moment. Only MAX is checked.
        if (componentList instanceof AttributeDescriptor) {
            // Cardinality 0..1
            if (attributeDescFound != null) {
                // If is new or different
                if (componentList.getId() == null || componentList.getId().compareTo(attributeDescFound.getId()) != 0) {
                    // Exception
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MAX, "Max attribute descriptor is 1");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }
            }
        } else if (componentList instanceof DimensionDescriptor) {
            // Cardinality 1
            if (dimensionDescFound != null) {
                // If is new or different
                if (componentList.getId() == null || componentList.getId().compareTo(dimensionDescFound.getId()) != 0) {
                    // Exception
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MAX, "Max dimension descriptor is 1");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }
            }
        } else if (componentList instanceof MeasureDescriptor) {
            // Cardinality 1
            if (measureDescFound != null) {
                // If is new or different
                if (componentList.getId() == null || componentList.getId().compareTo(measureDescFound.getId()) != 0) {
                    // Exception
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MAX, "Max measure descriptor is 1");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }
            }
        } else if (componentList instanceof GroupDimensionDescriptor) {
            // Cardinality 0..*
        }

        // Save Descriptors
        ComponentList componentListDescriptor = getSdmxBaseService().saveComponentList(ctx, componentList);

        // Association with DSD
        dataStructureDefinition.getGrouping().add(componentListDescriptor);

        // Save
        dataStructureDefinition = saveDsd(ctx, dataStructureDefinition);

        return componentListDescriptor;

    }

    @Override
    public void deleteDescriptorForDsd(ServiceContext ctx, DataStructureDefinition dataStructureDefinition, ComponentList componentList) throws MetamacException {
        // Right cardinality not checked in this moment. The DSD can't be completed.

        // Check cardinals constraints
        Iterator<ComponentList> iterCmpList = dataStructureDefinition.getGrouping().iterator();
        while (iterCmpList.hasNext()) {
            ComponentList cmpList = iterCmpList.next();
            if (cmpList.getId().compareTo(componentList.getId()) == 0) {
                iterCmpList.remove();
                break;
            }
        }

        // Save
        dataStructureDefinition = saveDsd(ctx, dataStructureDefinition);

        // Delete ComponentList
        if (componentList instanceof GroupDimensionDescriptor) {
            componentList.removeAllComponents(); // Don't remove Components (cascade evict)
        }
        getSdmxBaseService().deleteComponentList(ctx, componentList);

        // Delete Components
        if ((componentList instanceof AttributeDescriptor) || (componentList instanceof DimensionDescriptor) || (componentList instanceof MeasureDescriptor)) {
            for (Component component : componentList.getComponents()) {
                getSdmxBaseService().deleteComponent(ctx, component);
            }
        } else if (componentList instanceof GroupDimensionDescriptor) {
            ; // Nothing to remove
        } else {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "typeComponentList");
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }

    }

    @Override
    public Component saveComponentForDsd(ServiceContext ctx, DataStructureDefinition dataStructureDefinition, Component component, TypeComponentList typeComponentList) throws MetamacException {

        // Check type component
        if (typeComponentList == null) {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_REQUIRED, "TypeComponentList");
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }

        ComponentList componentList = null;

        // Find the right Descriptor
        for (ComponentList item : dataStructureDefinition.getGrouping()) {
            if (typeComponentList.equals(TypeComponentList.ATTRIBUTE_DESCRIPTOR)) {
                if (item instanceof AttributeDescriptor) {
                    componentList = item;
                    break;
                }
            } else if (typeComponentList.equals(TypeComponentList.DIMENSION_DESCRIPTOR)) {
                if (item instanceof DimensionDescriptor) {
                    componentList = item;
                    break;
                }
            } else if (typeComponentList.equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
                if (item instanceof GroupDimensionDescriptor) {
                    componentList = item;
                    break;
                }
            } else if (typeComponentList.equals(TypeComponentList.MEASURE_DESCRIPTOR)) {
                if (item instanceof MeasureDescriptor) {
                    componentList = item;
                    break;
                }
            } else {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "TypeComponentList");
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            }
        }

        // Check constraint: DimensionDescriptor have: 0..1 MeasureDimension, 0..1 TimeDimension o N Dimension
        if (component.getId() == null && componentList != null && componentList instanceof DimensionDescriptor) {
            for (Component itemComponent : componentList.getComponents()) {
                if (itemComponent instanceof MeasureDimension && component instanceof MeasureDimension) {
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MAX,
                            "Check constraint: DimensionDescriptor have: 0..1 MeasureDimension");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }
                if (itemComponent instanceof TimeDimension && component instanceof TimeDimension) {
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MAX,
                            "Check constraint: DimensionDescriptor have: 0..1 TimeDimension");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }
            }
        }

        // Save component
        boolean isNew = (component.getId() == null);
        component = getSdmxBaseService().saveComponent(ctx, component);

        // New descriptor is needed
        ComponentList newComponentList = null;

        // If new Component, merge relations with Descriptor and [Dsd]
        if (isNew) {
            if (componentList == null) {
                switch (typeComponentList) {
                    case ATTRIBUTE_DESCRIPTOR:
                        newComponentList = new AttributeDescriptor();
                        break;
                    case DIMENSION_DESCRIPTOR:
                        newComponentList = new DimensionDescriptor();
                        break;
                    case GROUP_DIMENSION_DESCRIPTOR:
                        newComponentList = new GroupDimensionDescriptor();
                        break;
                    case MEASURE_DESCRIPTOR:
                        newComponentList = new MeasureDescriptor();
                        break;
                    default:
                        MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "type of ComponentList");
                        metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                        throw metamacException;
                }

                // Add Component
                newComponentList.addComponent(component);
                // Save Descriptors (association with DSD)
                saveDescriptorForDsd(ctx, dataStructureDefinition, newComponentList);
            } else {
                // Add Component
                componentList.addComponent(component);
                // Save Descriptors (association with component)
                componentList = getSdmxBaseService().saveComponentList(ctx, componentList);
            }
        }

        return component;
    }

    @Override
    public void deleteComponentForDsd(ServiceContext ctx, DataStructureDefinition dataStructureDefinition, Component component, TypeComponentList typeComponentList) throws MetamacException {
        // Check type component
        if (typeComponentList == null) {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_REQUIRED, "TypeComponentList");
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }

        ComponentList componentListFound = null;
        // Find the right Descriptor
        for (ComponentList item : dataStructureDefinition.getGrouping()) {
            if (typeComponentList.equals(TypeComponentList.ATTRIBUTE_DESCRIPTOR)) {
                if (item instanceof AttributeDescriptor) {
                    componentListFound = item;
                    break;
                }
            } else if (typeComponentList.equals(TypeComponentList.DIMENSION_DESCRIPTOR)) {
                if (item instanceof DimensionDescriptor) {
                    componentListFound = item;
                    break;
                }
            } else if (typeComponentList.equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
                if (item instanceof GroupDimensionDescriptor) {
                    componentListFound = item;
                    break;
                }
            } else if (typeComponentList.equals(TypeComponentList.MEASURE_DESCRIPTOR)) {
                if (item instanceof MeasureDescriptor) {
                    componentListFound = item;
                    break;
                }
            } else {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "TypeComponentList");
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            }
        }

        Component componentFound = null;
        // Find the right Component
        if (componentListFound != null) {
            for (Component oldComponent : componentListFound.getComponents()) {
                if (oldComponent.getId().compareTo(component.getId()) == 0) {
                    componentFound = oldComponent;
                    break;
                }
            }
        }

        if (componentFound != null && componentListFound != null) {
            // Delete from descriptor
            componentListFound.removeComponent(componentFound);
            // Save Dsd (descriptor) changes.
            // getSdmxBaseService().saveComponentList(ctx, componentListFound);
            // Delete Component
            getSdmxBaseService().deleteComponent(ctx, componentFound);
        } else {
            // Not Found, throw exception
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND, component.getIdLogic());
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }
    }
}

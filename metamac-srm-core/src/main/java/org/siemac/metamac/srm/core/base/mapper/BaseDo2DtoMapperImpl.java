package org.siemac.metamac.srm.core.base.mapper;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.dto.IdentifiableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.ItemMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.ItemSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.LifeCycleBasicDto;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;
import org.siemac.metamac.srm.core.base.dto.MaintainableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.NameableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.StructureMetamacBasicDto;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItem;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("baseDo2DtoMapper")
public class BaseDo2DtoMapperImpl implements org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapper {

    @Autowired
    @Qualifier("baseDo2DtoMapperSdmxSrm")
    private com.arte.statistic.sdmx.srm.core.base.mapper.BaseDo2DtoMapper do2DtoMapperSdmxSrm;

    @Autowired
    private SrmConfiguration                                              srmConfiguration;

    @Override
    public LifeCycleDto lifeCycleDoToDto(SrmLifeCycleMetadata source) {
        if (source == null) {
            return null;
        }
        LifeCycleDto target = new LifeCycleDto();
        target.setProcStatus(source.getProcStatus());
        target.setProductionValidationDate(CoreCommonUtil.transformDateTimeToDate(source.getProductionValidationDate()));
        target.setProductionValidationUser(source.getProductionValidationUser());
        target.setDiffusionValidationDate(CoreCommonUtil.transformDateTimeToDate(source.getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getDiffusionValidationUser());
        target.setInternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getInternalPublicationDate()));
        target.setInternalPublicationUser(source.getInternalPublicationUser());
        target.setExternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getExternalPublicationDate()));
        target.setExternalPublicationUser(source.getExternalPublicationUser());
        return target;
    }

    @Override
    public void itemSchemeVersionDoToItemSchemeBasicDto(ItemSchemeVersion source, SrmLifeCycleMetadata lifeCycleSource, ItemSchemeMetamacBasicDto target) {
        target.setIsTaskInBackground(source.getItemScheme().getIsTaskInBackground());
        target.setResourceCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getItemScheme().getResourceCreatedDate()));
        target.setResourceLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getItemScheme().getResourceLastUpdated()));
        target.setItemSchemeId(source.getItemScheme().getId());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));

        target.setLifeCycle(lifeCycleDoToBasicDto(lifeCycleSource));
        maintainableArtefactDoToMaintainableArtefactBasicDto(source.getMaintainableArtefact(), lifeCycleSource, target);
    }

    @Override
    public void structureVersionDoToStructureBasicDto(StructureVersion source, SrmLifeCycleMetadata lifeCycleSource, StructureMetamacBasicDto target) {
        target.setResourceCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getStructure().getResourceCreatedDate()));
        target.setResourceLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getStructure().getResourceLastUpdated()));
        target.setStructureId(source.getStructure().getId());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));

        target.setLifeCycle(lifeCycleDoToBasicDto(lifeCycleSource));
        maintainableArtefactDoToMaintainableArtefactBasicDto(source.getMaintainableArtefact(), lifeCycleSource, target);
    }

    @Override
    public void itemDoToItemBasicDto(Item source, ItemMetamacBasicDto target) {
        target.setItemSchemeVersion(do2DtoMapperSdmxSrm.itemSchemeDoToRelatedResourceDto(source.getItemSchemeVersion()));
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        nameableArtefactDoToNameableArtefactBasicDto(source, target);
    }

    @Override
    public void identifiableArtefactDoToIdentifiableArtefactBasicDto(IdentifiableArtefact source, IdentifiableArtefactMetamacBasicDto target) {
        target.setCode(source.getCode());
        target.setUrn(source.getUrn());
        target.setUrnProvider(source.getUrnProvider());
    }

    @Override
    public void nameableArtefactDoToNameableArtefactBasicDto(NameableArtefact source, NameableArtefactMetamacBasicDto target) {
        target.setName(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getName()));
        identifiableArtefactDoToIdentifiableArtefactBasicDto(source, target);
    }

    @Override
    public ExternalItemDto externalItemStatisticalOperationsToExternalItemDto(TypeDozerCopyMode typeDozerCopyMode, ExternalItem source) throws MetamacException {
        ExternalItemDto target = do2DtoMapperSdmxSrm.externalItemToExternalItemDto(TypeDozerCopyMode.COPY_ALL_METADATA, source);
        if (target != null) {
            target.setUri(CoreCommonUtil.externalItemUrlDoToUrlDto(getStatisticalOperationsInternalApiUrlBase(), target.getUri()));
            target.setManagementAppUrl(CoreCommonUtil.externalItemUrlDoToUrlDto(getStatisticalOperationsInternalWebApplicationUrlBase(), target.getManagementAppUrl()));
        }
        return target;
    }

    private void nameableArtefactDoToNameableArtefactBasicDto(Item source, NameableArtefactMetamacBasicDto target) {
        nameableArtefactDoToNameableArtefactBasicDto(source.getNameableArtefact(), target);
    }

    private void maintainableArtefactDoToMaintainableArtefactBasicDto(MaintainableArtefact source, SrmLifeCycleMetadata lifeCycleSource, MaintainableArtefactMetamacBasicDto target) {
        target.setVersionLogic(source.getVersionLogic());
        if (source.getMaintainer() != null) {
            RelatedResourceDto maintainerDto = new RelatedResourceDto();
            do2DtoMapperSdmxSrm.nameableArtefactDoToRelatedResourceDto(source.getMaintainer().getNameableArtefact(), maintainerDto);
            maintainerDto.setType(RelatedResourceTypeEnum.AGENCY); // always is Agency
            target.setMaintainer(maintainerDto);
        }
        target.setValidTo(CoreCommonUtil.transformDateTimeToDate(source.getValidTo()));
        nameableArtefactDoToNameableArtefactBasicDto(source, target);
    }

    private LifeCycleBasicDto lifeCycleDoToBasicDto(SrmLifeCycleMetadata source) {
        if (source == null) {
            return null;
        }
        LifeCycleBasicDto target = new LifeCycleBasicDto();
        target.setProcStatus(source.getProcStatus());
        target.setInternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getInternalPublicationDate()));
        target.setInternalPublicationUser(source.getInternalPublicationUser());
        target.setExternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getExternalPublicationDate()));
        target.setExternalPublicationUser(source.getExternalPublicationUser());
        return target;
    }

    private String getStatisticalOperationsInternalWebApplicationUrlBase() throws MetamacException {
        return srmConfiguration.retrieveStatisticalOperationsInternalWebApplicationUrlBase();
    }

    private String getStatisticalOperationsInternalApiUrlBase() throws MetamacException {
        return srmConfiguration.retrieveStatisticalOperationsInternalApiUrlBase();
    }
}

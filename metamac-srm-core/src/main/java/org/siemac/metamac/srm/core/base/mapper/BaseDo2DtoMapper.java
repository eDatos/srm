package org.siemac.metamac.srm.core.base.mapper;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.dto.IdentifiableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.ItemMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.ItemSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;
import org.siemac.metamac.srm.core.base.dto.NameableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.StructureMetamacBasicDto;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;

public interface BaseDo2DtoMapper {

    public LifeCycleDto lifeCycleDoToDto(SrmLifeCycleMetadata source);

    public void identifiableArtefactDoToIdentifiableArtefactBasicDto(IdentifiableArtefact source, IdentifiableArtefactMetamacBasicDto target);
    public void nameableArtefactDoToNameableArtefactBasicDto(NameableArtefact source, NameableArtefactMetamacBasicDto target);
    public void itemSchemeVersionDoToItemSchemeBasicDto(ItemSchemeVersion source, SrmLifeCycleMetadata lifeCycleSource, ItemSchemeMetamacBasicDto target);
    public void structureVersionDoToStructureBasicDto(StructureVersion source, SrmLifeCycleMetadata lifeCycleSource, StructureMetamacBasicDto target);
    public void itemDoToItemBasicDto(Item source, ItemMetamacBasicDto target);

}

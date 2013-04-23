package org.siemac.metamac.srm.core.base.mapper;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.dto.IdentifiableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;
import org.siemac.metamac.srm.core.base.dto.MaintainableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.NameableArtefactMetamacBasicDto;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;

public interface BaseDo2DtoMapper {

    public LifeCycleDto lifeCycleDoToDto(SrmLifeCycleMetadata source);

    public void identifiableArtefactDoToIdentifiableArtefactBasicDto(IdentifiableArtefact source, IdentifiableArtefactMetamacBasicDto target);
    public void nameableArtefactDoToNameableArtefactBasicDto(NameableArtefact source, NameableArtefactMetamacBasicDto target);
    public void nameableArtefactDoToNameableArtefactBasicDto(Item source, NameableArtefactMetamacBasicDto target);
    public void maintainableArtefactDoToMaintainableArtefactBasicDto(MaintainableArtefact source, SrmLifeCycleMetadata lifeCycleSource, MaintainableArtefactMetamacBasicDto target);
    public void maintainableArtefactDoToMaintainableArtefactBasicDto(ItemSchemeVersion source, SrmLifeCycleMetadata lifeCycleSource, MaintainableArtefactMetamacBasicDto target);

}

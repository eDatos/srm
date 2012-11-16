package org.siemac.metamac.srm.core.code.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

public interface CodesDo2DtoMapper {

    // Codelists
    public CodelistMetamacDto codelistMetamacDoToDto(CodelistVersionMetamac source);
    public List<CodelistMetamacDto> codelistMetamacDoListToDtoList(List<CodelistVersionMetamac> sources);

    // Codes
    public CodeMetamacDto codeMetamacDoToDto(CodeMetamac source);
    public List<CodeMetamacDto> codeMetamacDoListToDtoList(List<CodeMetamac> sources);
    public List<ItemHierarchyDto> codeMetamacDoListToItemHierarchyDtoList(List<CodeMetamac> sources);
}

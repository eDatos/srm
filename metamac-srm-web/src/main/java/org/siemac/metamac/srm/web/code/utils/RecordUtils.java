package org.siemac.metamac.srm.web.code.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.web.code.model.record.CodeRecord;
import org.siemac.metamac.srm.web.code.model.record.CodelistFamilyRecord;
import org.siemac.metamac.srm.web.code.model.record.CodelistOrderRecord;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableElementOperationRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableElementRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableFamilyRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableRecord;

public class RecordUtils {

    // CODELISTS

    public static CodelistRecord getCodelistRecord(CodelistMetamacDto codelistDto) {
        CodelistRecord record = new CodelistRecord(codelistDto.getId(), codelistDto.getCode(), getLocalisedString(codelistDto.getName()), getLocalisedString(codelistDto.getDescription()),
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(codelistDto.getLifeCycle().getProcStatus()), codelistDto.getVersionLogic(), codelistDto.getUrn(), codelistDto);
        return record;
    }

    // CODES

    public static CodeRecord getCodeRecord(CodeMetamacDto codeDto) {
        CodeRecord record = new CodeRecord(codeDto.getId(), codeDto.getCode(), getLocalisedString(codeDto.getName()), codeDto.getUrn(), getLocalisedString(codeDto.getDescription()), codeDto);
        return record;
    }

    // CODELIST FAMILIES

    public static CodelistFamilyRecord getCodelistFamilyRecord(CodelistFamilyDto codelistFamilyDto) {
        CodelistFamilyRecord record = new CodelistFamilyRecord(codelistFamilyDto.getId(), codelistFamilyDto.getCode(), getLocalisedString(codelistFamilyDto.getName()), codelistFamilyDto.getUrn(),
                codelistFamilyDto);
        return record;
    }

    // VARIABLE FAMILIES

    public static VariableFamilyRecord getVariableFamilyRecord(VariableFamilyDto variableFamilyDto) {
        VariableFamilyRecord record = new VariableFamilyRecord(variableFamilyDto.getId(), variableFamilyDto.getCode(), getLocalisedString(variableFamilyDto.getName()), variableFamilyDto.getUrn(),
                variableFamilyDto);
        return record;
    }

    // VARIABLES

    public static VariableRecord getVariableRecord(VariableDto variableDto) {
        VariableRecord record = new VariableRecord(variableDto.getId(), variableDto.getCode(), getLocalisedString(variableDto.getName()), variableDto.getUrn(), variableDto);
        return record;
    }

    // VARIABLE ELEMENTS

    public static VariableElementRecord getVariableElementRecord(VariableElementDto variableElementDto) {
        VariableElementRecord record = new VariableElementRecord(variableElementDto.getId(), variableElementDto.getCode(), getLocalisedString(variableElementDto.getName()),
                variableElementDto.getUrn(), variableElementDto);
        return record;
    }

    public static VariableElementOperationRecord getVariableElementOperationRecord(VariableElementOperationDto variableElementOperationDto) {
        VariableElementOperationRecord record = new VariableElementOperationRecord(variableElementOperationDto.getId(), variableElementOperationDto.getCode(),
                CommonUtils.getVariableElementOperationTypeName(variableElementOperationDto.getOperationType()),
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourcesName(variableElementOperationDto.getSources()),
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourcesName(variableElementOperationDto.getTargets()), variableElementOperationDto);
        return record;
    }

    public static VariableElementOperationRecord[] getVariableElementOperationRecords(List<VariableElementOperationDto> variableElementOperationDtos) {
        VariableElementOperationRecord[] records = new VariableElementOperationRecord[variableElementOperationDtos.size()];
        int index = 0;
        for (VariableElementOperationDto variableElementOperationDto : variableElementOperationDtos) {
            records[index++] = getVariableElementOperationRecord(variableElementOperationDto);
        }
        return records;
    }

    // public static VariableElementOperationRecord[] getVariableElementOperationRecords(List<VariableElementOperationDto> variableElementOperationDtos) {
    // List<VariableElementOperationRecord> records = new ArrayList<VariableElementOperationRecord>();
    // for (VariableElementOperationDto variableElementOperationDto : variableElementOperationDtos) {
    // for (RelatedResourceDto source : variableElementOperationDto.getSources()) {
    // for (RelatedResourceDto target : variableElementOperationDto.getTargets()) {
    // VariableElementOperationRecord record = new VariableElementOperationRecord(variableElementOperationDto.getId(), variableElementOperationDto.getCode(),
    // CommonUtils.getVariableElementOperationTypeName(variableElementOperationDto.getOperationType()),
    // org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(source), org.siemac.metamac.srm.web.client.utils.CommonUtils.getRelatedResourceName(target),
    // variableElementOperationDto);
    // records.add(record);
    // }
    // }
    // }
    // return records.toArray(new VariableElementOperationRecord[records.size()]);
    // }

    // CODELIST ORDERS

    public static CodelistOrderRecord getCodelistOrderRecord(CodelistOrderVisualisationDto codelistOrder) {
        CodelistOrderRecord record = new CodelistOrderRecord(codelistOrder.getCode(), getLocalisedString(codelistOrder.getName()), codelistOrder.getUrn(), codelistOrder);
        return record;
    }
}

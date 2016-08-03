package org.siemac.metamac.srm.web.code.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.web.code.model.record.CodeRecord;
import org.siemac.metamac.srm.web.code.model.record.CodelistFamilyRecord;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.code.model.record.CodelistVisualisationRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableElementOperationRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableElementRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableFamilyRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableRecord;
import org.siemac.metamac.web.common.client.utils.BooleanWebUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CodesRecordUtils extends org.siemac.metamac.srm.web.client.utils.RecordUtils {

    // CODELISTS

    public static CodelistRecord getCodelistRecord(CodelistMetamacBasicDto codelistDto) {
        CodelistRecord record = new CodelistRecord();
        record = (CodelistRecord) getItemSchemeRecord(record, codelistDto, codelistDto.getLifeCycle());
        record.setIsRecommended(BooleanWebUtils.getBooleanLabel(codelistDto.getIsRecommended()));
        record.setVariable(codelistDto.getVariable());
        record.setCodelistBasicDto(codelistDto);
        return record;
    }

    // CODES

    public static CodeRecord getCodeRecord(CodeMetamacBasicDto codeDto) {
        CodeRecord record = new CodeRecord();
        record = (CodeRecord) getItemRecord(record, codeDto);
        record.setCodeBasicDto(codeDto);
        return record;
    }

    // CODELIST FAMILIES

    public static CodelistFamilyRecord getCodelistFamilyRecord(CodelistFamilyDto codelistFamilyDto) {
        CodelistFamilyRecord record = new CodelistFamilyRecord(codelistFamilyDto.getId(), codelistFamilyDto.getCode(), getLocalisedString(codelistFamilyDto.getName()), codelistFamilyDto.getUrn(),
                codelistFamilyDto);
        return record;
    }

    public static CodelistFamilyRecord getCodelistFamilyRecord(CodelistFamilyBasicDto codelistFamilyDto) {
        CodelistFamilyRecord record = new CodelistFamilyRecord();
        record.setCode(codelistFamilyDto.getCode());
        record.setName(getLocalisedString(codelistFamilyDto.getName()));
        record.setUrn(codelistFamilyDto.getUrn());
        record.setCodelistFamilyBasicDto(codelistFamilyDto);
        return record;
    }

    // VARIABLE FAMILIES

    public static VariableFamilyRecord getVariableFamilyRecord(VariableFamilyDto variableFamilyDto) {
        VariableFamilyRecord record = new VariableFamilyRecord(variableFamilyDto.getId(), variableFamilyDto.getCode(), getLocalisedString(variableFamilyDto.getName()), variableFamilyDto.getUrn(),
                variableFamilyDto);
        return record;
    }

    public static VariableFamilyRecord getVariableFamilyRecord(VariableFamilyBasicDto variableFamilyBasicDto) {
        VariableFamilyRecord record = new VariableFamilyRecord();
        record.setCode(variableFamilyBasicDto.getCode());
        record.setName(getLocalisedString(variableFamilyBasicDto.getName()));
        record.setUrn(variableFamilyBasicDto.getUrn());
        record.setVariableFamilyBasicDto(variableFamilyBasicDto);
        return record;
    }

    public static VariableFamilyRecord[] getVariableFamilyRecords(List<VariableFamilyBasicDto> variableFamilyBasicDtos) {
        VariableFamilyRecord[] records = new VariableFamilyRecord[variableFamilyBasicDtos.size()];
        int index = 0;
        for (VariableFamilyBasicDto variable : variableFamilyBasicDtos) {
            records[index++] = getVariableFamilyRecord(variable);
        }
        return records;
    }

    // VARIABLES

    public static VariableRecord getVariableRecord(VariableDto variableDto) {
        VariableRecord record = new VariableRecord(variableDto.getId(), variableDto.getCode(), getLocalisedString(variableDto.getName()), variableDto.getUrn(), variableDto);
        return record;
    }

    public static VariableRecord getVariableRecord(VariableBasicDto variableDto) {
        VariableRecord record = new VariableRecord();
        record.setCode(variableDto.getCode());
        record.setName(getLocalisedString(variableDto.getName()));
        record.setUrn(variableDto.getUrn());
        record.setVariableBasicDto(variableDto);
        return record;
    }

    // VARIABLE ELEMENTS

    public static VariableElementRecord getVariableElementRecord(VariableElementDto variableElementDto) {
        Boolean hasShape = StringUtils.isBlank(variableElementDto.getShapeWkt()) ? Boolean.FALSE : Boolean.TRUE;

        VariableElementRecord record = new VariableElementRecord(variableElementDto.getId(), variableElementDto.getCode(), getLocalisedString(variableElementDto.getShortName()),
                variableElementDto.getUrn(), BooleanWebUtils.getBooleanLabel(hasShape), variableElementDto);
        return record;
    }

    public static VariableElementRecord getVariableElementRecord(VariableElementBasicDto variableElementDto) {
        VariableElementRecord record = new VariableElementRecord();
        record.setCode(variableElementDto.getCode());
        record.setShortName(getLocalisedString(variableElementDto.getShortName()));
        record.setUrn(variableElementDto.getUrn());
        record.setHasShape(BooleanWebUtils.getBooleanLabel(variableElementDto.getHasShape()));
        record.setVariableElementBasicDto(variableElementDto);
        return record;
    }

    public static VariableElementOperationRecord getVariableElementOperationRecord(VariableElementOperationDto variableElementOperationDto) {
        VariableElementOperationRecord record = new VariableElementOperationRecord(variableElementOperationDto.getId(), variableElementOperationDto.getCode(),
                CommonUtils.getVariableElementOperationTypeName(variableElementOperationDto.getOperationType()),
                org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourcesName(variableElementOperationDto.getSources()),
                org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourcesName(variableElementOperationDto.getTargets()), variableElementOperationDto);
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

    // CODELIST VISUALISATIONS

    public static CodelistVisualisationRecord getCodelistVisualisationRecord(CodelistVisualisationDto codelistVisualisationDto) {
        CodelistVisualisationRecord record = new CodelistVisualisationRecord(codelistVisualisationDto.getCode(), getLocalisedString(codelistVisualisationDto.getName()),
                codelistVisualisationDto.getUrn(), codelistVisualisationDto);
        return record;
    }

    public static ListGridRecord[] getCodelistVisualisationRecords(List<CodelistVisualisationDto> codelistVisualisationDtos) {
        ListGridRecord[] records = new ListGridRecord[codelistVisualisationDtos.size()];
        for (int i = 0; i < codelistVisualisationDtos.size(); i++) {
            records[i] = getCodelistVisualisationRecord(codelistVisualisationDtos.get(i));
        }
        return records;

    }
}

package org.siemac.metamac.srm.core.code.serviceimpl;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.stream.mappers.impl.CodelistDo2AvroMapper;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService.StreamMessagingCallback;

import java.util.ArrayList;
import java.util.List;

class CodelistStreamMessagingCallbackImpl implements StreamMessagingCallback<CodelistMetamacDto, CodelistAvro, CodelistDo2AvroMapper> {

    private CodelistDo2AvroMapper codelistDo2AvroMapper;

    public CodelistStreamMessagingCallbackImpl(CodelistDo2AvroMapper codelistDo2AvroMapper) {
        this.codelistDo2AvroMapper = codelistDo2AvroMapper;
    }

    @Override
    public String getUniqueIdentifier(CodelistMetamacDto messageContent) {
        return messageContent.getCode();
    }

    @Override
    public String getProducerRecordKey(CodelistMetamacDto messageContent){
        return messageContent.getUrn();
    }

    @Override
    public List<CodelistMetamacDto> getPendingOrFailedMessages() throws MetamacException {
        return new ArrayList<>(); // TODO EDATOS-3433
    }

    @Override
    public CodelistDo2AvroMapper getMapper() {
        return codelistDo2AvroMapper;
    }

    @Override
    public StreamMessageStatusEnum getStreamMessageStatus(CodelistMetamacDto messageContent) {
        return null; // TODO EDATOS-3433
    }

    @Override
    public void setStreamMessageStatus(CodelistMetamacDto messageContent, StreamMessageStatusEnum streamMessageStatus) {
        // TODO EDATOS-3433
    }
}
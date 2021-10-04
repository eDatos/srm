package org.siemac.metamac.srm.core.code.serviceimpl;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.stream.mappers.impl.CodelistDo2AvroMapper;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService.StreamMessagingCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("codelistStreamMessagingCallback")
class CodelistStreamMessagingCallbackImpl implements StreamMessagingCallback<CodelistVersionMetamac, CodelistAvro, CodelistDo2AvroMapper> {

    @Autowired
    private CodelistDo2AvroMapper codelistDo2AvroMapper;

    @Autowired
    private CodelistVersionMetamacRepository codelistVersionMetamacRepository;

    @Override
    public String getUniqueIdentifier(CodelistVersionMetamac messageContent) {
        return messageContent.getMaintainableArtefact().getCode();
    }

    @Override
    public String getProducerRecordKey(CodelistVersionMetamac messageContent){
        return messageContent.getMaintainableArtefact().getUrn();
    }

    @Override
    public List<CodelistVersionMetamac> getPendingOrFailedMessages() throws MetamacException {
        // @formatter:off
        List<ConditionalCriteria> criteria = criteriaFor(CodelistVersionMetamac.class)
                .lbrace()
                .withProperty(CodelistVersionMetamacProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.PENDING)
                .or()
                .withProperty(CodelistVersionMetamacProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.FAILED)
                .rbrace()
                .and()
                .lbrace()
                .withProperty(CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.EXTERNALLY_PUBLISHED)
                .or()
                .withProperty(CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
                .rbrace()
                .build();
        // @formatter:on
        return codelistVersionMetamacRepository.findByCondition(criteria, PagingParameter.noLimits()).getValues();
    }

    @Override
    public CodelistDo2AvroMapper getMapper() {
        return codelistDo2AvroMapper;
    }

    @Override
    public StreamMessageStatusEnum getStreamMessageStatus(CodelistVersionMetamac messageContent) {
        return messageContent.getStreamMessageStatus();
    }

    @Override
    public void setStreamMessageStatus(CodelistVersionMetamac messageContent, StreamMessageStatusEnum streamMessageStatus) {
        messageContent.setStreamMessageStatus(streamMessageStatus);
    }
}
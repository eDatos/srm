package org.siemac.metamac.srm.core.dsd.serviceimpl;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacRepository;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService;
import org.siemac.metamac.srm.core.stream.mappers.impl.DataStructureDefinitionDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.DataStructureDefinitionAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

@Component("dsdSchemeStreamMessagingCallback")
public class DsdStreamMessagingCallbackImpl implements StreamMessagingService.StreamMessagingCallback<DataStructureDefinitionVersionMetamac, DataStructureDefinitionAvro, DataStructureDefinitionDo2AvroMapper> {

    @Autowired
    private DataStructureDefinitionDo2AvroMapper dataStructureDefinitionDo2AvroMapper;

    @Autowired
    private DataStructureDefinitionVersionMetamacRepository dataStructureDefinitionVersionMetamacRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public String getUniqueIdentifier(DataStructureDefinitionVersionMetamac messageContent) {
        return messageContent.getMaintainableArtefact().getCode();
    }

    @Override
    public String getProducerRecordKey(DataStructureDefinitionVersionMetamac messageContent){
        return messageContent.getMaintainableArtefact().getUrn();
    }

    @Override
    public List<DataStructureDefinitionVersionMetamac> getPendingOrFailedMessages() throws MetamacException {
        // @formatter:off
        List<ConditionalCriteria> criteria = criteriaFor(DataStructureDefinitionVersionMetamac.class)
                .lbrace()
                .withProperty(DataStructureDefinitionVersionMetamacProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.PENDING)
                .or()
                .withProperty(DataStructureDefinitionVersionMetamacProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.FAILED)
                .rbrace()
                .and()
                .lbrace()
                .withProperty(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.EXTERNALLY_PUBLISHED)
                .or()
                .withProperty(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
                .rbrace()
                .build();
        // @formatter:on
        return dataStructureDefinitionVersionMetamacRepository.findByCondition(criteria, PagingParameter.noLimits()).getValues();
    }

    @Override
    public DataStructureDefinitionDo2AvroMapper getMapper() {
        return dataStructureDefinitionDo2AvroMapper;
    }

    @Override
    public StreamMessageStatusEnum getStreamMessageStatus(DataStructureDefinitionVersionMetamac messageContent) {
        return messageContent.getStreamMessageStatus();
    }

    @Override
    public void setStreamMessageStatus(DataStructureDefinitionVersionMetamac messageContent, StreamMessageStatusEnum streamMessageStatus) {
        messageContent.setStreamMessageStatus(streamMessageStatus);
    }

    @Override
    public String getTopic() {
        return configurationService.retrieveKafkaTopicDsdPublication();
    }
}
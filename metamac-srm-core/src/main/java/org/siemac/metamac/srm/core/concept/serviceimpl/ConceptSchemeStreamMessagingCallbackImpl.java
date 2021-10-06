package org.siemac.metamac.srm.core.concept.serviceimpl;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService;
import org.siemac.metamac.srm.core.stream.mappers.impl.ConceptSchemeDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

@Component("conceptSchemeStreamMessagingCallback")
public class ConceptSchemeStreamMessagingCallbackImpl implements StreamMessagingService.StreamMessagingCallback<ConceptSchemeVersionMetamac, ConceptSchemeAvro, ConceptSchemeDo2AvroMapper> {

    @Autowired
    private ConceptSchemeDo2AvroMapper conceptSchemeDo2AvroMapper;

    @Autowired
    private ConceptSchemeVersionMetamacRepository conceptSchemeVersionMetamacRepository;

    @Override
    public String getUniqueIdentifier(ConceptSchemeVersionMetamac messageContent) {
        return messageContent.getMaintainableArtefact().getCode();
    }

    @Override
    public String getProducerRecordKey(ConceptSchemeVersionMetamac messageContent){
        return messageContent.getMaintainableArtefact().getUrn();
    }

    @Override
    public List<ConceptSchemeVersionMetamac> getPendingOrFailedMessages() throws MetamacException {
        // @formatter:off
        List<ConditionalCriteria> criteria = criteriaFor(ConceptSchemeVersionMetamac.class)
                .lbrace()
                .withProperty(ConceptSchemeVersionMetamacProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.PENDING)
                .or()
                .withProperty(ConceptSchemeVersionMetamacProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.FAILED)
                .rbrace()
                .and()
                .lbrace()
                .withProperty(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.EXTERNALLY_PUBLISHED)
                .or()
                .withProperty(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
                .rbrace()
                .build();
        // @formatter:on
        return conceptSchemeVersionMetamacRepository.findByCondition(criteria, PagingParameter.noLimits()).getValues();
    }

    @Override
    public ConceptSchemeDo2AvroMapper getMapper() {
        return conceptSchemeDo2AvroMapper;
    }

    @Override
    public StreamMessageStatusEnum getStreamMessageStatus(ConceptSchemeVersionMetamac messageContent) {
        return messageContent.getStreamMessageStatus();
    }

    @Override
    public void setStreamMessageStatus(ConceptSchemeVersionMetamac messageContent, StreamMessageStatusEnum streamMessageStatus) {
        messageContent.setStreamMessageStatus(streamMessageStatus);
    }

    @Override
    public String getTopic() {
        return "CONCEPT_SCHEME_PUBLICATIONS";
    }
}
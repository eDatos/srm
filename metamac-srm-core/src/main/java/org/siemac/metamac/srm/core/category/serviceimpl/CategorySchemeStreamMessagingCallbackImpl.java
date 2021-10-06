package org.siemac.metamac.srm.core.category.serviceimpl;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService.StreamMessagingCallback;
import org.siemac.metamac.srm.core.stream.mappers.impl.CategorySchemeDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

@Component("categorySchemeStreamMessagingCallback")
public class CategorySchemeStreamMessagingCallbackImpl implements StreamMessagingCallback<CategorySchemeVersionMetamac, CategorySchemeAvro, CategorySchemeDo2AvroMapper> {

    @Autowired
    private CategorySchemeDo2AvroMapper categorySchemeDo2AvroMapper;

    @Autowired
    private CategorySchemeVersionMetamacRepository categorySchemeVersionMetamacRepository;

    @Override
    public String getUniqueIdentifier(CategorySchemeVersionMetamac messageContent) {
        return messageContent.getMaintainableArtefact().getCode();
    }

    @Override
    public String getProducerRecordKey(CategorySchemeVersionMetamac messageContent){
        return messageContent.getMaintainableArtefact().getUrn();
    }

    @Override
    public List<CategorySchemeVersionMetamac> getPendingOrFailedMessages() throws MetamacException {
        // @formatter:off
        List<ConditionalCriteria> criteria = criteriaFor(CategorySchemeVersionMetamac.class)
                .lbrace()
                .withProperty(CategorySchemeVersionMetamacProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.PENDING)
                .or()
                .withProperty(CategorySchemeVersionMetamacProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.FAILED)
                .rbrace()
                .and()
                .lbrace()
                .withProperty(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.EXTERNALLY_PUBLISHED)
                .or()
                .withProperty(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
                .rbrace()
                .build();
        // @formatter:on
        return categorySchemeVersionMetamacRepository.findByCondition(criteria, PagingParameter.noLimits()).getValues();
    }

    @Override
    public CategorySchemeDo2AvroMapper getMapper() {
        return categorySchemeDo2AvroMapper;
    }

    @Override
    public StreamMessageStatusEnum getStreamMessageStatus(CategorySchemeVersionMetamac messageContent) {
        return messageContent.getStreamMessageStatus();
    }

    @Override
    public void setStreamMessageStatus(CategorySchemeVersionMetamac messageContent, StreamMessageStatusEnum streamMessageStatus) {
        messageContent.setStreamMessageStatus(streamMessageStatus);
    }

    @Override
    public String getTopic() {
        return "CATEGORY_SCHEME_PUBLICATIONS";
    }
}
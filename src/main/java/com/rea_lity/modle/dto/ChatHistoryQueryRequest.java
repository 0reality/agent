package com.rea_lity.modle.dto;

import com.rea_lity.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChatHistoryQueryRequest extends PageRequest {
    private Long appId;
}

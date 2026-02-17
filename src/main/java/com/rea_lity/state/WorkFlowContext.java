package com.rea_lity.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkFlowContext {
    /**
     * 当前节点名称
     */
    private String nodeName;

    /**
     * 初始提示词输入
     */
    private String initPrompt;

    /**
     * 节点输出信息记录
     */
    private List<String> nodesOutput;

    /**
     * 当前步数
     */
    private Integer currentStepCount;

    /**
     * 对话ID
     */
    private Long conversationId;
}

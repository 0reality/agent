package com.rea_lity.state;

import com.rea_lity.modle.enums.RouterEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkFlowContext implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * 当前节点名称
     */
    private String nodeName;

    /**
     * 路由信息
     */
    RouterEnums router;

    /**
     * 初始提示词输入
     */
    private String initPrompt;

    /**
     * 节点输出信息记录
     */
    private List<String> nodesOutput = new ArrayList<>();

    /**
     * 当前步数
     */
    private Integer currentStepCount;

    /**
     * 对话ID
     */
    private Long conversationId;
}

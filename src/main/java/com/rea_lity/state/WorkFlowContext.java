package com.rea_lity.state;

import com.rea_lity.modle.ImageCollectionPlan;
import com.rea_lity.modle.ImageResource;
import com.rea_lity.modle.enums.RouterEnums;
import dev.langchain4j.service.TokenStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.beans.Transient;
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

    /**
     * 图片收集计划
     */
    private ImageCollectionPlan imageCollectionPlan;

    /**
     * 图片资源
     */
    private List<ImageResource> imageResources = new ArrayList<>();

    /**
     * 部署报错信息
     */
    private String deployError;

    /**
     * 是否流式
     */
    private Boolean stream;
}

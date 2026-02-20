package com.rea_lity.nodes;

import cn.hutool.core.util.StrUtil;
import com.rea_lity.AiService.VueProjectGeneratorService;
import com.rea_lity.config.CustomConfig;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import com.rea_lity.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class VueProjectGeneratorNode implements NodeAction<AiAgentContext> {

    public static final String NODE_NAME = "VueProjectGeneratorNode";

    @Override
    public Map<String, Object> apply(AiAgentContext aiAgentContext) throws Exception {
        VueProjectGeneratorService vueProjectGeneratorService = SpringContextUtils.getBean(VueProjectGeneratorService.class);
        WorkFlowContext context = aiAgentContext.context();
        try {
            log.info("VueProjectGeneratorNode start");
            log.info("当前对话ID: {}", context.getConversationId());
            log.info("图片资源数量: {}", context.getImageResources() != null ? context.getImageResources().size() : 0);
            log.info("部署错误信息: {}", context.getDeployError());
            
            // 检查必要配置
            CustomConfig customConfig = SpringContextUtils.getBean(CustomConfig.class);
            String apiKey = customConfig.getDASHSCOPE_API_KEY();
            log.info("DashScope API Key 配置状态: {}", StrUtil.isNotBlank(apiKey) ? "已配置" : "未配置");
            
            if (StrUtil.isBlank(apiKey)) {
                throw new IllegalStateException("DashScope API Key 未配置，请在 application.yml 中配置 dashscope.api-key");
            }
            
            // 构造 prompt - 确保不为空且安全
            String prompt = "请根据用户的要求构建/修改项目，使得他正确的执行。";
            String imageInfo = context.getImageResources() != null ? context.getImageResources().toString() : "无图片资源";
            prompt += "静态资源图片如下：" + imageInfo;
            
            if(StrUtil.isNotBlank(context.getDeployError())) {
                prompt += "部署报错信息如下：" + context.getDeployError();
                context.setDeployError("");
            }
            
            // 确保 prompt 不为空且有实质性内容
            if (StrUtil.isBlank(prompt) || prompt.length() < 10) {
                prompt = "请生成一个完整的Vue.js项目，包含基本的页面结构和组件。";
            }
            
            log.info("构造的用户提示词长度: {}", prompt.length());
            log.info("提示词预览: {}", prompt.substring(0, Math.min(100, prompt.length())));
            
            // 添加检查：确保 conversationId 不为空
            if (context.getConversationId() == null) {
                throw new IllegalArgumentException("Conversation ID 不能为空");
            }
            
            log.info("准备调用 AI 服务，conversationId: {}", context.getConversationId());
            String s = vueProjectGeneratorService.generateVueProject(context.getConversationId(), prompt);
            log.info("VueProjectGeneratorNode result length: {}", s != null ? s.length() : 0);
            context.getNodesOutput().add(s != null ? s : "生成结果为空");
        } catch (Exception e) {
            log.error("VueProjectGeneratorNode error", e);
            String errorMsg = "Vue项目生成失败: " + e.getMessage();
            context.getNodesOutput().add(errorMsg);
            
            // 如果是配置相关的错误，给出更明确的提示
            if (e instanceof IllegalStateException && e.getMessage().contains("API Key")) {
                log.error("请检查 application.yml 配置文件，确保已正确配置 dashscope.api-key");
            }
        }
        context.setNodeName(NODE_NAME);
        return Map.of(AiAgentContext.CONTEXT_KEY, context);
    }
}

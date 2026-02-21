package com.rea_lity.nodes;

import com.rea_lity.common.SseEmitterContextHolder;
import com.rea_lity.modle.ImageCollectionPlan;
import com.rea_lity.modle.ImageResource;
import com.rea_lity.modle.enums.MessageTypeEnum;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import com.rea_lity.tools.LogoGenerator;
import com.rea_lity.tools.MermaidConverter;
import com.rea_lity.tools.SearchImage;
import com.rea_lity.utils.SseEmitterSendUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ImageCollectionNode implements NodeAction<AiAgentContext> {

    public static final String NODE_NAME = "ImageCollectionNode";

    @Resource
    ThreadPoolTaskExecutor imageCollectionThreadPool;

    @Override
    public Map<String, Object> apply(AiAgentContext aiAgentContext) throws Exception {
        WorkFlowContext context = aiAgentContext.context();
        ImageCollectionPlan imageCollectionPlan = context.getImageCollectionPlan();

        List<ImageResource> imageResources = new ArrayList<>();

        log.info("开始执行图片收集节点");
        SseEmitter sseEmitter = SseEmitterContextHolder.get(aiAgentContext.context().getConversationId());
        SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.NODE, "开始执行图片收集节点");
        if(imageCollectionPlan == null) {
            log.error("图片收集计划为null");
            throw new Exception("图片收集计划为null");
        }

        try {
            log.info("开始执行图片收集计划");
            List<CompletableFuture<List<ImageResource>>> futureTasks = new ArrayList<>();

            // 提交任务到线程池

            for (ImageCollectionPlan.ImageSearchTask task : imageCollectionPlan.getContentImageTasks()) {
                CompletableFuture<List<ImageResource>> futureTask = CompletableFuture.supplyAsync(() -> {
                    SearchImage searchImage = new SearchImage();
                    return searchImage.searchImage(task.query());
                }, imageCollectionThreadPool);
                futureTasks.add(futureTask);
            }

            for (ImageCollectionPlan.DiagramTask task : imageCollectionPlan.getDiagramTasks()) {
                CompletableFuture<List<ImageResource>> futureTask = CompletableFuture.supplyAsync(() -> {
                    MermaidConverter mermaidConverter = new MermaidConverter();
                    return mermaidConverter.generateMermaidDiagram(task.mermaidCode(), task.description());
                }, imageCollectionThreadPool);
                futureTasks.add(futureTask);
            }


            for (ImageCollectionPlan.LogoTask task : imageCollectionPlan.getLogoTasks()) {
                CompletableFuture<List<ImageResource>> futureTask = CompletableFuture.supplyAsync(() -> {
                    LogoGenerator logoGenerator = new LogoGenerator();
                    return logoGenerator.generateLogo(task.description());
                }, imageCollectionThreadPool);
                futureTasks.add(futureTask);
            }

            for (ImageCollectionPlan.IllustrationTask task : imageCollectionPlan.getIllustrationTasks()) {
                CompletableFuture<List<ImageResource>> futureTask = CompletableFuture.supplyAsync(() -> {
                    SearchImage searchImage = new SearchImage();
                    return searchImage.searchIllustration(task.query());
                }, imageCollectionThreadPool);
                futureTasks.add(futureTask);
            }
            CompletableFuture.allOf(futureTasks.toArray(new CompletableFuture[0])).join();

            for(CompletableFuture<List<ImageResource>> future: futureTasks) {
                imageResources.addAll(future.get());
            }
            context.setImageResources(imageResources);
            log.info("图片收集计划结果：{}", imageResources);
            SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.NODE, "图片收集成功，收集到" + imageResources.size() + "张图片");
            context.getNodesOutput().add(imageResources.toString());
        } catch (Exception e) {
            log.error("图片收集计划失败", e);
            context.getNodesOutput().add("图片收集计划失败");
        }
        context.setNodeName(NODE_NAME);
        return Map.of(AiAgentContext.CONTEXT_KEY, context);
    }
}

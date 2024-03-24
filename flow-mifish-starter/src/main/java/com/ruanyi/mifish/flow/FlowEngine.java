package com.ruanyi.mifish.flow;

import com.ruanyi.mifish.flow.model.FlowContext;
import com.ruanyi.mifish.flow.model.FlowResult;
import com.ruanyi.mifish.flow.model.PEventStatus;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2024-03-24 19:10
 */
public interface FlowEngine {

    /**
     * 初始化业务流，返回任务id *
     * <p>
     * 异步执行任务的第一个节点
     *
     * @param pipeline
     * @param flowContext
     * @return
     */
    FlowResult initPipeline(String pipeline, FlowContext flowContext);

    /**
     * failure
     *
     * @param pEventStatus
     * @param flowContext
     * @return
     */
    FlowResult failure(PEventStatus pEventStatus, FlowContext flowContext);

    /**
     * verify
     *
     * @param pipelineId
     * @param flowContext
     * @return
     */
    FlowResult verify(Long pipelineId, FlowContext flowContext);
}

package org.apache.dubbo.demo.provider.filter;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;
import static org.apache.dubbo.demo.provider.filter.ReqNoConsumerFilter.TRACE_ID;

@Activate(group = PROVIDER, order = -9000)
public class ReqNoProviderFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 获取入参的跟踪序列号值
        Map<String, Object> attachments = invocation.getObjectAttachments();
        String reqTraceId = attachments != null ? (String) attachments.get(TRACE_ID) : null;

        // 若 reqTraceId 为空则重新生成一个序列号值
        reqTraceId = reqTraceId == null ? generateTraceId() : reqTraceId;

        // 将序列号值设置到上下文对象中
        RpcContext.getServerAttachment().setObjectAttachment(TRACE_ID, reqTraceId);
        // 并且将序列号设置到日志打印器中，方便在日志中体现出来
        MDC.put(TRACE_ID, reqTraceId);

        // 继续后面过滤器的调用
        return invoker.invoke(invocation);
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
    }
}

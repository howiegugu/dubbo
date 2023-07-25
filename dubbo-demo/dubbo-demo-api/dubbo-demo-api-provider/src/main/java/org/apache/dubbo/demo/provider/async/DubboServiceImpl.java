package org.apache.dubbo.demo.provider.async;

import org.apache.dubbo.common.extension.support.WrapperComparator;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@DubboService
public class DubboServiceImpl {

    public String queryOrderById(String id) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        // 开启异步上下文
        AsyncContext asyncContext = RpcContext.startAsync();
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                // 同步上下文信息
                asyncContext.signalContextSwitch();

                // 这里模拟执行一段耗时的业务逻辑
                try {
                    TimeUnit.SECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                System.out.println("resultInfo");
                // 写入结果
                asyncContext.write("resultInfo");
            }
        });

        return null;
    }
}

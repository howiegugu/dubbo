package org.apache.dubbo.demo.provider.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class Demo1 {
    public static void main(String[] args) {
        CompletableFuture<Integer> taskA1 = CompletableFuture.supplyAsync(() -> 1);
        CompletableFuture<Integer> taskB1 = taskA1.thenApplyAsync(integer -> 2);
        CompletableFuture<Integer> taskC1 = taskA1.thenApplyAsync(integer -> 3);

        CompletableFuture<Integer> result1 = taskB1.thenCombine(taskC1, Integer::sum)
            .thenCombine(taskA1, Integer::sum)
            .thenApplyAsync(integer -> integer + 4);


        CompletableFuture<Integer> taskA2 = CompletableFuture.supplyAsync(() -> 1);
        CompletableFuture<Integer> taskB2 = taskA2.thenApplyAsync(integer -> 2);
        CompletableFuture<Integer> taskC2 = taskA2.thenApplyAsync(integer -> 3);

        CompletableFuture<Integer> result2 = taskB2.applyToEither(taskC2, Function.identity())
            .thenCombine(taskA2, Integer::sum)
            .thenApplyAsync(integer -> integer + 4);
        // todo 研究下CompletableFuture的源码
        CompletableFuture<Integer> result = result1.thenCombine(result2, Integer::sum);
        try {
            // 任务总超时时间设置为5s
            System.out.println(result.get(5, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // 超时则打印0
            System.out.println(0);
            e.printStackTrace();
        }


    }
}

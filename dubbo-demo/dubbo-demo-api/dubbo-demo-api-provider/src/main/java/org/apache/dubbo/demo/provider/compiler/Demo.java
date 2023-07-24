package org.apache.dubbo.demo.provider.compiler;

import org.apache.dubbo.common.compiler.support.JavassistCompiler;

import java.lang.reflect.InvocationTargetException;

public class Demo {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String sourceCode = "import org.apache.dubbo.demo.DemoService;\n" +
            "import org.apache.dubbo.rpc.RpcContext;\n" +
            "\n" +
            "import org.slf4j.Logger;\n" +
            "import org.slf4j.LoggerFactory;\n" +
            "\n" +
            "import java.util.concurrent.CompletableFuture;\n" +
            "\n" +
            "public class DemoServiceImpl implements DemoService {\n" +
            "    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);\n" +
            "\n" +
            "    @Override\n" +
            "    public String sayHello(String name) {\n" +
            "        logger.info(\"Hello \" + name + \", request from consumer: \" + RpcContext.getServiceContext().getRemoteAddress());\n" +
            "        return \"Hello \" + name + \", response from provider: \" + RpcContext.getServiceContext().getLocalAddress();\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public CompletableFuture<String> sayHelloAsync(String name) {\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "}";

        JavassistCompiler javassistCompiler = new JavassistCompiler();
        Class<?> compile = javassistCompiler.compile(sourceCode, Demo.class.getClassLoader());
        Object o = compile.newInstance();
        o.getClass().getMethod("sayHello", new Class[]{String.class}).invoke(o,"dd");
    }
}

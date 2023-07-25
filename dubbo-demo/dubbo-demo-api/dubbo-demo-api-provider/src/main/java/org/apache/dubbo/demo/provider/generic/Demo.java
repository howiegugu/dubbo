package org.apache.dubbo.demo.provider.generic;

import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;

public class Demo {

    public static String commonInvoke(String className,
                                      String mtdName,
                                      String parameterTypeName,
                                      String reqParamsStr) {
        // 然后试图通过类信息对象想办法获取到该类对应的实例对象
        ReferenceConfig<GenericService> referenceConfig = createReferenceConfig(className);

        // 远程调用
        GenericService genericService = referenceConfig.get();
        Object resp = genericService.$invoke(
            mtdName,
            new String[]{parameterTypeName},
            new Object[]{JSON.parseObject(reqParamsStr, Map.class)});

        // 判断响应对象的响应码，不是成功的话，则组装失败响应
        return "ok";
    }
    private static ReferenceConfig<GenericService> createReferenceConfig(String className) {

        DubboBootstrap instance = DubboBootstrap.getInstance();
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(instance.getApplicationModel().getApplicationName());
        String address = instance.getConfigManager().getRegistries().iterator().next().getAddress();
        RegistryConfig registryConfig = new RegistryConfig(address);

        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setScopeModel(applicationConfig.getScopeModel());
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(className);

        referenceConfig.setGeneric("true");

        referenceConfig.setTimeout(5 * 1000);
        return referenceConfig;

    }
}

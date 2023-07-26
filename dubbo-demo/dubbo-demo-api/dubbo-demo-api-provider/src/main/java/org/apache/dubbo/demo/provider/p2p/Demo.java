package org.apache.dubbo.demo.provider.p2p;

import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;

public class Demo {
    // 响应码为成功时的值
    public static final String SUCC = "000000";

    public static class RepairRequest {

        private String className;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getMtdName() {
            return mtdName;
        }

        public void setMtdName(String mtdName) {
            this.mtdName = mtdName;
        }

        public String getParameterTypeName() {
            return parameterTypeName;
        }

        public void setParameterTypeName(String parameterTypeName) {
            this.parameterTypeName = parameterTypeName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getParamsMap() {
            return paramsMap;
        }

        public void setParamsMap(String paramsMap) {
            this.paramsMap = paramsMap;
        }

        private String mtdName;

        private String parameterTypeName;

        private String url;

        private String paramsMap;


    }


    public String repairRequest(RepairRequest repairRequest){
        // 将入参的req转为下游方法的入参对象，并发起远程调用
        ReferenceConfig<GenericService> referenceConfig =
            createReferenceConfig(repairRequest.getClassName(), repairRequest.getUrl());

        // 远程调用
        GenericService genericService = referenceConfig.get();
        Object resp = genericService.$invoke(
            repairRequest.getMtdName(),
            new String[]{repairRequest.getParameterTypeName()},
            new Object[]{JSON.parseObject(repairRequest.getParamsMap(), Map.class)});

        // 判断响应对象的响应码，不是成功的话，则组装失败响应
        if(!SUCC.equals(OgnlUtils.getValue(resp, "respCode"))){
            return RespUtils.fail(resp);
        }

        // 如果响应码为成功的话，则组装成功响应
        return RespUtils.ok(resp);
    }

    private String commonInvoke(RepairRequest repairRequest) {
        // 然后试图通过类信息对象想办法获取到该类对应的实例对象
        ReferenceConfig<GenericService> referenceConfig =
            createReferenceConfig(repairRequest.getClassName(), repairRequest.getUrl());

        // 远程调用
        GenericService genericService = referenceConfig.get();
        Object resp = genericService.$invoke(
            repairRequest.getMtdName(),
            new String[]{repairRequest.getParameterTypeName()},
            new Object[]{JSON.parseObject(repairRequest.getParamsMap(), Map.class)});

        // 判断响应对象的响应码，不是成功的话，则组装失败响应
        if(!SUCC.equals(OgnlUtils.getValue(resp, "respCode"))){
            return RespUtils.fail(resp);
        }

        // 如果响应码为成功的话，则组装成功响应
        return RespUtils.ok(resp);

    }

    private static ReferenceConfig<GenericService> createReferenceConfig(String className, String url) {
        DubboBootstrap dubboBootstrap = DubboBootstrap.getInstance();

        // 设置应用服务名称
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(dubboBootstrap.getApplicationModel().getApplicationName());

        // 设置注册中心的地址
        String address = dubboBootstrap.getConfigManager().getRegistries().iterator().next().getAddress();
        RegistryConfig registryConfig = new RegistryConfig(address);

        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(className);

        // 设置泛化调用形式
        referenceConfig.setGeneric("true");
        // 设置默认超时时间5秒
        referenceConfig.setTimeout(5 * 1000);
        // 设置点对点连接的地址
        referenceConfig.setUrl(url);
        return referenceConfig;
    }


    private static class RespUtils {
        public static String ok(Object resp) {
            return null;
        }

        public static String fail(Object resp) {
            return null;
        }
    }

    private static class OgnlUtils {
        public static String getValue(Object resp, String respCode) {
            return null;
        }
    }

}

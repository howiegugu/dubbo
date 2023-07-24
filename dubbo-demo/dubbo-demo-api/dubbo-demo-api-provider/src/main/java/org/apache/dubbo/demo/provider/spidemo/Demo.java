package org.apache.dubbo.demo.provider.spidemo;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.model.ApplicationModel;

public class Demo {
    public static void main(String[] args) {
        ApplicationModel applicationModel = ApplicationModel.defaultModel();
        ExtensionLoader<IDemoSpi> extensionLoader = applicationModel.getExtensionLoader(IDemoSpi.class);
        IDemoSpi customSpi = extensionLoader.getExtension("customSpi");
        int defaultPort = customSpi.getDefaultPort();
        System.out.println(defaultPort);
    }
}

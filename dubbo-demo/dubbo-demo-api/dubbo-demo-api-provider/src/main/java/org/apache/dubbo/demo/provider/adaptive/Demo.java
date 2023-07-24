package org.apache.dubbo.demo.provider.adaptive;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.model.ApplicationModel;

public class Demo {
    public static void main(String[] args) {
        main2(args);

        System.out.println("====================================================");



        ApplicationModel applicationModel = ApplicationModel.defaultModel();
        // 通过 Geek 接口获取指定像 扩展点加载器
        ExtensionLoader<Geek> extensionLoader = applicationModel.getExtensionLoader(Geek.class);

        Geek geek = extensionLoader.getAdaptiveExtension();
        System.out.println("【指定的 geek=springcloud 的情况】动态获取结果为: "
            + geek.getCourse(URL.valueOf("xyz://127.0.0.1/?geek=springcloud&custom=dubbo")));
        System.out.println("【指定的 geek=dubbo 的情况】动态获取结果为: "
            + geek.getCourse(URL.valueOf("xyz://127.0.0.1/?geek=dubbo&custom=springcloud")));
        System.out.println("【不指定的 geek 走默认情况】动态获取结果为: "
            + geek.getCourse(URL.valueOf("xyz://127.0.0.1/")));
        System.out.println("【随便指定 geek=xyz 走报错情况】动态获取结果为: "
            + geek.getCourse(URL.valueOf("xyz://127.0.0.1/?geek=xyz")));
    }

    public static void main2(String[] args) {
        ApplicationModel applicationModel = ApplicationModel.defaultModel();
        // 通过 Geek 接口获取指定像 扩展点加载器
        ExtensionLoader<Geek> extensionLoader = applicationModel.getExtensionLoader(Geek.class);

        Geek geek = extensionLoader.getAdaptiveExtension();
        System.out.println("【custom 指定的 dubbo 的情况】动态获取结果为: "
            + geek.getCourse2(URL.valueOf("xyz://127.0.0.1/?geek=springcloud&custom=dubbo")));
        System.out.println("【custom 指定的 springcloud 的情况，但没有 custom 参数，然后直接使用 geek=dubbo 参数】动态获取结果为: "
            + geek.getCourse2(URL.valueOf("xyz://127.0.0.1/?geek=dubbo")));
        System.out.println("【custom、geek 参数都没有指定的话】动态获取结果为: "
            + geek.getCourse2(URL.valueOf("xyz://127.0.0.1/")));
    }
}

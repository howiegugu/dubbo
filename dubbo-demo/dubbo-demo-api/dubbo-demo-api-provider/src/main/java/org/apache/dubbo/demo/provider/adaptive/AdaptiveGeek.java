package org.apache.dubbo.demo.provider.adaptive;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;

//@Adaptive
public class AdaptiveGeek implements Geek {
    @Override
    public String getCourse(URL url) {
        return "17｜Adaptive 适配：Dubbo的Adaptive特殊在哪里？";
    }

    @Override
    public String getCourse2(URL url) {
        return "17｜Adaptive 适配：Dubbo的Adaptive特殊在哪里？(2)";
    }
}

package org.apache.dubbo.demo.provider.adaptive;

import org.apache.dubbo.common.URL;

public class SpringCloud implements Geek {

    @Override
    public String getCourse(URL url) {
        return "SpringCloud入门课程100集";
    }

    @Override
    public String getCourse2(URL url) {
        return "SpringCloud入门课程100集(2)";
    }
}

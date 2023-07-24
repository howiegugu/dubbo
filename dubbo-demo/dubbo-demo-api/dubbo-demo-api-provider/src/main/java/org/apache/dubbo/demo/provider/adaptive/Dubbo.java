package org.apache.dubbo.demo.provider.adaptive;

import org.apache.dubbo.common.URL;

public class Dubbo implements Geek {

    @Override
    public String getCourse(URL url) {
        return "Dubbo实战进阶课程";
    }

    @Override
    public String getCourse2(URL url) {
        return "Dubbo实战进阶课程(2)";
    }
}

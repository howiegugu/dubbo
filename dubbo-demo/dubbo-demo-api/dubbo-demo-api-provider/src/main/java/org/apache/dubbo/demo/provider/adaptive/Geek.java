package org.apache.dubbo.demo.provider.adaptive;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;


@SPI("dubbo")
public interface Geek {

    @Adaptive
    String getCourse(URL url);

    @Adaptive({"custom", "geek"})
    String getCourse2(URL url);
}


package org.apache.dubbo.demo.provider.spidemo;

import org.apache.dubbo.common.extension.SPI;

@SPI
public interface IDemoSpi {
    int getDefaultPort();
}

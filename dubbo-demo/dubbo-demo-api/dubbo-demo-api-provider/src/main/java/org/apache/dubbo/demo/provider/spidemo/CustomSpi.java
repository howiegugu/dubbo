package org.apache.dubbo.demo.provider.spidemo;

public class CustomSpi implements IDemoSpi{
    @Override
    public int getDefaultPort() {
        return 88;
    }
}

package cc.xfl12345.mybigdata.server.web.pojo.helloworld.impl;

import cc.xfl12345.mybigdata.server.web.pojo.helloworld.IHelloWorld;

public class HelloWorld implements IHelloWorld {

    @Override
    public void sayHelloWorld() {
        System.out.println("Hello world!");
    }

}

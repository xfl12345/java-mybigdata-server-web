package cc.xfl12345.mybigdata.server.pojo.helloworld.impl;

import cc.xfl12345.mybigdata.server.pojo.helloworld.IHelloWorld;

public class HelloWorld implements IHelloWorld {

    @Override
    public void sayHelloWorld() {
        System.out.println("Hello world!");
    }

}

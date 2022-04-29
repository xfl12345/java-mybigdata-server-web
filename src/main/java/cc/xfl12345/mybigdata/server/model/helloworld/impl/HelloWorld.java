package cc.xfl12345.mybigdata.server.model.helloworld.impl;

import cc.xfl12345.mybigdata.server.model.helloworld.IHelloWorld;

public class HelloWorld implements IHelloWorld {

    @Override
    public void sayHelloWorld() {
        System.out.println("Hello world!");
    }

}

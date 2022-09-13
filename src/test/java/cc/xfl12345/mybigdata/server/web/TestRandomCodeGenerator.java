package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.web.model.generator.impl.RandomCodeGeneratorImpl;
import com.fasterxml.uuid.Generators;

public class TestRandomCodeGenerator {
    public static void main(String[] args) {
        RandomCodeGeneratorImpl generator = new RandomCodeGeneratorImpl();
        generator.setUuidGenerator(Generators.timeBasedGenerator());

        System.out.println(generator.generate(20));
    }
}

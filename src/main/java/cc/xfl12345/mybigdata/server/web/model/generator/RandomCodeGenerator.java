package cc.xfl12345.mybigdata.server.web.model.generator;

public interface RandomCodeGenerator {
    String generate(RandomCodeGeneratorOptions options);

    String generate(int codeLength);
}

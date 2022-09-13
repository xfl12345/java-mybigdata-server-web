package cc.xfl12345.mybigdata.server.web.model.generator.impl;

import cc.xfl12345.mybigdata.server.web.model.generator.CharacterOptions;
import cc.xfl12345.mybigdata.server.web.model.generator.LetterOptions;
import cc.xfl12345.mybigdata.server.web.model.generator.RandomCodeGenerator;
import cc.xfl12345.mybigdata.server.web.model.generator.RandomCodeGeneratorOptions;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

public class RandomCodeGeneratorImpl implements RandomCodeGenerator {
    @Getter
    @Setter
    protected volatile NoArgGenerator uuidGenerator;

    @Override
    public String generate(RandomCodeGeneratorOptions options) {
        LetterOptions letterOptions = options.getLetterOption();
        CharacterOptions specialCharacterOptions = options.getSpecialCharacterOptions();
        CharacterOptions integerOption = options.getIntegerOption();
        int codeLength = options.getCodeLength();

        StringBuilder codeBuffer = new StringBuilder(codeLength);
        Random random = new Random(System.currentTimeMillis());
        byte[] commonHash = DigestUtils.sha256(uuidGenerator.generate().toString());
        long hash = 1315423911 + random.nextLong();
        byte[] takeNum = new byte[1];
        int finishCodeCount = 0;

        boolean toLowerCase = false;
        // 以下哈希算法我自己都不清楚发生碰撞的概率，但只求尽可能地难以被预测
        for (int i = 0; finishCodeCount < codeLength; ) {
            if (random.nextBoolean()) {//Justin Sobel写的一个 位操作 的哈希函数
                hash ^= ((hash << 5) + commonHash[i] + (hash >> 2));
                toLowerCase = false;
            } else {//变体算法
                hash ^= ((hash << 4) + commonHash[i] + (hash >> 1));
                toLowerCase = true;
            }

            if (letterOptions.isAllUpperCase()) {
                toLowerCase = false;
            } else if (letterOptions.isAllLowerCase()) {
                toLowerCase = true;
            }

            takeNum[0] = (byte) ((hash & 0xFF) +
                ((hash & 0xFF00) >> 8) +
                ((hash & 0xFF0000) >> 16) +
                ((hash & 0xFF000000) >> 24));
            String str = Hex.encodeHexString(takeNum, toLowerCase);

            // 一个 byte 大小的值 用 16进制 表示必须用 两个字符串 来表示
            // 凑数，以满足 随机码 的 位数 要求
            if (codeLength - finishCodeCount >= 2) {
                codeBuffer.append(str);
                finishCodeCount += 2;
            } else {
                codeBuffer.append(str.charAt(0));
                finishCodeCount += 1;
            }

            // 如果读完了 commonHash ，则从头再读过。
            if (i < commonHash.length - 1)
                i++;
            else
                i = 0;
        }

        return codeBuffer.toString();
    }

    @Override
    public String generate(int codeLength) {
        RandomCodeGeneratorOptions options = new RandomCodeGeneratorOptions();
        options.setCodeLength(codeLength);
        return generate(options);
    }
}

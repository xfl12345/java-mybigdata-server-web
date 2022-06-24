package cc.xfl12345.mybigdata.server;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.security.KeyPair;

public class StudyHuToolSmUtil {
    public static void main(String[] args) throws DecoderException {
        String text = "我是一段测试aaaa";

        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        // SecureUtil.generatePrivateKey()

        byte[] privateKey = pair.getPrivate().getEncoded();
        byte[] publicKey = pair.getPublic().getEncoded();

        System.out.println("privateKey=" + Hex.encodeHexString(privateKey));
        System.out.println("publicKey=" + Hex.encodeHexString(publicKey));

        SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
        // 公钥加密，私钥解密
        String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey);
        System.out.println("encryptStr=" + Hex.encodeHexString(Hex.decodeHex(encryptStr)));

        String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
        System.out.println("decryptStr=" + decryptStr);
    }
}

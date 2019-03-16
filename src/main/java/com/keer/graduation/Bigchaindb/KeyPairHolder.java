package com.keer.graduation.Bigchaindb;


import com.bigchaindb.util.KeyPairUtils;

import com.keer.graduation.Util.PropertyUtil;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;

public class KeyPairHolder {
    private static Logger logger = LoggerFactory.getLogger(KeyPairHolder.class);

    private static String keyPath= PropertyUtil.getProperties("keyPath");

    /**
     * 通过./keypair.txt获得密钥对
     *
     * @return
     */
    public static KeyPair getKeyPairFromTXT() {
        try {
            return KeyPairUtils.decodeKeyPair(getKeyPairFormTXT());
        } catch (Exception e) {
            logger.error("对应路径下没有密钥文件");
            return null;
        }
    }

    public static KeyPair getKeyPairFromString(String key) {
        try {
            return KeyPairUtils.decodeKeyPair(key);
        } catch (Exception e) {
            logger.error("对应路径下没有密钥文件");
            return null;
        }
    }


    public static String getKeyPairFormTXT(){
        try {
            FileInputStream in = new FileInputStream(keyPath);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            String key = new String(buffer);
            logger.info("成功获得"+keyPath+"路径下的密钥");
            return key;
        }catch(Exception e){
            logger.error("对应路径下没有密钥文件");
            return null;
        }
    }

    /**
     * 将密钥对存贮在./keypair.txt文件中
     *
     * @param keyPair
     */
    public static void SaveKeyPairToTXT(KeyPair keyPair) {
        try {
            logger.info("开始写密钥到"+keyPath);
            FileOutputStream fos = new FileOutputStream(keyPath);
            fos.write(KeyPairUtils.encodePrivateKeyBase64(keyPair).getBytes());
            fos.close();
            logger.info("写密钥成功");
        } catch (Exception e) {
            logger.error("写密钥失败");
            e.printStackTrace();
        }

    }


    /**
     * 获得密钥对
     *
     * @return
     */
    public static KeyPair getKeyPair() {
        KeyPairGenerator edDsaKpg = new KeyPairGenerator();
        logger.info("成功获取新的密钥对");
        return edDsaKpg.generateKeyPair();
    }

    //TODO 以下两个方法均是从项目下txt获得密钥，方便测试使用。！！！！之后应该吧路径写入配置文件

    /**
     * 从txt中获得发送交易使用的公钥
     *
     * @return
     */
    public static EdDSAPublicKey getPublic() {
        logger.info("获得"+keyPath+"中的公钥");
        return (EdDSAPublicKey) getKeyPairFromTXT().getPublic();
    }

    /**
     * 公钥转换成字符串
     * @param key
     * @return
     */
    public static String pubKeyToString(EdDSAPublicKey key){
        return KeyPairUtils.encodePublicKeyInBase58(key);
    }

    /**
     * 从txt获得发送交易使用的私钥
     *
     * @return
     */
    public static EdDSAPrivateKey getPrivate() {
        logger.info("获得"+keyPath+"中的私钥");
        return (EdDSAPrivateKey) getKeyPairFromTXT().getPrivate();
    }

    public static void main(String[] args) {
        logger.info(keyPath);
        KeyPairHolder.SaveKeyPairToTXT(KeyPairHolder.getKeyPair());
    }

}
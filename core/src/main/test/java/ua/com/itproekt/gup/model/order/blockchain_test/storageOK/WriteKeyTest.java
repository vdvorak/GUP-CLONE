package ua.com.itproekt.gup.model.order.blockchain_test.storageOK;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class WriteKeyTest {

    public static final int     KEY_SIZE = 2048;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown(){
    }

    @Test
    public void testReaderKey()
            throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        Security.addProvider(new BouncyCastleProvider());

        KeyPair    keyPair = generateRSAKeyPair();
        RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey   pub = (RSAPublicKey) keyPair.getPublic();

        writePemFile(priv, "PRIVATE KEY", "id_rsa");
        writePemFile(pub, "PUBLIC KEY", "id_rsa.pub");

        //////////////////////////////////////////////////////////////////////
        System.err.println("//////////////////////////////////////////////////////////////////////");
        KeyPair    keyPair22 = generateRSAKeyPair();
//        PemObject pemObject22 = new PemObject("PUBLIC KEY", keyPair22.getPublic().getEncoded());
////        System.err.println(Base64.getEncoder().encodeToString(pemObject22));
//
////        String pemKey00 = "-----BEGIN RSA PUBLIC KEY-----\n"
////                + "MIGHAoGBANAahj75ZIz9nXqW2H83nGcUao4wNyYZ9Z1kiNTUYQl7ob/RBmDzs5rY\n"
////                + "mUahXAg0qyS7+a55eU/csShf5ATGzAXv+DDPcz8HrSTcHMEFpuyYooX6PrIZ07Ma\n"
////                + "XtsJ2J4mhlySI5uOZVRDoaFY53MPQx5gud2quDz759IN/0gnDEEVAgED\n"
////                + "-----END RSA PUBLIC KEY-----\n";
////        PemReader pemReader00 = new PemReader(new StringReader(pemKey00));
////        RSAPublicKey rsaPubKey00 = (RSAPublicKey) pemReader00.readPemObject();
////        System.out.println("Public key: "+rsaPubKey00);

        PemObject pemObject22 = new PemObject("PUBLIC KEY", keyPair22.getPublic().getEncoded());
        PemWriter pemWriter22 = new PemWriter(new OutputStreamWriter(new FileOutputStream("id_rsa22.pub")));
        pemWriter22.writeObject(pemObject22);
        pemWriter22.close();
        PemReader pemReader22 = new PemReader( new InputStreamReader(new FileInputStream("id_rsa22.pub")) );
        PemObject pemObject33 = pemReader22.readPemObject();
        byte[]   publicKey33 = pemObject33.getContent();
        X509EncodedKeySpec pubKeySpec33 = new X509EncodedKeySpec(publicKey33);
        KeyFactory factory33 = KeyFactory.getInstance("RSA", "BC");
        PublicKey publicKey333 = factory33.generatePublic(pubKeySpec33);
        System.err.println(publicKey333);




        //////////////////////////////////////////////////////////////////////
        String content = new String(Files.readAllBytes(Paths.get("id_rsa.pub")));
        System.err.println(content);

//        PemReader pemReader = new PemReader( new InputStreamReader(new FileInputStream("id_rsa.pub")) );

//        String strPub2 = FileUtils.readFileToString(new File("id_rsa.pub"), "UTF-8");
        String strPub2 = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkkk5q+junsiy1J2pmKAA\n" +
                "wUvDgfgQfjeQZOIN5KXvyj2IxFaFje6LYaGdubrSGX8zyCodUZP7zgnqFaBpUojm\n" +
                "C8gvzY86WzaFfKS2uBh2+l8QLiDoz8EYXc3wucR93ZEY+aqK/cpxsTuy60dhwJ5c\n" +
                "vFlYh0UuWe7iBvw+I+jJsCAN5XsJ2sfbhtMvDMbhgs/f4DB+00y++QEWvY1lVGRG\n" +
                "7LWhHx8nbohtZl+lVIVAUXJOicjisV5Z253ixJQKr9elkshnFynIAmlAtPLZycWC\n" +
                "3fwFT0Xt1hZ7eMSn6HXf71B1Cc5u4VsytV/P9zDnwMKPBugaSriMzf8Sblgl+d53\n" +
                "1QIDAQAB\n" +
                "-----END PUBLIC KEY-----";
        PemReader pemReader = new PemReader( new InputStreamReader(new ByteArrayInputStream(strPub2.getBytes())) );

        PemObject pemObject = pemReader.readPemObject();
        byte[]   publicKey2 = pemObject.getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey2);
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PublicKey publicKey3 = factory.generatePublic(pubKeySpec);
        System.err.println(publicKey3);

        //////////////////////////////////////////////////////////////////////
        System.out.println("//////////////////////////////////////////////////");

        KeyPair    keyPair2 = generateRSAKeyPair();
        RSAPrivateKey priv2 = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey   pub2 = (RSAPublicKey) keyPair.getPublic();

        System.out.println(priv2);
        System.out.println(pub2);
    }

    public KeyPair generateRSAKeyPair()
            throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(KEY_SIZE);

        KeyPair keyPair = generator.generateKeyPair();
        return keyPair;
    }

    public void writePemFile(Key key, String description, String filename)
            throws FileNotFoundException, IOException {
        PemFile pemFile = new PemFile(key, description);
        pemFile.write(filename);
    }

}

//package ua.com.itproekt.gup.service.blockchain.storage2;
//
//
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.util.encoders.Hex;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.security.*;
//import java.security.spec.InvalidKeySpecException;
//
//
///**
// http://stackoverflow.com/questions/7124735/hmac-sha256-algorithm-for-signature-calculation
// */
//
//public class TestTransaction2 {
//
//    public static void main(final String[] args)
//            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IOException, SignatureException {
//        Security.addProvider(new BouncyCastleProvider());
//
//        // Digest the mutation twice.
//        byte[]  mutation = new byte[]{
//                10, 8, 47, 13, -126, -118, 96, -15, 87, 39,
//                18, 22, 10, 3, 107, 101, 121, 18, 6, 10,
//                4, 100, 97, 116, 97, 26, 7, 118, 101, 114,
//                115, 105, 111, 110, 26, 8, 109, 101, 116, 97,
//                100, 97, 116, 97};
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[]    digest = md.digest(mutation); //byte[]    digest = md.digest( md.digest(mutation) );
//        System.out.println("DIGEST   " + Hex.toHexString(digest));
//
//        ////////////////////////////////////////////////////////////////////////
//        System.out.println();
//
//        // Generate PRIVATE & PUBLIC KEYs from the private key shown in the send box wallet
//        String                PRIVATE_KEY = "XfC4pPna8BsQrcrC6FuFXgnqSgVHjfUrxW";
//
//        KeyPair    keyPair = Main2.generateRSAKeyPair();
//        PrivateKey privateKey2 = keyPair.getPrivate();
//
////        ECParameterSpec   ecParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
////        ECDomainParameters ecDomainParams = new ECDomainParameters(
////                ecParameterSpec.getCurve(),
////                ecParameterSpec.getG(),
////                ecParameterSpec.getN(),
////                ecParameterSpec.getH(),
////                ecParameterSpec.getSeed());
////        KeyFactory             keyFactory = KeyFactory.getInstance("ECDsA", "BC");
////        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec( new BigInteger(Base64.decode(PRIVATE_KEY)), ecParameterSpec );
////        PrivateKey             privateKey = keyFactory.generatePrivate(ecPrivateKeySpec);
////        System.out.println("PRIVATE  " + Hex.toHexString(privateKey.getEncoded()));
////        ECPoint                         Q = ecDomainParams.getG().multiply( new BigInteger(privateKey.getEncoded()) );
////        final byte[]            publicKey = Q.getEncoded( true );
//////        System.out.println("PUBLIC   " + Hex.toHexString(publicKey));
//
//        String publicKey2 = new String(Files.readAllBytes(Paths.get("id_rsa.pub")));
//        System.out.println("PUBLIC   " + publicKey2);
//
//
//        ////////////////////////////////////////////////////////////////////////
//        System.out.println();
//
//        // Sign the mutation with the private key
//        Signature signature = Signature.getInstance("RSA", "BC"); //Signature signature = Signature.getInstance("NONEwithECDSA", "BC");
//        signature.initSign( privateKey2, new FixedRand() ); //signature.initSign( privateKey, new FixedRand() );
//        signature.update(digest);
//        final byte[]   sign = signature.sign();
//        System.out.println("SIGN     " + Hex.toHexString(sign));
//
//
//        ////////////////////////////////////////////////////////////////////////
//        System.out.println();
//
//        // Create the JSON representation of the transaction to post
//        String format = "{\n" +
//                "    \"mutation\": \"%s\",\n" +
//                "    \"signature\": [\n" +
//                "        {\n" +
//                "            \"sign\": \"%s\"\n" +
//                "            \"publicKey\": \"%s\",\n" +
//                "        }\n" +
//                "    ]\n" +
//                "}";
//        System.out.println("POST");
//////        System.out.println( String.format(format, Hex.toHexString(mutation), Hex.toHexString(publicKey), Hex.toHexString(sign)) ); // json
////        System.out.println( String.format(format, Hex.toHexString(mutation), Hex.toHexString(sign), publicKey2) );
//    }
//
////    public static void main(final String[] args)
////            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IOException, SignatureException {
////        Security.addProvider(new BouncyCastleProvider());
////
////        // Digest the mutation twice.
////        byte[]  mutation = new byte[]{
////                10, 8, 47, 13, -126, -118, 96, -15, 87, 39,
////                18, 22, 10, 3, 107, 101, 121, 18, 6, 10,
////                4, 100, 97, 116, 97, 26, 7, 118, 101, 114,
////                115, 105, 111, 110, 26, 8, 109, 101, 116, 97,
////                100, 97, 116, 97};
////        MessageDigest md = MessageDigest.getInstance("SHA-256");
////        byte[]    digest = md.digest(mutation); //byte[]    digest = md.digest( md.digest(mutation) );
////        System.out.println("DIGEST   " + Hex.toHexString(digest));
////
////        ////////////////////////////////////////////////////////////////////////
////        System.out.println();
////
////        // Generate PRIVATE & PUBLIC KEYs from the private key shown in the send box wallet
////        String                PRIVATE_KEY = "XfC4pPna8BsQrcrC6FuFXgnqSgVHjfUrxW";
////        ECParameterSpec   ecParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
////        ECDomainParameters ecDomainParams = new ECDomainParameters(
////                ecParameterSpec.getCurve(),
////                ecParameterSpec.getG(),
////                ecParameterSpec.getN(),
////                ecParameterSpec.getH(),
////                ecParameterSpec.getSeed());
////        KeyFactory             keyFactory = KeyFactory.getInstance("ECDsA", "BC");
////        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec( new BigInteger(Base64.decode(PRIVATE_KEY)), ecParameterSpec );
////        PrivateKey             privateKey = keyFactory.generatePrivate(ecPrivateKeySpec);
////        System.out.println("PRIVATE  " + Hex.toHexString(privateKey.getEncoded()));
////        ECPoint                         Q = ecDomainParams.getG().multiply( new BigInteger(privateKey.getEncoded()) );
////        final byte[]            publicKey = Q.getEncoded( true );
////        System.out.println("PUBLIC   " + Hex.toHexString(publicKey));
////
////
////        ////////////////////////////////////////////////////////////////////////
////        System.out.println();
////
////        // Sign the mutation with the private key
////        Signature signature = Signature.getInstance("NONEwithECDSA", "BC");
////        signature.initSign( privateKey, new FixedRand() );
////        signature.update(digest);
////        final byte[]   sign = signature.sign();
////        System.out.println("SIGN     " + Hex.toHexString(sign));
////
////
////        ////////////////////////////////////////////////////////////////////////
////        System.out.println();
////
////        // Create the JSON representation of the transaction to post
////        String format = "{\n" +
////                "    \"mutation\": \"%s\",\n" +
////                "    \"signatures\": [\n" +
////                "        {\n" +
////                "            \"pub_key\": \"%s\",\n" +
////                "            \"signature\": \"%s\"\n" +
////                "        }\n" +
////                "    ]\n" +
////                "}";
////        System.out.println("POST");
////        System.out.println( String.format(format, Hex.toHexString(mutation), Hex.toHexString(publicKey), Hex.toHexString(sign)) ); // json
////    }
//
////    public static void main(final String[] args)
////            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IOException, SignatureException {
////        Security.addProvider(new BouncyCastleProvider());
////
////        Provider bc = Security.getProvider("BC");
//////        System.out.println("PROVIDER " + bc.toString());
////
////        // Digest the mutation twice.
////        byte[]  mutation = new byte[]{
////                10, 8, 47, 13, -126, -118, 96, -15, 87, 39,
////                18, 22, 10, 3, 107, 101, 121, 18, 6, 10,
////                4, 100, 97, 116, 97, 26, 7, 118, 101, 114,
////                115, 105, 111, 110, 26, 8, 109, 101, 116, 97,
////                100, 97, 116, 97};
////        MessageDigest md = MessageDigest.getInstance("SHA-256");
////        byte[]    digest = md.digest( md.digest(mutation) );
////        System.out.println("DIGEST   " + Hex.toHexString(digest));
////
////        // Generate PRIVATE & PUBLIC KEYs from the private key shown in the send box wallet.
////        String                PRIVATE_KEY = "XfC4pPna8BsQrcrC6FuFXgnqSgVHjfUrxW";
////        ECParameterSpec   ecParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
////        ECDomainParameters ecDomainParams = new ECDomainParameters(
////                ecParameterSpec.getCurve(),
////                ecParameterSpec.getG(),
////                ecParameterSpec.getN(),
////                ecParameterSpec.getH(),
////                ecParameterSpec.getSeed());
////        KeyFactory             keyFactory = KeyFactory.getInstance("ECDsA", "BC");
////        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec( new BigInteger(Base64.decode(PRIVATE_KEY)), ecParameterSpec );
////        PrivateKey             privateKey = keyFactory.generatePrivate(ecPrivateKeySpec);
////        System.out.println("PRIVATE  " + Hex.toHexString(privateKey.getEncoded()));
////        ECPoint                         Q = ecDomainParams.getG().multiply( new BigInteger(privateKey.getEncoded()) );
////        final byte[]            publicKey = Q.getEncoded( true );
////        System.out.println("PUBLIC   " + Hex.toHexString(publicKey));
////
////        // Sign the mutation with the private key
////        Signature signature = Signature.getInstance("NONEwithECDSA", "BC");
////        signature.initSign( privateKey, new FixedRand() );
////        signature.update(digest);
////        final byte[]   sign = signature.sign();
////        System.out.println("SIGN     " + Hex.toHexString(sign));
////
////        // Create the JSON representation of the transaction to post.
////        String format = "{\n" +
////                "    \"mutation\": \"%s\",\n" +
////                "    \"signatures\": [\n" +
////                "        {\n" +
////                "            \"pub_key\": \"%s\",\n" +
////                "            \"signature\": \"%s\"\n" +
////                "        }\n" +
////                "    ]\n" +
////                "}";
////        String json = String.format(format, Hex.toHexString(mutation), Hex.toHexString(publicKey), Hex.toHexString(sign));
////        System.out.println("POST");
////        System.out.println(json);
////
//////        // Post the transaction.
//////        OkHttpClient client = getUnsafeOkHttpClient();
//////        MediaType mediaType = MediaType.parse("application/octet-stream");
//////        RequestBody body = RequestBody.create(mediaType, json);
//////        Request request = new Request.Builder()
//////                .url("https://test.openchain.org/submit")
//////                .post(body)
//////                .addHeader("cache-control", "no-cache")
//////                .addHeader("postman-token", "d7796975-3862-c6f4-fe37-0088cc1b198f")
//////                .build();
//////
//////        // Show the response.
//////        System.out.println("RESPONSE");
//////        Response response = client.newCall(request).execute();
//////        System.out.println(response.body().string());
////    }
//
////    private static OkHttpClient getUnsafeOkHttpClient() {
////        try {
////            // Create a trust manager that does not validate certificate chains
////            final X509TrustManager x509TrustManager = new X509TrustManager() {
////                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
////                }
////                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
////                }
////                public X509Certificate[] getAcceptedIssuers() {
////                    return new X509Certificate[]{};
////                }
////            };
////            final TrustManager[] trustAllCerts = new TrustManager[]{ x509TrustManager };
////
////            // Install the all-trusting trust manager
////            final SSLContext sslContext = SSLContext.getInstance("SSL");
////            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
////
////            // Create an ssl socket factory with our all-trusting manager
////            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
////            OkHttpClient okHttpClient = new OkHttpClient.Builder()
////                    .sslSocketFactory(sslSocketFactory, x509TrustManager)
////                    .hostnameVerifier(new HostnameVerifier() {
////                        public boolean verify(String s, SSLSession sslSession) {
////                            return true;
////                        }
////                    })
////                    .build();
////            return okHttpClient;
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }
////    }
//
//}
//
//
///**
// * This class represents a SecureRandom which produces the same value.
// */
//class FixedRand extends SecureRandom { //private static class FixedRand extends SecureRandom {
//    MessageDigest sha;
//    byte[] state;
//
//    FixedRand() {
//        try {
//            sha = MessageDigest.getInstance("SHA-256");
//            state = sha.digest();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("can't find SHA-256!");
//        }
//    }
//    public void nextBytes(
//            byte[] bytes) {
//        int off = 0;
//        sha.update(state);
//        while (off < bytes.length) {
//            state = sha.digest();
//            if (bytes.length - off > state.length) {
//                System.arraycopy(state, 0, bytes, off, state.length);
//            } else {
//                System.arraycopy(state, 0, bytes, off, bytes.length - off);
//            }
//            off += state.length;
//            sha.update(state);
//        }
//    }
//}
//

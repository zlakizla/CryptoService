package webserver;

import crypto.Base58;
import org.glassfish.jersey.message.internal.OutboundJaxrsResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.fail;

public class ApiCryptoTest {

    private static final String MESSAGE = "Test message for check encrypt/decrypt";
    private static String SEED_ACCOUNT1;
    private static String SEED_ACCOUNT2;
    private static String Account1_privateKey;
    private static String Account1_publicKey;
    private static String Account2_privateKey;
    private static String Account2_publicKey;
    private String MESSAGE_ENCRYPT;
    private String SIGN;

    /**
     * Before init test generate two seed and key Pair for each seed
     */
    @Before
    public void initSeed() throws ParseException {

        byte[] seedAccount1 = new byte[32];
        new Random().nextBytes(seedAccount1);
        SEED_ACCOUNT1 = Base58.encode(seedAccount1);

        byte[] seedAccount2 = new byte[32];
        new Random().nextBytes(seedAccount2);
        SEED_ACCOUNT2 = Base58.encode(seedAccount2);

        Object result = new ApiCrypto().GenerateKeyPair(SEED_ACCOUNT1);
        Object keysObject = ((OutboundJaxrsResponse) result).getEntity();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(keysObject.toString());
        JSONObject keyPair = ((JSONObject) jsonObject.get("keyPair"));

        Account1_privateKey = keyPair.get("privateKey").toString();
        Account1_publicKey = keyPair.get("publicKey").toString();

        Object result2 = new ApiCrypto().GenerateKeyPair(SEED_ACCOUNT2);
        Object keysObject2 = ((OutboundJaxrsResponse) result2).getEntity();
        JSONParser jsonParser2 = new JSONParser();
        JSONObject jsonObject2 = (JSONObject) jsonParser2.parse(keysObject2.toString());
        JSONObject keyPair2 = ((JSONObject) jsonObject2.get("keyPair"));

        Account2_privateKey = keyPair2.get("privateKey").toString();
        Account2_publicKey = keyPair2.get("publicKey").toString();

    }

    @Test
    public void generateSeed() throws Exception {
        Object result = new ApiCrypto().GenerateSeed();
        Object seed = ((OutboundJaxrsResponse) result).getEntity();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(seed.toString());
        Assert.assertTrue(jsonObject.keySet().contains("seed"));
    }

    @Test
    public void generateKeyPair() throws ParseException {
        Object result = new ApiCrypto().GenerateKeyPair(SEED_ACCOUNT1);
        Object keysObject = ((OutboundJaxrsResponse) result).getEntity();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(keysObject.toString());
        JSONObject keys = (JSONObject) jsonObject.get("keyPair");

        Assert.assertTrue(keys.keySet().contains("privateKey"));
        Assert.assertTrue(keys.keySet().contains("publicKey"));
        Assert.assertNotNull(keys.get("publicKey"));
        Assert.assertNotNull(keys.get("privateKey"));
    }

    @Test
    public void encrypt() throws Exception {

        Object result = new ApiCrypto().Encrypt("{\"message\":\"" + MESSAGE + "\", " +
                "\"keyPair\":{\"publicKey\":\"" + Account2_publicKey + "\"," +
                "\"privateKey\":\"" + Account1_privateKey + "\"}}");
        Object encrypt = ((OutboundJaxrsResponse) result).getEntity();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(encrypt.toString());
        MESSAGE_ENCRYPT = jsonObject.get("encrypted").toString();

        Assert.assertNotNull(jsonObject.get("encrypted"));
    }

    @Test
    public void decrypt() throws Exception {
        if (MESSAGE_ENCRYPT == null)
            encrypt();

        Object result = new ApiCrypto().Decrypt("{\"message\": \"" + MESSAGE_ENCRYPT +
                "\", \"keyPair\":{\"publicKey\":\"" + Account1_publicKey + "\",\n" +
                "\"privateKey\":\"" + Account2_privateKey + "\"}}");

        Object encrypt = ((OutboundJaxrsResponse) result).getEntity();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(encrypt.toString());

        if (jsonObject.get("Error") != null)
            fail("Cannot decrypt in current keys. Check public and private key.");

        Assert.assertEquals(jsonObject.get("decrypted").toString(), MESSAGE);
        Assert.assertNotNull(jsonObject.get("decrypted"));
    }

    @Test
    public void sign() throws Exception {
        Object result = new ApiCrypto().Sign("{\"keyPair\":\n" +
                "{\"publicKey\":\"" + Account1_publicKey + "\",\n" +
                "\"privateKey\":\"" + Account1_privateKey + "\"}," +
                " \"message\":\"" + MESSAGE + "\"}");

        Object sign = ((OutboundJaxrsResponse) result).getEntity();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(sign.toString());

        Assert.assertNotNull(jsonObject.get("signature"));
        SIGN = jsonObject.get("signature").toString();
    }

    @Test
    public void verifySignature() throws Exception {
        if (SIGN == null)
            sign();

        Object result = new ApiCrypto().VerifySignature("{\"publicKey\":\"" + Account1_publicKey + "\"," +
                "\"signature\":\"" + SIGN + "\"," +
                "\"message\":\"" + MESSAGE + "\"}");

        Object sign = ((OutboundJaxrsResponse) result).getEntity();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(sign.toString());

        Assert.assertTrue(Boolean.parseBoolean(jsonObject.get("signatureVerify").toString()));

    }
}
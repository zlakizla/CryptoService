package webserver;


import crypto.AEScrypto;
import crypto.Base58;
import crypto.Crypto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import utils.Pair;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;

@Path("crypto")
public class ApiCrypto {

    /**
     * Generate random seed in Base Base58
     *
     * @return JSON string seed in encode Base58
     *
     * <h2>Example request</h2>
     * http://127.0.0.1:8080/crypto/generateSeed
     *
     * <h2>Example response</h2>
     * {"seed":"D9FFKCjo4cG2jL9FrZmXCKfQypZG8AdbnF7vtm5Aqou9"}
     */
    @GET
    @Path("generateSeed")
    public Response GenerateSeed() {

        byte[] seed = new byte[32];
        new Random().nextBytes(seed);
        String seedBase58 = Base58.encode(seed);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seed", seedBase58);

        return Response.status(200).header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .entity(jsonObject.toJSONString())
                .build();
    }

    /**
     * @param seed is a {@link #GenerateSeed()} master key for generating key pair
     * @return key pair. Public key - byte[32], Private key - byte[64]
     */
    @GET
    @Path("generateKeyPair/{seed}")
    public Response GenerateKeyPair(@PathParam("seed") String seed) {

        Pair<byte[], byte[]> keyPair = Crypto.getInstance().createKeyPair(Base58.decode(seed));

        HashMap<String, String> mapKey = new HashMap<>();
        mapKey.put("publicKey", Base58.encode(keyPair.getB()));
        mapKey.put("privateKey", Base58.encode(keyPair.getA()));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("keyPair", mapKey);

        return Response.status(200).header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .entity(jsonObject.toJSONString())
                .build();
    }

    /**
     * Encrypt message using keys {@link #GenerateKeyPair(String)} and some message.
     *
     * @param encrypt is JSON contains keys and message for encrypt
     * @return JSON string contains encode Base58 message
     * @throws Exception
     */
    @POST
    @Path("encrypt")
    public Response Encrypt(String encrypt) throws Exception {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(encrypt);

        String message = jsonObject.get("message").toString();
        JSONObject keys = (JSONObject) jsonObject.get("keyPair");
        byte[] publicKey = Base58.decode(keys.get("publicKey").toString());
        byte[] privateKey = Base58.decode(keys.get("privateKey").toString());

        String result = Base58.encode(AEScrypto.dataEncrypt(message.getBytes(), privateKey, publicKey));
        JSONObject jsonObjectResult = new JSONObject();
        jsonObjectResult.put("encrypted", result);
        return Response.status(200).header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .entity(jsonObjectResult.toJSONString())
                .build();
    }

    /**
     * Decrypt message
     *
     * @param decrypt Json row contain keys and message for decrypt
     * @return Json string. if the decryption was successful, it will return the message in coding UTF-8.
     * If cannot decrypt return error.
     * @throws Exception
     */
    @POST
    @Path("decrypt")
    public Response Decrypt(String decrypt) throws Exception {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(decrypt);
        byte[] message = Base58.decode(jsonObject.get("message").toString());
        JSONObject keys = (JSONObject) jsonObject.get("keyPair");
        byte[] publicKey = Base58.decode(keys.get("publicKey").toString());
        byte[] privateKey = Base58.decode(keys.get("privateKey").toString());
        JSONObject jsonObjectResult = new JSONObject();
        byte[] result = AEScrypto.dataDecrypt(message, privateKey, publicKey);

        if (result == null)
            jsonObjectResult.put("Error", "Cannot decrypt. Invalid keys.");
        else
            jsonObjectResult.put("decrypted", new String(result, StandardCharsets.UTF_8));

        return Response.status(200).header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .entity(jsonObjectResult.toJSONString())
                .build();
    }

    /**
     * Get signature
     *
     * @param toSign JSON string contains {@link #GenerateKeyPair(String)} keyPair for sign and message
     * @return
     * @throws Exception
     */
    @POST
    @Path("sign")
    public Response Sign(String toSign) throws Exception {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(toSign);
        String message = jsonObject.get("message").toString();
        JSONObject keys = (JSONObject) jsonObject.get("keyPair");

        Pair<byte[], byte[]> pair = new Pair<>();
        pair.setA(Base58.decode(keys.get("privateKey").toString()));
        pair.setB(Base58.decode(keys.get("publicKey").toString()));
        byte[] sign = Crypto.getInstance().sign(pair, message.getBytes());

        JSONObject jsonObjectSign = new JSONObject();
        jsonObjectSign.put("signature", Base58.encode(sign));

        return Response.status(200).header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .entity(jsonObjectSign.toJSONString())
                .build();
    }

    /**
     * Verify signature
     *
     * @param toVerifySign contains there public key, signature and message.
     * @return JSON string contains
     * @throws Exception
     */
    @POST
    @Path("verifySignature")
    public Response VerifySignature(String toVerifySign) throws Exception {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(toVerifySign);

        byte[] publicKey = Base58.decode(jsonObject.get("publicKey").toString());
        byte[] signature = Base58.decode(jsonObject.get("signature").toString());
        byte[] message = jsonObject.get("message").toString().getBytes();

        boolean statusVerify = Crypto.getInstance().verify(publicKey, signature, message);

        JSONObject jsonObjectResult = new JSONObject();
        jsonObjectResult.put("signatureVerify", statusVerify);

        return Response.status(200).header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .entity(jsonObjectResult.toJSONString())
                .build();
    }
}

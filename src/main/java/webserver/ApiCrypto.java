package webserver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Random;

@Path("crypto")
public class ApiCrypto {

    private Random random;

    @GET
    @Path("generateSeed")
    public Response GenerateSeed() {

        byte[] seed = new byte[32];
        this.random.nextBytes(seed);



        return Response.status(200).header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .entity("")
                .build();
    }


    /**
     * @param seed is a master key for generating key pair
     * @return key pair. Public key - byte[32], Private key - byte[64]
     */
    @GET
    @Path("generateKeyPair")
    public Response GenerateKeyPair(String seed) {

        return Response.status(200).header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .entity("safdwef")
                .build();


    }
}

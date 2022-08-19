package auth.server;

import auth.client.MySaslClient;
import auth.client.MySaslClientCallbackHandler;
import org.junit.jupiter.api.Test;

import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MySaslServerTest {
    @Test
    void saslServer() throws Exception  {
        MySaslServer saslServer = new MySaslServer(
                "CRAM-MD5",
                "PLAIN",
                "localhost",
                null,
                new MySaslServerCallbackHandler()
        );

        MySaslClient saslClient = new MySaslClient(new String[]{"CRAM-MD5"},
                null,
                "PLAIN",
                "localhost",
                null,
                new MySaslClientCallbackHandler());

        byte[] challenge;
        byte[] response;

        // authentication
        challenge = saslServer.evaluateResponse(new byte[0]);
        response = saslClient.evaluateChallenge(challenge);

        challenge = saslServer.evaluateResponse(response);
//        response = saslClient.evaluateChallenge(challenge);

        assertTrue(saslServer.isComplete());
        assertTrue(saslClient.isComplete());

        String qop = (String) saslClient.getNegotiatedProperty(Sasl.QOP);
        assertEquals("auth", qop);
    }
}
package auth.client;

import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import java.util.Map;

public class MySaslClient {
    SaslClient saslClient;

    public MySaslClient(String[] mechanisms,
                        String authorizationId,
                        String protocol,
                        String serverName,
                        Map<String,Object> properties,
                        CallbackHandler callbackHandler) throws SaslException {
        this.saslClient = Sasl.createSaslClient(
                mechanisms,
                authorizationId,
                protocol,
                serverName,
                properties,
                callbackHandler
        );
    }

    public byte[] evaluateChallenge(byte[] bytes) throws SaslException{
        return this.saslClient.evaluateChallenge(bytes);
    }

    public boolean isComplete() {
        return this.saslClient.isComplete();
    }

    public Object getNegotiatedProperty(String propertyName) {
        return this.saslClient.getNegotiatedProperty(propertyName);
    }

    public byte[] wrap(byte[] outgoing, int offset, int length) throws SaslException{
        return  this.saslClient.wrap(outgoing, offset, length);
    }
}

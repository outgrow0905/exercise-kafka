package auth.server;

import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import java.util.Map;

public class MySaslServer {
    private SaslServer saslServer;

    public MySaslServer(String mechanism,
                        String protocol,
                        String serverName,
                        Map<String ,Object> properties,
                        CallbackHandler callbackHandler) throws SaslException  {
        this.saslServer = Sasl.createSaslServer(
                mechanism,
                protocol,
                serverName,
                properties,
                callbackHandler
        );
    }

    public byte[] evaluateResponse(byte[] bytes) throws SaslException{
        return this.saslServer.evaluateResponse(bytes);
    }

    public boolean isComplete() {
        return saslServer.isComplete();
    }

    public byte[] unwrap(byte[] incoming, int offset, int length) throws SaslException {
        return this.saslServer.unwrap(incoming, offset, length);
    }
}

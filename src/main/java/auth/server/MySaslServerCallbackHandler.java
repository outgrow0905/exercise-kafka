package auth.server;

import javax.security.auth.callback.*;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;
import java.io.IOException;

public class MySaslServerCallbackHandler implements CallbackHandler {
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof AuthorizeCallback) {
                AuthorizeCallback ac = (AuthorizeCallback) callback;
                //Perform application-specific authorization action
                System.out.println("Server AuthorizeCallback AuthenticationID: " + ac.getAuthenticationID());
                System.out.println("Server AuthorizeCallback AuthorizationID: " + ac.getAuthorizationID());
                System.out.println("Server AuthorizeCallback AuthorizedID: " + ac.getAuthorizedID());
                ac.setAuthorized(true);
            } else if (callback instanceof NameCallback) {
                NameCallback nc = (NameCallback) callback;
                //Collect username in application-specific manner
                System.out.println("Server NameCallback DefaultName: " + nc.getDefaultName());
                System.out.println("Server NameCallback Name: " + nc.getName());
                System.out.println("Server NameCallback Prompt: " + nc.getPrompt());
                nc.setName("myName");
            } else if (callback instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) callback;
                //Collect password in application-specific manner
                System.out.println("Server PasswordCallback Prompt: " + pc.getPrompt());
                System.out.println("Server PasswordCallback Password: " + pc.getPassword());
                pc.setPassword("myPassword".toCharArray());
            } else if (callback instanceof RealmCallback) {
                RealmCallback rc = (RealmCallback) callback;
                //Collect realm data in application-specific manner
                System.out.println("Server RealmCallback DefaultText: " + rc.getDefaultText());
                System.out.println("Server RealmCallback Prompt: " + rc.getPrompt());
                System.out.println("Server RealmCallback Text: " + rc.getText());
                rc.setText("myServer");
            }
        }
    }


}

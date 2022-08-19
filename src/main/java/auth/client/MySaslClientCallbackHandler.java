package auth.client;

import javax.security.auth.callback.*;
import javax.security.sasl.RealmCallback;
import java.io.IOException;

public class MySaslClientCallbackHandler implements CallbackHandler {
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        System.out.println("size(): " + callbacks.length);
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                NameCallback nc = (NameCallback) callback;
                //Collect username in application-specific manner
                System.out.println("Client NameCallback DefaultName: " + nc.getDefaultName());
                System.out.println("Client NameCallback Name: " + nc.getName());
                System.out.println("Client NameCallback Prompt: " + nc.getPrompt());
                nc.setName("myName");
            } else if (callback instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) callback;
                //Collect password in application-specific manner
                System.out.println("Client PasswordCallback Prompt: " + pc.getPrompt());
                System.out.println("Client PasswordCallback Password: " + pc.getPassword());
                pc.setPassword("myPassword".toCharArray());
            } else if (callback instanceof RealmCallback) {
                RealmCallback rc = (RealmCallback) callback;
                //Collect realm data in application-specific manner
                System.out.println("Client RealmCallback DefaultText: " + rc.getDefaultText());
                System.out.println("Client RealmCallback Prompt: " + rc.getPrompt());
                System.out.println("Client RealmCallback Text: " + rc.getText());
                rc.setText("myServer");
            }
        }
    }
}

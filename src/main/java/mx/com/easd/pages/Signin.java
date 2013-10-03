package mx.com.easd.pages;

import mx.com.easd.annotations.AnonymousAccess;
import mx.com.easd.security.AuthenticationException;
import mx.com.easd.services.Authenticator;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * User can sign up on the
 * 
 */
@AnonymousAccess
public class Signin
{
    @Property
    private String flashmessage;

    @Property
    private String username;

    @Property
    private String password;

    @Inject
    private Authenticator authenticator;

    @Component
    private Form loginForm;

    @Inject
    private Messages messages;

    @Log
    public Object onSubmitFromLoginForm()
    {
        try
        {
            authenticator.login(username, password);
        }
        catch (AuthenticationException ex)
        {
            loginForm.recordError(messages.get("error.login"));
            return null;
        }

        return Index.class;
    }

    public String getFlashMessage()
    {
        return flashmessage;
    }

    public void setFlashMessage(String flashmessage)
    {
        this.flashmessage = flashmessage;
    }

}

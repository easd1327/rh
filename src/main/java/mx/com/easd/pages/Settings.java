package mx.com.easd.pages;

import mx.com.easd.dal.CrudServiceDAO;
import mx.com.easd.entities.User;
import mx.com.easd.services.Authenticator;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Allows the user to modify password
 * 
 */
public class Settings
{
    @Inject
    private CrudServiceDAO crudServiceDAO;

    @Inject
    private Messages messages;

    @Inject
    private Authenticator authenticator;

    @InjectPage
    private Signin signin;
    
    @Property
    private String password;

    @Property
    private String verifyPassword;

    @Component
    private Form settingsForm;

    public Object onSuccess()
    {
        if (!verifyPassword.equals(password))
        {
            settingsForm.recordError(messages.get("error.verifypassword"));

            return null;
        }

        User user = authenticator.getLoggedUser();
        authenticator.logout();

        user.setPassword(password);

        crudServiceDAO.update(user);

        signin.setFlashMessage(messages.get("settings.password-changed"));
        
        return signin;
    }
}

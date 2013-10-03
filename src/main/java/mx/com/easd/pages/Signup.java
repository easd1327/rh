package mx.com.easd.pages;

import mx.com.easd.annotations.AnonymousAccess;
import mx.com.easd.dal.CrudServiceDAO;
import mx.com.easd.dal.QueryParameters;
import mx.com.easd.entities.User;
import mx.com.easd.security.AuthenticationException;
import mx.com.easd.services.Authenticator;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This page the user can create an account
 * 
 */
@AnonymousAccess
public class Signup
{

    @Property
    @Validate("username")
    private String username;

    @Property
    @Validate("required, minlength=3, maxlength=50")
    private String fullName;

    @Property
    @Validate("required,email")
    private String email;

    @Property
    @Validate("password")
    private String password;

    @Property
    @Validate("password")
    private String verifyPassword;

    @SuppressWarnings("unused")
    @Property
    private String kaptcha;

    @Inject
    private CrudServiceDAO crudServiceDAO;

    @Component
    private Form registerForm;

    @Inject
    private Messages messages;

    @Inject
    private Authenticator authenticator;

    @SuppressWarnings("unused")
    @InjectPage
    private Signin signin;

    @OnEvent(value = EventConstants.VALIDATE, component = "RegisterForm")
    public void checkForm()
    {
        if (!verifyPassword.equals(password))
        {
            registerForm.recordError(messages.get("error.verifypassword"));
        }
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "RegisterForm")
    public Object proceedSignup()
    {

        User userVerif = crudServiceDAO.findUniqueWithNamedQuery(
                User.BY_USERNAME_OR_EMAIL,
                QueryParameters.with("username", username).and("email", email).parameters());

        if (userVerif != null)
        {
            registerForm.recordError(messages.get("error.userexists"));

            return null;
        }

        User user = new User(fullName, username, email, password);

        crudServiceDAO.create(user);

        try
        {
            authenticator.login(username, password);
        }
        catch (AuthenticationException ex)
        {
            registerForm.recordError("Authentication process has failed");
            return this;
        }

        return Search.class;
    }
}

package mx.com.easd.pages;

import mx.com.easd.annotations.AnonymousAccess;
import mx.com.easd.services.Authenticator;

import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Start page of application .
 */
@AnonymousAccess
public class Index
{
    @Inject
    private Authenticator authenticator;

    public Object onActivate()
    {
        return authenticator.isLoggedIn() ? Search.class : Signin.class;
    }
}

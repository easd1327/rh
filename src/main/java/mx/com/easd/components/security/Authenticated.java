package mx.com.easd.components.security;

import mx.com.easd.services.Authenticator;

import org.apache.tapestry5.corelib.base.AbstractConditional;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Checks if the user is logged in
 * 
 */
public class Authenticated extends AbstractConditional
{

    @Inject
    private Authenticator authenticator;

    @Override
    protected boolean test()
    {
        return authenticator.isLoggedIn();
    }

}

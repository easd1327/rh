package mx.com.easd.pages;

import mx.com.easd.dal.CrudServiceDAO;
import mx.com.easd.data.UserWorkspace;
import mx.com.easd.entities.Hotel;
import mx.com.easd.entities.User;
import mx.com.easd.services.Authenticator;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This page displays hotel details, and provide access to booking.
 * 
 */
public class View
{
    @SessionState
    @Property
    private UserWorkspace userWorkspace;

    @SuppressWarnings("unused")
    @Property
    @PageActivationContext
    private Hotel hotel;

    @Inject
    private Authenticator authenticator;

    @Inject
    private CrudServiceDAO dao;

    /**
     * Start booking process.
     * 
     * @param hotel
     * @return link to the current hotel booking
     */
    @OnEvent(value = EventConstants.SUCCESS, component = "startBookingForm")
    Object startBooking(Hotel hotel)
    {
        User user = (User) dao.find(User.class, authenticator.getLoggedUser().getId());
        userWorkspace.startBooking(hotel, user);
        return Book.class;
    }
}

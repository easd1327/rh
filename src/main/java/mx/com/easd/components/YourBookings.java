package mx.com.easd.components;

import java.util.List;

import mx.com.easd.dal.CrudServiceDAO;
import mx.com.easd.dal.QueryParameters;
import mx.com.easd.entities.Booking;
import mx.com.easd.services.Authenticator;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * List all the bookings of the current user.
 * 
 */
public class YourBookings
{
    @Inject
    private CrudServiceDAO crudDao;

    @Inject
    private Authenticator authenticator;

    @Property
    private List<Booking> bookings;

    @SuppressWarnings("unused")
    @Property
    private Booking current;

    /**
     * Prepare the list of booking to display, extract all the booking associated to the current
     * logged user.
     * 
     * @return
     */
    @SetupRender
    boolean listBookings()
    {
        bookings = crudDao.findWithNamedQuery(
                Booking.BY_USERNAME,
                QueryParameters.with("username", authenticator.getLoggedUser().getUsername())
                        .parameters());
        return bookings.size() > 0 ? true : false;
    }

    /**
     * Simply cancel the booking
     * 
     * @param booking
     */
    @OnEvent(value = "cancelBooking")
    void cancelBooking(Booking booking)
    {
        crudDao.delete(Booking.class, booking.getId());
    }

}

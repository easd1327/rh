package mx.com.easd.pages;

import java.util.Calendar;

import mx.com.easd.dal.CrudServiceDAO;
import mx.com.easd.data.BedType;
import mx.com.easd.data.Months;
import mx.com.easd.data.UserWorkspace;
import mx.com.easd.data.Years;
import mx.com.easd.entities.Booking;
import mx.com.easd.entities.Hotel;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This page implements booking process for a give hotel.
 * 
 */
public class Book
{
    @SessionState
    @Property
    private UserWorkspace userWorkspace;

    @SuppressWarnings("unused")
    @Property
    @PageActivationContext
    private Hotel hotel;

    @Inject
    private Block bookBlock;

    @Inject
    private Block confirmBlock;

    @Inject
    private Messages messages;

    @Inject
    private CrudServiceDAO dao;

    @InjectComponent
    private Form bookingForm;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private Booking booking;

    @Persist
    private boolean confirmationStep;

    @SuppressWarnings("unused")
    @Property
    private SelectModel bedType = new BedType();

    @SuppressWarnings("unused")
    @Property
    private SelectModel years = new Years();

    @SuppressWarnings("unused")
    @Property
    private SelectModel months = new Months();

    /**
     * Get the current step
     * 
     * @return confirmation or booking bloc
     */
    public Block getStep()
    {
        return confirmationStep ? confirmBlock : bookBlock;
    }

    public String getSecuredCardNumber()
    {

        return booking.getCreditCardNumber().substring(12);
    }

    @Log
    public Object onActivate(Long hotelId)
    {
        booking = userWorkspace.restoreBooking(hotelId);

        if (booking == null)
        {
            return Search.class;
        }
        else
        {
            confirmationStep = booking.getStatus();
            return null;
        }
    }

    @OnEvent(value = EventConstants.ACTIVATE)
    @Log
    public void setupBooking()
    {
        booking = userWorkspace.getCurrent();
        confirmationStep = booking.getStatus();
    }

    @OnEvent(value = EventConstants.VALIDATE, component = "bookingForm")
    public void validateBooking()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (booking.getCheckinDate().before(calendar.getTime()))
        {
            bookingForm.recordError(messages.get("booking_checkInNotFutureDate"));
            return;
        }
        else if (!booking.getCheckinDate().before(booking.getCheckoutDate()))
        {
            bookingForm.recordError(messages.get("booking_checkOutBeforeCheckIn"));
            return;
        }

        userWorkspace.getCurrent().setStatus(true);
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "confirmForm")
    public Object confirm()
    {
        // Create
        dao.create(booking);

        userWorkspace.confirmCurrentBooking(booking);

        booking = null;

        // Return to search
        return Search.class;
    }

    @OnEvent(value = "cancelConfirm")
    @Log
    public void cancelConfim()
    {
        booking.setStatus(false);
    }

    @OnEvent(value = "cancelBooking")
    @Log
    public Object cancelBooking()
    {
        userWorkspace.cancelCurrentBooking(booking);

        booking = null;

        return Search.class;
    }

}

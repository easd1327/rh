package mx.com.easd.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import mx.com.easd.data.UserWorkspace;
import mx.com.easd.entities.Booking;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

/**
 * Display the list of current booking that has not been confirmed yet. You can click on displayed
 * link to continue
 * 
 */
public class Workspace
{
    @SuppressWarnings("unused")
    @Property
    private DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    @SessionState
    @Property
    private UserWorkspace userWorkspace;

    @Inject
    private PageRenderLinkSource linkSource;

    @Property
    private Booking current;

    public List<Booking> getBookings()
    {
        return userWorkspace.getNotConfirmed();
    }

    public Link getBookLink()
    {
        Link result = linkSource.createPageRenderLinkWithContext("book", current.getHotel());
        return result;
    }

    public boolean getIsCurrent()
    {
        return current.equals(userWorkspace.getCurrent().equals(current));
    }
}

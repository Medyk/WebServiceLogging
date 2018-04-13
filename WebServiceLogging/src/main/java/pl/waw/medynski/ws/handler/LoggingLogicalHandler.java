/**
 * History:
 * 
 * 1 (2018-04-13) - Initial release.
 */
package pl.waw.medynski.ws.handler;




/**
 * Imports.
 */
import java.util.Map;
import java.util.UUID;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;




/**
 * LoggingLogicalHandler class.
 * 
 * @author Maciej Medyński
 * @version 1
 */
public class LoggingLogicalHandler implements LogicalHandler<LogicalMessageContext>
{
    /**
     * Message handler.
     * 
     * @param messageContext
     * @return 
     * @since 1
     * @version 1
     */
    @Override
    public boolean handleMessage(LogicalMessageContext messageContext)
    {
        // Outbound flag
        final Boolean outboundMessage = (Boolean)messageContext.get(LogicalMessageContext.MESSAGE_OUTBOUND_PROPERTY);
        
        // Outbound message
        if (Boolean.TRUE.equals(outboundMessage))
        {
            final UUID uuid = (UUID) messageContext.get(KEY_MESSAGE_UUID);
            final long time = (Long) messageContext.get(KEY_NANO_TIME);
            final long timeElapsed = System.nanoTime() - time;
            if (timeElapsed > LONG_TIME) // 1 second
            {
                final Map headers = (Map) messageContext.get(LogicalMessageContext.HTTP_REQUEST_HEADERS);
                LOG.error(LOG_MARKER, LOG_DETAILS, uuid, timeElapsed, headers);
                // header.get("content-length") has request size
            }
            else if (LOG.isDebugEnabled())
            {
                LOG.debug(LOG_MARKER, LOG_OUT, uuid, timeElapsed);
            }
        }
        // Inbound message
        else
        {
            final UUID uuid = UUID.randomUUID();
            messageContext.put(KEY_MESSAGE_UUID, uuid);
            messageContext.put(KEY_NANO_TIME, System.nanoTime());
            if (LOG.isDebugEnabled())
            {
                LOG.debug(LOG_MARKER, LOG_IN, uuid);
            }
        }
        return true;
    }
    

    /**
     * Fault handler.
     * 
     * @param messageContext
     * @return 
     * @since 1
     * @version 1
     */
    @Override
    public boolean handleFault(LogicalMessageContext messageContext)
    {
        return true;
    }
    

    /**
     * Close.
     * 
     * @param context 
     * @since 1
     * @version 1
     */
    @Override
    public void close(MessageContext context)
    {
    }
    
    
    
    
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(LoggingLogicalHandler.class);
    
    
    /**
     * WebService logging marker.
     */
    private static final Marker LOG_MARKER = MarkerFactory.getMarker("WS_PROFILE");
    
    
    /**
     * Message-in log format.
     */
    private static final String LOG_IN = "logical inbound: {}";
    
    
    /**
     * Message-out log format.
     */
    private static final String LOG_OUT = "logical outbound: {}, handling time: {} ns";
    
    
    /**
     * Message headers details log format.
     */
    private static final String LOG_DETAILS = "logical outbound: {}, handling time: {} ns, http headers: {}";
    
    
    /**
     * Message UUID key.
     */
    private static final String KEY_MESSAGE_UUID = "pl.waw.medynski.ws.handler.LoggingLogicalHandler.UUID";
    
    
    /**
     * Message nano-time key.
     */
    private static final String KEY_NANO_TIME = "pl.waw.medynski.ws.handler.LoggingLogicalHandler.TIME";
    
    
    /**
     * WebService long time.
     */
    private static final long LONG_TIME = 1_000_000_000L;
}

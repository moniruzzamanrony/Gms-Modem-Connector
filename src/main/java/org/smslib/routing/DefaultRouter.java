/**
 *
 */
package org.smslib.routing;

import org.smslib.AGateway;
import org.smslib.OutboundMessage;

import java.util.Collection;

/**
 * Default Router implementation which actually doesn't perform any custom routing, instead relies on basic routing of ARouter.
 *
 * @author Bassam Al-Sarori
 *
 */
public class DefaultRouter extends ARouter {

    /* (non-Javadoc)
     * @see org.smslib.routing.ARouter#route(org.smslib.OutboundMessage, java.util.Collection)
     */
    @Override
    public Collection<AGateway> customRoute(OutboundMessage msg,
                                            Collection<AGateway> gateways) {

        return gateways;
    }

}

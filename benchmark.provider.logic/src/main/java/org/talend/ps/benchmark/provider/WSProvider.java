package org.talend.ps.benchmark.provider;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.talend.ps.benchmark.common.events.BenchmarkConstants;
import org.talend.ps.benchmark.common.events.History;
import org.w3c.dom.Element;

/**
 * This class implements a generic policy provider.
 * 
 */
@ServiceMode(Service.Mode.PAYLOAD)
public class WSProvider implements Provider<Source> {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(WSProvider.class.getPackage().getName());

    /**
     * Access to WS-Context Attributes. Needed for SOAP-Action and Service-,
     * Port-Name.
     */
    @Resource
    private WebServiceContext wsContext;

    private History history;

	
    public void setHistory(History history) {
		this.history = history;
	}

	@Override
    public Source invoke(Source request) {
        try {

            // Return service response to consumer
        	String id = getMessageId(wsContext.getMessageContext());
        	history.addEvent(id);
            return request;

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error due WSProvider invocation: " + e.getMessage(), e);
            throw new WSProviderException("Error due WSProvider invocation: " + e.getMessage(), e);
        }
    }

	private String getMessageId(MessageContext messageContext) {
			if (messageContext == null || !(messageContext instanceof WrappedMessageContext)) {
			     return null;
			}

			Message message = ((WrappedMessageContext) messageContext).getWrappedMessage();
			List<Header> headers = CastUtils.cast((List<?>) message.get(Header.HEADER_LIST));
			for (Header header : headers) {
				if (header.getName().equals(BenchmarkConstants.ID_HEADER_NAME)) {
					return ((Element) header.getObject()).getTextContent();
				}
			}
			return String.valueOf(((WrappedMessageContext)wsContext.getMessageContext()).getWrappedMessage().hashCode());
		}
}
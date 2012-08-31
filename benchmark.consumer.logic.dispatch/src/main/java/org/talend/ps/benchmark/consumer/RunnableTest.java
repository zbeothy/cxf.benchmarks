package org.talend.ps.benchmark.consumer;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.ws.Dispatch;

import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.staxutils.StaxSource;
import org.apache.cxf.staxutils.StaxUtils;
import org.talend.ps.benchmark.common.events.BenchmarkConstants;
import org.talend.ps.benchmark.common.events.EventsHistory;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class RunnableTest implements Runnable {

	private Dispatch<Source> dispatch;
	private byte[] message;
	private int messageCount;
	private EventsHistory requestHistory;
	private EventsHistory responseHistory;
	

	public RunnableTest(Dispatch<Source> dispatch, byte[] message,
			int messageCount, EventsHistory requestHistory,
			EventsHistory responseHistory) {
		this.message = message;
		this.messageCount = messageCount;
		this.requestHistory = requestHistory;
		this.responseHistory = responseHistory;
		this.dispatch = dispatch;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < messageCount; i++) {
				ByteArrayInputStream is = new ByteArrayInputStream(message);
				StaxSource source = new StaxSource(StaxUtils.createXMLStreamReader(is));
				UUID uuid = UUID.randomUUID();
				setMessageID(dispatch, uuid.toString());
				requestHistory.addEvent(uuid.toString());
				Source s = dispatch.invoke(source);
				if (s instanceof SAXSource) {
				    XMLReader reader = ((SAXSource)s).getXMLReader();
				    reader.setContentHandler(new DefaultHandler());
				    reader.parse((String)null);
				}
				
				responseHistory.addEvent(uuid.toString());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new WSConsumerException("Error in consumer thread: " + e.getMessage(), e);
		}
	}

    private void setMessageID(Dispatch<Source> dispatcher, String messageID) throws JAXBException {
    	List<Header> headersList = new ArrayList<Header>();
    	Header testSoapHeader = new Header(BenchmarkConstants.ID_HEADER_NAME, messageID, new JAXBDataBinding(String.class));
    	headersList.add(testSoapHeader);
    	dispatcher.getRequestContext().put(Header.HEADER_LIST, headersList);
    }
    
}

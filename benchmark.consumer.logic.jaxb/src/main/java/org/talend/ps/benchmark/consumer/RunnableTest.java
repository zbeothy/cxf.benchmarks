package org.talend.ps.benchmark.consumer;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.talend.benchmark.Benchmark;
import org.talend.ps.benchmark.common.events.BenchmarkConstants;
import org.talend.ps.benchmark.common.events.EventsHistory;
import org.w3c.dom.Document;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class RunnableTest implements Runnable {
    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    

	private Benchmark client;
	private byte[] message;
	private int messageCount;
	private EventsHistory requestHistory;
	private EventsHistory responseHistory;
	

	public RunnableTest(Benchmark client, byte[] message,
			int messageCount, EventsHistory requestHistory,
			EventsHistory responseHistory) {
		this.message = message;
		this.messageCount = messageCount;
		this.requestHistory = requestHistory;
		this.responseHistory = responseHistory;
		this.client = client;
	}

	@Override
	public void run() {
        try {
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document document = builder.parse(new ByteArrayInputStream(message));
            DOMSource domSource = new DOMSource(document);
			for (int i = 0; i < messageCount; i++) {
				UUID uuid = UUID.randomUUID();
				setMessageID(client, uuid.toString());
				requestHistory.addEvent(uuid.toString());
				Holder<Source> holder = new Holder<>(domSource);
				client.requestResponse(holder);
				if (holder.value instanceof SAXSource) {
					XMLReader reader = ((SAXSource)holder.value).getXMLReader();
					reader.setContentHandler(new DefaultHandler());
					reader.parse((String)null);
				}
				//client.oneWay(content);
				responseHistory.addEvent(uuid.toString());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new WSConsumerException("Error in consumer thread: " + e.getMessage(), e);
		}
	}

    private void setMessageID(Benchmark client, String messageID) throws JAXBException {
		BindingProvider provider = (BindingProvider) client;
    	List<Header> headersList = new ArrayList<Header>();
    	Header testSoapHeader = new Header(BenchmarkConstants.ID_HEADER_NAME, messageID, new JAXBDataBinding(String.class));
    	headersList.add(testSoapHeader);
    	provider.getRequestContext().put(Header.HEADER_LIST, headersList);
    }
    
}

package org.talend.ps.benchmark.consumer;

import org.apache.cxf.binding.soap.SoapBindingConstants;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.benchmark.Benchmark;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.MessageContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Ignore
public class WSConsumerTest {

    private static final String OPERATION_NAME = "invoke";
    private static final QName OPERATION_QNAME = new QName("http://www.talend.org/benchmark", OPERATION_NAME);

    private static final String LARGE = "large";
    private static final String MEDIUM = "medium";
    private static final String SMALL = "small";

    private static final int threadCount = 40;
    private static final int messageCount = 5000;

    @Test
    public void testInvokation() throws Exception {
        final byte[] message = loadMessage("medium");

        runConcurrently(message);
    }

    private void runConcurrently(byte[] message) throws InterruptedException {
        ExecutorService executorSvc = Executors.newScheduledThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass(Benchmark.class);
            factory.setAddress("http://localhost:8040/services/WSProvider");
            Benchmark client = (Benchmark) factory.create();
            setOperation(client);
            Runnable runnableTest = new RunnableTest(client, message, messageCount);
            executorSvc.execute(runnableTest);
        }
        executorSvc.shutdown();
        while(!executorSvc.isTerminated()) {
            executorSvc.awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void setOperation(Benchmark benchmark) {
        Client client = ClientProxy.getClient(benchmark);
        client.getRequestContext().put(SoapBindingConstants.SOAP_ACTION, "");
        client.getRequestContext().put(MessageContext.WSDL_OPERATION, OPERATION_QNAME);
        client.getRequestContext().put("use.async.http.conduit", Boolean.TRUE);
//        HTTPConduit conduit = (HTTPConduit) client.getConduit();
//        HTTPClientPolicy policy = new HTTPClientPolicy();
//        policy.setConnectionTimeout(0);
//        policy.setReceiveTimeout(0);
//        policy.setAllowChunking(false);
//        policy.setAccept("text/xml");
//        policy.setContentType("text/xml");
//        policy.setConnection(ConnectionType.KEEP_ALIVE);
//        conduit.setClient(policy);
    }

    private byte[] loadMessage(String messageSize) throws IOException,
            ParserConfigurationException, SAXException {
        InputStream is = null;
        try {
            String name = null;
            if (messageSize.equals(LARGE)) {
                name = "/" + LARGE + ".xml";
            } else if (messageSize.equals(MEDIUM)) {
                name = "/" + MEDIUM + ".xml";
            } else {
                name = "/" + SMALL + ".xml";
            }
            is = this.getClass().getResourceAsStream(name);
            if (is == null) {
                throw new IllegalStateException("Cannot load resource: " + name);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1;) {
                baos.write(b, 0, n);
            }
            return baos.toByteArray();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    /**
     * The Runnable Test.
     */
    public class RunnableTest implements Runnable {
        private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        private Benchmark client;
        private byte[] message;
        private int messageCount;

        public RunnableTest(Benchmark client, byte[] message, int messageCount) {
            this.message = message;
            this.messageCount = messageCount;
            this.client = client;
        }

        @Override
        public void run() {
            try {
                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document document = builder.parse(new ByteArrayInputStream(message));
                DOMSource domSource = new DOMSource(document);
                for (int i = 0; i < messageCount; i++) {
                    client.requestResponse(new Holder<>(domSource));
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

package org.talend.ps.benchmark.consumer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.binding.soap.SoapBindingConstants;
import org.springframework.beans.factory.InitializingBean;
import org.talend.benchmark.Benchmark;
import org.talend.ps.benchmark.common.events.BenchmarkConstants;
import org.talend.ps.benchmark.common.events.EventsHistory;
import org.xml.sax.SAXException;

/**
 * This class implements a generic policy provider.
 * 
 */
public class WSConsumerInvoker implements InitializingBean {
    private static final String OPERATION_NAME = "invoke";
    private static final QName OPERATION_QNAME = new QName("http://www.talend.org/benchmark", OPERATION_NAME);

	private static final String LARGE = "large";
	private static final String MEDIUM = "medium";
	private static final String SMALL = "small";

	private int threadCount;
	private int messageCount;
	private String messageSize;
	private long waitTimeout;

	private String loadFileName;
	private EventsHistory requestHistory;
	private EventsHistory responseHistory;

	private Benchmark client;

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public void setMessageSize(String messageSize) {
		this.messageSize = messageSize;
	}
	
	public void setWaitTimeout(long waitTimeout) {
		this.waitTimeout = waitTimeout;
	}

	public void setLoadFileName(String loadFileName) {
		this.loadFileName = loadFileName;
	}

	public void setRequestHistory(EventsHistory history) {
		this.requestHistory = history;
	}

	public void setResponseHistory(EventsHistory history) {
		this.responseHistory = history;
	}
	
 	public void setClient(Benchmark client) {
		this.client = client;
	}

 	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Consumer initialized");
		setOperation(client);
		final byte[] message = loadMessage(messageSize);

		ExecutorService executorSvc = Executors.newSingleThreadExecutor();
		executorSvc.execute(new Runnable() {
			
			@Override
			public void run() {
				runTests(message);			
			}
		});
		
	}

	public void runTests(byte[] message) {

		try {
			String time = new SimpleDateFormat(BenchmarkConstants.DATE_FORMAT).format(new Date()); 
			String loadFile = String.format(loadFileName, time);
			FileOutputStream fo = new FileOutputStream(loadFile);
			writeTestLogHeader(time, fo);
				    
			final long timeBefore = System.currentTimeMillis();
			try {
				runConcurrently(message);
				//run(message);
			} catch (Throwable e) {
				throw new WSConsumerException("Critical exception: " + e.getMessage(), e);
			} finally {
				final long timeAfter = System.currentTimeMillis();
				requestHistory.save();
				responseHistory.save();				
				writeTestLogFooter(fo, timeBefore, timeAfter);
				fo.close();
			}
		} catch (Exception e) {
			throw new WSConsumerException("Logging file processing exception: " + e.getMessage(), e);
		}
	}

//	private void run(byte[] message) {
//			Runnable runnableTest = new RunnableTest(client, message,
//					messageCount, requestHistory, responseHistory);
//			runnableTest.run();
//	}

	private void runConcurrently(byte[] message)
			throws InterruptedException {
		ExecutorService executorSvc = Executors.newFixedThreadPool(threadCount);
		for (int i = 0; i < threadCount; i++) {
			Runnable runnableTest = new RunnableTest(client, message,
					messageCount, requestHistory, responseHistory);
			executorSvc.execute(runnableTest);
		}
		executorSvc.shutdown();
		executorSvc.awaitTermination(waitTimeout, TimeUnit.MILLISECONDS);
	}

	private void writeTestLogHeader(String time, FileOutputStream fo)
			throws IOException {
		fo.write("************ Start Test ***************\n".getBytes());
		fo.write(String.format("Start time: %s", time).getBytes());
		System.out.println("************ Start Test ***************");
		System.out.println(String.format("Start time: %s", time));
	}

	private void writeTestLogFooter(FileOutputStream fo, final long timeBefore,
			final long timeAfter) throws IOException {
		StringBuilder logMessage = new StringBuilder();
		float throughput = messageCount * threadCount * 1000;
		throughput = throughput / (timeAfter - timeBefore);
		logMessage.append("Summary time: " + (timeAfter - timeBefore)
				+ "; runs: " + ((long) messageCount * threadCount)
				+ "; throughput: " + throughput + "(msg/sec)").append("\n");
		logMessage.append("************ End Test ***************").append("\n");
		fo.write(logMessage.toString().getBytes());
		System.out.println(logMessage.toString());
	}

	private void setOperation(Benchmark client) {
		BindingProvider provider = (BindingProvider) client;
		provider.getRequestContext().put(SoapBindingConstants.SOAP_ACTION, "");
		provider.getRequestContext().put(MessageContext.WSDL_OPERATION, OPERATION_QNAME);        
		provider.getRequestContext().put("thread.local.request.context", "true");
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

}

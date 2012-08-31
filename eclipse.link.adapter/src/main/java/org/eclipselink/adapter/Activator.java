package org.eclipselink.adapter;

import java.util.Hashtable;

import org.eclipse.persistence.jpa.PersistenceProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String PERSISTENCE_PROVIDER = "javax.persistence.provider";
	public static final String ECLIPSELINK_OSGI_PROVIDER = "org.eclipse.persistence.jpa.PersistenceProvider";
	private static BundleContext context;

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		registerProviderService();
	}

	public void registerProviderService() throws Exception {
		PersistenceProvider providerService = new PersistenceProvider();
		Hashtable props = new Hashtable();
		props.put(PERSISTENCE_PROVIDER, ECLIPSELINK_OSGI_PROVIDER);

		context.registerService("javax.persistence.spi.PersistenceProvider",
				providerService, props);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}

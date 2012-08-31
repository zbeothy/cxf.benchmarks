public class LoggingFeature extends AbstractFeature {
	private static final int DEFAULT_LIMIT = 100 * 1024;
	private static final LoggingInInterceptor IN = new LoggingInInterceptor(
			DEFAULT_LIMIT);
	private static final LoggingOutInterceptor OUT = new LoggingOutInterceptor(
			DEFAULT_LIMIT);
	int limit = DEFAULT_LIMIT;

	@Override
	protected void initializeProvider(InterceptorProvider provider, Bus bus) {
		if (limit == DEFAULT_LIMIT) {
			provider.getInInterceptors().add(IN);
			provider.getInFaultInterceptors().add(IN);
			provider.getOutInterceptors().add(OUT);
			provider.getOutFaultInterceptors().add(OUT);
		} else {
			LoggingInInterceptor in = new LoggingInInterceptor(limit);
			LoggingOutInterceptor out = new LoggingOutInterceptor(limit);
			provider.getInInterceptors().add(in);
			provider.getInFaultInterceptors().add(in);
			provider.getOutInterceptors().add(out);
			provider.getOutFaultInterceptors().add(out);
		}
	}

	/** * This function has no effect at this time. * @param lim */
	public void setLimit(int lim) {
		limit = lim;
	}

	/** * Retrieve the value set with {@link #setLimit(int)}. * @return */
	public int getLimit() {
		return limit;
	}
}

package org.talend.ps.benchmark.provider.commands;

import java.io.PrintWriter;

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.talend.ps.benchmark.common.events.History;

@Command(scope = "benchmark", name = "report", description = "Reports benchmark provider results")
public class Report extends OsgiCommandSupport {
	private History historyIn;
	
	public void setHistoryIn(History historyIn) {
		this.historyIn = historyIn;
	}

	@Override
	protected Object doExecute() throws Exception {
		PrintWriter writer = new PrintWriter(System.out, true);
		historyIn.reportLoad(writer, 200);
		writer.flush();
		return null;
	}
	
}

package org.talend.ps.benchmark.provider.commands;

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.talend.ps.benchmark.common.events.History;

@Command(scope = "benchmark", name = "reset", description = "Resets benchmark provider results")
public class Reset extends OsgiCommandSupport {
	private History historyIn;
	private History historyOut;
	
	public void setHistoryIn(History historyIn) {
		this.historyIn = historyIn;
	}

	public void setHistoryOut(History historyOut) {
		this.historyOut = historyOut;
	}

	@Override
	protected Object doExecute() throws Exception {
		historyIn.reset();
		historyOut.reset();
		System.out.println("History is reset");
		return null;
	}
	
}

package org.talend.ps.benchmark.provider.commands;

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.talend.ps.benchmark.common.events.History;

@Command(scope = "benchmark", name = "save", description = "Saves benchmark provider results")
public class Save extends OsgiCommandSupport {
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
		historyIn.save();
		historyOut.save();
		System.out.println("History is saved");
		return null;
	}
	
}

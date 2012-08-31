package org.talend.ps.benchmark.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.talend.ps.benchmark.common.events.History;

public class ReportServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 34992072289535683L;
	private History history;

	public void setHistory(History history) {
		this.history = history;
	}

	private static final String COMMAND = "command";
	private static final String COMMAND_RESET = "reset";
	private static final String COMMAND_SAVE = "save";
	private static final String HTML_RESPONSE_PREFIX = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 "
		+ "Transitional//EN\">\n"
		+ "<HTML>\n"
		+ "<HEAD><TITLE>DGF Prototype Echo Service - current load</TITLE></HEAD>\n"
		+ "<BODY>\n";
	private static final String HTML_RESPONSE_POSTFIX = "</BODY></HTML>";
	
	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);
		history.start();
	}
	
	@Override
	public void destroy() {
		history.release();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(HTML_RESPONSE_PREFIX);
			String command = request.getParameter(COMMAND);
			if (COMMAND_RESET.equals(command)) {
				history.reset();
				out.println("Counter is reseted");
			} else if (COMMAND_SAVE.equals(command)) {
				try {
					String fileName = history.save();
					out.println("Report is saved into file: " + fileName);
				} catch(Throwable e) {
					out.println("Error saving into file: " + e.getMessage());
				}
			} else {
				history.reportLoad(out, 500);
			}
			out.println(HTML_RESPONSE_POSTFIX);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_OK);
	}

	public static String inputStreamAsString(InputStream stream)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}

		br.close();
		return sb.toString();
	}
}

package com.example.trading.platform.diagnostics.jfr;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.StackTrace;

@Name("trading.http.Exchange")
@Label("HTTP Exchange")
@Description("Completed HTTP request handled by the broker")
@Category({ "Trading", "HTTP" })
@StackTrace(true)
public class HttpExchangeEvent extends Event {

	@Label("Method")
	public String method;

	@Label("Path")
	public String path;

	@Label("Status")
	public int status;

	@Label("Outcome")
	public String outcome;
}

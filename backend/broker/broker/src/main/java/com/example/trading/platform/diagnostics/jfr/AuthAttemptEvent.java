package com.example.trading.platform.diagnostics.jfr;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.StackTrace;

@Name("trading.auth.Attempt")
@Label("Authentication Attempt")
@Description("Login, logout, or registration outcome. No email or password is recorded.")
@Category({ "Trading", "Auth" })
@StackTrace(false)
public class AuthAttemptEvent extends Event {

	@Label("Action")
	public String action;

	@Label("Success")
	public boolean success;

	@Label("Detail")
	public String detail;
}

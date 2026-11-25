package com.example.trading.platform.diagnostics.jfr;

public final class JfrEvents {

	private JfrEvents() {
	}

	public static void auth(String action, boolean success, String detail) {
		AuthAttemptEvent event = new AuthAttemptEvent();
		if (!event.isEnabled()) {
			return;
		}
		event.action = action;
		event.success = success;
		event.detail = detail;
		event.commit();
	}
}

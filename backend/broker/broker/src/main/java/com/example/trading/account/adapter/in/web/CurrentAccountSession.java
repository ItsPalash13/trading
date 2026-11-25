package com.example.trading.account.adapter.in.web;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

/**
 * Stores the selected trading account id in the HTTP session.
 * <p>
 * Kept under account (not auth) because account switching will use the same key:
 * login sets a default account, logout clears it, and a future switch-account
 * API will call {@link #set} after ownership is verified.
 * <p>
 * This is the account module's web-facing contract for the session selection;
 * auth's web and security adapters depend on it deliberately.
 */
@Component
public class CurrentAccountSession {

	public static final String CURRENT_ACCOUNT_ID = "CURRENT_ACCOUNT_ID";

	public void set(HttpSession session, Long accountId) {
		session.setAttribute(CURRENT_ACCOUNT_ID, accountId);
	}

	public Long get(HttpSession session) {
		Object value = session.getAttribute(CURRENT_ACCOUNT_ID);
		if (value instanceof Long id) {
			return id;
		}
		if (value instanceof Number number) {
			return number.longValue();
		}
		return null;
	}

	public void clear(HttpSession session) {
		session.removeAttribute(CURRENT_ACCOUNT_ID);
	}

	/*
	 * Expected account switching (not wired yet).
	 * Callers must verify ownership first, e.g. AccountQuery.getOwnedAccount.
	 *
	 * public void switchAccount(HttpSession session, Long accountId) {
	 *     set(session, accountId);
	 * }
	 */
}

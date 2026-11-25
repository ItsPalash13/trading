package com.example.trading.auth.application.port.in;

import com.example.trading.auth.application.result.SessionView;

/**
 * Port in — session capabilities auth exposes (start / resolve current account).
 * Called by web and security adapters; implemented by SessionService.
 */
public interface SessionUseCase {

	SessionView startSession(String email);

	SessionView resolveSession(String email, Long selectedAccountId);
}

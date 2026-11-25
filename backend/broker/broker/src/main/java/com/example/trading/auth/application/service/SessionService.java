package com.example.trading.auth.application.service;

import org.springframework.stereotype.Service;

import com.example.trading.account.application.port.in.AccountQuery;
import com.example.trading.account.application.result.AccountView;
import com.example.trading.auth.application.port.in.SessionUseCase;
import com.example.trading.auth.application.result.SessionView;
import com.example.trading.platform.exception.UnauthorizedException;
import com.example.trading.user.application.port.in.UserQuery;
import com.example.trading.user.application.result.UserView;

@Service
public class SessionService implements SessionUseCase {

	private final UserQuery userQuery;
	private final AccountQuery accountQuery;

	public SessionService(UserQuery userQuery, AccountQuery accountQuery) {
		this.userQuery = userQuery;
		this.accountQuery = accountQuery;
	}

	@Override
	public SessionView startSession(String email) {
		UserView user = requireUser(email);
		return new SessionView(user, accountQuery.getDefaultAccount(user.id()));
	}

	@Override
	public SessionView resolveSession(String email, Long selectedAccountId) {
		UserView user = requireUser(email);
		AccountView account = selectedAccountId != null
				? accountQuery.getOwnedAccount(selectedAccountId, user.id())
				: accountQuery.getDefaultAccount(user.id());
		return new SessionView(user, account);
	}

	private UserView requireUser(String email) {
		return userQuery.findByEmail(email)
				.orElseThrow(() -> new UnauthorizedException("Not authenticated"));
	}
}

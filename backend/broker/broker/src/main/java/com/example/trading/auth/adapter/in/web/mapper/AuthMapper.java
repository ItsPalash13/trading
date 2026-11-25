package com.example.trading.auth.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import com.example.trading.account.application.result.AccountView;
import com.example.trading.auth.adapter.in.web.response.AccountSummaryResponse;
import com.example.trading.auth.adapter.in.web.response.RegisterResponse;
import com.example.trading.auth.adapter.in.web.response.SessionResponse;
import com.example.trading.auth.application.result.SessionView;
import com.example.trading.user.application.result.UserView;

@Component
public class AuthMapper {

	public RegisterResponse toRegisterResponse(UserView user) {
		return new RegisterResponse(user.id(), user.name(), user.email());
	}

	public SessionResponse toSessionResponse(SessionView sessionView) {
		UserView user = sessionView.user();
		return new SessionResponse(
				user.id(),
				user.name(),
				user.email(),
				toAccountSummary(sessionView.currentAccount()));
	}

	public AccountSummaryResponse toAccountSummary(AccountView account) {
		return new AccountSummaryResponse(account.id(), account.type(), account.name());
	}
}

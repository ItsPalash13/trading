package com.example.trading.auth.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.example.trading.account.application.port.in.AccountQuery;
import com.example.trading.account.application.result.AccountView;
import com.example.trading.auth.application.result.SessionView;
import com.example.trading.platform.exception.UnauthorizedException;
import com.example.trading.user.application.port.in.UserQuery;
import com.example.trading.user.application.result.UserView;

class SessionServiceTest {

	private final UserQuery userQuery = mock(UserQuery.class);
	private final AccountQuery accountQuery = mock(AccountQuery.class);
	private final SessionService service = new SessionService(userQuery, accountQuery);

	private final UserView alice = new UserView(1L, "Alice", "alice@example.com");

	@Test
	void resolveSessionUsesDefaultAccountWhenNoneSelected() {
		AccountView regular = new AccountView(10L, "REGULAR", "Regular");
		when(userQuery.findByEmail("alice@example.com")).thenReturn(Optional.of(alice));
		when(accountQuery.getDefaultAccount(1L)).thenReturn(regular);

		SessionView view = service.resolveSession("alice@example.com", null);

		assertThat(view.user()).isEqualTo(alice);
		assertThat(view.currentAccount()).isEqualTo(regular);
	}

	@Test
	void resolveSessionVerifiesOwnershipOfSelectedAccount() {
		AccountView selected = new AccountView(11L, "REGULAR", "Regular");
		when(userQuery.findByEmail("alice@example.com")).thenReturn(Optional.of(alice));
		when(accountQuery.getOwnedAccount(11L, 1L)).thenReturn(selected);

		SessionView view = service.resolveSession("alice@example.com", 11L);

		assertThat(view.currentAccount()).isEqualTo(selected);
		verify(accountQuery).getOwnedAccount(11L, 1L);
	}

	@Test
	void unknownUserIsUnauthorized() {
		when(userQuery.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.startSession("ghost@example.com"))
				.isInstanceOf(UnauthorizedException.class);
	}
}

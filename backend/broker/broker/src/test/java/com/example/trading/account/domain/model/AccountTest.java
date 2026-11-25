package com.example.trading.account.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.example.trading.platform.exception.ForbiddenException;
import com.example.trading.platform.exception.ValidationException;

class AccountTest {

	@Test
	void openRegularCreatesRegularAccountForOwner() {
		Account account = Account.openRegular(7L);

		assertThat(account.getUserId()).isEqualTo(7L);
		assertThat(account.getType()).isEqualTo(AccountType.REGULAR);
		assertThat(account.getName()).isEqualTo("Regular");
		assertThat(account.getCreatedAt()).isNotNull();
	}

	@Test
	void openRegularRequiresOwner() {
		assertThatThrownBy(() -> Account.openRegular(null)).isInstanceOf(ValidationException.class);
	}

	@Test
	void ensureOwnedByRejectsOtherUsers() {
		Account account = Account.openRegular(7L);

		assertThatCode(() -> account.ensureOwnedBy(7L)).doesNotThrowAnyException();
		assertThatThrownBy(() -> account.ensureOwnedBy(8L)).isInstanceOf(ForbiddenException.class);
	}
}

package com.example.trading.account.application.port.in;

import com.example.trading.account.application.result.AccountView;

public interface OpenAccountUseCase {

	AccountView openRegularAccount(Long userId);
}

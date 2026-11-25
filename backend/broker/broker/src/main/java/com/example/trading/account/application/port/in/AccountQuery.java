package com.example.trading.account.application.port.in;

import com.example.trading.account.application.result.AccountView;

/**
 * Port in — read capabilities account exposes. Auth uses this instead of Account / repositories.
 */
public interface AccountQuery {

	AccountView getDefaultAccount(Long userId);

	AccountView getOwnedAccount(Long accountId, Long userId);
}

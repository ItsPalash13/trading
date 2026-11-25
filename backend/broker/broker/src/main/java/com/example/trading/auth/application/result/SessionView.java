package com.example.trading.auth.application.result;

import com.example.trading.account.application.result.AccountView;
import com.example.trading.user.application.result.UserView;

/**
 * Result — output of session use cases. AuthMapper turns this into SessionResponse
 * (HTTP DTO). Built from other modules' results, not their domain entities.
 */
public record SessionView(UserView user, AccountView currentAccount) {
}

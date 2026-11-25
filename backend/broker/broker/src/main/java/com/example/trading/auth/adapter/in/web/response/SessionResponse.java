package com.example.trading.auth.adapter.in.web.response;

public class SessionResponse {

	private Long id;
	private String name;
	private String email;
	private AccountSummaryResponse currentAccount;

	public SessionResponse() {
	}

	public SessionResponse(Long id, String name, String email, AccountSummaryResponse currentAccount) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.currentAccount = currentAccount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AccountSummaryResponse getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(AccountSummaryResponse currentAccount) {
		this.currentAccount = currentAccount;
	}
}

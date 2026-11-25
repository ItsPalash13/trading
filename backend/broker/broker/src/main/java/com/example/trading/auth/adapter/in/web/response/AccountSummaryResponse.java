package com.example.trading.auth.adapter.in.web.response;

public class AccountSummaryResponse {

	private Long id;
	private String type;
	private String name;

	public AccountSummaryResponse() {
	}

	public AccountSummaryResponse(Long id, String type, String name) {
		this.id = id;
		this.type = type;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

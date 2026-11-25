package com.example.trading;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "diagnostics.jfr.enabled=false")
class TradingApplicationTests {

	@Test
	void contextLoads() {
	}
}

package com.sa.finance_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.sap.common_lib.security.web.SecurityConfig;
import com.sap.common_lib.security.web.WebClientConfig;
import com.sap.common_lib.security.web.filter.MicroServiceFilter;
import com.sap.common_lib.util.DateUtils;
import com.sap.common_lib.util.PublicEndpointUtil;

@SpringBootApplication
@Import({ PublicEndpointUtil.class, SecurityConfig.class, MicroServiceFilter.class, DateUtils.class, WebClientConfig.class })
public class FinanceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceServiceApplication.class, args);
	}

}

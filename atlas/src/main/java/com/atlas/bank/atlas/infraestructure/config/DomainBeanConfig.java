package com.atlas.bank.atlas.infraestructure.config;

import com.atlas.bank.atlas.application.port.out.FraudCheckPort;
import com.atlas.bank.atlas.domain.service.TransferDomainService;
import com.atlas.bank.atlas.domain.strategy.fee.CheckingFeeCalculator;
import com.atlas.bank.atlas.domain.strategy.fee.PremiumFeeCalculator;
import com.atlas.bank.atlas.domain.strategy.fee.SavingsFeeCalculator;
import com.atlas.bank.atlas.domain.strategy.fee.DefaultFeeCalculator;
import com.atlas.bank.atlas.domain.validation.AccountStatusValidator;
import com.atlas.bank.atlas.domain.validation.FraudValidator;
import com.atlas.bank.atlas.domain.validation.SufficientFundsValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class DomainBeanConfig {

    @Bean
    public TransferDomainService transferDomainService() {
        return new TransferDomainService();
    }

    @Bean
    @Order(1)
    public SavingsFeeCalculator savingsFeeCalculator() {
        return new SavingsFeeCalculator();
    }

    @Bean
    @Order(2)
    public CheckingFeeCalculator checkingFeeCalculator() {
        return new CheckingFeeCalculator();
    }

    @Bean
    @Order(3)
    public PremiumFeeCalculator premiumFeeCalculator() {
        return new PremiumFeeCalculator();
    }

    @Bean
    public DefaultFeeCalculator defaultFeeCalculator() {
        return new DefaultFeeCalculator();
    }

    @Bean
    @Order(1)
    public AccountStatusValidator accountStatusValidator() {
        return new AccountStatusValidator();
    }

    @Bean
    @Order(2)
    public SufficientFundsValidator sufficientFundsValidator() {
        return new SufficientFundsValidator();
    }

    @Bean
    @Order(3)
    public FraudValidator fraudValidator(FraudCheckPort fraudCheckPort) {
        return new FraudValidator(fraudCheckPort);
    }
}

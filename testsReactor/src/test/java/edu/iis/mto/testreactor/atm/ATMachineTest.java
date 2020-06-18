package edu.iis.mto.testreactor.atm;

import static org.hamcrest.MatcherAssert.assertThat;

import edu.iis.mto.testreactor.atm.bank.Bank;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ATMachineTest {

    @Mock
    private Bank bank;

    @BeforeEach
    public void setUp() throws Exception {}

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    private ATMachine atMachine;
    @BeforeEach
    void beforeEach() {
        atMachine = new ATMachine(bank,Currency.getInstance(Locale.GERMANY));
    }

    @Test
    void successMoneyDeposit(){
        List<BanknotesPack> banknotesPackList = new ArrayList<BanknotesPack>();
        banknotesPackList.add(BanknotesPack.create(2,Banknote.PL_20));
        banknotesPackList.add(BanknotesPack.create(2,Banknote.PL_10));

        MoneyDeposit deposit = MoneyDeposit.create(Currency.getInstance(Locale.GERMANY),banknotesPackList);






    }



}

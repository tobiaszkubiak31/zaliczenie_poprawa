package edu.iis.mto.testreactor.atm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import edu.iis.mto.testreactor.atm.bank.AccountException;
import edu.iis.mto.testreactor.atm.bank.AuthorizationException;
import edu.iis.mto.testreactor.atm.bank.AuthorizationToken;
import edu.iis.mto.testreactor.atm.bank.Bank;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ATMachineTest {

    @Mock
    private Bank bank;
    public static final Currency POLISH_CURRENCY = Currency.getInstance("PLN");
    public static final PinCode PIN_CODE = PinCode.createPIN(1, 2, 3, 4);
    public static final int PROPER_VALUE_TO_WITHDRAW = 30;

    @BeforeEach
    public void setUp() throws Exception {}


    private ATMachine atMachine;
    @BeforeEach
    void beforeEach() {
        atMachine = new ATMachine(bank,POLISH_CURRENCY);
        Currency polishCurrency = Currency.getInstance("PLN");
        List<BanknotesPack> banknotesPackList = new ArrayList<BanknotesPack>();
        banknotesPackList.add(BanknotesPack.create(2,Banknote.PL_20));
        banknotesPackList.add(BanknotesPack.create(2,Banknote.PL_10));
        MoneyDeposit deposit = MoneyDeposit.create(polishCurrency,banknotesPackList);
        atMachine.setDeposit(deposit);

    }

    @Test
    void successMoneyDepositWithExprectedBankontesAndExptectedMoneyToWithdraw()
        throws ATMOperationException, AuthorizationException {
        PinCode pinCode = PinCode.createPIN(1,2,3,4);
        int valueToWithraw = 30;
        Money exptectedMoneyToWithdraw = new Money(valueToWithraw,POLISH_CURRENCY);
        Card card = Card.create("12345");
        AuthorizationToken authorizationToken= AuthorizationToken.create("123456");
        when(bank.autorize(PIN_CODE.getPIN(),card.getNumber())).thenReturn(authorizationToken);

        Withdrawal withdrawal = atMachine.withdraw(PinCode.createPIN(1,2,3,4),card,exptectedMoneyToWithdraw);

        List<Banknote> expectedBanknotes = new ArrayList<Banknote>();
        expectedBanknotes.add(Banknote.PL_20);
        expectedBanknotes.add(Banknote.PL_10);

        assertTrue(expectedBanknotes.equals(withdrawal.getBanknotes()));
    }


    @Test
    void autorizeExpcetiob() throws ATMOperationException, AuthorizationException {
        Currency polishCurrency = Currency.getInstance(Locale.GERMANY);
        List<BanknotesPack> banknotesPackList = new ArrayList<BanknotesPack>();
        banknotesPackList.add(BanknotesPack.create(2,Banknote.PL_20));
        banknotesPackList.add(BanknotesPack.create(2,Banknote.PL_10));
        MoneyDeposit deposit = MoneyDeposit.create(polishCurrency,banknotesPackList);


        PinCode pinCode = PinCode.createPIN(1,2,3,4);
        Money money = new Money(5,polishCurrency);
        Card card = Card.create("12345");



        doThrow(new ATMOperationException(ErrorCode.AHTHORIZATION)).when(bank).autorize(pinCode.getPIN(),card.getNumber());

//        Assertions.assertThrows(new ATMOperationException(ErrorCode.AHTHORIZATION), atMachine.withdraw(PinCode.createPIN(1,2,3,4),card,money));

    }

    @Test
    void ifSuccesWithdrawMethodsShouldCallInProperOrder()
        throws ATMOperationException, AuthorizationException, AccountException {
        Money exptectedMoneyToWithdraw = new Money(PROPER_VALUE_TO_WITHDRAW, POLISH_CURRENCY);
        Card card = Card.create("12345");
        AuthorizationToken authorizationToken= AuthorizationToken.create("123456");
        when(bank.autorize(PIN_CODE.getPIN(),card.getNumber())).thenReturn(authorizationToken);

        Withdrawal withdrawal = atMachine.withdraw(PinCode.createPIN(1,2,3,4),card,exptectedMoneyToWithdraw);


        InOrder inOrder = inOrder(bank);
        inOrder.verify(bank).autorize(PIN_CODE.getPIN(),card.getNumber());
        inOrder.verify(bank).charge(authorizationToken,exptectedMoneyToWithdraw);
        inOrder.verifyNoMoreInteractions();

    }



}

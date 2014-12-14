package cse110.com.goldencash.modelAccount;
import com.parse.ParseClassName;

@ParseClassName("CreditAccount")

/**
 *  Title: class CreditAccount
 *  Description: A type of Concrete Account, CreditAccount
 */
public class CreditAccount extends Account {

    public CreditAccount(){
        this.accountType = "Credit";
    }

    /**
     *  No InterestRate for creditAccount
     */
    public int getCurrentInterestRate() {return 0; }

    /**
     *  No InterestRate for creditAccount
     */
    public int getMonthInterestRate() {return 0; }

    /**
     *  If Amount is less than 100 for 30days, penalty
     */
    public double getMonthInterest() {
        return getAmount() < 100 ? -25:0;
    }
}

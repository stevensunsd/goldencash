package cse110.com.goldencash.modelAccount;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *  Title: class SideFunctionFacadeImp
 *  Description: Implement of Interface SideFunctionFacade
 */
public class SideFunctionFacadeImp implements SideFunctionFacade {
    public String currentTimeString() {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat df =new SimpleDateFormat("d MMM yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return df.format(currentTime);
    }
    public String stringFormater(double value,int interestRate) {
        String newLogFrom;
        if(value>0)
            newLogFrom = currentTimeString() + " + $" + value + " Monthly Interest based on your Interest Rate " + interestRate + "%" +'\n';
        else if(value<0)
            newLogFrom = currentTimeString() + " - $" + Math.abs(value) + " Penalty For Balance Below $100 over 30 days" + '\n';
        else newLogFrom = currentTimeString() + " No Interest or Penalty Apply" + '\n';
        return newLogFrom;
    }
    public String stringFormater(String choose,double value) {
        String newLogFrom;
        if (choose == "Withdraw")
            newLogFrom = currentTimeString() + " - $" + value + " Teller Withdraw" +'\n';
        else
            newLogFrom = currentTimeString() + " + $" + value + " Teller Deposit" + '\n';
        return newLogFrom;
    }
    public double NumberFormater(double value) {
        BigDecimal number = new BigDecimal(value);
        return number.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public boolean isOver30days(Date updateTime) {
        Date currentTime = new Date(System.currentTimeMillis());
        long days= (currentTime.getTime() - updateTime.getTime()) / (1000*60*60*24);
        return days>=30?true:false;
    }
}

package cse110.com.goldencash;

import android.widget.EditText;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *  Title: class SideFunctionFacadeImp
 *  Description: Implement of Interface SideFunctionFacade
 */
public class SideFunctionFacadeImp implements SideFunctionFacade {
    /**
     *  return a formatted time string
     */
    public String currentTimeString() {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat df =new SimpleDateFormat("d MMM yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return df.format(currentTime);
    }
    /**
     *  helper method for format string for log
     */
    public String stringFormater(double value,int interestRate) {
        String newLogFrom;
        if(value>0)
            newLogFrom = currentTimeString() + " + $" + value + " Monthly Interest based on your Interest Rate " + interestRate + "%" +'\n';
        else if(value<0)
            newLogFrom = currentTimeString() + " - $" + Math.abs(value) + " Penalty For Balance Below $100 over 30 days" + '\n';
        else newLogFrom = currentTimeString() + " No Interest or Penalty Apply" + '\n';
        return newLogFrom;
    }
    /**
     *  helper method for format string for log
     */
    public String stringFormater(String choose,double value) {
        String newLogFrom;
        if (choose == "Withdraw")
            newLogFrom = currentTimeString() + " - $" + value + " Teller Withdraw" +'\n';
        else
            newLogFrom = currentTimeString() + " + $" + value + " Teller Deposit" + '\n';
        return newLogFrom;
    }
    /**
     *  helper method for format double number
     */
    public double NumberFormater(double value) {
        BigDecimal number = new BigDecimal(value);
        return number.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     *  helper method for checking if the day is over 30days from now
     */
    public boolean isOver30days(Date updateTime) {
        Date currentTime = new Date(System.currentTimeMillis());
        long days= (currentTime.getTime() - updateTime.getTime()) / (1000*60*60*24);
        return days>=30?true:false;
    }

    /**
     *  helper method for checking if the input is empty
     */
    public boolean isEmpty(String s) {
        return s.trim().length() > 0 ? false : true;
    }

    /**
     *  helper method for checking if the input is valid input
     */
    public boolean isValidInput(EditText input) {
        String s = input.getText().toString();
        //Double d = Double.parseDouble(s);
        if(s.matches("")){
            return false;
        }else if(!s.matches("[0-9.]+")){
            return false;
        }
        return true;
    }
}

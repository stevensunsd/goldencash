package cse110.com.goldencash;

import android.widget.EditText;

import java.util.Date;

/**
 *  Title: interface SideFunctionFacade
 *  Description: An interface consist of numbers of side functions
 */
public interface SideFunctionFacade {
    public String currentTimeString();
    public String stringFormater(double value,int interestRate);
    public String stringFormater(String choose,double value);
    public double NumberFormater(double value);
    public boolean isOver30days(Date updateTime);
    public boolean isEmpty(String s);
    public boolean isValidInput(EditText input);
}

package cse110.com.goldencash.modelAccount;

/**
  * Created by stevensun on 12/4/14.
  */
 public interface AccountInterface {
    public double getAmount();
    public boolean isOpen();
    public void withdraw(double value);
    public void deposit(double value);
    // transfer to self
    public void transfer(String To,double value);
    // transfer to other
    public void transfer(DebitAccount account,double value);
 }

package db.mappers;

import java.util.List;
import db.model.Account;

public interface AccountMapper {
    public List<Account> getAllAccounts();
    public Account getAccountById(int id);
    public void updateAccount(Account account);
    public void createAccount(Account account);
    public void deleteAccountById(int id);


}

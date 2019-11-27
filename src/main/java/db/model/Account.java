package db.model;

import java.io.Serializable;

public class Account implements Serializable {
    private int account_id;
    private int client_id;
    private Double balance;

    public Account() {}

    public Account(int account_id, int client_id, Double balance) {
        this.account_id = account_id;
        this.client_id = client_id;
        this.balance = balance;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account_id=" + account_id +
                ", client_id=" + client_id +
                ", balance=" + balance +
                '}';
    }
}

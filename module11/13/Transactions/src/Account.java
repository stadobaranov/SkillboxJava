public class Account implements Comparable<Account> {
    private final String number;
    private volatile long money;
    private volatile boolean blocked;

    public Account(String number, long money) {
        this.number = number;
        this.money = money;
    }

    public String getNumber() {
        return number;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void block() {
        blocked = true;
    }

    @Override
    public int compareTo(Account other) {
        return number.compareTo(other.number);
    }
}

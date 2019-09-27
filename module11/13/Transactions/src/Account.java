public class Account {
    private static final int STATE_NORMAL = 0;
    private static final int STATE_FRAUD_CHECKING = 1;
    private static final int STATE_BLOCKED = 2;

    private final String number;
    private volatile long balance;
    private volatile int state;

    public Account(String number, long balance) {
        this.number = number;
        this.balance = balance;
        this.state = STATE_NORMAL;
    }

    public String getNumber() {
        return number;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public boolean isNormal() {
        return state == STATE_NORMAL;
    }

    public boolean isFraudChecking() {
        return state == STATE_FRAUD_CHECKING;
    }

    public boolean isBlocked() {
        return state == STATE_BLOCKED;
    }

    public void changeStateToNormal() {
        state = STATE_NORMAL;
    }

    public void changeStateToFraudChecking() {
        state = STATE_FRAUD_CHECKING;
    }

    public void changeStateToBlocked() {
        state = STATE_BLOCKED;
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Account && number.equals(((Account)other).number);
    }
}

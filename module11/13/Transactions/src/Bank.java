import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Bank {
    private final Map<String, Account> accounts;
    private final Random random = new Random();

    public Bank(Account accounts[]) {
        // Т.к. карта с аккаунтами не модифицируется, достаточно HashMap реализации, freeze action для
        // final поля в конце конструктора обеспечивает безопасную публикацию.
        this.accounts = mapAccounts(accounts);
    }

    private static Map<String, Account> mapAccounts(Account accounts[]) {
        Map<String, Account> mapped = new HashMap<>();

        for(Account account: accounts) {
            mapped.put(account.getNumber(), account);
        }

        return mapped;
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(String fromAccountNumber, String toAccountNumber, long amount) {
        if(amount < 0) {
            throw new IllegalArgumentException(
                String.format("Отрицательное значение суммы перевода - %d", amount));
        }

        Account from = findAccount(fromAccountNumber);

        // Т.к. поле с флагом блокировки volatile, записи в (4) и (5) happens-before (6)
        if(from.isBlocked()) { // (6)
            return;
        }

        Account to = findAccount(toAccountNumber);

        // Т.к. поле с флагом блокировки volatile, записи в (4) и (5) happens-before (7)
        if(to.isBlocked()) { // (7)
            return;
        }

        int comparison = from.compareTo(to);
        Account first;
        Account second;

        if(comparison > 0) {
            first = to;
            second = from;
        }
        else if(comparison < 0) {
            first = from;
            second = to;
        }
        else {
            return;
        }

        synchronized(first) {
            // Т.к. поле с флагом блокировки volatile, записи в (4) и (5) happens-before (8)
            if(first.isBlocked()) { // (8)
                return;
            }

            synchronized(second) {
                // Т.к. поле с флагом блокировки volatile, записи в (4) и (5) happens-before (9)
                if(second.isBlocked()) { // (9)
                    return;
                }

                long fromBalance = from.getMoney();
                long toBalance = to.getMoney();

                if(amount > fromBalance) {
                    return;
                }

                from.setMoney(fromBalance - amount); // (1)
                to.setMoney(toBalance + amount); // (2)
            }
        }

        if(amount > 50000 && isFraud(fromAccountNumber, toAccountNumber, amount)) {
            synchronized(from) {
                from.block(); // (4)
            }

            synchronized(to) {
                to.block(); // (5)
            }
        }
    }

    private synchronized boolean isFraud(String fromAccountNumber, String toAccountNumber, long amount) {
        long start = System.nanoTime();
        long remaining = 1000;

        while(remaining > 0) {
            try {
                Thread.sleep(remaining);
                break;
            }
            catch(InterruptedException exception) {
                remaining = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            }
        }

        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNumber) {
        // Т.к. поле с суммой аккаунта volatile, записи в (1) и (2) happens-before (3)
        return findAccount(accountNumber).getMoney(); // (3)
    }

    private Account findAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);

        if(account == null) {
            throw new AccountNotFoundException(
                String.format("Аккаунт с номером %s не существует", accountNumber));
        }

        return account;
    }
}

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Bank {
    private final long fraudCheckingTime;
    private final long fraudThreshold;
    private final Map<String, Account> accounts;
    private final Random random = new Random();

    public Bank(long fraudCheckingTime, long fraudThreshold, Account accounts[]) {
        this.fraudCheckingTime = fraudCheckingTime;
        this.fraudThreshold = fraudThreshold;
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
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNumber) {
        return findAccount(accountNumber).getBalance();
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
        Account to = findAccount(toAccountNumber);

        if(from.equals(to)) {
            return;
        }

        if(from.isBlocked()) {
            throwAccountBlockedException(fromAccountNumber);
        }

        if(to.isBlocked()) {
            throwAccountBlockedException(toAccountNumber);
        }

        synchronized(from) {
            // Снятие денег с первого аккаунта.
            changeBalance(from, -amount);
        }

        try {
            synchronized(to) {
                // Зачисление денег на второй аккаунт.
                changeBalance(to, amount);
            }
        }
        catch(AccountBlockedException exception) {
            synchronized(from) {
                // В случае если второй аккаунт заблокирован, возврат денег на 1й аккаунт в любом случае.
                from.setBalance(from.getBalance() + amount);

                // Если аккаунт был помечен как проверяемый у СБ, возврат к исходному состоянию,
                // и уведомление всех потоков, которые ждут окончания его проверки.
                if(amount >= fraudThreshold) {
                    // assert from.isFraudChecking();
                    from.changeStateToNormal();
                    from.notifyAll();
                }
            }

            throw exception;
        }

        if(amount >= fraudThreshold) {
            boolean fraud = isFraud(fromAccountNumber, toAccountNumber, amount);

            synchronized(from) {
                changeStateAfterFraudChecking(from, fraud);
            }

            synchronized(to) {
                changeStateAfterFraudChecking(to, fraud);
            }
        }
    }

    private Account findAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);

        if(account == null) {
            throw new AccountNotFoundException(
                String.format("Аккаунт с номером %s не существует", accountNumber));
        }

        return account;
    }

    private static void throwAccountBlockedException(String number) {
        throw new AccountBlockedException(String.format("Аккаунт %s заблокирован", number));
    }

    private void changeBalance(Account account, long amount) {
        // Spurious wakeup...
        for(;;) {
            // Если аккаунт заблокирован, то баланс остается неизменным.
            if(account.isBlocked()) {
                throwAccountBlockedException(account.getNumber());
            }
            // Если аккаунт находится на проверке у СБ, паркуем данный поток и ожидаем ее окончание.
            else if(account.isFraudChecking()) {
                // boolean interrupted = false;

                for(;;) {
                    try {
                        account.wait();
                        break;
                    }
                    catch(InterruptedException exception) {
                        // interrupted = true;
                    }
                }

                // if(interrupted) {
                //     Thread.currentThread().interrupt();
                // }
            }
            else {
                break;
            }
        }

        long balance = account.getBalance() + amount;

        if(balance < 0) {
            throw new AccountInsufficientFundsException(
                String.format("На аккаунте %s недостаточно средств", account.getNumber()));
        }

        account.setBalance(balance);

        // Помечаем аккаунт как проверяемы СБ, если сумма больше fraudThreshold.
        if(amount >= fraudThreshold) {
            account.changeStateToFraudChecking();
        }
    }

    private static void changeStateAfterFraudChecking(Account account, boolean fraud) {
        if(fraud) {
            account.changeStateToBlocked();
        }
        else {
            account.changeStateToNormal();
        }

        account.notifyAll();
    }

    private synchronized boolean isFraud(String fromAccountNumber, String toAccountNumber, long amount) {
        long start = System.nanoTime();
        long remaining = fraudCheckingTime;

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
}

public class BankApp {
    private static final int ACCOUNTS_COUNT = 1000;

    private static final long MIN_ACCOUNT_BALANCE = 150000;
    private static final long MAX_ACCOUNT_BALANCE = 1500000;

    private static final int THREADS_COUNT = 4;
    private static final int TRANSFERS_PER_THREAD = 100;

    // Для теста на большее кол-во операций и жесткой конкуренции, стоит понизить
    // максимальную сумму трансфера, чтоб исключить задержки по 1сек. на
    // "службу безопасности" в классе BankTransfers.
    // private static final int THREADS_COUNT = 10;
    // private static final int TRANSFERS_PER_THREAD = 1000000;

    public static void main(String... args) {
        long totalBalance = 0;
        Account accounts[] = new Account[ACCOUNTS_COUNT];

        for(int i = 0; i < accounts.length; i++) {
            long balance = MIN_ACCOUNT_BALANCE + (long)((MAX_ACCOUNT_BALANCE - MIN_ACCOUNT_BALANCE) * Math.random());
            accounts[i] = new Account(String.valueOf(40000 + i), balance);
            totalBalance += balance;
        }

        Bank bank = new Bank(accounts);
        Thread threads[] = new Thread[THREADS_COUNT];

        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new BankTransfers(TRANSFERS_PER_THREAD, accounts, bank));
        }

        for(Thread thread: threads) {
            thread.start();
        }

        for(Thread thread: threads) {
            for(;;) {
                try {
                    thread.join();
                    break;
                }
                catch(InterruptedException exception) {}
            }
        }

        long totalBalanceAfterTransfers = 0;

        for(Account account: accounts) {
            totalBalanceAfterTransfers += bank.getBalance(account.getNumber());
        }

        System.out.printf(
            "Общий баланс до и после операций составляет %d и %d соответсвенно%n", totalBalance, totalBalanceAfterTransfers);
    }
}

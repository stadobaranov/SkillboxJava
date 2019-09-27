public class BankTransfers implements Runnable {
    private static final long MIN_TRANSFER = 2_500;
    private static final long MAX_TRANSFER = 52_500;

    // private static final long MIN_TRANSFER = 2_500;
    // private static final long MAX_TRANSFER = 42_500;

    private final int transfers;
    private final Account accounts[];
    private final Bank bank;

    public BankTransfers(int transfers, Account accounts[], Bank bank) {
        this.transfers = transfers;
        this.accounts = accounts;
        this.bank = bank;
    }

    @Override
    public void run() {
        for(int i = 0; i < transfers; i++) {
            int from = (int)(Math.random() * accounts.length);
            int to;

            for(;;) {
                to = (int)(Math.random() * accounts.length);

                if(from != to) {
                    break;
                }
            }

            long amount = MIN_TRANSFER + (long)((MAX_TRANSFER - MIN_TRANSFER) * Math.random());

            try {
                bank.transfer(accounts[from].getNumber(), accounts[to].getNumber(), amount);
            }
            catch(BankException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}

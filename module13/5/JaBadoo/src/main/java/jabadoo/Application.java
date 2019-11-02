package jabadoo;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private static final double PURCHASE_CHANCE = .1;

    private static final int SHOW_TIMES = 1000000000;
    private static final int DELAY_BETWEEN_SHOWS = 50;

    private static final int USER_COUNT = 20;

    public static void main(String... args) throws InterruptedException {
        List<String> userNames = generateUserNames();

        try(UserStorageFactory userStorageFactory = new UserStorageFactory("192.168.233.129")) {
            UserStorage userStorage = userStorageFactory.create();

            for(String userName: userNames) {
                userStorage.register(userName);
            }

            for(int i = 0; i < SHOW_TIMES; i++) {
                if(Math.random() < PURCHASE_CHANCE) {
                    String userName = userNames.get((int)(Math.random() * USER_COUNT));
                    userStorage.top(userName);
                    System.out.printf("Пользователь оплатил платную услугу: %s%n", userName);
                }

                System.out.printf("На главной странице показываем: %s%n", userStorage.getNextName());
                Thread.sleep(DELAY_BETWEEN_SHOWS);
            }
        }
    }

    private static List<String> generateUserNames() {
        List<String> userNames = new ArrayList<>(USER_COUNT);

        for(int i = 0; i < USER_COUNT; i++) {
            userNames.add("Пользователь" + (i + 1));
        }

        return userNames;
    }
}

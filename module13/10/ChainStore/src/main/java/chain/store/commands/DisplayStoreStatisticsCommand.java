package chain.store.commands;

import chain.store.ChainStore;
import chain.store.StoreStatistics;
import chain.store.command.Command;
import chain.store.command.CommandHandlingContext;
import java.util.List;

public class DisplayStoreStatisticsCommand implements Command {
    private final ChainStore chainStore;

    public DisplayStoreStatisticsCommand(ChainStore chainStore) {
        this.chainStore = chainStore;
    }

    @Override
    public String getName() {
        return "СТАТИСТИКА_ТОВАРОВ";
    }

    @Override
    public void handle(CommandHandlingContext context) {
        List<StoreStatistics> statistics = chainStore.getStoreStatistics();

        if(statistics.isEmpty()) {
            System.out.println("Статистика отсутствует!");
        }
        else {
            for(StoreStatistics s: statistics) {
                System.out.printf("=== %s%n", s.getName());
                System.out.printf("Средняя цена товара: %.4f%n", s.getAverageGoodsPrice());
                System.out.printf("Минимальная цена товара: %d%n", s.getMinGoodsPrice());
                System.out.printf("Максимальная цена товара: %d%n", s.getMaxGoodsPrice());
                System.out.printf("Общее число товаров: %d%n", s.getGoodsCount());
                System.out.printf("Количество товаров дешевле 100: %d%n", s.getGoodsCountWherePriceIsLessThan100());
            }
        }
    }
}

package chain.store;

public class StoreStatistics {
    private final String name;
    private final double averageGoodsPrice;
    private final int minGoodsPrice;
    private final int maxGoodsPrice;
    private final int goodsCount;
    private final int goodsCountWherePriceIsLessThan100;

    public StoreStatistics(String name, double averageGoodsPrice, int minGoodsPrice, int maxGoodsPrice,
            int goodsCount, int goodsCountWherePriceIsLessThan100) {
        this.name = name;
        this.averageGoodsPrice = averageGoodsPrice;
        this.minGoodsPrice = minGoodsPrice;
        this.maxGoodsPrice = maxGoodsPrice;
        this.goodsCount = goodsCount;
        this.goodsCountWherePriceIsLessThan100 = goodsCountWherePriceIsLessThan100;
    }

    public String getName() {
        return name;
    }

    public double getAverageGoodsPrice() {
        return averageGoodsPrice;
    }

    public int getMinGoodsPrice() {
        return minGoodsPrice;
    }

    public int getMaxGoodsPrice() {
        return maxGoodsPrice;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public int getGoodsCountWherePriceIsLessThan100() {
        return goodsCountWherePriceIsLessThan100;
    }
}

package chain.store;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChainStore implements AutoCloseable {
    private static final String DB_NAME = "chain_store";
    private static final String STORES_COLLECTION_NAME = "stores";
    private static final String GOODS_COLLECTION_NAME = "goods";

    private static final String STORE_FIELD_ID = "_id";
    private static final String STORE_FIELD_NAME = "name";
    private static final String STORE_FIELD_GOODS = "goods";

    private static final String GOODS_FIELD_ID = "_id";
    private static final String GOODS_FIELD_NAME = "name";
    private static final String GOODS_FIELD_PRICE = "price";

    private final String clientHost;
    private final int clientPort;
    private MongoClient client;
    private MongoCollection<Document> storesCollection;
    private MongoCollection<Document> goodsCollection;
    private boolean closed;

    public ChainStore(String clientHost, int clientPort) {
        this.clientHost = clientHost;
        this.clientPort = clientPort;
    }

    public List<StoreStatistics> getStoreStatistics() {
        ensureInitialization();

        List<Document> aggregations = new ArrayList<>();

        // {
        //     $lookup: {
        //         localField: "goods",
        //         foreignField: "_id",
        //         from: "goods",
        //         as: "goodsRef"
        //     },
        // }
        aggregations.add(
            new Document("$lookup",
                new Document()
                    .append("localField", STORE_FIELD_GOODS)
                    .append("foreignField", GOODS_FIELD_ID)
                    .append("from", GOODS_COLLECTION_NAME)
                    .append("as", "goodsRef")
            )
        );

        // {
        //     $unwind: {
        //         path: "$goodsRef"
        //     }
        // },
        aggregations.add(
            new Document("$unwind", new Document("path", "$goodsRef"))
        );

        // {
        //     $project: {
        //         name: 1,
        //         goodsRef: 1,
        //         priceIsLessThan100: {
        //             $cond: {
        //                 if: {
        //                     $lt: ["$goodsRef.price", 100]
        //                 },
        //                 then: 1,
		// 			       else: 0
        //             }
        //         }
        //     }
        // }
        aggregations.add(
            new Document("$project",
                new Document()
                    .append(STORE_FIELD_NAME, 1)
                    .append("goodsRef", 1)
                    .append("priceIsLessThan100",
                        new Document("$cond",
                            new Document()
                                .append("if", new Document("$lt", Arrays.asList("$goodsRef.price", 100)))
                                .append("then", 1)
                                .append("else", 0)
                        )
                    )
            )
        );

        // {
        //     $group: {
        //         _id: "$name",
        //         avg: {
        //             $avg: "$goodsRef.price"
        //         },
        //         min: {
        //             $min: "$goodsRef.price"
        //         },
        //         max: {
        //             $max: "$goodsRef.price"
        //         },
        //         count: {
        //             $sum: 1,
        //         },
        //         countWherePriceIsGreaterThan100: {
        //             $sum: "$priceIsLessThan100"
        //         }
        //     }
        // }
        aggregations.add(
            new Document("$group",
                new Document()
                    .append("_id", "$name")
                    .append("avg", new Document("$avg", "$goodsRef.price"))
                    .append("min", new Document("$min", "$goodsRef.price"))
                    .append("max", new Document("$max", "$goodsRef.price"))
                    .append("count", new Document("$sum", 1))
                    .append("countWherePriceIsLessThan100", new Document("$sum", "$priceIsLessThan100"))
            )
        );

        List<StoreStatistics> statistics = new ArrayList<>();

        try {
            for(Document doc: storesCollection.aggregate(aggregations)) {
                statistics.add(new StoreStatistics(
                    doc.getString(STORE_FIELD_ID),
                    doc.getDouble("avg"),
                    doc.getInteger("min"),
                    doc.getInteger("max"),
                    doc.getInteger("count"),
                    doc.getInteger("countWherePriceIsLessThan100")
                ));
            }
        }
        catch(MongoException exception) {
            throw new ChainStoreException("Не удалось получить статистику", exception);
        }

        return statistics;
    }

    public void addStore(String name) {
        ensureInitialization();

        Document document = new Document();
        document.append(STORE_FIELD_NAME, name);
        document.append(STORE_FIELD_GOODS, Collections.emptySet());

        try {
            storesCollection.insertOne(document);
        }
        catch(MongoException exception) {
            throw new ChainStoreException("Не удалось добавить магазин", exception);
        }
    }

    public void addGoods(String name, int price) {
        ensureInitialization();

        Document document = new Document();
        document.append(GOODS_FIELD_NAME, name);
        document.append(GOODS_FIELD_PRICE, price);

        try {
            goodsCollection.insertOne(document);
        }
        catch(MongoException exception) {
            throw new ChainStoreException("Не удалось добавить товар", exception);
        }
    }

    public void bindGoodsToStore(String goodsName, String storeName) {
        ensureInitialization();

        try {
            Document goods = goodsCollection.find(new Document(GOODS_FIELD_NAME, goodsName)).first();

            if(goods == null) {
                throw new ChainStoreException(String.format("Товар \"%s\" не существует", goodsName));
            }

            Document store = storesCollection.find(new Document(STORE_FIELD_NAME, storeName)).first();

            if(store == null) {
                throw new ChainStoreException(String.format("Магазин \"%s\" не существует", storeName));
            }

            ObjectId goodsId = goods.getObjectId(GOODS_FIELD_ID);
            List<ObjectId> goodsIds = store.getList(STORE_FIELD_GOODS, ObjectId.class);

            if(!goodsIds.contains(goodsId)) {
                goodsIds.add(goodsId);

                storesCollection.updateOne(
                    new Document(STORE_FIELD_ID, store.getObjectId(STORE_FIELD_ID)),
                    new Document("$set", new Document(STORE_FIELD_GOODS, goodsIds))
                );
            }
        }
        catch(MongoException exception) {
            throw new ChainStoreException("Не удалось выставить товар в магазине", exception);
        }
    }

    private void ensureInitialization() {
        if(closed) {
            throw new IllegalStateException("Объект сети магазинов уже закрыт");
        }

        if(client == null) {
            try {
                client = new MongoClient(clientHost, clientPort);

                MongoDatabase db = client.getDatabase(DB_NAME);
                db.drop();
                db.createCollection(STORES_COLLECTION_NAME);
                db.createCollection(GOODS_COLLECTION_NAME);

                IndexOptions nameIndexOptions = new IndexOptions();
                nameIndexOptions.unique(true);

                storesCollection = db.getCollection(STORES_COLLECTION_NAME);
                storesCollection.createIndex(Indexes.ascending(STORE_FIELD_NAME), nameIndexOptions);

                goodsCollection = db.getCollection(GOODS_COLLECTION_NAME);
                goodsCollection.createIndex(Indexes.ascending(GOODS_FIELD_NAME), nameIndexOptions);
            }
            catch(MongoException exception) {
                throw new ChainStoreException(
                    "Не удалось проинициализировать объект сети магазинов", exception);
            }
        }
    }

    @Override
    public void close() {
        if(client != null) {
            client.close();
            client = null;
        }

        closed = true;
    }
}

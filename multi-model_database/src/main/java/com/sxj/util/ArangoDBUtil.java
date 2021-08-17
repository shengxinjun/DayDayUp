package com.sxj.util;

import com.arangodb.*;
import com.sxj.domain.Person;

/***
 * Created by moyongzhuo On 2017/10/31 ***16:44.
 ******/
public class ArangoDBUtil {

    public static void main(String[] args) {
        arangodb =  new ArangoDB
                .Builder()
                //.serializer(new ArangoJack())
                .host("120.55.63.242", 8529)
                .user("root")
                .password("liyang")
                .build();

        ArangoDatabase db = arangodb.db("sxj");
        ArangoCursor<Person> cursorO = db.query("For p in User sort RAND() Limit 9 return p", Person.class);
        cursorO.asListRemaining().forEach(System.out::println);

        arangodb.shutdown();
    }

    public static ArangoDB arangodb = null;

    private static final ThreadLocal<ArangoDB> arangoDBThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<ArangoDatabase> arangoDatabaseThreadLocal = new ThreadLocal<>();

    public static void shutdown() {
        if (arangodb != null)
            arangodb.shutdown();
    }

    public static ArangoDB getArangoDB() {
        return new ArangoDB
                .Builder()
                //.serializer(new ArangoJack())
                .host("120.55.63.242",8529)
                .user("root")
                .password("liyang")
                .maxConnections(128)
                .build();
    }

    public static ArangoDatabase getArangoDatabase() {
        if (arangodb == null)
            arangodb = getArangoDB();
        return arangodb.db("sxj");
    }

    public static ArangoDB getArangoDBFromThreadLocal () {
        ArangoDB db = arangoDBThreadLocal.get();
        try {
            if (db == null) {
                db = getArangoDB();
                arangoDBThreadLocal.set(db);
                System.out.println("[Thread]" + Thread.currentThread().getName() + "获取数据库");
                return db;
            }
            return db;
        } catch (Exception e) {
            System.out.println("[ThreadLocal Get Connection Error]" + e.getMessage());
        }
        return null;
    }

    public static ArangoDatabase getDBFromThreadLocal () {
        ArangoDatabase db = arangoDatabaseThreadLocal.get();
        try {
            if (db == null) {
                db = getArangoDatabase();
                arangoDatabaseThreadLocal.set(db);
                System.out.println("[Thread]" + Thread.currentThread().getName() + "获取数据库");
            }
            return db;
        } catch (Exception e) {
            System.out.println("[ThreadLocal Get Connection Error]" + e.getMessage());
        }
        return null;
    }
}

package com.util;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.arangodb.util.MapBuilder;

import java.util.Collection;
import java.util.Map;

/***
 * Created by moyongzhuo On 2017/10/31 ***16:44.
 ******/
public class ArangoUtil {
	protected ArangoDB arangoDB;
	// protected ArangoConfig config;
	protected ArangoDatabase[] dbs;
	protected ArangoCollection[] colls;
	// private static final Logger log =
	// LoggerFactory.getLogger(ArangoUtil.class);

	public void shutDown() {
		arangoDB.shutdown();
	}

	public void initArango(String host, int port, String username, String password) {
		// 连接Arangodb服务器
		arangoDB = new ArangoDB.Builder().host(host, port).user(username).password(password).build();
	}

	public void initDbs(String dbname) {
		dbs = new ArangoDatabase[1];
		dbs[0] = arangoDB.db(dbname);
	}

	public void insert(int dbIndex, String doc, BaseDocument insertDoc) {
		String insertCmmd = "insert @insertDoc into @@doc";
		dbs[dbIndex].query(insertCmmd, new MapBuilder().put("insertDoc", insertDoc).put("@doc", doc).get(), null, null);
	}

	public void update(int dbIndex, String doc, String key, BaseDocument updateDoc) {
		String updateCmmd = "update {_key:@key} with @updateDoc into @@doc";
		dbs[dbIndex].query(updateCmmd,
				new MapBuilder().put("key", key).put("updateDoc", updateDoc).put("@doc", doc).get(), null, null);
	}

	public void upsert(int dbIndex, String doc, BaseDocument upsertDoc, BaseDocument insertDoc,
			BaseDocument updateDoc) {
		String upsertCmmd = "upsert @upsertDoc insert @insertDoc update @updateDoc in @doc  OPTIONS { keepNull: false }";
		dbs[dbIndex].query(upsertCmmd, new MapBuilder().put("upsertDoc", upsertDoc).put("insertDoc", insertDoc)
				.put("updateDoc", updateDoc).put("doc", doc).get(), null, null);
	}


	public ArangoCursor<BaseDocument> query(int dbIndex, String query, Map<String, Object> params) {
		return dbs[dbIndex].query(query, params, null, BaseDocument.class);
	}
}
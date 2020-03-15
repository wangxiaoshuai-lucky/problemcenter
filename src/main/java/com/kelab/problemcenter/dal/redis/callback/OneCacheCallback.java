package com.kelab.problemcenter.dal.redis.callback;

public interface OneCacheCallback<K, V> {

    V queryFromDB(K missKey);

}

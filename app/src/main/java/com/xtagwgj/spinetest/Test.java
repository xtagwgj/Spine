package com.xtagwgj.spinetest;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

/**
 * Created by xtagwgj on 2017/12/4.
 */

public class Test {
    public void test(){
        Observable.combineLatest(Observable.just(true), Observable.just(false), new BiFunction<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                return null;
            }
        });
    }
}

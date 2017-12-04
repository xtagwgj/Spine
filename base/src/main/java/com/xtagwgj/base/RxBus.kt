package com.xtagwgj.base

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.ConcurrentHashMap

/**
 * 使用 RxJava 实现的类似于 EventBus 的事件总线
 * Created by xtagwgj on 2017/12/3.
 */

class RxBus {
    private val subjectMapper = ConcurrentHashMap<Any, MutableList<Subject<Any>>>()

    fun onEvent(mObservable: Observable<*>, consumer: Consumer<Any>): RxBus {
        mObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, Consumer { throwable ->
                    throwable.printStackTrace()
                })
        return this
    }

    fun <T> register(tag: Any): Observable<T> {
        var subjectList: MutableList<Subject<Any>>? = this.subjectMapper.get(tag)
        if (subjectList == null) {
            subjectList = mutableListOf()
            this.subjectMapper.put(tag, subjectList)
        }

        val subject = PublishSubject.create<Any>()
        subjectList.add(subject)
        return subject as Observable<T>
    }

    fun unRegister(tag: Any) {
        val subjectList = this.subjectMapper[tag]
        if (subjectList != null) {
            this.subjectMapper.remove(tag)
        }

    }

    fun unRegisterAll() {
        if (this.subjectMapper != null && this.subjectMapper.size != 0) {
            this.subjectMapper.clear()
        }
    }

    fun unRegister(tag: Any, observable: Observable<*>): RxBus {
        if (observable != null) {
            val subjectList = this.subjectMapper[tag]
            if (subjectList != null) {
                subjectList.remove(observable)
                if (this.isEmpty(subjectList)) {
                    this.subjectMapper.remove(tag)
                }
            }
        }
        return this
    }

    fun post(content: Any) {
        this.post(content.javaClass.name, content)
    }

    fun post(tag: Any, content: Any) {
        val subjectList = this.subjectMapper[tag]
        if (!this.isEmpty(subjectList)) {
            val var4 = subjectList!!.iterator()

            while (var4.hasNext()) {
                val subject = var4.next()
                subject.onNext(content)
            }
        }

    }

    private fun isEmpty(collection: Collection<Subject<*>>?): Boolean =
            null == collection || collection.isEmpty()
}

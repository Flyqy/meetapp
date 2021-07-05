package com.example.meetapp.stuff

inline fun <E : Any> androidx.collection.SparseArrayCompat<E>.findKeyByValue(predicate: (E) -> Boolean): Int? {
    for (i in 0 until size()) {
        val key = keyAt(i)
        val value = valueAt(i)
        if (predicate(value)) {
            return key
        }
    }
    return null
}

fun <T> MutableList<T>.put(obj: T) {
    val iOf = indexOf(obj)
    if (iOf != -1) set(iOf, obj)
    else add(obj)
}
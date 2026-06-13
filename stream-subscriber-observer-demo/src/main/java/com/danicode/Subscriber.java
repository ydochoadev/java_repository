package com.danicode;

public interface Subscriber<T> {

    // Procesa el siguiente elemento
    void onNext(T next);

    // Para dar nombre el subscritor y saber quién es
    String getName();
}

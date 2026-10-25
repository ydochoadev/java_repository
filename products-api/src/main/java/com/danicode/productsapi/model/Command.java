package com.danicode.productsapi.model;

public record Command<T>(String type, Long id, T body) {
}

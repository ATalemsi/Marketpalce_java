package com.market.marketplace.service;

public interface CommandProductService {
    void addProductToCommand(int productId, int quantity, int commandId);
}

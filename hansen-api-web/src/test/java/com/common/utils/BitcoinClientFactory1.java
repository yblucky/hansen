package com.common.utils;


import ru.paradoxs.bitcoin.client.BitcoinClient;

public class BitcoinClientFactory1 {
    public static void main(String[] args) {
        BitcoinClient bitcoinClient = new BitcoinClient("127.0.0.1", "user", "password", 20099);
        System.out.println(bitcoinClient.getBalance());
    }
}

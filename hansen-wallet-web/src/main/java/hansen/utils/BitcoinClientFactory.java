package hansen.utils;


import hansen.bitcoin.client.BitcoinClient;

public class BitcoinClientFactory {

	private static final boolean IS_TESTNET = true;
	public static BitcoinClient bitcoinClient = null;

	public static BitcoinClient getBitcoinClient(String type) {
		if (bitcoinClient == null) {
			if (IS_TESTNET) {
				bitcoinClient = new BitcoinClient(ResourceUtil.getAddnode(type), ResourceUtil.getRpcuser(type), ResourceUtil.getRpcpassword(type),Integer.valueOf(ResourceUtil.getRpcport(type)));
				System.out.println(bitcoinClient);
			} else {
				bitcoinClient = new BitcoinClient(ResourceUtil.getAddnode(type), ResourceUtil.getRpcuser(type), ResourceUtil.getRpcpassword(type));
				System.out.println(bitcoinClient);
			}

		}
		return bitcoinClient;
	}
}

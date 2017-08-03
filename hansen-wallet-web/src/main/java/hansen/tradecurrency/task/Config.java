package hansen.tradecurrency.task;
/**
 * @author: zzwei
 */
public class Config {
	public static long START_CHANGE =  24*3*60*60*1000;
	public static long END_CHANGE =  60*60*2*1000;
	public static long START = System.currentTimeMillis()-Config.START_CHANGE;
	public static long END = System.currentTimeMillis();
	public static long LAST_WALLET_INSERT_TIME = 0L;
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		System.out.println(Config.START);
		System.out.println(Config.END);
	}
}
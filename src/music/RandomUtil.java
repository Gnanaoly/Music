package music;
import java.util.Random;

@SuppressWarnings("serial")
public class RandomUtil extends Random{
	
	public RandomUtil() {
		super(System.currentTimeMillis());
	}
	
	/* 
	 * includes 0, doesn't include num.
	 * skew = 0 has even distribution,
	 * skew < 0 skews toward zero,
	 * skew > 0 skews to toward num.
	 */
	public int nextSkewed(int num, double skew) {
		boolean positive = (skew >= 0);
		skew = Math.abs(skew) + 1;
		double[] values = new double[num];
		values[0] = skew;
		for(int i = 1; i < num; i++) {
			if(positive) {
				values[i] = values[i-1] * skew;
			} else {
				values[i] = values[i-1] / skew;
			}
		}
		double sum = 0;
		for(int i = 0; i < num; i++) {
			sum += values[i];
		}
		double selection = nextDouble() * sum;
		for(int i = 0; i < num; i++) {
			selection -= values[i];
			if(selection < 0) {
				return i;
			}
		}
		return num;
	}
	
	public int nextEven(int low, int high) {
		if(low % 2 == 1) low++;
		if(high % 2 == 1) high--;
		int ret = nextInt((high - low) / 2);
		ret *= 2;
		ret += low;
		return ret;
	}

}

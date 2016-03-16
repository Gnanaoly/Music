import java.util.Random;

@SuppressWarnings("serial")
public class RandomUtil extends Random{
	
	public RandomUtil() {
		super(System.currentTimeMillis());
	}
	
	/* 
	 * includes 0, doesn't include num.
	 * skew = 0 has even distribution,
	 * skew < 0 skews to the start of the array,
	 * skew > 0 skews to the end of the array.
	 */
	public int randomSkewed(int num, int skew) {
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

}

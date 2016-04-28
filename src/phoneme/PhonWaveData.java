package phoneme;

/*
 * The data in this class was all obtained by applying DFT's to audio of each phoneme.
 */

public class PhonWaveData {

	public static final double recordHz = 20000;

	private static final double IYcos[] = { -2.38720, 2.18718, -2.47245, -0.45249, 1.87714, -1.38851, -1.41312, 1.49434,
			-0.54684, 0.83636 };
	private static final double IYsin[] = { -12.30418, 5.23806, 1.09730, -2.55118, 0.19248, 1.19324, -0.87553, 0.72046,
			1.03891, 0.35617 };
	private static final double IYhz[] = { 1, 2, 16, 13, 15, 14, 18, 17, 12, 19 };
	public static final double IY[][] = { IYcos, IYsin, IYhz };
	public static final int IYrecordFrames = 92;

	private static final double IHcos[] = { 5.31648, -5.54051, 2.39079, 2.05011, -3.39947, 0.23798, 1.04482, 1.47156,
			1.25938, -1.22389, -1.32537, -1.56414, -1.24196, 0.85553, -0.87763 };
	private static final double IHsin[] = { 10.20922, -9.63917, -6.22495, -4.72205, 2.59248, -2.75103, -2.55168,
			1.96580, 1.93201, -1.47141, 1.24599, -0.10666, -0.94708, 1.04362, 0.51594 };
	private static final double IHhz[] = { 3, 2, 1, 14, 10, 4, 11, 13, 19, 9, 15, 12, 5, 16, 18 };
	public static final double IH[][] = { IHcos, IHsin, IHhz };
	public static final int IHrecordFrames = 94;

	private static final double EYcos[] = { 2.69194, 4.87577, -3.09187, 2.56360, -2.13621, 0.54269, 0.84350, -1.64526,
			0.90812, -1.78344, 1.06102, 1.12320, -1.03218, -0.89572, -0.28711 };
	private static final double EYsin[] = { -11.547642, 0.698348, 2.969739, -1.726859, -1.974048, 2.562519, -1.944404,
			1.316639, 1.579400, -0.151362, 0.980715, -0.092242, -0.411158, 0.550444, -0.759159 };
	private static final double EYhz[] = { 2, 14, 1, 13, 12, 15, 3, 16, 19, 11, 4, 18, 21, 20, 17 };
	public static final double EY[][] = { EYcos, EYsin, EYhz };
	public static final int EYrecordFrames = 97;

	private static final double EHcos[] = { 8.427588, -3.509527, -3.464722, 0.740001, -2.917087, -2.976174, 3.016841,
			1.650161, -0.750388, 0.834867, -0.552268, 1.577885, -1.519733, 0.395564, 1.132063, 0.092676, -1.049002,
			0.861053, -0.799919, -0.313603 };
	private static final double EHsin[] = { -12.18153, -2.88049, 2.77866, -3.49258, 1.56878, 0.70505, 0.47635, 2.44067,
			-2.63418, 1.68247, 1.72860, 0.74421, 0.30860, -1.25010, -0.47036, 1.19836, 0.40658, -0.28916, -0.37489,
			0.74512 };
	private static final double EHhz[] = { 4, 2, 5, 1, 3, 11, 10, 15, 16, 6, 9, 12, 14, 13, 21, 17, 22, 18, 8, 20 };
	public static final double EH[][] = { EHcos, EHsin, EHhz };
	public static final int EHrecordFrames = 103;

	private static final double AEcos[] = { 0.161328, 5.771586, -4.640152, -4.166085, 1.181594, -1.082042, 1.684036,
			2.716474, -2.253479, 0.661187, 0.469588, 0.437583, -1.844764, 0.578051, 0.046306 };
	private static final double AEsin[] = { -7.71358, -0.69827, 1.80968, 0.76902, -3.34500, 3.25086, 2.40779, 0.76253,
			-1.64842, 2.67420, 1.97733, -1.90776, -0.64034, -1.23311, 1.15408 };
	private static final double AEhz[] = { 5, 6, 9, 7, 10, 4, 15, 8, 1, 2, 11, 3, 16, 14, 13 };
	public static final double AE[][] = { AEcos, AEsin, AEhz };
	public static final int AErecordFrames = 105;

	private static final double AAcos[] = { 12.09306, -11.37750, -0.79315, 9.18444, -3.03327, -5.74088, 0.90256,
			3.30271, 1.15599, -1.81896, 0.19468, 1.50373, -1.26116, -0.51182, -0.64240, 0.24471, -1.13714, 0.82193,
			-0.95547, -0.15671 };
	private static final double AAsin[] = { 14.90364, -10.25048, 11.45715, -0.68722, -8.67461, 4.58156, 5.76240,
			3.79592, -1.67814, -0.42516, 1.74257, -0.55307, -0.75785, -1.26231, 1.05794, 1.20470, 0.39012, 0.85427,
			0.56020, -0.97357 };
	private static final double AAhz[] = { 7, 5, 1, 4, 8, 6, 2, 3, 26, 9, 25, 22, 27, 23, 21, 28, 15, 16, 10, 14 };
	public static final double AA[][] = { AAcos, AAsin, AAhz };
	public static final int AArecordFrames = 184;

	private static final double AOcos[] = { -4.93679, 6.77237, -0.52541, 3.71164, 1.93270, -3.81176, -1.74061, -2.56564,
			1.42479, -0.81719, 0.60845, 0.66588 };
	private static final double AOsin[] = { -13.24419, -2.27642, 7.08704, 4.38197, -3.70977, 0.42834, -3.14882, 1.52665,
			1.19019, 1.40159, -0.91104, 0.61636 };
	private static final double AOhz[] = { 5, 7, 6, 4, 1, 8, 2, 3, 9, 15, 14, 13 };
	public static final double AO[][] = { AOcos, AOsin, AOhz };
	public static final int AOrecordFrames = 102;

	private static final double OWcos[] = { -14.515653, 6.167130, 4.693119, 1.855401, 1.626621, 2.616586, -1.691815,
			0.492294, 0.180520, 0.561436, 0.098527, 0.360611, -0.472149, -0.317913, -0.462052, -0.356014, 0.379698,
			0.305869, -0.034307, 0.196397 };
	private static final double OWsin[] = { -1.794183, -5.931307, -2.506687, -3.389214, 2.955450, 0.985419, 1.996362,
			1.797305, 0.802926, -0.594088, 0.679761, -0.504506, -0.168178, -0.365256, 0.093098, -0.166547, 0.040182,
			-0.211565, -0.356476, -0.220833 };
	private static final double OWhz[] = { 3, 2, 1, 6, 4, 5, 7, 13, 8, 18, 17, 14, 12, 15, 16, 19, 9, 22, 11, 10 };
	public static final double OW[][] = { OWcos, OWsin, OWhz };
	public static final int OWrecordFrames = 93;

	private static final double UHcos[] = { -7.65943, 8.90704, 1.88753, -5.07691, 2.56030, -2.42512, -0.74535, 1.98131,
			-1.17065, -0.23262, 0.82775, 1.12206, 0.63182, 0.68646, -0.54578 };
	private static final double UHsin[] = { -6.569566, 2.881447, -6.242169, -2.116368, 0.604268, 0.562755, -2.183594,
			-0.039602, 0.855810, 1.341859, -0.893451, 0.442885, 0.782803, 0.624867, -0.704902 };
	private static final double UHhz[] = { 2, 3, 1, 8, 12, 13, 4, 9, 5, 11, 7, 14, 6, 17, 18 };
	public static final double UH[][] = { UHcos, UHsin, UHhz };
	public static final int UHrecordFrames = 91;

	private static final double UWcos[] = { 7.412604, -6.268318, -0.378366, -0.258615, 0.420275, -0.952182, -0.193090,
			-0.630001, 0.691966, -0.058783, 0.402010, 0.477124, 0.192422, -0.426389, 0.211772 };
	private static final double UWsin[] = { -17.359, -2.2861, -2.286, 1.6686, 1.1892, 0.029524, 0.89772, 0.43594,
			-0.0043898, 0.63749, 0.47147, 0.23915, 0.42284, 0.14869, 0.33187 };
	private static final double UWhz[] = { 2, 1, 7, 3, 8, 6, 11, 4, 13, 9, 5, 16, 20, 12, 10 };
	public static final double UW[][] = { UWcos, UWsin, UWhz };
	public static final int UWrecordFrames = 89;

	private static final double ERcos[] = { -7.60917, 6.86628, 5.76779, -5.08831, 1.39926, -2.32280, 1.93201, -0.66619,
			-1.45415, 0.98432, 0.39308, 0.61874, -0.11896, -0.23286, 0.49486 };
	private static final double ERsin[] = { -5.468006, 2.227046, -0.866835, -2.085420, -5.276593, 1.948800, -1.496331,
			-2.178032, 0.545358, 1.051110, -0.768642, 0.473880, 0.528042, 0.453336, -0.051594 };
	private static final double ERhz[] = { 2, 3, 9, 8, 1, 10, 7, 4, 5, 6, 18, 11, 19, 17, 20 };
	public static final double ER[][] = { ERcos, ERsin, ERhz };
	public static final int ERrecordFrames = 93;

	private static final double AXcos[] = { 5.0587, 3.2679, -5.3843, -1.7548, -1.6738, -1.1186, -1.1207, 1.0238,
			0.66783, 0.15977, -0.17077, 0.47129, 0.3441, 0.5446, 0.30789 };
	private static final double AXsin[] = { -4.8589, -4.8636, 1.4469, -4.5444, -2.8636, 2.1496, -1.1825, 1.2362, 1.0734,
			-1.0284, 0.90934, 0.56873, 0.46404, 0.17614, 0.45431 };
	private static final double AXhz[] = { 4, 1, 3, 2, 8, 6, 5, 7, 9, 14, 15, 19, 13, 10, 16 };
	public static final double AX[][] = { AXcos, AXsin, AXhz };
	public static final int AXrecordFrames = 96;

	private static final double AHcos[] = { 1.610661, -1.613098, 4.783209, -0.489140, -2.508523, -0.642892, -2.429368,
			-0.585514, 0.016602, 0.822532, -0.683690, 0.599523 };
	private static final double AHsin[] = { 11.84715, 4.75992, -0.89794, -4.14730, -2.77443, -3.54193, 1.17895,
			-1.81878, -1.85424, 1.63837, 1.64348, -0.80233 };
	private static final double AHhz[] = { 4, 2, 3, 9, 1, 5, 8, 15, 7, 10, 14, 13 };
	public static final double AH[][] = { AHcos, AHsin, AHhz };
	public static final int AHrecordFrames = 98;

	private static final double AYcos[] = { 2.9648, -2.2846, 2.028, 0.11447, -3.0341, 2.3136, -3.0342, 0.20739, 1.8413,
			-0.5111, -1.9376, 1.8868, -1.5247, 0.81263, 1.1414, -0.85778, -0.22829, -0.55333, 0.26788, 0.38564 };
	private static final double AYsin[] = { -9.1408, 4.9344, -3.0163, -3.5216, 1.6605, -2.5684, -0.34912, 2.7755,
			2.0044, 2.3642, -1.1279, 0.9148, -1.366, -1.4438, -1.0964, -0.35857, 0.82762, 0.38362, -0.46529, 0.2078 };
	private static final double AYhz[] = { 5, 4, 9, 2, 10, 1, 6, 14, 8, 7, 3, 11, 13, 15, 12, 16, 17, 20, 19, 18 };
	public static final double AY[][] = { AYcos, AYsin, AYhz };
	public static final int AYrecordFrames = 102;

	private static final double AW1cos[] = { -4.218958, -4.424844, 3.741194, 3.291570, 1.256005, 1.319496, -1.700449,
			1.935074, -1.033264, -0.290656, -0.822027, -0.161169, 0.505128, 0.659857, -0.092836 };
	private static final double AW1sin[] = { -5.89693, 2.06198, 2.91881, 0.29167, -2.96017, -2.86744, -1.69496,
			-0.27283, 1.33290, 1.41646, -0.36657, -0.86695, -0.58979, 0.34072, 0.72677 };
	private static final double AW1hz[] = { 5, 8, 6, 4, 7, 1, 2, 9, 3, 14, 10, 13, 15, 12, 11 };
	public static final double AW1[][] = { AW1cos, AW1sin, AW1hz };
	public static final int AW1recordFrames = 100;

	private static final double AW2cos[] = { 2.7925, -2.2142, 1.5316, -1.0918, -1.2122, 0.71815, 0.12112, -0.003138,
			-0.29997, 0.1614, -0.12428, -0.038247, 0.15472, -0.060637, -0.062573 };
	private static final double AW2sin[] = { -4.0072, -2.5808, 2.7321, -2.6979, 1.8522, 0.07203, 0.31191, 0.31109,
			0.056556, 0.21126, -0.21298, -0.18673, -0.065498, 0.15286, 0.082194 };
	private static final double AW2hz[] = { 3, 5, 4, 1, 2, 6, 8, 12, 7, 16, 15, 13, 11, 10, 14 };
	public static final double AW2[][] = { AW2cos, AW2sin, AW2hz };
	public static final int AW2recordFrames = 88;

	private static final double OY1cos[] = { -0.2127, -3.3897, 1.9974, 0.25503, 0.54303, -0.10185, 0.41316, -0.084739,
			0.13491, -0.033469, 0.057176, 0.29896, 0.20437, 0.032339, 0.035268 };
	private static final double OY1sin[] = { -10.482, 2.342, -3.3851, -3.7678, 0.62088, 0.81845, 0.19999, 0.40536,
			0.35595, 0.37249, 0.34144, 0.00025033, 0.18978, 0.22392, 0.22264 };
	private static final double OY1hz[] = { 2, 1, 4, 3, 5, 6, 16, 7, 8, 18, 9, 15, 17, 10, 12 };
	public static final double OY1[][] = { OY1cos, OY1sin, OY1hz };
	public static final int OY1recordFrames = 95;

	private static final double OY2cos[] = { 7.3005, -7.056, -0.91176, 2.7161, -1.7198, 0.73861, -1.4904, -0.36802,
			-1.5442, 0.78831, 1.2355, 1.0918, 0.65552, -0.54687, -0.45481 };
	private static final double OY2sin[] = { -7.2839, 1.3129, -5.5705, 1.4817, -2.0116, 1.9427, 1.0784, -1.7172,
			-0.72971, 1.451, 0.52786, -0.01226, 0.64645, -0.42963, 0.40875 };
	private static final double OY2hz[] = { 3, 2, 1, 10, 13, 12, 4, 9, 11, 14, 5, 8, 19, 18, 7 };
	public static final double OY2[][] = { OY2cos, OY2sin, OY2hz };
	public static final int OY2recordFrames = 94;
	
	private static final double Pcos[] = { 13.938, 8.3533, -6.9972, 11.473, 4.7612, 8.3217, -6.1905, -10.663, -1.7156,
			-7.8728, -7.516, -7.959, 4.3592, -0.86249, 4.488, 2.1342, 5.0155, -4.1394, -5.554, 5.8372 };
	private static final double Psin[] = { 6.0126, -12.528, -11.222, -2.4648, 10.552, -7.5588, -9.3752, 0.4292, -10.157,
			5.7705, 5.7165, -4.8351, 8.1064, 8.3784, -6.9669, 7.6034, 6.0557, -6.1718, 4.7014, -4.1574 };
	private static final double Phz[] = { 107, 3, 101, 102, 98, 106, 4, 6, 269, 132, 7, 5, 86, 111, 257, 108, 95, 267,
			270, 110 };
	public static final double P[][] = { Pcos, Psin, Phz };
	public static final int PrecordFrames = 1795;
}

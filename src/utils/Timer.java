package utils;

/**
 * Class providing timer functionality
 * 
 * @author Stefan Dumitrescu
 * @author Radu Petrut 
 */
public class Timer {
	long initialTimeStamp;
	long intermediateTimeStamp;

	public Timer() {
		initialTimeStamp = intermediateTimeStamp = System.nanoTime();
	}

	static public String timeFromSecs(double secs) {
		long val = (long) (secs * 1000);
		if (val < 1000)
			return "[" + val + "ms]";
		else if (val < 1000 * 60)
			return String.format("[%.2fs]", ((double) val / 1000.0));
		else {
			val = val / 1000; // seconds
			long minutes = val / 60;
			long sec = val - minutes * 60;
			return "[" + minutes + "m" + sec + "s]";

		}
	}

	public void start() {
		initialTimeStamp = intermediateTimeStamp = System.nanoTime();
	}

	public String total() {
		long val = (System.nanoTime() - initialTimeStamp) / 1000000; // miliseconds
		if (val < 1000)
			return "[" + val + "ms]";
		else if (val < 1000 * 60)
			return String.format("[%.2fs]", ((double) val / 1000.0));
		else {
			val = val / 1000; // secunde
			long minutes = val / 60;
			long sec = val - minutes * 60;
			return "[" + minutes + "m" + sec + "s]";

		}
	}

	public String mark() {
		long val = (System.nanoTime() - intermediateTimeStamp) / 1000000; // miliseconds
		intermediateTimeStamp = System.nanoTime();
		if (val < 1000)
			return "[" + val + "ms]";
		else if (val < 1000 * 60)
			return String.format("[%.2fs]", ((double) val / 1000.0));
		else {
			val = val / 1000; // secunde
			long minutes = val / 60;
			long sec = val - minutes * 60;
			return "[" + minutes + "m" + sec + "s]";
		}
	}

	public double markGetSeconds() {
		return (System.nanoTime() - intermediateTimeStamp) / 1000000000;
	}

	public double totalGetSeconds() {
		return (System.nanoTime() - initialTimeStamp) / 1000000000;
	}

	public double markGetMiliSeconds() {
		return (System.nanoTime() - intermediateTimeStamp) / 1000000;
	}

	public double totalGetMiliSeconds() {
		return (System.nanoTime() - initialTimeStamp) / 1000000;
	}

	public String all() {
		return mark() + ":" + total();
	}

}

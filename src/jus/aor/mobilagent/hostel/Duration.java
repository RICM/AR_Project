/**
 * 
 */
package jus.aor.mobilagent.hostel;

import jus.aor.mobilagent.kernel._Service;

public class Duration implements _Service<Double> {

	public Duration(Object... args) {}

	@Override
	public Double call(Object... params) throws IllegalArgumentException {
		if (params.length != 1)
			throw new IllegalArgumentException();
		Long start, end;
		start = (Long) params[0];
		end = System.nanoTime();
		return (end - start) * 10e-9;
	}

}

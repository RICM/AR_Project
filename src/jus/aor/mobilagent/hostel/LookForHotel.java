/**
 * 
 */
package jus.aor.mobilagent.hostel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel._Service;

public class LookForHotel extends Agent {

	private static final long serialVersionUID = -4912085058159815502L;

	private String localisation;
	private Collection<Hotel> hotels;
	private Map<Hotel, Numero> numbers;

	private long start;

	public LookForHotel(Object... args) {
		localisation = (String) args[0];
		hotels = new ArrayList<>();
		numbers = new HashMap<>();
		start = System.nanoTime();
	}

	protected _Action findHotel = new _Action() {

		private static final long serialVersionUID = -3360083386110147495L;

		@SuppressWarnings("unchecked")
		@Override
		public void execute() {
			_Service<?> ch = getService("Hotels");
			Collection<Hotel> h = (Collection<Hotel>) ch.call(new Object[] { localisation });
			hotels.addAll(h);
			logger.log(Level.INFO, "found " + h.size() + " hotels in " + localisation);
		}
	};

	protected _Action findTelephone = new _Action() {

		private static final long serialVersionUID = -6683783783071081484L;

		@Override
		public void execute() {
			logger.log(Level.INFO, "getting phone numbers");
			_Service<?> annuaire = getService("Telephones");
			for (Hotel h : hotels) {
				Numero num = (Numero) annuaire.call(new Object[] { h.name });
				numbers.put(h, num);
			}
		}
	};

	@Override
	protected _Action retour() {
		return new _Action() {

			private static final long serialVersionUID = 201367165026377098L;

			@Override
			public void execute() {
				_Service<?> duration = getService("Duration");
				double d = (Double) duration.call(new Object[] { start });
				logger.log(Level.INFO, "found " + hotels.size() + " hotels in " + localisation);
				logger.log(Level.INFO, "lookup time: " + d);
				//System.out.println("found " + hotels.size() + " hotels in " + localisation);
				//System.out.println("lookup time: " + d);
			}
		};
	}

}

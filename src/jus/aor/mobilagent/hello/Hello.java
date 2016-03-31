package jus.aor.mobilagent.hello;

import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.Agent;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 * @author  Morat
 */
public class Hello extends Agent{

	private static final long serialVersionUID = 3882387847447156013L;
	
	private String visited = "";

	/**
	  * construction d'un agent de type hello.
	  * @param args aucun argument n'est requis
	  */
	 public Hello(Object... args) {}

	 /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doIt = new _Action(){
		
		private static final long serialVersionUID = -9129644307555501553L;

		public void execute() {
			System.out.println(server);
			visited = visited.concat(String.format("%s\n", server));
		}
	};

	@Override
	protected _Action retour(){
		return new _Action() {
			
			private static final long serialVersionUID = 8112403583439231794L;

			@Override
			public void execute() {
				System.out.println(String.format("List of visited servers:\n%s", visited));
			}
		};
	}

}

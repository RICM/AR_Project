package jus.aor.rmi.client;

import java.rmi.RemoteException;

/**
 * Created by matthieu on 19/02/16.
 */
public class Client {

    public static void main(String args[]){
    	LookForHotel lfh = new LookForHotel("Paris");
    	try {
			long nano = lfh.call();
			double seconds = nano / 1000000000;
			System.out.println(seconds + " secondes");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    }

}

import akka.actor.PoisonPill;
import akka.actor.UntypedActor;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * This class handles the Jail for the TSA screening process. If a passenger has
 * failed the screening process, either the passenger themselves or their baggage,
 * that passenger is sent to the Jail to be detained. At the end of the day, all
 * passengers detained in this fashion are sent to more permanent holding facilities.
 */

public class Jail extends VerboseActor {
	private List<FailedPassenger> passengers;   // the collection of criminals
	private shutdown_count;   		    // the countdown for shutdown

	/*
	 * Constructor for the Jail object.
	 * 
	 * @param line_count   the number of lines being created for the current simulation
	 */
	
	public Jail(int line_count) {
		super( "Jail" );
		shutdown_count = line_count;
	}

	public void onReceive( Object message ) {
		if( message instanceof FailedPassenger ) {
			/**** handles receiving a FailedPassenger message *****/
			System.out.println("Looks like another passenger is going to JAIL!"
				+ "(Jail receives FailedPassenger message)");
			FailedPassenger passenger = (FailedPassenger) message;
			passengers.add(passenger); // add passenger to collection
			System.out.println("They're in the jailhouse now!(FailedPassenger added to Jail)");
		}else if (message instanceof Shutdown) {
			/**** handles receiving a Shutdown message *****/
			shutdown_count--;

			// if the shutdown counter reaches 0, then every line in the simulation has
			// shutdown. Therefore, the Jail object can begin the shutdown sequence.
			if (shutdown_count == 0) {
				System.out.printf("Today, %d criminals are being sent to the bighouse!\n", passengers.size());
				Iterator<FailedPassenger> itr = passengers.iterator();
				FailedPassenger current_passenger = null;
				// print out each individual criminal's name for the purpose of SHAME!
				while (itr.hasNext()) {
					current_passenger = itr.next();
					System.out.printf("\t%s\n", current_passenger.getPassenger().toString());
				}
				shutdown();
			}
		} else {
			unhandled( message );
		}
	}
}

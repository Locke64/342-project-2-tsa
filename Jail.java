import akka.actor.PoisonPill;
import akka.actor.UntypedActor;
import java.util.List;
import java.util.ArrayList;

public class Jail extends VerboseActor {
	List<FailedPassenger> passengers = new ArrayList<FailedPassenger>();
	
	public Jail() {
		super( "Jail" );
	}

	public void onReceive( Object message ) {
		if( message instanceof FailedPassenger ) {
			//TODO
			System.out.println("Looks like another passenger is going to JAIL!"
				+ "(Jail receives FailedPassenger message)");
			FailedPassenger passenger = (FailedPassenger) message;
			passengers.add(passenger);
			System.out.println("They're in the jailhouse now!(FailedPassenger added to Jail)");
		}else if (message instanceof PoisonPill) {
			//TODO this section is merely conjecture. Will consider other options later.
			System.out.printf("Today, %d criminals are being sent to the bighouse!\n", passengers.size()); 
		} else {
			unhandled( message );
		}
	}
}

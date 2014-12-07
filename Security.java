import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Security extends UntypedActor {
	//TODO data structures containing passengers and/or baggage waiting for the other to pass its respective scanner

	private ActorRef jail;
	
	public Security( ActorRef jail ) {
		this.jail = jail;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Passenger ) {
			//TODO
		} else if( message instanceof FailedPassenger ) {
			//TODO
		} else if( message instanceof Baggage ) {
			//TODO
		} else if( message instanceof FailedBaggage ) {
			//TODO
		} else {
			unhandled( message );
		}
	}
}
import akka.actor.UntypedActor;

public class Security extends UntypedActor {
	//TODO data structures containing passengers and/or baggage waiting for the other to pass its respective scanner

	private ActorRef jail;
	
	public Security( ActorRef jail ) {
		this.jail = jail;
	}
	
	public void onRecieve( Object message ) {
		if( typeof message Passenger ) {
			//TODO
		} else if( typeof message FailedPassenger ) {
			//TODO
		} else if( typeof message Baggage ) {
			//TODO
		} else if( typeof message FailedBaggage ) {
			//TODO
		} else {
			unhandled( message );
		}
	}
}
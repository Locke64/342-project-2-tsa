import akka.actor.UntypedActor;

public class BaggageCheck implements UntypedActor {
	
	private ActorRef security;
	
	public BaggageCheck( ActorRef security ) {
		this.security = security;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Baggage ) {
			//TODO
		} else {
			unhandled( message );
		}
	}
}
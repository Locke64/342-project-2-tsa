import akka.actor.UntypedActor;

public class BodyCheck implements UntypedActor {
	
	private ActorRef security;
	
	public BodyCheck( ActorRef security ) {
		this.security = security;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Passenger ) {
			//TODO
		} else {
			unhandled( message );
		}
	}
}
import akka.actor.ActorRef;
import java.util.Random;

public class BodyCheck extends VerboseActor {
	
	private Random gen = new Random();
	
	private int id;
	private ActorRef security;
	
	public BodyCheck( int id, ActorRef security ) {
		super( "Body Check " + id );
		this.id = id;
		this.security = security;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Passenger ) {
			Passenger passenger = (Passenger) message;
			receiveMessage( passenger );
			processMessage( passenger );
			sendMessage( gen.nextInt( 5 ) > 0 ?
						 passenger :
						 new FailedPassenger( passenger ),
						 security, "Security " + id );
		} else if( message instanceof Shutdown ) {
			receiveMessage( (Shutdown) message );
			shutdown();
			shutdown( security, "Security " + id, false );
		} else {
			unhandled( message );
		}
	}
}
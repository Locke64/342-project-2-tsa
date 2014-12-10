import akka.actor.ActorRef;
import java.util.Random;

public class BaggageCheck extends VerboseActor {
	
	private Random gen = new Random();
	
	private int id;
	private ActorRef security;
	
	public BaggageCheck( int id, ActorRef security ) {
		super( "Baggage Check " + id );
		this.id = id;
		this.security = security;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Baggage ) {
			Baggage baggage = (Baggage) message;
			receiveMessage( baggage );
			processMessage( baggage );
			sendMessage( gen.nextInt( 5 ) > 0 ?
						 baggage :
						 new FailedBaggage( baggage ),
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
import akka.actor.ActorRef;
import java.util.Random;

public class BaggageCheck extends VerboseActor {
	
	private Random gen = new Random();
	
	private ActorRef security;
	
	public BaggageCheck( int id, ActorRef security ) {
		super( "Baggage Check " + id );
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
						 security, "Security" );
		} else {
			unhandled( message );
		}
	}
}
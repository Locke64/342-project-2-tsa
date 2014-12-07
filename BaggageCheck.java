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
			sendMessage( gen.nextInt( 5 ) > 0 ?
						 (Baggage) message :
						 new FailedBaggage( (Baggage) message ),
						 security );
		} else {
			unhandled( message );
		}
	}
}
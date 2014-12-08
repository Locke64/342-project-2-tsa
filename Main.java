import static akka.actor.Actors.*;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;


public class Main {

	public static void main(String[] args) throws InterruptedException,
	ExecutionException {
		
		int count = 3; //TODO user input
		
		final Queue<ActorRef> scannerQs = new LinkedList<ActorRef>();
		
		// create and start jail
		final ActorRef jail = actorOf( Jail.class );
		jail.start();
		
		// create security lines
		for(int index = 0; index != count; index++) {
			final int id = index;
			// create and start security, given jail
			final ActorRef security = actorOf( new UntypedActorFactory() {
				public UntypedActor create() {
					return new Security( jail );
				}
			} );
			security.start();
			
			// create and start body and baggage checks, given security
			final ActorRef bodyCheck = actorOf( new UntypedActorFactory() {
				public UntypedActor create() {
					return new BodyCheck( id, security );
				}
			} );
			final ActorRef baggageCheck = actorOf( new UntypedActorFactory() {
				public UntypedActor create() {
					return new BaggageCheck( id, security );
				}
			} );
			bodyCheck.start();
			baggageCheck.start();
			
			// create and start scanner queue, given body and baggage checks
			final ActorRef scannerQ = actorOf( new UntypedActorFactory() {
				public UntypedActor create() {
					return new ScannerQueue( id, bodyCheck, baggageCheck );
				}
			} );
			scannerQ.start();
			scannerQs.add( scannerQ );
		}
		
		// create and start document check, given scanner queues
		final ActorRef documentCheck = actorOf( new UntypedActorFactory() {
			public UntypedActor create() {
				return new DocumentCheck( scannerQs );
			}
		} );
		documentCheck.start();
		
		for( int i = 0; i < 10; ++i ) {
			documentCheck.tell( new Passenger( i ) );
		}
		
		documentCheck.tell( new Shutdown() );
		documentCheck.tell( poisonPill() );
		
	}
	
	
	

}

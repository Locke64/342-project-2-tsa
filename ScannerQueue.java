import akka.actor.ActorRef;

/*
 * ScannerQueue receives passengers and sends the passenger to the BodyCheck and his or her baggage to the BaggageCheck.
 * Messages Received:
 *		Passenger from DocumentCheck
 * Messages Sent:
 *		Passenger to BodyCheck
 *		Baggage to BaggageCheck
 */
public class ScannerQueue extends VerboseActor {
	
	private int id;
	private ActorRef bodyCheck;
	private ActorRef baggageCheck;
	
	public ScannerQueue( int id, ActorRef bodyCheck, ActorRef baggageCheck ) {
		super( "Scanner Queue " + id );
		this.id = id;
		this.bodyCheck = bodyCheck;
		this.baggageCheck = baggageCheck;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Passenger ) {
			Passenger passenger = (Passenger) message;
			receiveMessage( passenger );
			processMessage( passenger );
			sendMessage( passenger, bodyCheck, "Body Check " + id );
			sendMessage( passenger.getBaggage(), baggageCheck, "Baggage Check " + id );
		} else if( message instanceof Shutdown ) {
			receiveMessage( (Shutdown) message );
			shutdown();
			shutdown( bodyCheck, "Body Check " + id );
			shutdown( baggageCheck, "Baggage Check " + id );
		} else {
			unhandled( message );
		}
	}
}
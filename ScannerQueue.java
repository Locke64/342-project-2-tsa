import akka.actor.ActorRef;

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
		} else {
			unhandled( message );
		}
	}
}
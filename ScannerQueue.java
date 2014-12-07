import akka.actor.ActorRef;

public class ScannerQueue extends VerboseActor {
	
	private ActorRef bodyCheck;
	private ActorRef baggageCheck;
	
	public ScannerQueue( int id, ActorRef bodyCheck, ActorRef baggageCheck ) {
		super( "Scanner Queue " + id );
		this.bodyCheck = bodyCheck;
		this.baggageCheck = baggageCheck;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Passenger ) {
			sendMessage( (Passenger) message, bodyCheck );
			sendMessage( ((Passenger) message).getBaggage(), baggageCheck );
		} else {
			unhandled( message );
		}
	}
}
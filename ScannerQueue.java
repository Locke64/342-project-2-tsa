import akka.actor.UntypedActor;

public class ScannerQueue implements UntypedActor {
	
	private ActorRef bodyCheck;
	private ActorRef baggageCheck;
	
	public ScannerQueue( ActorRef bodyCheck, ActorRef baggageCheck ) {
		this.bodyCheck = bodyCheck;
		this.baggageCheck = baggageCheck;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Passenger ) {
			bodyCheck.tell( message );
			baggageCheck.tell( ((Passenger) message).getBaggage() );
		} else {
			unhandled( message );
		}
	}
}
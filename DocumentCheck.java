import akka.actor.ActorRef;
import java.util.Queue;
import java.util.Random;

public class DocumentCheck extends VerboseActor {
	
	private Random gen = new Random();
	
	private Queue<ActorRef> scannerQueues;
	
	public DocumentCheck( Queue<ActorRef> scannerQueues ) {
		super( "Document Check" );
		this.scannerQueues = scannerQueues;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Passenger ) {
			Passenger passenger = (Passenger) message;
			receiveMessage( passenger );
			processMessage( passenger ); //TODO put this after processing is done?
			ActorRef sq = scannerQueues.remove();
			if( gen.nextInt( 5 ) > 0 )
				sendMessage( passenger, sq );
			else
				turnAway( passenger );
			scannerQueues.add( sq );
		} else {
			unhandled( message );
		}
	}
	
	private void turnAway( Passenger passenger ) {
		System.out.println( this + " turned away " + passenger + "." );
	}
}
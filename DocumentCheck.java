import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import java.util.Queue;
import java.util.Random;

/*
 * DocumentCheck receives passengers and either turns them away with document problems or sends them to the scanner queues.
 * Messages Received:
 *		Passenger from main driver
 * Messages Sent:
 *		Passenger to ScanerQueue
 */
public class DocumentCheck extends VerboseActor {
	
	private Random gen = new Random();

	// list of scanner queues to cycle through
	private Queue<ActorRef> scannerQueues;
	private int cur = 0;
	private int max;
	
	public DocumentCheck( Queue<ActorRef> scannerQueues ) {
		super( "Document Check" );
		this.scannerQueues = scannerQueues;
		max = scannerQueues.size();
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Passenger ) {
			Passenger passenger = (Passenger) message;
			receiveMessage( passenger );
			processMessage( passenger );
			if( gen.nextInt( 5 ) > 0 ) {
				ActorRef sq = scannerQueues.remove(); // get next scanner queue
				sendMessage( passenger, sq, "Scanner Queue " + cur ); // send passenger to it
				scannerQueues.add( sq ); // put scanner queue back at the end of our list
				if( ++cur >= max ) cur = 0;
			} else
				turnAway( passenger );
		} else if( message instanceof Shutdown ) {
			receiveMessage( (Shutdown) message );
			shutdown();
			while( scannerQueues.size() > 0 ) {
				shutdown( scannerQueues.remove(), "Scanner Queue " + cur );
				if( ++cur >= max ) cur = 0;
			}
		} else {
			unhandled( message );
		}
	}
	
	// print a message stating the given passenger was turned away.
	private void turnAway( Passenger passenger ) {
		System.out.println( this + " turned away " + passenger + "." );
	}
}
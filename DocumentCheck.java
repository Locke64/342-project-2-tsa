import akka.actor.UntypedActor;
import java.util.Queue;
import java.util.Random;

public class DocumentCheck implements UntypedActor {
	
	private Random gen = new Random();
	
	private Queue<ActorRef> scannerQueues;
	
	public DocumentCheck( Queue<ActorRef> scannerQueues ) {
		this.scannerQueues = scannerQueues;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Passenger ) {
			ActorRef sq = scannerQueues.remove();
			if( gen.nextInt( 5 ) > 0 )
				sq.tell( message );
			// else passenger is turned away
			scannerQueues.add( sq );
		} else {
			unhandled( message );
		}
	}
}
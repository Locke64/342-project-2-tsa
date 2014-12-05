import akka.actor.UntypedActor;
import java.util.Queue;

public class DocumentCheck implements UntypedActor {
	
	private Queue<ActorRef> scannerQueues;
	
	public DocumentCheck( Queue<ActorRef> scannerQueues ) {
		this.scannerQueues = scannerQueues;
	}
	
	public void onReceive( Object message ) {
		if( message instanceof Passenger ) {
			//TODO
		} else {
			unhandled( message );
		}
	}
}
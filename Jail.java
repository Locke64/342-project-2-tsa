import akka.actor.UntypedActor;
import java.util.List;
import java.util.ArrayList;

public class Jail extends UntypedActor {
	List<FailedPassenger> passengers = new ArrayList<FailedPassenger>();

	public void onReceive( Object message ) {
		if( message instanceof FailedPassenger ) {
			//TODO
		} else {
			unhandled( message );
		}
	}
}
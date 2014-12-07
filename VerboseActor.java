import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public abstract class VerboseActor extends UntypedActor {

	protected String name;
	
	protected VerboseActor( String name ) {
		this.name = name;
	}
	
	//FIXME ActorRef doesn't have the toString() we want. Can we get the Actor somehow? otherwise take a string and do it manually.
	protected void sendMessage( VerboseMessage message, ActorRef receiver ) {
		System.out.println( this + " sent " + message + " to " + receiver + "." );
		receiver.tell( message );
	}
	
	protected void receiveMessage( VerboseMessage message ) {
		System.out.println( this + " received " + message + "." );
	}
	
	protected void processMessage( VerboseMessage message ) {
		System.out.println( this + " processed " + message + "." );
	}
	
	public String toString() {
		return name;
	}
	
	public void onReceive( Object message ) {
		unhandled( message );
	}
}
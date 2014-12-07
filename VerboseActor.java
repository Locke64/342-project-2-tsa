import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public abstract class VerboseActor extends UntypedActor {

	protected String name;
	
	protected VerboseActor( String name ) {
		this.name = name;
	}
	
	protected void sendMessage( VerboseMessage message, ActorRef receiver, String recName ) {
		System.out.println( this + " sent " + message + " to " + recName + "." );
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
import akka.actor.Actor;
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
	
	public void shutdown() {
		System.out.println( this + " received a shutdown message." );
		System.out.println( this + " is shutting down." );
	}
	
	public void shutdown( ActorRef target, String recName ) {
		System.out.println( this + " sent a shutdown message to " + recName + "." );
		target.tell( new Shutdown() );
		target.tell( Actor.poisonPill() );
	}
}
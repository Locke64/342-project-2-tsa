// a Passenger message, containing the Baggage that it owns
public class Passenger extends VerboseMessage {

	private final Baggage baggage;
	
	public Passenger( int id ) {
		super( "Passenger " + id );
		this.baggage = new Baggage( id );
	}
	
	public Baggage getBaggage() {
		return baggage;
	}
	
	// check that the given Baggage is owned by this Passenger
	public boolean owns( Baggage baggage ) {
		return this.baggage == baggage;
	}
}
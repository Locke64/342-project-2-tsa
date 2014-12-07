public class Passenger extends VerboseMessage {

	private final Baggage baggage;
	
	public Passenger( int id ) {
		super( "Passenger " + id );
		this.baggage = new Baggage( id );
	}
	
	public Baggage getBaggage() {
		return baggage;
	}
	
	public boolean owns( Baggage baggage ) {
		return this.baggage == baggage;
	}
}
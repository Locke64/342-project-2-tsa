public class Passenger {
	private final Baggage baggage;
	
	public Passenger( Baggage baggage ) {
		this.baggage = baggage;
	}
	
	public Baggage getBaggage() {
		return baggage;
	}
	
	public boolean owns( Baggage baggage ) {
		return this.baggage == baggage;
	}
}
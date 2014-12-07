public class FailedPassenger extends VerboseMessage {

	private final Passenger passenger;
	
	public FailedPassenger( Passenger passenger ) {
		super( passenger + " (failed)" );
		this.passenger = passenger;
	}
	
	public Passenger getPassenger() {
		return passenger;
	}
}
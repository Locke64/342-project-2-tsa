// a message for a Passenger who has failed the body scanner, containing the original Passenger message
public class FailedPassenger extends VerboseMessage {

	private final Passenger passenger;
	
	public FailedPassenger( Passenger passenger ) {
		super( passenger + " (failed)" );
		this.passenger = passenger;
	}
	
	// get the original Passenger message
	public Passenger getPassenger() {
		return passenger;
	}
}
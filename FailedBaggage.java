// a message for Baggage that has failed the Baggage Scanner, containing the original Baggage message
public class FailedBaggage extends VerboseMessage {

	private final Baggage baggage;
	
	public FailedBaggage( Baggage baggage ) {
		super( baggage + " (failed)" );
		this.baggage = baggage;
	}
	
	// get the original Baggage message
	public Baggage getBag() {
		return baggage;
	}
}
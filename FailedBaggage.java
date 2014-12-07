public class FailedBaggage extends VerboseMessage {

	private final Baggage baggage;
	
	public FailedBaggage( Baggage baggage ) {
		super( baggage + " (failed)" );
		this.baggage = baggage;
	}
}
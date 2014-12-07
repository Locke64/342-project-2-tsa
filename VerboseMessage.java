public abstract class VerboseMessage {

	protected String name;
	
	protected VerboseMessage( String name ) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
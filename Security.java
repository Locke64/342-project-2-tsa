import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.ArrayList;

public class Security extends UntypedActor {
	//TODO data structures containing passengers and/or baggage waiting for the other to pass its respective scanner
	private ArrayList<VerboseMessage> passengers;
	private ArrayList<VerboseMessage> baggage;
	private ActorRef jail;
	
	public Security( ActorRef jail ) {
		this.jail = jail;
		passengers = new ArrayList<VerboseMessage>();
		baggage = new ArrayList<VerboseMessage>();
	}
	
	public void onReceive( Object message ) {
		VerboseMessage current_passenger = null;
		VerboseMessage current_bag = null;
		if( message instanceof Passenger ) {
			//TODO
			Passenger my_passenger = (Passenger) message;
			current_bag = checkWaitingBaggage(baggage, current_passenger);
			if (current_bag != null) {
				baggage.remove(current_bag);
				if (current_bag instanceof FailedBaggage) {
					sendMessage(new FailedPassenger(my_passenger), jail);
				} else {
					System.out.printf("%s passed the checkpoint. Bon voyage!\n", myPassenger.toString());
			} else {
				passengers.add(myPassenger);
			}
		} else if( message instanceof FailedPassenger ) {
			//TODO
			FailedPassenger failure = (FailedPassenger) message;
			currentBag = checkWaitingBaggage(baggage, failure);
			if (current_bag != null) {
				baggage.remove(current_bag);
				sendMessage(failure, jail);
			} else {
				passengers.add(failure);
			}
		} else if( message instanceof Baggage ) {
			//TODO
			Baggage bag = (Baggage) message;
			current_passenger = checkWaitingPassengers(passengers, bag);
			if (current_passenger != null) {
				passengers.remove(current_passenger);
				if (current_passenger instanceof Passenger) {
					System.out.printf("%s passed the checkpoint. Bon voyage!\n", current_passenger.toString());
				} else {
					sendMessage(current_passenger, jail);
				}
			} else {
				baggage.add(bag);
			}
		} else if( message instanceof FailedBaggage ) {
			//TODO
			FailedBaggage failed_bag = (FailedBaggage) message;
			current_passenger = checkWaitingPassengers(passengers, bag);
			if (current_passenger != null) {
				passengers.remove(current_passenger);
				System.out.printf
				if (current_passenger instanceof Passenger) {
					sendMessage(new FailedPassenger(current_passenger), jail);
				} else {
					sendMessage(current_passenger, jail);
				}
		} else {
			unhandled( message );
		}
	}

	private static VerboseMessage checkWaitingBaggage(ArrayList<VerboseMessage> p, VerboseMessage pass) {
		ListIterator<VerboseMesage> itr = p.iterator();
		boolean owns_it = false;
		VerboseMessage my_bag = null;
		VerboseMessage temp_bag = null;
		while(itr.hasNext()) {
			temp_bag = itr.hasNext();
			if (bag instanceof Baggage) {
				if (pass instanceof Passenger) {
 					owns_it = pass.owns(temp_bag);
				} else {
					owns_it = pass.getPassenger().owns(temp_bag);
				}

				if (owns_it) {
					my_bag = temp_bag;
					break;
				}
			} else if (bag instanceof FailedBaggage) {
				if (pass instanceof Passenger) {
					owns_it = pass.owns(temp_bag.getBag());
				} else {
					owns_it = pass.getPassenger().owns(temp_bag.getBag());
				}

				if (owns_it) {
					my_bag = bag;
					break;
				}
			}else {
				System.err.println("Found something other than baggage in the baggage list");
			}
		}
		return my_bag;
	}

	private static VerboseMessage checkWaitingPassengers(ArrayList<VerboseMessage> passengers, VerboseMessage bag) {
		ListIterator<VerboseMessage> itr = passengers.iterator();
		boolean owns_me = false;
		VerboseMessage owner = null;
		VerboseMessage temp_pass = null;
		while(itr.hasNext()) {
			temp_pass = itr.next();
			if (temp_pass instanceof Passenger) {
				if (bag instanceof Bag) {
					owns_me = temp_pass.owns(bag);
				} else {
					owns_me = temp_pass.owns(bag.getBag());
				}
				
				if (owns_me) {
					owner = temp_pass;
					break;
				}
			} else if (bag instanceof FailedBag) {
				if (temp_pas instanceof Passenger) {
					owns_me = temp_pass.owns(bag.getBag());
				} else {
					owns_me = temp_pass.getPassenger().owns(bag.getBag());
				}
				
				if (owns_me) {
					owner = temp_pass;
					break;
				}
			} else {
				System.err.println("Found something other than people in the list.");
			}
			return owner;
	}
}

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.ArrayList;
import java.util.Iterator;

public class Security extends VerboseActor {
	//TODO data structures containing passengers and/or baggage waiting for the other to pass its respective scanner
	private ArrayList<VerboseMessage> passengers;
	private ArrayList<VerboseMessage> baggage;
	private ActorRef jail;
	
	public Security( ActorRef jail ) {
		super( "Security" );
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
					sendMessage(new FailedPassenger(my_passenger), jail, "Jail");
				} else {
					System.out.printf("%s passed the checkpoint. Bon voyage!\n", my_passenger.toString());
				}
			} else {
				passengers.add(my_passenger);
			}
		} else if( message instanceof FailedPassenger ) {
			//TODO
			FailedPassenger failure = (FailedPassenger) message;
			current_bag = checkWaitingBaggage(baggage, failure);
			if (current_bag != null) {
				baggage.remove(current_bag);
				sendMessage(failure, jail, "Jail");
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
					sendMessage(current_passenger, jail, "Jail");
				}
			} else {
				baggage.add(bag);
			}
		} else if( message instanceof FailedBaggage ) {
			//TODO
			FailedBaggage failed_bag = (FailedBaggage) message;
			current_passenger = checkWaitingPassengers(passengers, failed_bag);
			if (current_passenger != null) {
				passengers.remove(current_passenger);
				if (current_passenger instanceof Passenger) {
					sendMessage(new FailedPassenger((Passenger) current_passenger), jail, "Jail");
				} else {
					sendMessage(current_passenger, jail, "Jail");
				}
			}
		} else {
			unhandled( message );
		}
	}

	private static VerboseMessage checkWaitingBaggage(ArrayList<VerboseMessage> p, VerboseMessage pass) {
		Iterator<VerboseMessage> itr = p.iterator();
		boolean owns_it = false;
		VerboseMessage my_bag = null;
		VerboseMessage temp_bag = null;
		while(itr.hasNext()) {
			temp_bag = itr.next();
			if (temp_bag instanceof Baggage) {
				Baggage good_bag = (Baggage) temp_bag;
				if (pass instanceof Passenger) {
 					owns_it = ((Passenger) pass).owns(good_bag);
				} else {
					owns_it = ((FailedPassenger) pass).getPassenger().owns(good_bag);
				}

				if (owns_it) {
					my_bag = temp_bag;
					break;
				}
			} else if (temp_bag instanceof FailedBaggage) {
				FailedBaggage failed_bag = (FailedBaggage) temp_bag;
				if (pass instanceof Passenger) {
					owns_it = ((Passenger) pass).owns(failed_bag.getBag());
				} else {
					owns_it = ((FailedPassenger) pass).getPassenger().owns(failed_bag.getBag());
				}

				if (owns_it) {
					my_bag = failed_bag;
					break;
				}
			}else {
				System.err.println("Found something other than baggage in the baggage list");
			}
		}
		return my_bag;
	}

	private static VerboseMessage checkWaitingPassengers(ArrayList<VerboseMessage> passengers, VerboseMessage bag) {
		Iterator<VerboseMessage> itr = passengers.iterator();
		boolean owns_me = false;
		VerboseMessage owner = null;
		VerboseMessage temp_pass = null;
		while(itr.hasNext()) {
			temp_pass = itr.next();
			if (temp_pass instanceof Passenger) {
				Passenger good_pass = (Passenger) temp_pass;
				if (bag instanceof Baggage) {
					owns_me = good_pass.owns((Baggage) bag);
				} else {
					owns_me = good_pass.owns(((FailedBaggage) bag).getBag());
				}
				
				if (owns_me) {
					owner = good_pass;
					break;
				}
			} else if (bag instanceof FailedBaggage) {
				FailedBaggage failed_bag = (FailedBaggage) bag;
				if (temp_pass instanceof Passenger) {
					owns_me = ((Passenger) temp_pass).owns(failed_bag.getBag());
				} else {
					owns_me = ((FailedPassenger) temp_pass).getPassenger().owns(failed_bag.getBag());
				}
				
				if (owns_me) {
					owner = temp_pass;
					break;
				}
			} else {
				System.err.println("Found something other than people in the list.");
			}
		}
		return owner;
	}
}

package common;

import common.handling.Handler;

/**
 * Interface defining a Master class, such as Server or Client. Note that it
 * extends runnable, meaning that server or client objects can be encapsulated
 * into their own thread if necessary.
 * @author etudiant
 * @see Handler
 */
public interface MasterClass extends Runnable {

}

package szabivan.loginfalk.data;

/**
 * A TruthVariable is an atomic formula, a predicate symbol of arity 0.
 * Each TruthVariable has a unique ID number and a unique name.
 * 
 * Each TruthVariable object is Immutable
 * TODO Serialization should be implemented to ensure  equal <=> ==.
 */
import java.util.HashMap;
import java.util.Map;

public class TruthVariable implements Formula, Comparable<TruthVariable> {
	/**
	 * Lookup tables for accessing the TruthVariable instances by either their
	 * ID or name.
	 */
	private static final Map<Integer, TruthVariable> lookupTableInt = new HashMap<Integer, TruthVariable>();
	private static final Map<String, TruthVariable> lookupTableString = new HashMap<String, TruthVariable>();

	/**
	 * The unique ID of the variable. Immutable.
	 */
	public final int varIndex;
	/**
	 * The unique name of the variable. Immutable.
	 */
	public final String varName;

	/**
	 * The constructor is private to ensure uniqueness of IDs and names.
	 * 
	 * @param index
	 *            The index of the newly constructed variable.
	 * @param name
	 *            The name of the newly constructed variable.
	 */
	private TruthVariable(int index, String name) {
		this.varIndex = index;
		this.varName = name;
	}

	/**
	 * Gets the TruthVariable having the given ID.
	 * 
	 * @param index
	 *            The ID of the requested TruthVariable.
	 * @return The TruthVariable having this ID if such one exists,
	 *         <code>null</code> otherwise.
	 */
	public static TruthVariable getVariable(int index) {
		return lookupTableInt.get(index);
	}

	/**
	 * Gets the {@link TruthVariable} having the given name.
	 * 
	 * @param name
	 *            The name of the requested {@link TruthVariable}.
	 * @return The {@link TruthVariable} having this name is such one exists,
	 *         <code>null</code> otherwise.
	 */
	public static TruthVariable getVariable(String name) {
		return lookupTableString.get(name);
	}

	/**
	 * Attempts to construct a {@link TruthVariable} having the given ID and
	 * name.
	 * 
	 * @param index
	 *            The ID of the newly constructed instance.
	 * @param name
	 *            The name of the newly constructed instance.
	 * @return <code>true</code> if neither the ID nor the name were used
	 *         before, in which case a new instance is constructed with the
	 *         specified parameters. Otherwise, returns <code>false</code> and
	 *         no new instance is constructed.
	 */
	public static boolean registerVariable(int index, String name) {
		if (lookupTableInt.containsKey(index))
			return false;
		if (lookupTableString.containsKey(name))
			return false;
		TruthVariable var = new TruthVariable(index, name);
		lookupTableInt.put(index, var);
		lookupTableString.put(name, var);
		return true;
	}

	@Override
	/**
	 * A truth variable is always atomic.
	 */
	public boolean isAtomic() {
		return true;
	}

	@Override
	/**
	 * Truth variables are compared according to their IDs.
	 */
	public int compareTo(TruthVariable rhs) {
		if (rhs == null)
			return 1;
		return rhs.varIndex - this.varIndex;
	}

	/**
	 * hashCode is the ID of the variable TODO after implementing serialization
	 * this method can be safely removed
	 */
	public int hashCode() {
		return varIndex;
	}

	/**
	 * Two TruthVariables are equal iff their ID is equal TODO after
	 * implementing serialization this method can be safely removed
	 */
	public boolean equals(TruthVariable rhs) {
		if (rhs == null)
			return false;
		return this.varIndex == rhs.varIndex;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TruthVariable)
			return equals((TruthVariable) obj);
		return false;
	}

	/**
	 * The string representation of a TruthVariable is its name.
	 */
	@Override
	public String toString() {
		return this.varName;
	}

	/**
	 * A TruthVariable is already normalized.
	 */
	@Override
	public Formula normalize() {
		return this;
	}

	/**
	 * Method that attempts to register the instances (0,"p"), (1,"q"), ...,
	 * (6,"v").
	 * 
	 * @see #registerVariable(int, String)
	 */
	public static void registerDefaults() {
		for (int i = 0; i < 7; i++)
			registerVariable(i, Character.toString((char) ('p' + i)));
	}
}

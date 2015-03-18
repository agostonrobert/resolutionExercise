package szabivan.loginfalk.data;

import java.util.BitSet;

/**
 * A Clause is a set of literals, i.e. possibly negated TruthVariables.
 * 
 * For efficiency, a clause is represented as a 32-bit integer. Bit 2*i is set
 * iff the TruthVariable having ID i occurs in the clause positively. Bit 2*i+1
 * is set iff the TruthVariable having ID i occurs in the clause negatively.
 * 
 * Thus, this representation ONLY WORKS IF THE IDs ARE WITHIN THE RANGE [0..15],
 * inclusive!
 * 
 * TODO change clause representation to a {@link BitSet}. It'll be slower,
 * however.
 * 
 * @author szabivan
 * @version 1.0
 */

public class ClausePrinter {
	public static final String EMPTY_CLAUSE_STRING = "☐";

	/**
	 * Gives a string representation of a clause given as an integer.
	 * 
	 * @param clause
	 *            The clause as an integer
	 * @param varCount
	 *            An upper bound for the IDs of the variables that can occur in
	 *            the clause.
	 * @return A human-readable string representation of the clause.
	 */
	public static String toString(int clause, int varCount) {
		// no bits set, return itty-bitty empty box
		if (clause == 0)
			return EMPTY_CLAUSE_STRING;
		// otherwise we are building a string
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		boolean hadElement = false;
		for (int var = 0; var < varCount; var++) {
			if ((clause & (1 << (var * 2))) != 0) {
				if (hadElement)
					builder.append(",");
				builder.append(" ");
				// TODO a direct ID -> name map would be handy at this point in
				// {@link TruthVariable}
				builder.append(TruthVariable.getVariable(var).toString());
				hadElement = true;
			}
			if ((clause & (1 << (var * 2 + 1))) != 0) {
				if (hadElement)
					builder.append(",");
				builder.append(" ");
				builder.append(UnaryConnective.NOT.toString());
				builder.append(TruthVariable.getVariable(var).toString());
				hadElement = true;
			}
		}
		builder.append(" }");
		return builder.toString();
	}
}

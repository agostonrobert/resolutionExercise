package szabivan.loginfalk.data;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

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
public class Clause {
//	private int storage;

	// MAGIC. do not touch.
	public static int resolve(int clause1, int clause2) {
		int vee = clause1 | clause2;
		int test = vee & (vee >> 1) & 0x1555555;
		if (test == 0)
			return -1;
		if ((test & (test - 1)) != 0)
			return -1;
		return vee & ~(test | (test << 1));
	}

	public static boolean isClash(int valuation) {
		// Rate on 1..10: what's your opinion wrt those magic constants? :P
		return ((valuation & (valuation >> 1) & 0x1555555) != 0);
	}

	public static boolean isTrivial(int clause) {
		return isClash(clause);
	}

	public static boolean isSubsetOf(int clause1, int clause2) {
		return ((clause1 & clause2) == clause1);
	}

	public static boolean isUnit(int clause) {
		return ((clause & (clause - 1)) == 0);
	}

	public static final String EMPTY_CLAUSE_STRING = "☐";

	public static Set<Integer> veeValuations(Set<Integer>[] valuations) {
		Set<Integer> ret = new HashSet<Integer>();
		for (Set<Integer> orig : valuations) {
			ret.addAll(orig);
		}
		return ret;
	}

	public static Set<Integer> wedgeValuations(Set<Integer> valuation1,
			Set<Integer> valuation2) {
		Set<Integer> ret = new HashSet<Integer>();
		for (int v1 : valuation1) {
			for (int v2 : valuation2) {
				if (!Clause.isClash(v1 | v2))
					ret.add(v1 | v2);
			}
		}
		return ret;
	}

	public static Set<Integer> wedgeValuations(Set<Integer>[] valuations) {
		if (valuations.length <= 1)
			return valuations[0];
		Set<Integer> ret = wedgeValuations(valuations[0], valuations[1]);
		for (int i = 2; i < valuations.length; i++) {
			ret = wedgeValuations(ret, valuations[i]);
		}
		return ret;
	}

	
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
				builder.append(Not.CONNECTIVE_STRING);
				builder.append(TruthVariable.getVariable(var).toString());
				hadElement = true;
			}
		}
		builder.append(" }");
		return builder.toString();
	}
}

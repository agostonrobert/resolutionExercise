package szabivan.loginfalk.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Resolver {

	public static class ClauseReason {
		final int clause;
		int fromClause1;
		int fromClause2;

		public ClauseReason(int clause) {
			this(clause, -1, -1);
		}

		public ClauseReason(int clause, int fromClause1, int fromClause2) {
			this.clause = clause;
			this.fromClause1 = fromClause1;
			this.fromClause2 = fromClause2;
		}
	}

	private Set<Integer> minimals = new HashSet<Integer>();
	private Map<Integer, ClauseReason> reasons = new HashMap<Integer, ClauseReason>();
	private Vector<Set<Integer>> lookupTable = new Vector<Set<Integer>>();
	private int bitCount;

	private boolean addClause(int clause) {
		Iterator<Integer> minimalIterator = minimals.iterator();
		while (minimalIterator.hasNext()) {
			int oldMinimal = minimalIterator.next();
			if (Clause.isSubsetOf(oldMinimal, clause))
				return false;
			if (Clause.isSubsetOf(clause, oldMinimal)) {
				minimalIterator.remove();
				for (int i = 0; i < bitCount; i++) {
					if ((oldMinimal & (1 << i)) != 0) {
						lookupTable.elementAt(i).remove(oldMinimal);
					}
				}
			}
		}
		minimals.add(clause);
		for (int i = 0; i < bitCount; i++) {
			if ((clause & (1 << i)) != 0) {
				lookupTable.elementAt(i).add(clause);
			}
		}
		return true;
	}

	public Map<Integer, ClauseReason> refute(Set<Integer> sigma, int varCount) {
		minimals.clear();
		reasons.clear();
		lookupTable.clear();
		this.bitCount = 2 * varCount;
		for (int i = 0; i < bitCount; i++) {
			lookupTable.add(new HashSet<Integer>());
		}
		for (int clause : sigma) {
			if (Clause.isTrivial(clause))
				continue;
			if (addClause(clause)) {
				reasons.put(clause, new ClauseReason(clause));
			}
		}
		while (!minimals.contains(0)) {
			Set<ClauseReason> resNew = new HashSet<ClauseReason>();
			for (int i = 0; i < bitCount; i += 2) {
				for (int clause1 : lookupTable.get(i)) {
					for (int clause2 : lookupTable.get(i + 1)) {
						int resolvent = Clause.resolve(clause1, clause2);
						if (resolvent == -1)
							continue;
						resNew.add(new ClauseReason(resolvent, clause1, clause2));
					}
				}
			}
			for (ClauseReason reason : resNew) {
				if (addClause(reason.clause)) {
					reasons.put(reason.clause, reason);
				}
			}
		}
		return reasons;
	}

	public Vector<ClauseReason> constructRefutationVector(
			Map<Integer, ClauseReason> allResolvents) {
		Vector<ClauseReason> ret = new Vector<ClauseReason>();
		Map<Integer, Integer> clauseIndexes = new HashMap<Integer, Integer>();
		constructRefutationVector(allResolvents, 0, clauseIndexes, ret);
		return ret;
	}

	private int constructRefutationVector(
			Map<Integer, ClauseReason> allResolvents, int clause,
			Map<Integer, Integer> clauseIndexes, Vector<ClauseReason> ret) {
		if (clauseIndexes.containsKey(clause)) {
			return clauseIndexes.get(clause);
		}
		ClauseReason reason = allResolvents.get(clause);
		if (reason.fromClause1 != -1) {
			reason.fromClause1 = constructRefutationVector(allResolvents,
					reason.fromClause1, clauseIndexes, ret);
			reason.fromClause2 = constructRefutationVector(allResolvents,
					reason.fromClause2, clauseIndexes, ret);
		}
		ret.add(reason);
		clauseIndexes.put(clause, ret.size());
		return ret.size();
	}
}

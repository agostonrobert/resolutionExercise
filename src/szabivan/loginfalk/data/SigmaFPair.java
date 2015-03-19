package szabivan.loginfalk.data;

/**
 * A Pair< Set<Formula>, Formula> class.
 * 
 * Why on earth does Java NOT have a Pair template?...
 * ...or does it?
 * 
 * @author szabivan
 * @version 1.0
 */

import java.util.HashSet;
import java.util.Set;

public class SigmaFPair {

	public final Sigma sigma;
	public final Formula f;

	public SigmaFPair(Sigma sigma, Formula f) {
		this.sigma = sigma;
		this.f = f;
	}

	public Set<Integer> createCnf() {
		Set<Integer> ret = new HashSet<Integer>();
		for (Formula form : sigma) {
			ret.addAll(CnfComputer.createCnf(form, true));
		}
		ret.addAll(CnfComputer.createCnf(f, false));
		return ret;
	}
}

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

import java.util.Set;

public class SigmaFPair {
	
	public final Set< Formula > sigma;
	public final Formula f;
	
	public SigmaFPair( Set<Formula> sigma, Formula f ){ this.sigma = sigma; this.f = f; }
}

package szabivan.loginfalk.data;

public class Test {
	public static void main(String[] args) {
		TruthVariable.registerDefaults();
		int varCount = 6;

		Formula test = new Implies(new Formula[] {
				new And(new Formula[] {
						TruthVariable.getVariable("q"),
						new Implies(new Formula[] {
								TruthVariable.getVariable("s"),
								TruthVariable.getVariable("r") }) }),
				new Not(new Formula[] { new And(new Formula[] {
						TruthVariable.getVariable("p"),
						TruthVariable.getVariable("q") }) }) });

		System.out.println(test);
		for (int clause : CnfComputer.createCnf(test, true)) {
			System.out.println(Clause.toString(clause, varCount));
		}

	}
}

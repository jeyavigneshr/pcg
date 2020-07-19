package mario.optimisation;

public interface IObjectiveFunction {

	double valueOf(double x[]);

	boolean isFeasible(double x[]);

}

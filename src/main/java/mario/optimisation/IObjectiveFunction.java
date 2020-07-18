package mario.optimisation;

public interface IObjectiveFunction {

    /** @param x  a point (candidate solution) in the pre-image of the objective function 
        @return  objective function value of the input search point  
     */
    double valueOf(double x[]);
    boolean isFeasible(double x[]);

}

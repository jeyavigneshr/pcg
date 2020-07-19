package resource.tools;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//import ch.idsia.tools.EvaluationInfo;
//import ch.idsia.tools.EvaluationOptions;
//import ch.idsia.tools.ToolsConfigurator;
//import ch.idsia.tools.evBasicFitnessComparator;
import level.simulator.BasicSimulator;
import level.simulator.Simulation;
import mario.communication.Server;
import mario.communication.ServerAgent;
import mario.engine.GlobalOptions;
import mario.environment.Environment;

public class Evaluator implements Runnable{
    Thread thisThread = null;
    EvaluationOptions evaluationOptions;

    private List<EvaluationInfo> evaluationSummary = new ArrayList<EvaluationInfo>();

    @SuppressWarnings("unchecked")
	private void evaluateServerMode()
    {
        Server server = new Server(evaluationOptions.getServerAgentPort(), Environment.numberOfObservationElements, Environment.numberOfButtons);
        evaluationOptions.setAgent(new ServerAgent(server, evaluationOptions.isFastTCP()));

        Simulation simulator = new BasicSimulator(evaluationOptions.getSimulationOptionsCopy());
        while (server.isRunning())
        {
            String resetData = server.recvUnSafe();
            if (resetData.startsWith("ciao"))
            {
                System.out.println("Evaluator: ciao received from client; restarting server");
                server.restartServer();
                continue;
            }
            if (resetData.startsWith("reset"))
            {
                resetData = resetData.split("reset\\s*")[1];
                evaluationOptions.setUpOptions(resetData.split("[\\s]+"));
                ((ServerAgent)evaluationOptions.getAgent()).setFastTCP(evaluationOptions.isFastTCP());
                init(evaluationOptions);
                // Simulate One Level
                EvaluationInfo evaluationInfo;

                simulator.setSimulationOptions(evaluationOptions);
                evaluationInfo = simulator.simulateOneLevel();

                evaluationInfo.levelType = evaluationOptions.getLevelType();
                evaluationInfo.levelDifficulty = evaluationOptions.getLevelDifficulty();
                evaluationInfo.levelRandSeed = evaluationOptions.getLevelRandSeed();
                evaluationSummary.add(evaluationInfo);
                if (!this.evaluationOptions.getMatlabFileName().equals(""))
                Collections.sort(evaluationSummary, new evBasicFitnessComparator());
            }
            else
            {
                System.err.println("Evaluator: Message <" + resetData + "> is incorrect client behavior. Exiting evaluation...");
                server.restartServer();
            }
        }
    }

    @SuppressWarnings("unchecked")
	public List<EvaluationInfo> evaluate()
    {
        if (this.evaluationOptions.isServerMode() )
        {
            this.evaluateServerMode();
            return null;
        }


        Simulation simulator = new BasicSimulator(evaluationOptions.getSimulationOptionsCopy());
        EvaluationInfo evaluationInfo;

            evaluationInfo = simulator.simulateOneLevel();
            evaluationInfo.levelType = evaluationOptions.getLevelType();
            evaluationInfo.levelDifficulty = evaluationOptions.getLevelDifficulty();
            evaluationInfo.levelRandSeed = evaluationOptions.getLevelRandSeed();
            evaluationSummary.add(evaluationInfo);
        if (!this.evaluationOptions.getMatlabFileName().equals(""))
        Collections.sort(evaluationSummary, new evBasicFitnessComparator());
        return evaluationSummary;
    }


    public void getMeanEvaluationSummary()
    {
    }

    public String exportToMatLabFile()
    {
        FileOutputStream fos;
        String fileName = this.evaluationOptions.getMatlabFileName() + ".m";
        try {

            fos = new FileOutputStream(fileName);              
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.newLine();
            bw.write("%% " + this.evaluationOptions.getAgent().getName());
            bw.newLine();
            bw.write("% BasicFitness ");            
            bw.newLine();
            bw.write("Attempts = [1:" + evaluationSummary.size() + "];");
            bw.newLine();
            bw.write("% BasicFitness ");
            bw.newLine();
            bw.write("BasicFitness = [");
            for (EvaluationInfo ev : evaluationSummary)
                bw.write(String.valueOf(ev.computeBasicFitness()) + " ");
            bw.write("];");
            bw.newLine();
            bw.write("plot(Attempts,BasicFitness, '.')");
            bw.close();
            return fileName;
        }
        catch (FileNotFoundException e)  {  e.printStackTrace(); return "Null" ;       }
        catch (IOException e) {     e.printStackTrace();  return "Null";      }
    }

    public void exportToPyPlot(String fileName)
    {
        //TODO:SK
    }

    public void reset()
    {
        evaluationSummary = new ArrayList<EvaluationInfo>();
    }

    public Evaluator(EvaluationOptions evaluationOptions)
    {                      
        init(evaluationOptions);
    }

    public void run()
    {
        evaluate();
    }

    public void start()
    {
        if (thisThread.getState() == Thread.State.NEW)
            thisThread.start();
    }

    public void init(EvaluationOptions evaluationOptions)
    {
        ToolsConfigurator.CreateMarioComponentFrame(
                evaluationOptions);
        
        GlobalOptions.pauseWorld = evaluationOptions.isPauseWorld();
        this.evaluationOptions = evaluationOptions;
        if (thisThread == null)
            thisThread = new Thread(this);
    }
}

class evCoinsFitnessComparator implements Comparator
{
    public int compare(Object o, Object o1)
    {
        int ei1Fitness = ((EvaluationInfo)(o)).numberOfGainedCoins;

        int ei2Fitness = ((EvaluationInfo)(o1)).numberOfGainedCoins;
        if (ei1Fitness < ei2Fitness)
            return 1;
        else if (ei1Fitness > ei2Fitness)
            return -1;
        else
            return 0;
    }
}

class evDistanceFitnessComparator implements Comparator
{
    public int compare(Object o, Object o1)
    {
        double ei1Fitness = ((EvaluationInfo)(o)).computeDistancePassed();
        double ei2Fitness = ((EvaluationInfo)(o1)).computeDistancePassed();
        if (ei1Fitness < ei2Fitness)
            return 1;
        else if (ei1Fitness > ei2Fitness)
            return -1;
        else
            return 0;
    }
}

class evBasicFitnessComparator implements Comparator
{
    public int compare(Object o, Object o1)
    {
        double ei1Fitness = ((EvaluationInfo)(o)).computeBasicFitness();
        double ei2Fitness = ((EvaluationInfo)(o1)).computeBasicFitness();
        if (ei1Fitness < ei2Fitness)
            return 1;
        else if (ei1Fitness > ei2Fitness)
            return -1;
        else
            return 0;
    }
}

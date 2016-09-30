/**
 * Created by MuhammadGumilang on 23/09/2016.
 */
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static ModelAndView prepareMV(Graph oldGraph, Graph newGraph, int iteration) {
        Map<String, Object> model = new HashMap<>();
        Gson gson = new Gson();
        String oldJson = gson.toJson(oldGraph);
        String newJson = gson.toJson(newGraph);

        model.put("oldData", oldJson);
        model.put("data", newJson);
        model.put("iteration", iteration);
        model.put("conflicts", newGraph.getConflicts());
        return new ModelAndView(model, "index.vm");
    }

    public static ModelAndView prepareHC(Graph initGraph) {
        HillClimbing hc = new HillClimbing(initGraph);
        hc.run();
        Graph hcGraph = hc.getGraph();

        return prepareMV(initGraph, hcGraph, hc.getIteration());
    }

    public static ModelAndView prepareSA(Graph initGraph) {
        SimulatedAnnealing sa = new SimulatedAnnealing(initGraph, 10000, 0.97);
        sa.run();
        Graph saGraph = sa.getGraph();

        return prepareMV(initGraph, saGraph, sa.getIteration());
    }

    public static ModelAndView prepareGA(Graph initGraph) {
        DNA dna = new DNA(50, true);
        GeneticAlgorithm ga = new GeneticAlgorithm(true, false);

        int generationCount = 0;
        while (dna.getFittestChromosome().getFitness() < dna.getFittestChromosome().getMaxFitness() && generationCount <= 10) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + dna.getFittestChromosome().getFitness());
            dna = ga.evolve(dna);
        }
        generationCount++;

        return prepareMV(initGraph, dna.getFittestChromosome().getGraph(), generationCount);
    }

    public static ModelAndView prepareInit(Graph initGraph) {
        return prepareMV(initGraph, initGraph, 0);
    }

    public static void main(String[] args) {
        Spark.staticFileLocation("/public");
        Graph initGraph = new Graph("Testcase.txt");
        initGraph.randomInitialize();

        get("/", (req, res) -> {
            return prepareInit(initGraph);
        }, new VelocityTemplateEngine());

        get("/HC", (req, res) -> {
            return prepareHC(initGraph);
        }, new VelocityTemplateEngine());

        get("/SA", (req, res) -> {
            return prepareSA(initGraph);
        }, new VelocityTemplateEngine());

        get("/GA", (req, res) -> {
            return prepareGA(initGraph);
        }, new VelocityTemplateEngine());
    }
}

package network;
import network.ExecuteNetwork;

import java.io.Serializable;
import java.util.Scanner;

public class Guess extends ExecuteNetwork implements Serializable {
    private static final long serialVersionUID = 7L;
    private int numGuessed = -1;
    double[][] finalWeights;

    public Guess (double[][] finalWeights) {
        this.finalWeights = finalWeights;
    }

    public void start() {
        redeNeural[0] = new double[numOfInputs];
        preencheInputNeuron (redeNeural[0]);
        novaCamada(finalWeights, redeNeural[0], new double[15]);
        this.numGuessed = chosenNeuron(redeNeural[1]);
    }

    public void test(double[] input) {
        redeNeural[0] = new double[numOfInputs];
        System.arraycopy(input, 0, redeNeural[0], 0, 15);
        novaCamada(finalWeights, redeNeural[0], new double[15]);
        this.numGuessed = chosenNeuron(redeNeural[1]);
    }
    /*
     *	Recebe o uma camada de neuronios e preenche ela
     *	de acordo com inputs a serem digitados pelo
     *	usuario.
     *
     *	Entradas:
     *		- inputNeurons : Um arranjo de inteiros que
     *		representa uma camada de neuronios.
     *	Saidas:
     *		nao ha.
     */
    void preencheInputNeuron (double[] inputNeurons){
        Scanner scan = new Scanner(System.in);
        System.out.println("Input grid: ");
        String linha = "";

        for (int i = 0; i < 5; i++) {
            linha += scan.nextLine();
        }
        for (int v = 0; v < linha.length(); v++){
            inputNeurons[v] = (linha.charAt(v) == 'X')?
                    1 : 0;
        }

    }

    public int getNumGuessed(){
        return numGuessed;
    }
    public double[][] getFinalWeights() {
        return finalWeights;
    }

}

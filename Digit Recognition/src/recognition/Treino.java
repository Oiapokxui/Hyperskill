package recognition;

import java.util.Random;

public class Treino extends ExecuteNetwork {

    // *************************** CONSTRUTORES ******************************
    public Treino (int totalGens) {
        this.totalGens = totalGens;
        // Cada geracao possui 10 conjuntos de
        // 15 pesos, cada conjunto para um output
        // especifico.
        pesos = new double[totalGens][numOfOutputs][numOfInputs];
        treinaGen(1);
    }

    // ********************** ATRIBUTOS DA CLASSE ****************************
    // Coeficiente de Taxa de Aprendizado, utilizado para calcular novos pesos.
    static final double coefTxAprend = 0.5;

    // Qual digito esta sendo treinado no momento.
    // Digito = (training + 1)%10
    // Isso ocorre pois o problema exige que os outputs correspondam à ordem
    // 1,2,3,4,5,6,7,8,9,0 de acordo com o indice
    static int training = 0;

    // ********************** ATRIBUTOS DO OBJETO ****************************
    private final int totalGens;
    private final double[][][] pesos;
    // Cada linha `i` dessa matriz representa os 15
    // inputs ideais do digito (i + 1)%10
    // Como sao 10 digitos, idealNeurons possui 10 linhas
    // Como sao 15 pixels de input, idealNeurons possui
    // 15 colunas.
    final double[][] idealNeurons =
            {
                    {	// Um
                            0,1,0,
                            0,1,0,
                            0,1,0,
                            0,1,0,
                            0,1,0},
                    {	// Dois
                            1,1,1,
                            0,0,1,
                            1,1,1,
                            1,0,0,
                            1,1,1},
                    {	// Tres
                            1,1,1,
                            0,0,1,
                            1,1,1,
                            0,0,1,
                            1,1,1},
                    {	// Quatro
                            1,0,1,
                            1,0,1,
                            1,1,1,
                            0,0,1,
                            0,0,1},
                    { 	// Cinco
                            1,1,1,
                            1,0,0,
                            1,1,1,
                            0,0,1,
                            1,1,1},
                    {	// Seis
                            1,1,1,
                            1,0,0,
                            1,1,1,
                            1,0,1,
                            1,1,1},
                    {	// Sete
                            1,1,1,
                            0,0,1,
                            0,0,1,
                            0,0,1,
                            0,0,1},
                    {	// Oito
                            1,1,1,
                            1,0,1,
                            1,1,1,
                            1,0,1,
                            1,1,1},
                    { 	// Nove
                            1,1,1,
                            1,0,1,
                            1,1,1,
                            0,0,1,
                            1,1,1},
                    {	// Zero
                            1,1,1,
                            1,0,1,
                            1,0,1,
                            1,0,1,
                            1,1,1}
            } ;


    private void treinaGen (int numGen){
        numGen-- ;

        if (numGen == 1) atribuiPesos(pesos[numGen]) ;
        int proxGen = numGen + 1;
        if (proxGen == this.totalGens) return;
        double[] bias = new double[15];

        for (int i = 0; i < 10; i++) {
            training = i;
            novaCamada(pesos[numGen], idealNeurons[i], bias);
            System.arraycopy(pesos[numGen][i], 0, pesos[proxGen][i], 0, pesos[0][0].length);
            newWeights(pesos[proxGen][i]);
        }
        treinaGen(numGen + 2);
    }

    private void newWeights(double[] pesos){
        for (int i = 0; i < pesos.length; i++){
            pesos[i] += meanDeltaWeight(i);
        }
    }

    /*
     *	Calcula a media de todos os deltaWeight possiveis
     * 	para um determinado neuronio de entrada.
     *
     * 	Entrada:
     * 		- neuronIndex : Inteiro que diz o indice do neu-
     * 		ronio de entrada.
     * 	Saida:
     * 		- A media aritmetica de todos os deltaWeights.
     */
    private double meanDeltaWeight(int neuronIndex){
        double sumDeltaWeight = 0;
        double refNeuron = idealNeurons[inputIndex][neuronIndex];
        for (int i = 0; i < redeNeural[outputIndex].length; i++) {
            sumDeltaWeight += deltaWeight(refNeuron, i);
        }
        return sumDeltaWeight / numOfOutputs;
    }

    /*
     *	Sinceramente, eu nao entendi muito bem o porque dessa expressao,
     *	mas sei que ela eh necessaria para se obter novos pesos que vao
     * 	identificar um dos digitos na nova geracao
     * 	Entrada:
     * 		- refNeuron : double que representa um dos neuronios do input,
     * 		o que recebera variacao em seu peso (o que identifica um
     *		dos digitos).
     * 		- nextLayerNeuronIndex : inteiro que diz qual dos output estamos
     * 		levando em consideracao.
     * 	Saida:
     * 		- o valor double da Variacao de Peso do neuronio refNeuron em
     * 		relacao ao output nextLayerNeuronIndex;
     *
     */
    private double deltaWeight(double refNeuron, int nextLayerNeuronIndex) {
        int numToTrain = training;
        int idealOutput = (nextLayerNeuronIndex == numToTrain) ? 1 : 0;
        double actualOutput = redeNeural[1][nextLayerNeuronIndex];
        return coefTxAprend * refNeuron * (idealOutput - actualOutput);
    }

    /*
     *	Atribui valores aleatorios aos pesos iniciais
     *	de inputNeuron. Os valores sao fornecidos atraves
     *	da classe Random e do metodo nextGaussian();
     */
    private static void atribuiPesos(double[][] pesos){
        Random rand = new Random();
        for (int i = 0; i < pesos.length ; i++){
            for (int v = 0; v < pesos[0].length; v++){
                pesos[i][v] = rand.nextGaussian();
            }
        }
    }

    /*
     *  Getter dos pesos da ultima geracao treinada.
     */
    public double[][] getPesos(){
        return pesos[totalGens - 1];
    }
}

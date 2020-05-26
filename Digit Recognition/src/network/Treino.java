package network;
import java.util.Random;
/*
 *  Instruções do que fazer:
 * -1º: Criar um arranjo para os Delta Pesos
 *      15x10, 10 Delta pesos para cada peso.
 *  0º: Criar arranjo que recebe a soma de todos
 *      os Delta Pesos de todos os dígitos. 15x10
 *  1º: Calcular pesos aleatórios.
 *  2º: Para cada digito ideal:
     *  2.1º: Rodar a rede com o digito de input
     *        e os pesos aleatorios.
     *  2.2º: Guardar o output em redeNeural
     *  2.3º: Calcular o delta peso de cada
     *        input de entrada em relacao ao
     *        ideal do digito (1) e a saida atual
     *        do neuronio que identifica o digito;
     *        (Serão 150 Delta Pesos. 10 para cada
     *         input.)
     * 2.4º:  Adicionar cada Delta Peso ao arranjo
     *        da somatoria. EXEMPLO:
     *        deltaWeight[i][k] -> soma[i][k] += delltaWeight[i]k];
 *  3º: Para cada peso original aleatorio (indice i):
     *  3.1º: Dividir por 10 a soma do Delta Peso em
     *        relacao ao peso e ao digito.
     *  3.2º: Adicionar essa média ao peso.
     *  3.3º: Salvar o peso no arranjo de pesos.
 *  4º: Se todas as médias anteriores forem menor que um
 *      determinado peso,
 */
public class Treino extends ExecuteNetwork {

    // *************************** CONSTRUTORES ******************************
    public Treino () {
        // Cada geracao possui 10 conjuntos de
        // 15 pesos, cada conjunto para um output
        // especifico.
        pesos = new double[numOfOutputs][numOfInputs];
        // Soma a si mesmo deltaWeights apos o treinamento de cada
        // digito.
        avgDeltaWeights = new double[numOfOutputs][numOfInputs];
        executeTraining();
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
    private int totalGens = 0;
    // pesos[i] guarda os 150 pesos daquela geracao
    // pesos[i][j] guarda os 15 pesos relativos ao digito
    // (j + 1)%10
    private final double[][] pesos;
    private double[][] avgDeltaWeights;
    private final double  error = 1E-7;
    private boolean areAllSmallEnough = false;
    // Cada linha `i` dessa matriz representa os 15
    // inputs ideais do digito (i + 1)%10
    // Como sao 10 digitos, idealNeurons possui 10 linhas
    // Como sao 15 pixels de input, idealNeurons possui
    // 15 colunas.
    public final double[][] idealNeurons =
            {
                    {	// Um
                            0,1,0, 0,1,0, 0,1,0, 0,1,0, 0,1,0},
                    {	// Dois
                            1,1,1, 0,0,1, 1,1,1, 1,0,0, 1,1,1},
                    {	// Tres
                            1,1,1, 0,0,1, 0,1,1, 0,0,1, 1,1,1},
                    {	// Quatro
                            1,0,1, 1,0,1, 1,1,1, 0,0,1, 0,0,1},
                    { 	// Cinco
                            1,1,1, 1,0,0, 1,1,1, 0,0,1, 1,1,1},
                    {	// Seis
                            1,1,1, 1,0,0, 1,1,1, 1,0,1, 1,1,1},
                    {	// Sete
                            1,1,1, 0,0,1, 0,0,1, 0,0,1, 0,0,1},
                    {	// Oito
                            1,1,1, 1,0,1, 1,1,1, 1,0,1, 1,1,1},
                    { 	// Nove
                            1,1,1, 1,0,1, 1,1,1, 0,0,1, 1,1,1},
                    {	// Zero
                            1,1,1, 1,0,1, 1,0,1, 1,0,1, 1,1,1}
            } ;

    private void executeTraining(){
        while (!areAllSmallEnough){
            totalGens++;
            treinaGen();
            // if (totalGens % 100 == 0) System.out.println("Geracao: " + totalGens);
            // if (totalGens % 1000 == 0) System.out.println("avgDW: " + avgDeltaWeights[0][0]);
            avgDeltaWeights = new double[numOfOutputs][numOfInputs];

        }
    }
    private void treinaGen (){
        if (totalGens == 1) atribuiPesos(pesos);

        double[] bias = new double[15];
        // Para cada numero a ser treinado,
        // calular

        for (int i = 0; i < 10; i++) {
            training = i;
            novaCamada(pesos, idealNeurons[i], bias);
            obtainDeltaWeights();
        }
        takeAvg(avgDeltaWeights, numOfOutputs);
        updateWeights();
    }

    /*
     *  Recebe 15 pesos de uma camada relativos a um digito
     *  e os atualizam com valores delta
     */
    /*
    private void newWeights(double[] pesos){
        for (int i = 0; i < pesos.length; i++){
            double refNeuron = idealNeurons[training][i];
            double deltaWeight = deltaWeight(refNeuron, training);
            if (Math.abs(deltaWeight - lastDeltaWeight[i]) < error) {
                isSmallEnough[i] = true;
            }
            pesos[i] += deltaWeight;
            lastDeltaWeight[i] = deltaWeight;
        }
    }
    */

    /*
    private boolean areAllSmallEnough () {
        boolean condition = true;
        for (boolean b : isSmallEnough) {
            condition = b;
            if (!condition) break;
        }
        if (!condition) System.out.println("not small enough");
        if (condition) System.out.println("small enough");
        return condition;
    }
    */

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

    /*
     *	Aqui é aplicada a Regra Delta, ou Gradient Descent
     *  Através da regra podemos obter variacoes aos pesos
     *  existentes e atingir um minimo local da funcao que
     *  permite achar pesos que funcionem para a tarefa.
     *  A variacao dW é obtida pela seguinte expressao:
     *      dW = k * Ai * (Jn - On)
     *  Onde:
     *      k : Taxa de aprendizado, quanto maior, menos
     *          geracoes serao precisas para alcancar
     *          o objetivo, porem mais facil ultrapassar
     *          um minimo local.
     *      Ai : Um dos inputs da rede
     *      Jn : O valor ideal do neuronios de saida em
     *      redeNeural[inputIndex][out]
     *      On : O valor obtido desse neuronios de saida
     * 	Entrada:
     * 		- refNeuron : double que representa um dos neuronios do input,
     * 		o que recebera variacao em seu peso (o que identifica o digito
     *      em treinamento).
     * 		- out : inteiro que diz qual dos output estamos
     * 		levando em consideracao.
     * 	Saida:
     * 		- o valor double da Variacao de Peso do neuronio refNeuron em
     * 		relacao ao output Out;
     *
     */
    private double deltaWeight(double refNeuron, int out){
        double deltaWeight = 0;
        double idealOutput, actualOutput;
        double cons = coefTxAprend * refNeuron;
        idealOutput = ( out == training)? 1 : 0;
        actualOutput = redeNeural[outputIndex][out];
        deltaWeight += cons * (idealOutput - actualOutput);

        return deltaWeight;
    }

    private void takeAvg(double[][] avgDeltaWeights, int numOfData) {
        for (double[] line: avgDeltaWeights) {
            for (double element : line) {
                areAllSmallEnough = false;
                element /= numOfData;
                if (Math.abs(element) < error) areAllSmallEnough = true;
            }

        }
    }

    private void obtainDeltaWeights() {
        int numToTrain = training;
        double refNeuron;
        // out stands for output index.
        // i.e., for every 15 weights related to an output neuron
        for (int out = 0; out < numOfOutputs; out++) {
            // for every single weight in that group
            for (int in = 0; in < numOfInputs; in++) {
                refNeuron = idealNeurons[numToTrain][in];
                // dW is one of the 10 delta Weights to
                // be applied on each weight
                avgDeltaWeights[out][in] += deltaWeight(refNeuron, out);
            }
        }
    }

    private void updateWeights() {
        for (int linha  = 0; linha < numOfOutputs; linha++) {
            for (int coluna  = 0; coluna < numOfOutputs; coluna++) {
                pesos[linha][coluna] += avgDeltaWeights[linha][coluna];
            }
        }
    }


    /*
     *	Atribui valores aleatorios aos pesos iniciais
     *	de inputNeuron. Os valores sao fornecidos atraves
     *	da classe Random e do metodo nextGaussian();
     */

    private static void atribuiPesos(double[][] pesos){
        Random rand = new Random();
        for (int i = 0; i < pesos.length ; i++) {
            for (int v = 0; v < pesos[0].length; v++) {
                pesos[i][v] = rand.nextGaussian();
            }
        }
    }

    /*
     *  Getter dos pesos da ultima geracao treinada.
     */
    public double[][] getPesos(){
        return pesos;
    }

    public int getTotalGens() {
        return totalGens;
    }

    public double[] getInput(int num) {
        return idealNeurons[num];
    }
}

package recognition;
import java.util.Scanner;
import java.util.Random;

/*
 *				RECOGNITION.
 *	Projeto do curso Hyperskill.
 *	Visa projetar uma rede neural do tipo perceptron
 *	a fim de identificar digitos em uma grid 3x3
 *
 *	São recebidas, por input do usuario, 5 linhas que
 *	contem alguma permutacao de 3 'X' ou '_', essas 5
 *	linhas, representam o desenh de um digito,
 *	onde o carac. 'X' representaria um pixel
 *	aceso e o caract '_' um pixel apagado.
 *
 *	Apos o processamento, espera-se que a rede saiba identificar
 *	qual o digito desenhado, imprimindo no terminal o inteiro que
 *	corresponde ao dígito.
 *
 */

class ExecuteNetwork {

	double[][] redeNeural = new double[numCamadas][];
	boolean ehTreino;

	static int numCamadas = 2;
	static final int inputIndex = 0;
	static final int outputIndex = 1;
	static final int numOfOutputs = 10;
	static final int numOfInputs = 15;

	/*
	 *	Realiza operacoes necessarias para se obter uma
	 *	nova camada de neuronios, a partir de uma matriz
	 *	de pesos, do vetor da camada anterior de neuronios
	 *	e do vetor dos pesos.
	 *
	 *	Entrada:
	 *		- pesos : Matriz que representa todos os pesos
	 *		para o calculo.
	 *		- neuron : Arranjo que representa a camada anterior
	 *		de neuronios a ser transformada.
	 *		- bias : Arranjo que armazena o valor de todos os vies
	 *		necessarios.
	 *	Saida:
	 *		- resultante : Uma nova camada de neuronios.
	 */
	void novaCamada (double[][] pesos, double[] neuron, double[] bias) {
		// MULTIPLICAO ENTRE PESOS E NEURON
		double[] resultante = new double[pesos.length];

		// PRECISA ADICIONAR BLOCO TRY...CATCH PARA EVITAR ARRAY INDEX
		// OUT OF BOUNDS AQUI -> (pesos[0].length != neuron.length)
		try {
			int linhasR = pesos.length;
			int colunasR = 1;
			int colunasM1 = pesos[0].length;
			for (int i = 0; i < linhasR ; i++){
				for (int v = 0; v < colunasR ; v++){
					resultante[i] = 0;
					for(int w = 0; w < colunasM1; w++){
						resultante[i] += pesos[i][w] * neuron[w];
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}

		// ADICAO DOS VIESES.
		somaNeuronioPeso (resultante, bias);

		// APLICACAO DA FUNCAO SIGMOIDE EM CADA ELEMENTO DA NOVA CAMADA.
		for (int i = 0; i < resultante.length; i++) {
			resultante[i] = sigmoid(resultante[i]);
		}
		if (!ehTreino) redeNeural[1] = resultante ;
	}

	/*
	 *	Adiciona os vies a uma camada existente de neuronios
	 *
	 *	Entrada:
	 *		- vetor : Arranjo que representa a
	 *		camada de neuronio existente
	 *		- bias : O arranjo de vieses.
	 *	Saida:
	 *		nao ha.
	 */
	static void somaNeuronioPeso (double[] vetor, double[] bias) {
		for (int i = 0; i < vetor.length; i++){
			vetor[i] += bias[i];
		}
	}

	/*
	 *	Dentro da camada, escolhe o neuronio de valor maximo
	 *
	 *	Entrada:
	 *		- neuron : Arranjo de inteiros que representa a
	 *		camada de neuronios.
	 *	Saida
	 *		nao ha.
	 */
	static int chosenNeuron (double[] neuron) {
		int maxInd = 0;
		for (int i = 0; i < neuron.length ; i++){
			if (neuron[maxInd] < neuron[i]) maxInd = i;
		}
		return (maxInd + 1)% 10;
	}

	/*
	 * 	Mapeia o valor de um neuronio no intervalo fechado
	 * 	de 0 a 1 atraves da funcao Sigmoide (Curva logistica).
	 *
	 *	Entrada:
	 *		- neuronValue : O valor onde a funcao sera
	 *		calculada
	 *	Saida:
	 *		- 1 / (1 + exp) : A funcao no ponto neuronValue.
	 */
	static double sigmoid(double neuronValue){
		double exp = Math.exp(neuronValue * -1);
		return 1 / (1 + exp);
	}
}
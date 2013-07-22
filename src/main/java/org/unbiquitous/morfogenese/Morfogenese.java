package org.unbiquitous.morfogenese;

//MORFOGÊNESE
//obra de arte computacional - 2013 - versão 192
//Tiago Barros Pontes e Silva
//tiagobarros@unb.br

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import processing.core.PApplet;
import processing.video.Capture;

public class Morfogenese extends PApplet {

	float escala = 1; // escala da cena [VS GLOBAIS]
	float vezesescala = 1; // para multiplicar a escala e deixar menos espaço
							// com poucos
	boolean escalaauto = true; // para utilizar a escala automática

	int NUMERODEBICHOS = 30; // cria V do número inicial de bichos randômicos
	int maximobichos = 300; // número máximo de bichos permitidos. Não há
							// nascimento acima desse número

	boolean comopengl = false; // para ligar e desligar o opengl mais fácil
	int minimoportipo = 2; // número mínimo de bichos por tipo (forma) para que
							// um novo semelhante seja gerado
	boolean modoexterminio = true; // para ligar e desligar a possibilidade de
									// exteminar uma forma
	int commetadados = 0; // para mostrar os números dos metadados dos bichos
	int reagemouse = 1; // para reagir ao mouse (possui 3 tempos)

	int quadrosporsegundo = 30; // taxa de quadros por segundo

	float chancemutacao = 0.01f;

	boolean som = true; // liga e desliga o som
	float volume = 100; // volume geral do som dos bichos
	float duracao = 0.5f; // duração da nota

	final static int NORTH = 1; // cria um WASD para mover apenas 1 bicho para
								// testar
	final static int EAST = 2;
	final static int SOUTH = 4;
	final static int WEST = 8;
	int result; // WASD
	int meuindexatual; // para controlar o index do bicho

	Instrument[] instruments = null; // para iniciar a array de instrumento,
										// sintetizador e canais midi
	Synthesizer synth = null;
	MidiChannel[] channels = null;

	Capture cam;
	Capture cam2;
	boolean camerasfuncionando;
	boolean procurapelacor;
	boolean pegacor;
	float niveldeprecisaodacor = 100;

	boolean ligasaida = false; // para ligar e desligar saída de Vs (economizar
								// processamento)
	PrintWriter saida; // para fazer os gráficos das Vs

	ArrayList<bicho> bichos = new ArrayList<bicho>(); // crio a arraylist
														// bichos. É uma array
														// de objetos. Bicho é a
														// classe
	int[] meudna = new int[24];

	// float[] escalanotas = {57, 60, 60, 60, 62, 64, 67, 67, 69, 72, 72, 72,
	// 74, 76, 79}; //escala para o som ficar um pouco mais harmônico. Prefiro o
	// som orgânico de engarrafamento

	int filanascimento = 0; // para enviar o comando de que um deve nascer

	int mudadia; // para ligar e desligar a mudança dia e noite
	float corfundo = 0; // cor do background inicial
	int sentidodia = 2; // para inverter o sentido da cor do fundo
	float corfundor; // cor do background inicial
	float corfundog; // cor do background inicial
	float corfundob; // cor do background inicial
	float tempcorfundor; // cor do background inicial
	float tempcorfundog; // cor do background inicial
	float tempcorfundob; // cor do background inicial
	float posicaofinalmouseX;
	float posicaofinalmouseY;

	Iterator bichoIt; // cria um iterator para não ter q ficar fazendo i++. Para
						// isso uso o tempbicho

	bicho tempbicho; // cria bichotemps para gerar os bichos depois e agir em
						// cada bicho sem o i++ com o iterator
	bicho meubicho; // para criar um bicho novo
	bicho bichoculpado; // para ser um dos pais do bicho novo
	bicho bichaculpada; // para ser um dos pais do bicho novo
	bicho eubicho;

	int nr; // estatística: número de retângulos
	int nt; // estatística: número de triângulos
	int ne; // estatística: número de elipses
	int total; // estatística: número total de bichos

	float adaptacaomedia;

	class bicho { // classe bicho usada lá na array: cria Vs [CLASSE] [BICHO:
					// CADA INDIVÍDUO!!!]

		float positionx; // posição X para alocar o bicho
		float positiony; // posição Y para alocar o bicho
		float velocidadeauto; // velocidadeauto (treme treme) É fixa depois de
								// gerada, só muda em situações específicas
		float easing; // easing (em cascata: easing+o*o*easingaceleration)
		float easingaceleration; // easingaceleration
		float tamanho; // tamanho (retângulo possível)
		int numerodepontos; // número de pontos do bicho. O index é 0. Com todos
							// os pontos separados o menor número é 5
		float pesodalinha; // peso da linha do bicho
		float corr; // cores linha R
		float corg; // cores linha G
		float corb; // cores linha B
		float corlinhar; // cores corlinha R
		float corlinhag; // cores corlinha G
		float corlinhab; // cores corlinha B
		float formadiam; // diâmetro de cada forma (todas elas) a partir do diam
		int forma1; // forma da cabeça
		int forma2; // forma do pescoço
		int formarabo; // forma do rabo
		int instrumento; // para definir o instrumento de cada som
		int nota; // define a nota que o bicho vai reproduzir
		float energia; // equivale à quantidade de vida inicial
		float pontomadurocruza; // define um ponto de maturidade para que possam
								// cruzar novamente
		int chance; // pode fazer ou não algo: surtar, caçar, fugir (1:chapado,
					// 2:surta, 3:berserker, 4:medroso, 5:violento, 6:esperto /
					// tarado>=3 / ataca com bando >=5)

		int cor;
		int corlinha;
		float coralpha; // alpha da linha associado à vida
		float corlinhaalpha; // alpha da corlinha associado à vida
		float velocidade; // velocidade WASD
		float novachance; // contador para alterar a probabilidade da chance
		float evoluichance; // para mudar de personalidade conforme ganha
							// experiência
		float maturidade; // tempo para poder cruzar de novo
		float angulo1; // para rodar as formas geométricas da cabeça
		float angulo2; // para rodar as formas geométricas do pescoço
		float angulorabo; // para rodar as formas geométricas do rabo
		boolean vida; // vivo ou morto
		boolean podre; // morto e podre
		float velocidadeautooriginal; // para que ele tenda a retoranar a essa
										// velocidade
		int cruza; // para poder ter 2 fazes no cruzamento
		float bando; // V que determina se a energia coletiva é maior que a do
						// inimigo
		float finalbando; // V que determina se a energia coletiva é maior que a
							// do inimigo
		float tamanhoformadiam; // para que eles decidam a área de interação e
								// aprendam com os próprios erros
		int sova; // V para o bicho perceber que está apanhando
		float distpontox1mouse; // para reagir ao mouse
		float distpontoy1mouse; // para reagir ao mouse
		int contagemmorte; // para contar os mortos
		int bancodadosinstrumento; // para definidr o banco de dados do
									// instrumento
		int geracao; // para saber de qual geração é o bicho
		float atracao; // para alterar automaticamente o fator de atração
		float minhaadaptacao;
		float maxformadiam;
		int numerodepontosdalinha;
		float anguloAB;

		float[] pontox; // cria a lista de pontos x e y
		float[] pontoy;

		float[] distx1; // cria a lista de distâncias entre os pontos de cada
						// bicho
		float[] disty1;

		int[] ordemxy;

		float ponto1xdir; // influencia o ponto1 do bicho (por enquanto uso no
							// wandering da novachance)
		float ponto1ydir;

		float ponto1xdircor; // influencia o ponto1 do bicho: busca cor
		float ponto1ydircor;

		float ponto1xdirmouse; // influencia o ponto1 do bicho: mouse
		float ponto1ydirmouse;

		float resultantex; // para definir a influência do movimento
		float resultantey;

		float finalponto1xdirfome; // influencia o ponto1 do bicho: fome
		float finalponto1ydirfome;
		float finalponto1xdirfoge; // influencia o ponto1 do bicho: foge
		float finalponto1ydirfoge;
		float finalponto1xdirpersegue; // influencia o ponto1 do bicho: persegue
		float finalponto1ydirpersegue;
		float finalponto1xdirtarado; // influencia o ponto1 do bicho: tarado
		float finalponto1ydirtarado;
		float finalponto1xdirsatisfeito; // influencia o ponto1 do bicho:
											// satisfeito
		float finalponto1ydirsatisfeito;
		float finalponto1xdirgangue; // influencia o ponto1 do bicho: gangue
		float finalponto1ydirgangue;
		float finalponto1xdirafastapodre; // influencia o ponto1 do bicho:
											// afastapodre
		float finalponto1ydirafastapodre;

		float ponto1xdirfome; // influencia o ponto1 do bicho: fome
		float ponto1ydirfome;
		float ponto1xdirfoge; // influencia o ponto1 do bicho: foge
		float ponto1ydirfoge;
		float ponto1xdirpersegue; // influencia o ponto1 do bicho: persegue
		float ponto1ydirpersegue;
		float ponto1xdirtarado; // influencia o ponto1 do bicho: tarado
		float ponto1ydirtarado;
		float ponto1xdirsatisfeito; // influencia o ponto1 do bicho: satisfeito
		float ponto1ydirsatisfeito;
		float ponto1xdirgangue; // influencia o ponto1 do bicho: gangue
		float ponto1ydirgangue;
		float ponto1xdirafastapodre; // influencia o ponto1 do bicho:
										// afastapodre
		float ponto1ydirafastapodre;

		bicho( // define valores Vs no construtor com temporárias [CONSTRUTOR]
		int temppositionx, // posição X par alocar o bicho
				int temppositiony, // posição Y para alocar o bicho
				float tempvelocidadeauto, // velocidadeauto (treme treme) É fixa
											// depois de gerada, só muda em
											// situações específicas
				float tempeasing, // easing (em cascata:
									// easing+o*o*easingaceleration)
				float tempeasingaceleration, // easingaceleration
				float temptamanho, // tamanho (retângulo possível)
				int tempnumerodepontos, // número de pontos do bicho. O index é
										// 0. Com todos os pontos separados o
										// menor número é 5
				float temppesodalinha, // peso da linha do bicho
				float tempcorr, // cores linha R
				float tempcorg, // cores linha G
				float tempcorb, // cores linha B
				float tempcorlinhar, // cores corlinha R
				float tempcorlinhag, // cores corlinha G
				float tempcorlinhab, // cores corlinha B
				float tempformadiam, // diâmetro de cada forma (todas elas) a
										// partir do diam
				int tempforma1, // forma da cabeça
				int tempforma2, // forma do pescoço
				int tempformarabo, // forma do rabo
				int tempinstrumento, // para definir o instrumento de cada som
				int tempnota, // define a nota que o bicho vai reproduzir
				float tempenergia, // equivale à quantidade de vida inicial
				float temppontomadurocruza, // define um ponto de maturidade
											// para que possam cruzar novamente
				int tempchance // pode fazer ou não algo: surtar, caçar, fugir
								// (1:chapado, 2:surta, 3:berserker, 4:medroso,
								// 5:violento, 6:esperto / tarado>=3 / ataca com
								// bando >=5)
		) {

			positionx = temppositionx; // posição X para alocar o bicho
			positiony = temppositiony; // posição Y para alocar o bicho
			velocidadeauto = tempvelocidadeauto; // velocidadeauto (treme treme)
													// É fixa depois de gerada,
													// só muda em situações
													// específicas
			easing = tempeasing; // easing (em cascata:
									// easing+o*o*easingaceleration)
			easingaceleration = tempeasingaceleration; // easingaceleration
			tamanho = temptamanho; // tamanho (retângulo possível)
			numerodepontos = tempnumerodepontos; // número de pontos do bicho. O
													// index é 0. Com todos os
													// pontos separados o menor
													// número é 5
			pesodalinha = temppesodalinha; // peso da linha do bicho
			corr = tempcorr; // cores linha R
			corg = tempcorg; // cores linha G
			corb = tempcorb; // cores linha B
			corlinhar = tempcorlinhar; // cores corlinha R
			corlinhag = tempcorlinhag; // cores corlinha G
			corlinhab = tempcorlinhab; // cores corlinha B
			formadiam = tempformadiam; // diâmetro de cada forma (todas elas) a
										// partir do diam
			forma1 = tempforma1; // forma da cabeça
			forma2 = tempforma2; // forma do pescoço
			formarabo = tempformarabo; // forma do rabo
			instrumento = tempinstrumento; // para definir o instrumento de cada
											// som
			nota = tempnota; // define a nota que o bicho vai reproduzir
			energia = tempenergia; // equivale à quantidade de vida inicial
			pontomadurocruza = temppontomadurocruza; // define um ponto de
														// maturidade para que
														// possam cruzar
														// novamente
			chance = tempchance; // pode fazer ou não algo: surtar, caçar, fugir
									// (1:chapado, 2:surta, 3:berserker,
									// 4:medroso, 5:violento, 6:esperto /
									// tarado>=3 / ataca com bando >=5)

			cor = color(corr, corg, corb);
			corlinha = color(corlinhar, corlinhag, corlinhab);
			coralpha = 50 + energia * 4; // características de cada bicho
											// geradas aleatoriamente e outras
											// Vs definidas automaticamente
			corlinhaalpha = 50 + energia * 4;
			velocidade = 5;
			novachance = random(100);
			maturidade = (int) random(pontomadurocruza);
			vida = true;
			podre = false;
			velocidadeautooriginal = tempvelocidadeauto;
			finalbando = energia;
			bancodadosinstrumento = (int) random(4);
			atracao = 0.5f;
			maxformadiam = (int) (random(40, 60));
			numerodepontosdalinha = (int) (random(numerodepontos - 4,
					numerodepontos - 2));

			pontox = new float[numerodepontos];
			pontoy = new float[numerodepontos];

			for (int i = 1; i < numerodepontos; i++) { // iniciando pontos x e y
														// com listas
				pontox[i] = (int) (random(positionx - tamanho, positionx
						+ tamanho));
			}
			for (int j = 1; j < numerodepontos; j++) {
				pontoy[j] = (int) (random(positiony - tamanho, positiony
						+ tamanho));
			}

			distx1 = new float[numerodepontos]; // distâncias inicial dos pontos
												// que define a forma do corpo
												// de cada bicho. Outras listas
			disty1 = new float[numerodepontos];

			for (int i = 1; i < numerodepontos; i++) { // captura das distâncias
														// iniciais dos pontos
														// para manter forma do
														// bicho. Listas de novo
				distx1[i] = abs(abs(pontox[1]) - abs(pontox[i]));
			}
			for (int j = 1; j < numerodepontos; j++) {
				disty1[j] = abs(abs(pontoy[1]) - abs(pontoy[j]));
			}

			ordemxy = new int[numerodepontosdalinha];

			for (int i = 1; i < numerodepontosdalinha; i++) {
				if (i + 2 < numerodepontos - 1) {
					ordemxy[i] = i + 2;
				} else {
					ordemxy[i] = (int) (random(2, numerodepontos - 1));
				}

			}

			randomizar(ordemxy);

		}

		void randomizar(int[] a) {
			for (int k = 1; k < a.length - 2; k++) {
				int temp = a[k];
				int x = (int) random(1, a.length - 2);
				a[k] = a[x];
				a[x] = temp;
			}

		}

		void semostra() {

			if (mudadia == 5) {
				cor = color(0, 0, 0);
				corlinha = color(0, 0, 0);
				coralpha = 255;
				corlinhaalpha = 255;
			} else {
				cor = color(corr, corg, corb);
				corlinha = color(corlinhar, corlinhag, corlinhab);
			}

			desenhaforma(pontox[1], pontoy[1], angulo1, forma1, formadiam, 1,
					pesodalinha, cor, coralpha, corlinha, corlinhaalpha); // desenha
																			// a
																			// cabeça
			desenhaforma(pontox[2], pontoy[2], angulo2, forma2, formadiam, 2,
					pesodalinha, cor, coralpha, corlinha, corlinhaalpha); // desenha
																			// o
																			// dorso
			desenhaforma(pontox[numerodepontos - 1],
					pontoy[numerodepontos - 1], angulorabo, formarabo,
					formadiam, 5, pesodalinha, cor, coralpha, corlinha,
					corlinhaalpha); // desenha o rabo

			noFill(); // desenho da estrutura
			beginShape();
			curveVertex(pontox[1], pontoy[1]);
			curveVertex(pontox[1], pontoy[1]);
			curveVertex(pontox[2], pontoy[2]);

			for (int w = 1; w < numerodepontosdalinha; w++) {

				curveVertex(pontox[ordemxy[w]], pontoy[ordemxy[w]]);

				// if (w>1 && w<numerodepontos-1){
				// if (random(1) < chancemutacao) {
				// curveVertex(pontox[w+int(random(-2,2))],
				// pontoy[w+int(random(-2,2))]);
				// }
				// }

				// curveVertex(pontox[int(random(1,numerodepontos-1))],
				// pontoy[int(random(1,numerodepontos-1))]);

				// curveVertex(pontox[w], pontoy[w]);

			}

			if (numerodepontos - 2 != 2) {
				curveVertex(pontox[numerodepontos - 2],
						pontoy[numerodepontos - 2]);
			}

			curveVertex(pontox[numerodepontos - 1], pontoy[numerodepontos - 1]);
			curveVertex(pontox[numerodepontos - 1], pontoy[numerodepontos - 1]);
			endShape();

		}

		void desenhaforma(float coordx, float coordy, float angulorotacao,
				int tipoforma, float tamanho, float fator,
				float pesodalinhafator, int corfator, float coralphafator,
				int corlinhafator, float corlinhaalphafator) {

			pushMatrix(); // inicia a matrix, translada e roda ponto
			translate(coordx, coordy);
			rotate(radians(angulorotacao));

			strokeWeight(pesodalinhafator);
			stroke(corlinhafator, corlinhaalphafator);
			fill(corfator, coralphafator);

			if (tipoforma == 2) { // forma cabeça ponto
				rectMode(CENTER);
				rect(0, 0, tamanho / fator, tamanho / fator);
			} else if (tipoforma == 3) {
				triangle(0,
						(0 - tamanho / (2 * fator)) - tamanho / (3 * fator),
						(0 - tamanho / (2 * fator)) - tamanho / (6 * fator),
						0 + (tamanho / (fator)) / 2.6f, (0 + tamanho
								/ (2 * fator))
								+ tamanho / (6 * fator),
						0 + (tamanho / (fator)) / 2.6f);
			} else {
				ellipse(0, 0, tamanho / fator, tamanho / fator);
			}
			popMatrix(); // fehca matrix ponto
		}

		void vive() {

			if (vida == true) { // se ele estiver vivo

				semove();
				buscacor();
				seajusta();
				semostravivo();

			}
		}

		void semove() { // função para mover os bichos no automático e criar os
						// easings [SEMOVE]

			coralpha = 50 + energia * 4;
			corlinhaalpha = 50 + energia * 4;

			energia = energia - 0.0001f * velocidadeauto * numerodepontos; // a
																			// vida
																			// tende
																			// a
																			// acabar
			maturidade = maturidade + 0.1f; // amadurece para reproduzir
			novachance = novachance + 0.1f; // evolui e pode mudar chance de
											// reagir diferente
			tamanhoformadiam = tamanhoformadiam - (tamanhoformadiam / 100); // para
																			// não
																			// crescer
																			// ou
																			// encolher
																			// para
																			// sempre

			pontox[1] = movimentorandomico(pontox[1], -1, 1, 1, resultantex,
					velocidadeauto, 0); // movimenta a cabeça do bicho
			pontoy[1] = movimentorandomico(pontoy[1], -1, 1, 1, resultantey,
					velocidadeauto, 0); // movimenta a cabeça do bicho

			pontox[1] = selimita(pontox[1], -((width / escala) - width) / 2,
					(width / escala) - ((width / escala) - width) / 2); // para
																		// não
																		// sair
																		// da
																		// tela
			pontoy[1] = selimita(pontoy[1], -((height / escala) - height) / 2,
					(height / escala) - ((height / escala) - height) / 2); // para
																			// não
																			// sair
																			// da
																			// tela

			for (int m = 2; m < numerodepontos; m++) { // movimento da estrutura
														// do bicho com listas
														// tbm
				pontox[m] = movimentorandomico(pontox[m], -1, 1, 1, 0,
						velocidadeauto / m, 0);
			}
			for (int n = 2; n < numerodepontos; n++) {
				pontoy[n] = movimentorandomico(pontoy[n], -1, 1, 1, 0,
						velocidadeauto / n, 0);
			}

			corr = movimentorandomico(corr, -1, 1, 1, 0, velocidadeauto, 0);
			corg = movimentorandomico(corg, -1, 1, 1, 0, velocidadeauto, 0);
			corb = movimentorandomico(corb, -1, 1, 1, 0, velocidadeauto, 0);
			corlinhar = movimentorandomico(corlinhar, -1, 1, 1, 0,
					velocidadeauto, 0);
			corlinhag = movimentorandomico(corlinhag, -1, 1, 1, 0,
					velocidadeauto, 0);
			corlinhab = movimentorandomico(corlinhab, -1, 1, 1, 0,
					velocidadeauto, 0);

			corr = selimita(corr, 0, 255);
			corg = selimita(corg, 0, 255);
			corb = selimita(corb, 0, 255);
			corlinhar = selimita(corlinhar, 0, 255);
			corlinhag = selimita(corlinhag, 0, 255);
			corlinhab = selimita(corlinhab, 0, 255);

			coralpha = selimita(coralpha, 0, 255);
			corlinhaalpha = selimita(corlinhaalpha, 0, 255);

			if (random(1) < 0.005 * velocidadeauto) {
				nota = nota + (int) (random(-2, 2));
			}

			nota = (int) (selimita(nota, 0, 99));
			instrumento = (int) (selimita(instrumento, 0, 127));

			if (random(1) < 0.005 * velocidadeauto) {
				instrumento = instrumento + (int) (random(-2, 2));
			}

			formadiam = movimentorandomico(formadiam, -1, 1, 1,
					tamanhoformadiam, velocidadeauto / 1.5f, 0);
			formadiam = selimita(formadiam, 5, maxformadiam);

			for (int o = 2; o < numerodepontos; o++) { // quando a cabeça move,
														// o resto deve
														// acompanhar abrindo a
														// linha e depois
														// retomando a forma
														// original
				if (abs(abs(pontox[1]) - abs(pontox[o])) > distx1[o]) {
					pontox[o] = movimentaeasing(pontox[o], pontox[1], easing
							+ o * o * easingaceleration);
				}
			}
			for (int p = 2; p < numerodepontos; p++) {
				if (abs(abs(pontoy[1]) - abs(pontoy[p])) > disty1[p]) {
					pontoy[p] = movimentaeasing(pontoy[p], pontoy[1], easing
							+ p * p * easingaceleration);
				}
			}

			angulo1 = movimentorandomico(angulo1, -1, 1, 1, 0,
					velocidadeauto * 3, 0);
			angulo2 = movimentorandomico(angulo2, -1, 1, 1, 0,
					velocidadeauto * 3, 0);
			angulorabo = movimentorandomico(angulorabo, -1, 1, 1, 0,
					velocidadeauto * 3, 0);

		}

		float movimentaeasing(float A, float B, float fator) {

			A = A + (B - A) / fator;
			return A;

		}

		float movimentorandomico(float A, float limitemenor, float limitemaior,
				float fatormultiplica, float fatorsoma,
				float fatormultiplicaresultado, float fatorsomaresultado) {

			A = A
					+ (random(limitemenor * fatormultiplica + fatorsoma,
							limitemaior * fatormultiplica + fatorsoma)
							* fatormultiplicaresultado + fatorsomaresultado);
			return A;

		}

		float desloca(float influencia, float eixo, float angulo, float fator) {

			if (eixo == 1) {
				influencia = influencia + (cos(angulo)) * fator;
			}
			if (eixo == 2) {
				influencia = influencia + (sin(angulo)) * fator;
			}
			return influencia;

		}

		float selimita(float A, float limitemenor, float limitemaior) { // para
																		// se
																		// limitar
																		// ao
																		// espectro
																		// pretendido
			if (A < limitemenor) {
				A = limitemenor;
			} else if (A > limitemaior) {
				A = limitemaior;
			}
			return A;
		}

		void buscacor() {

			if (camerasfuncionando && pegacor || camerasfuncionando
					&& procurapelacor) {

				float worldRecord = 500;
				int closestX = 0;
				int closestY = 0;
				float proximidade = 5;

				for (int x = 0; x < cam.width; x++) {
					for (int y = 0; y < cam.height; y++) {
						int loc = x + y * cam.width;
						int currentColor = cam.pixels[loc]; // TODO: FIX
						float r1 = red(currentColor);
						float g1 = green(currentColor);
						float b1 = blue(currentColor);
						float d = dist(r1, g1, b1, corr, corg, corb);

						if (pegacor) {
							if (pontox[1] <= ((x * 24 / escala) - ((width / escala) - width) / 2)
									+ proximidade
									&& pontox[1] >= ((x * 24 / escala) - ((width / escala) - width) / 2)
											- proximidade
									&& pontoy[1] <= ((y * 26.667 / escala) - ((height / escala) - height) / 2)
											+ proximidade
									&& pontoy[1] >= ((y * 26.667 / escala) - ((height / escala) - height) / 2)
											- proximidade) {
								corr = movimentaeasing(corr, r1, 10);
								corg = movimentaeasing(corg, g1, 10);
								corb = movimentaeasing(corb, b1, 10);
								corlinhar = movimentaeasing(corlinhar, r1, 10);
								corlinhag = movimentaeasing(corlinhag, g1, 10);
								corlinhab = movimentaeasing(corlinhab, b1, 10);
							}
						}

						if (d < worldRecord) {
							worldRecord = d;
							// closestX =
							// int(((x*24/escala)-((width/escala)-width)/2)-(width));
							closestX = (int) ((x * 24 / escala) - ((width / escala) - width) / 2);
							closestY = (int) ((y * 26.667 / escala) - ((height / escala) - height) / 2);
						}
					}
				}

				if (procurapelacor) {

					if (worldRecord < niveldeprecisaodacor) {

						float angulocor = atan2((pontoy[1] - closestY),
								(pontox[1] - closestX)); // calcula o ângulo
															// entre os pontos
															// (trigonometria)

						ponto1xdircor = desloca(ponto1xdircor, 1, angulocor,
								-atracao);
						ponto1ydircor = desloca(ponto1ydircor, 2, angulocor,
								-atracao);

						if (commetadados >= 2) {
							desenhaforma(closestX, closestY, -angulo1, forma1,
									formadiam, 0.5f, pesodalinha, cor,
									coralpha / 3, corlinha, corlinhaalpha / 3);
							desenhalinhacontato(pontox[1], pontoy[1], closestX,
									closestY);
						}
					}
				}
			}
		}

		void seajusta() { // função com a IA de cada bicho [PENSA]

			if (bancodadosinstrumento == 1 && instrumento > 95
					|| bancodadosinstrumento == 1 && instrumento < 27) { // para
																			// não
																			// ficar
																			// em
																			// silêncio
																			// devido
																			// ao
																			// buraco
																			// no
																			// banco
																			// de
																			// dados
				instrumento = (int) (random(27, 95));
			}

			if (bancodadosinstrumento == 3 && instrumento > 46) { // para não
																	// ficar em
																	// silêncio
																	// devido ao
																	// buraco no
																	// banco de
																	// dados
				instrumento = (int) (random(46));
			}

			if (energia < 4 && chance == 2 || energia < 4 && chance == 3) { // surto
																			// kamikaze
																			// ou
																			// berserker
				velocidadeauto = 9;
			}

			if (finalbando > energia) { // retoma energia se não está em bando e
										// atualiza caso perca energia
				finalbando = finalbando - 1;
			} else if (finalbando < energia) { // acompanha a energia caso ela
												// aumente
				finalbando = finalbando + 1;
			}

			if (velocidadeauto > velocidadeautooriginal) { // tendência a
															// retomar
															// velocidade
															// original
				velocidadeauto = velocidadeauto - 0.1f;
			}

			if (evoluichance >= 15) { // ganhando experiência com o evoluichance
										// pode ajudar a evoluir a personalidade
										// (chance)
				chance = chance + 1;
				novachance = 0;
				evoluichance = 0;
			}

			if (novachance > 150) { // muda a chance depois de 100 e desloca,
									// pois a estratégia não deve estar
									// funcionando
				ponto1xdir = movimentorandomico(ponto1xdir, -3, 3, 1, 0, 1, 0);
				ponto1ydir = movimentorandomico(ponto1ydir, -3, 3, 1, 0, 1, 0);
				chance = (int) (random(chance, 7));
				novachance = 0;
			}

			if (sova == 5) { // se está se lascando, diminui a área de contato
								// (não usa no bichos.get(t). porque o energia
								// também é assim)
				tamanhoformadiam = tamanhoformadiam - 0.1f;
				sova = 0;
			}

			minhaadaptacao = energia - adaptacaomedia;

			ponto1xdir = tendencia(ponto1xdir, 3, 0.02f); // tendência a retomar
															// direção aleatória
			ponto1ydir = tendencia(ponto1ydir, 3, 0.02f);

			ponto1xdircor = tendencia(ponto1xdircor, 3, 0.05f); // tendência a
																// retomar
																// direção
																// aleatória
			ponto1ydircor = tendencia(ponto1ydircor, 3, 0.05f);

			ponto1xdirmouse = tendencia(ponto1xdirmouse, 1, 0.01f); // tendência
																	// a retomar
																	// direção
																	// aleatória:
																	// mouse
			ponto1ydirmouse = tendencia(ponto1ydirmouse, 1, 0.01f);

			finalponto1xdirfome = tendencia(finalponto1xdirfome, 0.8f, 0.01f); // tendência
																				// a
																				// retomar
																				// direção
																				// aleatória:
																				// fome
			finalponto1ydirfome = tendencia(finalponto1ydirfome, 0.8f, 0.01f);
			finalponto1xdirfoge = tendencia(finalponto1xdirfoge, 0.8f, 0.01f); // tendência
																				// a
																				// retomar
																				// direção
																				// aleatória:
																				// foge
			finalponto1ydirfoge = tendencia(finalponto1ydirfoge, 0.8f, 0.01f);
			finalponto1xdirpersegue = tendencia(finalponto1xdirpersegue, 0.8f,
					0.01f); // tendência a retomar direção aleatória: persegue
			finalponto1ydirpersegue = tendencia(finalponto1ydirpersegue, 0.8f,
					0.01f);
			finalponto1xdirtarado = tendencia(finalponto1xdirtarado, 0.8f,
					0.01f); // tendência a retomar direção aleatória: tarado
			finalponto1ydirtarado = tendencia(finalponto1ydirtarado, 0.8f,
					0.01f);
			finalponto1xdirsatisfeito = tendencia(finalponto1xdirsatisfeito,
					0.8f, 0.01f); // tendência a retomar direção aleatória:
									// satisfeito
			finalponto1ydirsatisfeito = tendencia(finalponto1ydirsatisfeito,
					0.8f, 0.01f);
			finalponto1xdirgangue = tendencia(finalponto1xdirgangue, 0.8f,
					0.01f); // tendência a retomar direção aleatória: gangue
			finalponto1ydirgangue = tendencia(finalponto1ydirgangue, 0.8f,
					0.01f);
			finalponto1xdirafastapodre = tendencia(finalponto1xdirafastapodre,
					0.8f, 0.01f); // tendência a retomar direção aleatória:
									// afastapodre
			finalponto1ydirafastapodre = tendencia(finalponto1ydirafastapodre,
					0.8f, 0.01f);

			resultantex = ponto1xdir + ponto1xdircor + ponto1xdirmouse
					+ finalponto1xdirfome + finalponto1xdirfoge
					+ finalponto1xdirpersegue + finalponto1xdirtarado
					+ finalponto1xdirsatisfeito + finalponto1xdirgangue
					+ finalponto1xdirafastapodre; // calcula influência de
													// outras Vs
			resultantey = ponto1ydir + ponto1ydircor + ponto1ydirmouse
					+ finalponto1ydirfome + finalponto1ydirfoge
					+ finalponto1ydirpersegue + finalponto1ydirtarado
					+ finalponto1ydirsatisfeito + finalponto1ydirgangue
					+ finalponto1ydirafastapodre;

		}

		float tendencia(float umponto, float limite, float incremento) { // tendência
																			// a
																			// retomar
																			// direção
																			// aleatória

			if (umponto > 0 && umponto <= limite) {
				umponto = umponto - incremento;
			} else if (umponto > limite) {
				umponto = limite;
			} else if (umponto < 0 && umponto >= -limite) {
				umponto = umponto + incremento;
			} else if (umponto < -limite) {
				umponto = -limite;
			}
			return umponto;
		}

		void semostravivo() { // função para desenhar as formas [SEMOSTRA]

			if (maturidade >= pontomadurocruza) { // mostra a elipse no
													// penúltimo ponto se ele
													// estiver maduro
				desenhaforma(pontox[numerodepontos - 2],
						pontoy[numerodepontos - 2], angulo1, forma1, formadiam,
						3, pesodalinha, corlinha, corlinhaalpha, 0, 0); // com
																		// preenchimento
																		// igual
																		// ao
																		// contorno
			}

			if (som == true && random(1) < 0.005 * velocidadeauto) { // reproduz
																		// o som
																		// poucas
																		// vezes
				pegainstrumento(bancodadosinstrumento, instrumento);
				tocanota(nota, volume, duracao);
				desenhaforma(pontox[numerodepontos - 1],
						pontoy[numerodepontos - 1], angulorabo, 1, formadiam,
						0.5f, pesodalinha, cor, coralpha / 3, corlinha,
						corlinhaalpha); // para saber qual é o que está fazendo
										// barulho

			}

			if (commetadados >= 2) { // valor das Vs para visualização
										// individual

				for (int w = 1; w < numerodepontos; w++) {
					desenhaforma(pontox[w], pontoy[w], angulorabo, 1,
							formadiam, 7, pesodalinha, cor, coralpha, corlinha,
							corlinhaalpha);
				}

				fill(corr, corg, corb, 255); // preenchimento do texto na cor do
												// contorno do bicho!
				textSize(12);
				text("adp: " + (int) (minhaadaptacao), pontox[1] + formadiam
						+ 10, pontoy[1] - 15);
				text("vid: " + (int) (energia) + "  |  " + (int) (finalbando),
						pontox[1] + formadiam + 10, pontoy[1]); // texto em cada
																// bicho para
																// realizar os
																// testes
				text("som: " + (int) (bancodadosinstrumento) + "  |  "
						+ (int) (instrumento) + "  |  " + (int) (nota),
						pontox[1] + formadiam + 10, pontoy[1] + 15);
				text("tam: " + (numerodepontos - 1) + "  |  "
						+ (numerodepontosdalinha) + "  |  "
						+ (int) (tamanhoformadiam * 100) + "  |  "
						+ (int) (sova), pontox[1] + formadiam + 10,
						pontoy[1] + 30);
				text("cha: " + (int) (chance) + "  |  " + (int) (evoluichance)
						+ "  |  " + (int) (novachance), pontox[1] + formadiam
						+ 10, pontoy[1] + 45);
				text("mat: " + (int) (maturidade) + "  |  "
						+ (int) (pontomadurocruza), pontox[1] + formadiam + 10,
						pontoy[1] + 60);
				text("ger: " + geracao, pontox[1] + formadiam + 10,
						pontoy[1] + 75);
				text("ind: " + bichos.indexOf(tempbicho), pontox[1] + formadiam
						+ 10, pontoy[1] + 90);
			}
		}

		void morre() { // método que determina as condições em que um bicho
						// morre e suas consequências [MORRE]

			if (energia < 0) { // por falta de energia

				vida = false;

				energia = energia - 0.002f; // continua até ficar podre

				coralpha = 100 + energia * 10;
				corlinhaalpha = 100 + energia * 10;

				coralpha = selimita(coralpha, 0, 100);
				corlinhaalpha = selimita(corlinhaalpha, 0, 100);

				corr = 100;
				corg = 100;
				corb = 100;
				corlinhar = 100;
				corlinhag = 100;
				corlinhab = 100;

				chance = 0;
				novachance = 0;
				evoluichance = 0;
				sova = 0;
				maturidade = 0;

				if (contagemmorte == 0) { // contagem morte
					if (forma1 == 1) {
						ne = ne - 1;
					} else if (forma1 == 2) {
						nr = nr - 1;
					} else if (forma1 == 3) {
						nt = nt - 1;
					}
				}

				contagemmorte = 1;

				if (energia < -10) { // apodrece
					podre = true;
				}
			}
		}

		void sebate() {

			for (int t = bichos.indexOf(tempbicho) + 1; t < bichos.size(); t++) { // condição
																					// para
																					// a
																					// interseção
																					// [COLISÃO]

				if (contatoAeB(tempbicho, bichos.get(t)) == 1) { // de longe
																	// define se
																	// persegue
																	// ou foge
																	// [COLISÃO
																	// LONGE]

					novachance = 0;
					bichos.get(t).novachance = 0;

					desenhalinhacontato(pontox[1], pontoy[1],
							bichos.get(t).pontox[1], bichos.get(t).pontoy[1]);

					if (forma1 == bichos.get(t).forma1) { // se for da mesma
															// forma... [COLISÃO
															// LONGE FORMA==]

						if (vida == true && bichos.get(t).vida == true) { // ...e
																			// estiverem
																			// vivos
																			// [COLISÃO
																			// LONGE
																			// FORMA==
																			// VIVOS]

							if (AeBmaduros(tempbicho, bichos.get(t))) { // procuram
																		// pelo
																		// próximo
																		// maduros
																		// [COLISÃO
																		// LONGE
																		// FORMA==
																		// VIVOS
																		// MADUROS]

								AquercruzarB(tempbicho, bichos.get(t)); // ESSE
																		// QUER
																		// CRUZAR
								AquercruzarB(bichos.get(t), tempbicho); // OUTRO
																		// QUER
																		// CRUZAR

							} else { // fogem se sem maturidade. O >5 é só para
										// manter o efeito de reprodução
										// [COLISÃO LONGE FORMA== VIVOS
										// IMATUROS]

								AdesinteressaB(tempbicho, bichos.get(t)); // ESSE
																			// BICHO
																			// SE
																			// DESINTERESSA
								AdesinteressaB(bichos.get(t), tempbicho); // OUTRO
																			// BICHO
																			// SE
																			// DESINTERESSA

								if (AeBseagruparem(tempbicho, bichos.get(t))) { // gangue:
																				// assim
																				// as
																				// formas
																				// iguais
																				// tendem
																				// a
																				// permanecer
																				// perto
																				// no
																				// raio
																				// entre
																				// *2
																				// e
																				// *5
																				// [COLISÃO
																				// LONGE
																				// FORMA==
																				// VIVOS
																				// IMATUROS
																				// GANGUE]

									AgangueB(tempbicho, bichos.get(t)); // ESSE
																		// ENTRA
																		// PARA
																		// A
																		// GANGUE
									AgangueB(bichos.get(t), tempbicho); // OUTRO
																		// ENTRA
																		// PARA
																		// A
																		// GANGUE

								}
							}

						} else { // ...e estiver morto ele evita [COLISÃO LONGE
									// FORMA== ALGUÉM MORTO]

							AtemnojodeB(tempbicho, bichos.get(t)); // ESSE TEM
																	// NOJO
							AtemnojodeB(bichos.get(t), tempbicho); // OUTRO TEM
																	// NOJO

						}

					} else { // se as formas forem diferentes... [COLISÃO LONGE
								// FORMA!=]

						if (vida == true && bichos.get(t).vida == true) { // ...e
																			// estiverem
																			// vivos
																			// [COLISÃO
																			// LONGE
																			// FORMA!=
																			// VIVOS]

							if (AmaisfracoqueB(tempbicho, bichos.get(t))) { // se
																			// a
																			// energia
																			// ou
																			// o
																			// bando
																			// de
																			// A
																			// são
																			// menores
																			// [COLISÃO
																			// LONGE
																			// FORMA!=
																			// VIVOS
																			// A<B]

								AfogedeB(tempbicho, bichos.get(t)); // EFEITO DE
																	// FUGA
																	// NESSE
								AatacaB(bichos.get(t), tempbicho); // EFEITO DE
																	// CAÇA NO
																	// OUTRO

							} else if (AmaisfracoqueB(bichos.get(t), tempbicho)) { // se
																					// a
																					// energia
																					// ou
																					// o
																					// bando
																					// de
																					// B
																					// são
																					// menores
																					// [COLISÃO
																					// LONGE
																					// FORMA!=
																					// VIVOS
																					// A>B]

								AatacaB(tempbicho, bichos.get(t)); // EFEITO DE
																	// CAÇA
																	// NESSE
								AfogedeB(bichos.get(t), tempbicho); // EFEITO DE
																	// FUGA NO
																	// OUTRO

							}

						} else { // ...um dos dois estiver morto o outro procura
									// para comer [COLISÃO LONGE FORMA!= ALGUÉM
									// MORTO]

							AvaicomerB(tempbicho, bichos.get(t)); // EFEITO
																	// NESSE
							AvaicomerB(bichos.get(t), tempbicho); // EFEITO NO
																	// OUTRO

						}
					}

				} else if (contatoAeB(tempbicho, bichos.get(t)) == 2) { // de
																		// perto
																		// cruza
																		// ou
																		// briga
																		// ou
																		// come
																		// [COLISÃO
																		// PERTO]

					novachance = 0;
					bichos.get(t).novachance = 0;

					if (forma1 == bichos.get(t).forma1) { // [COLISÃO PERTO
															// FORMA==]

						if (vida == true && bichos.get(t).vida == true) { // [COLISÃO
																			// PERTO
																			// FORMA==
																			// VIVOS]

							if (AeBmaduros(tempbicho, bichos.get(t))) { // [COLISÃO
																		// PERTO
																		// FORMA==
																		// A
																		// VIVOS
																		// MADUROS]

								if (cruza < 200) { // se for do mesmo: cruza...
													// [COLISÃO PERTO FORMA== B
													// VIVOS CRUZANDO]

									AcruzacomB(tempbicho, bichos.get(t)); // EFEITO
																			// NESSE
									AcruzacomB(bichos.get(t), tempbicho); // EFEITO
																			// NO
																			// OUTRO

								} else { // ...depois reproduz [COLISÃO PERTO
											// FORMA== B VIVOS REPRODUZINDO]

									AreproduzcomB(tempbicho, bichos.get(t)); // EFEITO
																				// NESSE
									AreproduzcomB(bichos.get(t), tempbicho); // EFEITO
																				// NO
																				// OUTRO

									bichoculpado = (bicho) tempbicho; // define
																		// quem
																		// são
																		// os
																		// bichosculpados:
																		// pai1
									bichaculpada = (bicho) bichos.get(t); // define
																			// quem
																			// são
																			// os
																			// bichosculpados:
																			// pai2
									filanascimento = 1;

								}
							}
						}

					} else { // se for diferente: briga [COLISÃO PERTO FORMA!=]

						if (vida == true && bichos.get(t).vida == true) { // [COLISÃO
																			// PERTO
																			// FORMA!=
																			// VIVOS]

							AbrigacomB(tempbicho, bichos.get(t)); // ESSE BRIGA
							AbrigacomB(bichos.get(t), tempbicho); // OUTRO BRIGA

						} else { // [COLISÃO PERTO FORMA!= ALGUÉM MORTO]

							AcomeB(tempbicho, bichos.get(t)); // ESSE COME
							AcomeB(bichos.get(t), tempbicho); // OUTRO COME

						}
					}
				}

				for (int q = 3; q < bichos.get(t).numerodepontos - 2; q++) { // cai
																				// na
																				// teia
																				// do
																				// outro
					if (ApassarpelospontosdeB(tempbicho, bichos.get(t), q)) {

						AcainateiadeB(tempbicho, bichos.get(t), q); // ESSE CAI
																	// NA TEIA
																	// DO OUTRO

					}
				}

				for (int q = 3; q < numerodepontos - 2; q++) { // gruda na teia
																// desse
					if (ApassarpelospontosdeB(bichos.get(t), tempbicho, q)) {

						AcainateiadeB(bichos.get(t), tempbicho, q); // OUTRO CAI
																	// NA TEIA
																	// DESSE

					}
				}
			}

			finalponto1xdirfome = transferesomaparafinal(finalponto1xdirfome,
					ponto1xdirfome); // para transferir para a final o efeito
			finalponto1ydirfome = transferesomaparafinal(finalponto1ydirfome,
					ponto1ydirfome);
			finalponto1xdirfoge = transferesomaparafinal(finalponto1xdirfoge,
					ponto1xdirfoge);
			finalponto1ydirfoge = transferesomaparafinal(finalponto1ydirfoge,
					ponto1ydirfoge);
			finalponto1xdirpersegue = transferesomaparafinal(
					finalponto1xdirpersegue, ponto1xdirpersegue);
			finalponto1ydirpersegue = transferesomaparafinal(
					finalponto1ydirpersegue, ponto1ydirpersegue);
			finalponto1xdirtarado = transferesomaparafinal(
					finalponto1xdirtarado, ponto1xdirtarado);
			finalponto1ydirtarado = transferesomaparafinal(
					finalponto1ydirtarado, ponto1ydirtarado);
			finalponto1xdirsatisfeito = transferesomaparafinal(
					finalponto1xdirsatisfeito, ponto1xdirsatisfeito);
			finalponto1ydirsatisfeito = transferesomaparafinal(
					finalponto1ydirsatisfeito, ponto1ydirsatisfeito);
			finalponto1xdirgangue = transferesomaparafinal(
					finalponto1xdirgangue, ponto1xdirgangue);
			finalponto1ydirgangue = transferesomaparafinal(
					finalponto1ydirgangue, ponto1ydirgangue);
			finalponto1xdirafastapodre = transferesomaparafinal(
					finalponto1xdirafastapodre, ponto1xdirafastapodre);
			finalponto1ydirafastapodre = transferesomaparafinal(
					finalponto1ydirafastapodre, ponto1ydirafastapodre);

			ponto1xdirfome = 0; // para zerar o efeito e não acumular resíduos
			ponto1ydirfome = 0;
			ponto1xdirfoge = 0;
			ponto1ydirfoge = 0;
			ponto1xdirpersegue = 0;
			ponto1ydirpersegue = 0;
			ponto1xdirtarado = 0;
			ponto1ydirtarado = 0;
			ponto1xdirsatisfeito = 0;
			ponto1ydirsatisfeito = 0;
			ponto1xdirgangue = 0;
			ponto1ydirgangue = 0;
			ponto1xdirafastapodre = 0;
			ponto1ydirafastapodre = 0;

			if (bando != energia) {
				finalbando = bando;
			}

			bando = energia;

		}

		float transferesomaparafinal(float finalponto, float somaponto) {

			if (somaponto != 0) {
				finalponto = somaponto;
			}
			return finalponto;
		}

		void tocanota(int temponota, float tempovolume, float tempoduracao) { // inicia
																				// e
																				// encerra
																				// o
																				// som
																				// da
																				// nota
			channels[0].noteOn(temponota, (int) (tempovolume));
			try {
				Thread.sleep((long) tempoduracao);
			} catch (InterruptedException e) {
			}
			channels[0].noteOff(temponota);
		}

		void pegainstrumento(int tempobancodadosinstrumento,
				int tempoinstrumento) { // define o instrumento
			channels[0].programChange(tempobancodadosinstrumento,
					tempoinstrumento);
		}

		void AvaicomerB(bicho A, bicho B) {

			if (A.vida == true) {

				anguloAB = calculaanguloAB(anguloAB, A, B);

				A.ponto1xdirfome = desloca(A.ponto1xdirfome, 1, anguloAB,
						A.atracao * -1);
				A.ponto1ydirfome = desloca(A.ponto1ydirfome, 2, anguloAB,
						A.atracao * -1);

			}

		}

		void AfogedeB(bicho A, bicho B) {

			if (A.chance > 6 || A.chance % 2 == 0) {

				anguloAB = calculaanguloAB(anguloAB, A, B);

				desenhaforma(A.pontox[2], A.pontoy[2], B.angulo2 + A.angulo2
						* -1 * 2, 3, A.formadiam, 1, A.pesodalinha * 0.5f, 0,
						0, A.corlinha, A.corlinhaalpha); // EFEITO DE FUGA

				A.angulo2 = A.angulo2 + 2;

				A.ponto1xdirfoge = desloca(A.ponto1xdirfoge, 1, anguloAB,
						A.atracao);
				A.ponto1ydirfoge = desloca(A.ponto1ydirfoge, 2, anguloAB,
						A.atracao);

			}

		}

		void AatacaB(bicho A, bicho B) {

			if (A.chance == 3 || A.chance >= 5) {

				anguloAB = calculaanguloAB(anguloAB, A, B);

				desenhaforma(A.pontox[2], A.pontoy[2], B.angulo2 + A.angulo2
						* 2, 2, A.formadiam, 1, A.pesodalinha * 0.5f, 0, 0,
						A.corlinha, A.corlinhaalpha); // EFEITO DE CAÇA

				A.angulo2 = A.angulo2 + 2;

				A.ponto1xdirpersegue = desloca(A.ponto1xdirpersegue, 1,
						anguloAB, A.atracao * -1);
				A.ponto1ydirpersegue = desloca(A.ponto1ydirpersegue, 2,
						anguloAB, A.atracao * -1);

			}

		}

		void AquercruzarB(bicho A, bicho B) {

			if (A.chance >= 3) {

				anguloAB = calculaanguloAB(anguloAB, A, B);

				desenhaforma(A.pontox[A.numerodepontos - 2],
						A.pontoy[A.numerodepontos - 2], A.angulo1, 1,
						A.formadiam, 1, A.pesodalinha * 0.5f, 0, 0, A.corlinha,
						A.corlinhaalpha);

				A.ponto1xdirtarado = desloca(A.ponto1xdirtarado, 1, anguloAB,
						A.atracao * -1 * (B.energia / 20));
				A.ponto1ydirtarado = desloca(A.ponto1ydirtarado, 2, anguloAB,
						A.atracao * -1 * (B.energia / 20));

			}

		}

		void AdesinteressaB(bicho A, bicho B) {

			anguloAB = calculaanguloAB(anguloAB, A, B);

			A.ponto1xdirsatisfeito = desloca(A.ponto1xdirsatisfeito, 1,
					anguloAB, A.atracao);
			A.ponto1ydirsatisfeito = desloca(A.ponto1ydirsatisfeito, 2,
					anguloAB, A.atracao);

		}

		void AgangueB(bicho A, bicho B) {

			anguloAB = calculaanguloAB(anguloAB, A, B);

			A.ponto1xdirgangue = desloca(A.ponto1xdirgangue, 1, anguloAB,
					A.atracao * 2 * -1); // deixei assim para ficar com 0.5 a
											// mais do que o movimento de
											// separação
			A.ponto1ydirgangue = desloca(A.ponto1ydirgangue, 2, anguloAB,
					A.atracao * 2 * -1);

			A.bando = A.bando + B.energia; // soma energias para tomar decisão
											// de bando

			float somametadecaminhox = ((((B.pontox[B.numerodepontos - 1] + A.pontox[A.numerodepontos - 1]) / 2) - A.pontox[A.numerodepontos - 1]) / 50);
			float somametadecaminhoy = ((((B.pontoy[B.numerodepontos - 1] + A.pontoy[A.numerodepontos - 1]) / 2) - A.pontoy[A.numerodepontos - 1]) / 50);

			A.pontox[A.numerodepontos - 1] = movimentorandomico(
					A.pontox[A.numerodepontos - 1], -1, 1, 1,
					somametadecaminhox, A.velocidadeauto, 0); // faz com que os
																// rabos se unam
			A.pontoy[A.numerodepontos - 1] = movimentorandomico(
					A.pontoy[A.numerodepontos - 1], -1, 1, 1,
					somametadecaminhoy, A.velocidadeauto, 0);

			A.corr = movimentaeasing(A.corr, B.corr, 70); // compartilha a cor
															// do contorno
			A.corg = movimentaeasing(A.corg, B.corg, 70);
			A.corb = movimentaeasing(A.corb, B.corb, 70);

			A.corlinhar = movimentaeasing(A.corlinhar, B.corlinhar, 70); // compartilha
																			// a
																			// cor
																			// da
																			// corlinha
			A.corlinhag = movimentaeasing(A.corlinhag, B.corlinhag, 70);
			A.corlinhab = movimentaeasing(A.corlinhab, B.corlinhab, 70);

			A.angulorabo = A.angulorabo + 10; // roda forma

			A.nota = (int) (movimentaeasing(A.nota, B.nota, 2) + ((int) (random(
					4, 6)) * 0.1)); // compartilha a nota

			A.instrumento = (int) (movimentaeasing(A.instrumento,
					B.instrumento, 2) + ((int) (random(4, 6)) * 0.1)); // compartilha
																		// o
																		// instrumento

			A.bancodadosinstrumento = B.bancodadosinstrumento; // compartilha o
																// banco de
																// dados de
																// instrumentos

		}

		void AtemnojodeB(bicho A, bicho B) {

			anguloAB = calculaanguloAB(anguloAB, A, B);

			A.ponto1xdirafastapodre = desloca(A.ponto1xdirafastapodre, 1,
					anguloAB, A.atracao * 0.5f); // deixei assim para ficar com
													// a metade do valor
			A.ponto1ydirafastapodre = desloca(A.ponto1ydirafastapodre, 2,
					anguloAB, A.atracao * 0.5f);

		}

		void AcruzacomB(bicho A, bicho B) {

			A.pontox[1] = B.pontox[1]; // une os pontos durante cruzamento
			A.pontoy[1] = B.pontoy[1];
			A.pontox[A.numerodepontos - 1] = (B.pontox[B.numerodepontos - 1] + A.pontox[A.numerodepontos - 1]) / 2;
			A.pontoy[A.numerodepontos - 1] = (B.pontoy[B.numerodepontos - 1] + A.pontoy[A.numerodepontos - 1]) / 2;
			A.pontox[A.numerodepontos - 2] = (B.pontox[B.numerodepontos - 2] + A.pontox[A.numerodepontos - 2]) / 2;
			A.pontoy[A.numerodepontos - 2] = (B.pontoy[B.numerodepontos - 2] + A.pontoy[A.numerodepontos - 2]) / 2;

			A.angulo1 = A.angulo1 + A.velocidadeauto;
			A.angulo2 = A.angulo2 + A.velocidadeauto;
			A.angulorabo = A.angulorabo + A.velocidadeauto;

			A.desenhaforma(A.pontox[1], A.pontoy[1], A.angulo1, 1, A.formadiam,
					0.25f, A.pesodalinha, A.cor, A.coralpha * 0.75f,
					A.corlinha, A.corlinhaalpha); // resposta visual do toque
			A.desenhaforma(A.pontox[A.numerodepontos - 2],
					A.pontoy[A.numerodepontos - 2], A.angulo1, 1, A.formadiam,
					1, A.pesodalinha * 0.5f, 0, 0, A.corlinha, A.corlinhaalpha); // quando
																					// cruza
																					// o
																					// órgão
																					// se
																					// destaca

			A.velocidadeauto = A.velocidadeauto + 0.04f; // aumenta a velocidade
															// aos poucos
															// durante
															// cruzamento
			A.tamanhoformadiam = A.tamanhoformadiam + 0.01f; // se está se dando
																// bem, aumenta
																// a área de
																// contato
																// (causa um
																// efeito de
																// aceleramento
																// no domínio do
																// espaço por
																// uma forma
																// só!!!)

			if (som == true && cruza % 20 == 0) { // reproduz barulho em
													// intervalos regulares
				pegainstrumento(A.bancodadosinstrumento, A.instrumento); // toca
																			// o
																			// som
				tocanota(A.nota, volume, duracao);
				desenhaforma(A.pontox[A.numerodepontos - 1],
						A.pontoy[A.numerodepontos - 1], A.angulorabo, 1,
						A.formadiam, 0.5f, A.pesodalinha, A.cor,
						A.coralpha / 3, A.corlinha, A.corlinhaalpha); // para
																		// saber
																		// qual
																		// é o
																		// que
																		// está
																		// fazendo
																		// barulho
			}

			A.cruza = A.cruza + 1;

		}

		void AreproduzcomB(bicho A, bicho B) {

			A.energia = A.energia - 1; // perde energia para gerar filho
			A.tamanhoformadiam = A.tamanhoformadiam + 0.01f; // se está se dando
																// bem, aumenta
																// a área de
																// contato
			A.chance = A.chance + 1; // evolui a personalidade conforme ganha
										// experiência. Teve filho muda uma
										// direto
			A.maturidade = 0;

			A.pontox[1] = A.pontox[1] + 100 * ((int) (random(-2, 2))); // depois
																		// separa
																		// os
																		// pontos
																		// e se
																		// afastam
			A.pontoy[1] = A.pontoy[1] + 100 * ((int) (random(-2, 2)));

			A.cruza = 0;

		}

		void AbrigacomB(bicho A, bicho B) {

			anguloAB = calculaanguloAB(anguloAB, A, B);

			A.sova = A.sova + 1;
			A.evoluichance = A.evoluichance + 0.5f;
			A.velocidadeauto = 7;
			A.energia = A.energia - 1;

			desenhaforma(A.pontox[1], A.pontoy[1], A.angulo1 * -1 * 2, 2,
					A.formadiam, 0.25f, A.pesodalinha, A.cor,
					A.coralpha * 0.75f, A.corlinha, A.corlinhaalpha); // resposta
																		// visual
																		// do
																		// toque

			if (som == true) { // se o som estiver ligado
				pegainstrumento(A.bancodadosinstrumento, A.instrumento); // toca
																			// o
																			// som
				tocanota(A.nota, volume, duracao);
				desenhaforma(A.pontox[A.numerodepontos - 1],
						A.pontoy[A.numerodepontos - 1], A.angulorabo, 1,
						A.formadiam, 0.5f, A.pesodalinha, A.cor,
						A.coralpha / 3, A.corlinha, A.corlinhaalpha); // para
																		// saber
																		// qual
																		// é o
																		// que
																		// está
																		// fazendo
																		// barulho
			}

			A.pontox[1] = A.pontox[1] + (cos(anguloAB)) * 5;
			A.pontoy[1] = A.pontoy[1] + (sin(anguloAB)) * 5;

		}

		void AcomeB(bicho A, bicho B) {

			if (A.vida == true) {

				desenhaforma(A.pontox[1], A.pontoy[1], B.angulo1 + A.angulo1
						* -1 * 2, 2, A.formadiam, 0.5f, A.pesodalinha, 0, 0,
						A.corlinha, A.corlinhaalpha);

				A.angulo1 = A.angulo1 + 8; // roda mais rápido quando se
											// alimenta
				A.tamanhoformadiam = A.tamanhoformadiam + 0.004f; // se está se
																	// dando
																	// bem,
																	// aumenta a
																	// área de
																	// contato
				A.evoluichance = A.evoluichance + 0.01f;
				A.energia = A.energia + 0.04f;
				B.energia = B.energia - 0.04f;

			}

		}

		void AcainateiadeB(bicho A, bicho B, int numerodoponto) {

			A.pontox[1] = B.pontox[numerodoponto];
			A.pontoy[1] = B.pontoy[numerodoponto];
			A.velocidadeauto = B.velocidadeauto;
			B.energia = B.energia + 0.01f;
			A.energia = A.energia - 0.01f;
			A.formadiam = movimentaeasing(A.formadiam, B.formadiam * 0.5f, 100);
			B.tamanhoformadiam = B.tamanhoformadiam + 0.01f;
			A.tamanhoformadiam = 0;
			A.pesodalinha = movimentaeasing(A.pesodalinha, B.pesodalinha, 100);
			A.corr = movimentaeasing(A.corr, B.corr, 100);
			A.corg = movimentaeasing(A.corg, B.corg, 100);
			A.corb = movimentaeasing(A.corb, B.corb, 100);
			A.corlinhar = movimentaeasing(A.corlinhar, B.corlinhar, 100);
			A.corlinhag = movimentaeasing(A.corlinhag, B.corlinhag, 100);
			A.corlinhab = movimentaeasing(A.corlinhab, B.corlinhab, 100);
			A.nota = (int) (movimentaeasing(A.nota, B.nota, 2) + ((int) (random(
					4, 6)) * 0.1));
			A.instrumento = (int) (movimentaeasing(A.instrumento,
					B.instrumento, 2) + ((int) (random(4, 6)) * 0.1));
			A.bancodadosinstrumento = B.bancodadosinstrumento;

		}

		int contatoAeB(bicho A, bicho B) { // PARA DEFINIR AS DUAS DISTÂNCIAS DE
											// CONTATO

			float distanciaAB = dist(A.pontox[1], A.pontoy[1], B.pontox[1],
					B.pontoy[1]); // calcula o tamanho da diagonal entre os
									// bichos

			if (distanciaAB > (A.formadiam + B.formadiam) / 2
					&& distanciaAB < (A.formadiam + B.formadiam) * 4) {
				return 1;
			} else if (distanciaAB < (A.formadiam + B.formadiam) / 2) {
				return 2;
			} else {
				return 0;
			}

		}

		boolean AeBseagruparem(bicho A, bicho B) { // PARA DEFINIR A CONDIÇÃO DE
													// GANGUE

			float distanciaAB = dist(A.pontox[1], A.pontoy[1], B.pontox[1],
					B.pontoy[1]); // calcula o tamanho da diagonal entre os
									// bichos

			if (distanciaAB > (A.formadiam + B.formadiam) * 1.5
					&& A.formarabo == B.formarabo) {
				return true;
			} else {
				return false;
			}

		}

		boolean AmaisfracoqueB(bicho A, bicho B) {

			if (A.energia < B.energia || A.energia > B.energia
					&& A.energia < B.finalbando) {
				return true;
			} else {
				return false;
			}

		}

		boolean AeBmaduros(bicho A, bicho B) {

			if (A.maturidade > A.pontomadurocruza
					&& B.maturidade > B.pontomadurocruza) {
				return true;
			} else {
				return false;
			}

		}

		boolean ApassarpelospontosdeB(bicho A, bicho B, int nponto) {

			if (A.vida == true && B.vida == true && A.forma1 == B.forma2
					&& A.pontox[1] >= B.pontox[nponto] - 10
					&& A.pontox[1] <= B.pontox[nponto] + 10
					&& A.pontoy[1] >= B.pontoy[nponto] - 10
					&& A.pontoy[1] <= B.pontoy[nponto] + 10) {
				return true;
			} else {
				return false;
			}

		}

		void desenhalinhacontato(float pontoxA, float pontoyA, float pontoxB,
				float pontoyB) {

			if (commetadados >= 3) { // mostrador de distâncias de colisão
				noFill(); // desenho que liga os que interagem
				stroke(corlinhar, corlinhag, corlinhab, corlinhaalpha);
				strokeWeight(2);
				beginShape();
				curveVertex(pontoxA, pontoyA);
				curveVertex(pontoxA, pontoyA);
				curveVertex(pontoxB, pontoyB);
				curveVertex(pontoxB, pontoyB);
				endShape();
			}

		}

		boolean mousetocarnessebicho() {

			if (posicaofinalmouseX > pontox[1] - (formadiam / 2)
					&& posicaofinalmouseX < pontox[1] + (formadiam / 2)
					&& posicaofinalmouseY > pontoy[1] - (formadiam / 2)
					&& posicaofinalmouseY < pontoy[1] + (formadiam / 2)
					||

					posicaofinalmouseX > pontox[2] - (formadiam / 4)
					&& posicaofinalmouseX < pontox[2] + (formadiam / 4)
					&& posicaofinalmouseY > pontoy[2] - (formadiam / 4)
					&& posicaofinalmouseY < pontoy[2] + (formadiam / 4)
					||

					posicaofinalmouseX > pontox[numerodepontos - 2]
							- (formadiam / 6)
					&& posicaofinalmouseX < pontox[numerodepontos - 2]
							+ (formadiam / 6)
					&& posicaofinalmouseY > pontoy[numerodepontos - 2]
							- (formadiam / 6)
					&& posicaofinalmouseY < pontoy[numerodepontos - 2]
							+ (formadiam / 6)
					||

					posicaofinalmouseX > pontox[numerodepontos - 1]
							- (formadiam / 10)
					&& posicaofinalmouseX < pontox[numerodepontos - 1]
							+ (formadiam / 10)
					&& posicaofinalmouseY > pontoy[numerodepontos - 1]
							- (formadiam / 10)
					&& posicaofinalmouseY < pontoy[numerodepontos - 1]
							+ (formadiam / 10)) {

				return true;
			} else {
				return false;
			}

		}

		void interagemouse() {

			if (mousePressed == true) {

				float distponto1mouse = dist(pontox[1], pontoy[1],
						posicaofinalmouseX, posicaofinalmouseY);
				float anguloentrebichoemouse = atan2(
						(pontoy[1] - (posicaofinalmouseY)),
						(pontox[1] - (posicaofinalmouseX))); // calcula o ângulo
																// entre os
																// pontos
																// (trigonometria)

				if (mouseButton == LEFT) {

					if (vida == true) {

						if (reagemouse == 1 && distponto1mouse < 200) { // reage
																		// ao
																		// mouse
																		// à
																		// distância
																		// afastando

							ponto1xdirmouse = desloca(ponto1xdirmouse, 1,
									anguloentrebichoemouse, 1);
							ponto1ydirmouse = desloca(ponto1ydirmouse, 2,
									anguloentrebichoemouse, 1);

							desenhaforma(posicaofinalmouseX,
									posicaofinalmouseY, angulo1, 1, 50,
									random(0.5f, 1), pesodalinha, cor,
									coralpha / 5, corlinha, corlinhaalpha / 5);

							desenhalinhacontato(pontox[1], pontoy[1],
									posicaofinalmouseX, posicaofinalmouseY);

						} else if (reagemouse == 2) { // reage ao mouse à
														// distância atraindo

							if (distponto1mouse < 300 && distponto1mouse > 100) {

								ponto1xdirmouse = desloca(ponto1xdirmouse, 1,
										anguloentrebichoemouse, -1);
								ponto1ydirmouse = desloca(ponto1ydirmouse, 2,
										anguloentrebichoemouse, -1);

								desenhaforma(posicaofinalmouseX,
										posicaofinalmouseY, -angulo1, forma1,
										formadiam, 0.5f, pesodalinha, cor,
										coralpha / 3, corlinha,
										corlinhaalpha / 3);

								desenhalinhacontato(pontox[1], pontoy[1],
										posicaofinalmouseX, posicaofinalmouseY);

							} else if (distponto1mouse < 100) {

								ponto1xdirmouse = desloca(ponto1xdirmouse, 1,
										anguloentrebichoemouse, 1);
								ponto1ydirmouse = desloca(ponto1ydirmouse, 2,
										anguloentrebichoemouse, 1);

							}
						}

						if (mousetocarnessebicho()) {

							desenhaforma(pontox[1], pontoy[1], angulo1, forma1,
									formadiam, 0.5f, pesodalinha, cor,
									coralpha, corlinha, corlinhaalpha);
							velocidadeauto = velocidadeauto + 3;
							pegainstrumento(bancodadosinstrumento, instrumento);
							tocanota(nota, volume, duracao);
							desenhaforma(pontox[numerodepontos - 1],
									pontoy[numerodepontos - 1], angulorabo, 1,
									formadiam, 0.5f, pesodalinha, cor,
									coralpha / 3, corlinha, corlinhaalpha); // para
																			// saber
																			// qual
																			// é
																			// o
																			// que
																			// está
																			// fazendo
																			// barulho

							if (keyPressed) {
								if (key == 'u' || key == 'U') {
									eubicho = tempbicho;
								}
							}

						}
					}

				} else if (mouseButton == RIGHT) {

					if (mousetocarnessebicho()) {

						if (vida == true) { // mata
							desenhaforma(pontox[1], pontoy[1], angulo1, forma1,
									formadiam, 0.5f, pesodalinha, cor,
									coralpha, corlinha, corlinhaalpha);
							energia = -1;

						} else if (vida == false) { // apodrece
							desenhaforma(pontox[1], pontoy[1], angulo1, forma1,
									formadiam, 0.5f, pesodalinha, cor,
									coralpha, corlinha, corlinhaalpha);
							energia = energia - 1;

						}
					}
				}
			}
		}

		float calculaanguloAB(float angulo, bicho A, bicho B) { // calcula o
																// ângulo entre
																// os pontos
																// (trigonometria)

			angulo = atan2((A.pontoy[1] - B.pontoy[1]),
					(A.pontox[1] - B.pontox[1]));
			return angulo;

		}

	}

	void alteracoesforadna() {

		meubicho.maturidade = 0; // zera a maturidade quando nasce
		meubicho.geracao = (abs(bichoculpado.geracao + bichaculpada.geracao) / 2)
				- (abs(bichoculpado.geracao - bichaculpada.geracao) / 2) + 1; // seleciona
																				// o
																				// menor
																				// número
																				// da
																				// geração
																				// dos
																				// pais
																				// e
																				// soma
																				// 1

		if (bichoculpado.forma2 == bichaculpada.forma2) { // se o pescoço dos
															// pais for igual o
															// filho ganha
															// energia
			meubicho.energia = meubicho.energia + 5;
			if (random(1) < 0.25) { // chance de 1/4 de mutação
				meubicho.forma1 = bichoculpado.forma2;
			} else {
				meubicho.forma2 = bichoculpado.forma2;
			}

			if (bichoculpado.formarabo == bichaculpada.formarabo) { // se o rabo
																	// dos pais
																	// for igual
																	// o filho
																	// ganha
																	// mais
																	// energia
				meubicho.energia = meubicho.energia + 5;

				if (bichoculpado.formarabo == bichoculpado.forma2) { // se o
																		// pescoço
																		// e o
																		// rabo
																		// dos
																		// pais
																		// forem
																		// todos
																		// iguais
																		// a
																		// forma
																		// pode
																		// mudar
																		// a
																		// cabeça
																		// (mutação)
																		// :D
					meubicho.forma1 = bichoculpado.forma2;
					meubicho.energia = meubicho.energia + 10;
				} else if (random(1) < 0.25) { // se não, chance de 1/4 de
												// mutação
					meubicho.forma1 = bichoculpado.formarabo;
				} else {
					meubicho.formarabo = bichoculpado.formarabo;
				}
			}
		}

		meubicho.pontoy[1] = (bichoculpado.pontoy[1] + bichaculpada.pontoy[1]) / 2;
		meubicho.pontox[1] = (bichoculpado.pontox[1] + bichaculpada.pontox[1]) / 2;

		for (int i = 2; i < meubicho.numerodepontos; i++) {
			meubicho.pontoy[i] = meubicho.pontoy[1];
		}
		for (int j = 2; j < meubicho.numerodepontos; j++) {
			meubicho.pontox[j] = meubicho.pontox[1];
		}

		meubicho.maxformadiam = (bichoculpado.maxformadiam + bichaculpada.maxformadiam) / 2;

		// MUTAÇÕES fora do DNA:

		if (random(1) < chancemutacao) { // mudar o tamanho máximo da cabeça
			meubicho.maxformadiam = meubicho.maxformadiam
					+ (int) (random(-100, 200));
			if (meubicho.maxformadiam < 20) {
				meubicho.maxformadiam = 20;
			}
		}

	}

	void ambiente() {

		posicaofinalmouseX = (mouseX / escala) - ((width / escala) - width) / 2;
		posicaofinalmouseY = (mouseY / escala) - ((height / escala) - height)
				/ 2;

		if (escalaauto == true) {
			escala = escala
					+ (vezesescala * (1 - (0.0022f * (total))) - escala) / 8; // escala
																				// automática
																				// de
																				// acordo
																				// com
																				// o
																				// número
																				// de
																				// bichos
																				// (0.0033
																				// para
																				// chegar
																				// em
																				// 30%
																				// com
																				// 200)
																				// (0.0022
																				// para
																				// chegar
																				// em
																				// 30%
																				// com
																				// 300)
			if (escala < 0.1) {
				escala = 0.1f;
			}
		}
		scale(escala); // escala de tuuuuudo
		translate(((width / escala) - width) / 2,
				((height / escala) - height) / 2);
		frameRate(quadrosporsegundo); // configs iniciais de frame/seg
		int sec = millis() / 1000; // contagem de tempo

		if (ligasaida == true) {
			saida.println("tempo," + sec + ", ret," + nr + ", tri," + nt
					+ ", ell," + ne + ", total," + total); // meu log para
															// registrar o
															// balanço entre
															// tipos
		}

		if (camerasfuncionando) {

			if (cam.available() == true) {
				cam.read();
				cam.loadPixels();
			}
		}

		if (mudadia == 0) { // para mudar a cor do fundo de acordo com a média
							// das cores dos bichos

			tempcorfundor = 0;
			tempcorfundog = 0;
			tempcorfundob = 0;

			for (int i = 0; i < bichos.size(); i++) {
				tempcorfundor = tempcorfundor + bichos.get(i).corr;
				tempcorfundog = tempcorfundog + bichos.get(i).corg;
				tempcorfundob = tempcorfundob + bichos.get(i).corb;
			}

			corfundor = corfundor
					+ (((tempcorfundor / bichos.size()) - corfundor) / 30);
			corfundog = corfundog
					+ (((tempcorfundog / bichos.size()) - corfundog) / 30);
			corfundob = corfundob
					+ (((tempcorfundob / bichos.size()) - corfundob) / 30);

			background(corfundor, corfundog, corfundob, 50); // para mudar fundo

		} else if (mudadia == 1) {

			tempcorfundor = 0;
			tempcorfundog = 0;
			tempcorfundob = 0;

			for (int i = 0; i < bichos.size(); i++) {
				tempcorfundor = tempcorfundor + bichos.get(i).corr;
				tempcorfundog = tempcorfundog + bichos.get(i).corg;
				tempcorfundob = tempcorfundob + bichos.get(i).corb;
			}

			corfundor = corfundor
					+ (((255 - (tempcorfundor / bichos.size())) - corfundor) / 30);
			corfundog = corfundog
					+ (((255 - (tempcorfundog / bichos.size())) - corfundog) / 30);
			corfundob = corfundob
					+ (((255 - (tempcorfundob / bichos.size())) - corfundob) / 30);

			background(corfundor, corfundog, corfundob, 50); // para mudar fundo

		} else if (mudadia == 2) {

			if (camerasfuncionando) {

				if (cam2.available() == true) {
					cam2.read();
				}

				// pushMatrix();
				// scale(-1.0, 1.0);

				// image(cam2, ((0/escala)-((width/escala)-width)/2)-(width),
				// (0/escala)-((height/escala)-height)/2, width/escala,
				// height/escala);
				image(cam2, (0 / escala) - ((width / escala) - width) / 2,
						(0 / escala) - ((height / escala) - height) / 2, width
								/ escala, height / escala);

				// The following does the same, and is faster when just drawing
				// the image without any additional resizing, transformations,
				// or tint.
				// set(0, 0, cam);

				// image(cam, ((0/escala)-((width/escala)-width)/2)-(width),
				// (0/escala)-((height/escala)-height)/2, width/escala,
				// height/escala);

				// popMatrix();

			} else {
				mudadia = mudadia + 1;
			}

		} else if (mudadia == 3) {

			background(0); // para a cor do fundo se preta

		} else if (mudadia == 4) {

			background(255); // para a cor do fundo se branca

		} else if (mudadia == 5) {

			background(255); // para a cor do fundo se branca (mudança nos
								// bichos)

		} else if (mudadia == 6) {

			// sem background

		} else { // para alternar a cor do fundo entre branco e preto (tons de
					// cinza)

			corfundo = 100;

			if (sentidodia == 1) { // dia e noite
				corfundo = corfundo - 0.01f;
				if (corfundo <= 5) {
					sentidodia = 2;
				}
			} else if (sentidodia == 2) { // noite e dia
				corfundo = corfundo + 0.01f;
				if (corfundo >= 250) {
					sentidodia = 1;
				}
			}

			background(corfundo); // para mudar fundo

		}

		total = nr + nt + ne; // estatística: número total de bichos
		// total = bichos.size(); //estatística: número total de bichos. Essa
		// versão soma os bichos mortos e ainda não podres

		float energiatotal = 0;
		for (int f = 0; f < bichos.size(); f++) {
			energiatotal = energiatotal + bichos.get(f).energia;
		}
		adaptacaomedia = energiatotal / bichos.size();

		if (commetadados >= 1) { // mostrador de estatísticas
			fill(255, 255, 255, 255); // texto em
			textSize(11 / escala); // varia de tamanho de acordo com a escala
			text("Fundo: " + mudadia + "    Som: " + som + "    Mouse: "
					+ reagemouse + "    Modo Extermínio: " + modoexterminio
					+ "    Retângulos: " + nr + "   Triângulos: " + nt
					+ "   Elipses: " + ne + "   Total: " + total + "   Saída: "
					+ ligasaida + "    Tempo: " + sec + "    FPS: "
					+ quadrosporsegundo + "    Escala Auto: " + escalaauto
					+ "    Escala: " + (int) (escala * 100) + "    Câmeras: "
					+ camerasfuncionando + "    Pega Cor: " + pegacor
					+ "    Busca Cor: " + procurapelacor
					+ "    Precisão da Cor: " + (int) (niveldeprecisaodacor),
					(10 / escala) - ((width / escala) - width) / 2,
					(20 / escala) - ((height / escala) - height) / 2);
		}

	}

	public void keyPressed() { // WASD se apertar vai

		switch (key) {
		case ('w'): // controle de deslocamento de um bicho
		case ('W'):
			result |= NORTH;
			break;
		case ('d'):
		case ('D'):
			result |= EAST;
			break;
		case ('s'):
		case ('S'):
			result |= SOUTH;
			break;
		case ('a'):
		case ('A'):
			result |= WEST;
			break;

		case ('*'):
			quadrosporsegundo += 1;
			break;
		case ('_'):
			quadrosporsegundo -= 1;
			break;

		case ('0'):
			niveldeprecisaodacor += 1;
			break;
		case ('9'):
			niveldeprecisaodacor -= 1;
			break;

		}

	}

	public void keyReleased() { // se soltar interrompe

		switch (key) {
		case ('w'): // controle de deslocamento de um bicho
		case ('W'):
			result ^= NORTH;
			break;
		case ('d'):
		case ('D'):
			result ^= EAST;
			break;
		case ('s'):
		case ('S'):
			result ^= SOUTH;
			break;
		case ('a'):
		case ('A'):
			result ^= WEST;
			break;

		case ('p'): // enquanto apertar o p ele faz uma cópia em tif da tela
			saveFrame("tela-#####.tif");
			break;

		case ('o'): // para fechar o arquivo de log com as Vs
			if (ligasaida == true) {
				ligasaida = false;
			} else if (ligasaida == false) {
				saida = createWriter("variaveis.txt"); // cria o arquivo txt
														// para que eu faça as
														// análises depois
				ligasaida = true;
			}
			break;

		case ('O'): // para fechar o arquivo de log com as Vs
			if (ligasaida == true) { // para fechar o arquivo de log com as Vs
				saida.flush();
				saida.close();
				exit();
			}
			break;

		case ('V'): // para que os bichos procurem por uma cor parecida com a
					// sua
			if (camerasfuncionando) {
				if (procurapelacor == true) {
					procurapelacor = false;
				} else if (procurapelacor == false) {
					procurapelacor = true;
				}
			}
			break;

		case ('v'): // para que os bichos mudem de cor de acordo com o video
			if (camerasfuncionando) {
				if (pegacor == true) {
					pegacor = false;
				} else if (pegacor == false) {
					pegacor = true;
				}
			}
			break;

		case ('B'): // para ligar câmeras
			if (camerasfuncionando == false) {

				String[] cameras = Capture.list();

				if (cameras.length == 0) {

					camerasfuncionando = false;

					println("There are no cameras available for capture.");
					exit();
				} else {
					println("Available cameras:");
					for (int i = 0; i < cameras.length; i++) {
						println(cameras[i]);
					}

					// The camera can be initialized directly using an
					// element from the array returned by list():
					cam = new Capture(this, cameras[13]);
					cam.start();

					cam2 = new Capture(this, cameras[0]);
					cam2.start();

					camerasfuncionando = true;

				}
			} else {

				cam.stop();
				cam2.stop();
				pegacor = false;
				procurapelacor = false;
				camerasfuncionando = false;
			}
			break;

		case ('z'): // ligar e desligar o som
			if (som == true) {
				som = false;
			} else if (som == false) {
				som = true;
			}
			break;

		case ('f'): // ligar e desligar a escala automática
			if (escalaauto == true) {
				escalaauto = false;
			} else if (escalaauto == false) {
				escalaauto = true;
			}
			break;

		case ('x'): // ligar e desligar o modo extermínio
			if (modoexterminio == true) {
				modoexterminio = false;
			} else if (modoexterminio == false) {
				modoexterminio = true;
			}
			break;

		case ('c'):
			if (commetadados == 0) {
				commetadados = 1;
			} else if (commetadados == 1) {
				commetadados = 2;
			} else if (commetadados == 2) {
				commetadados = 3;
			} else if (commetadados == 3) {
				commetadados = 0;
			}
			break;

		case ('b'): // ligar e desligar a cor de fundo automática
			if (mudadia == 0) {
				mudadia = 1;
			} else if (mudadia == 1) {
				mudadia = 2;
			} else if (mudadia == 2) {
				mudadia = 3;
			} else if (mudadia == 3) {
				mudadia = 4;
			} else if (mudadia == 4) {
				mudadia = 5;
			} else if (mudadia == 5) {
				mudadia = 6;
			} else if (mudadia == 6) {
				mudadia = 7;
			} else if (mudadia == 7) {
				mudadia = 0;
			}
			break;

		case ('m'): // ligar e desligar a reação dos bichos ao mouse
			if (reagemouse == 0) {
				reagemouse = 1;
			} else if (reagemouse == 1) {
				reagemouse = 2;
			} else if (reagemouse == 2) {
				reagemouse = 0;
			}
			break;

		case ('y'): // mudar o bicho que é controlado
			if (meuindexatual < bichos.size() - 1) {
				eubicho = bichos.get(meuindexatual + 1);
			} else {
				eubicho = bichos.get(0);
			}
			break;

		}

	}

	public void mousePressed() {

		if (mouseButton == LEFT) {
			if (keyPressed) {
				if (key == 'q' || key == 'Q') { // cria um novo bicho e
												// acrescenta à array no local
												// do mouse
					definednagenerico(); // define a sequência de dna
					meudna[0] = (int) ((mouseX / escala) - ((width / escala) - width) / 2); // posição
																							// X
																							// para
																							// alocar
																							// o
																							// bicho
					meudna[1] = (int) ((mouseY / escala) - ((height / escala) - height) / 2); // posição
																								// Y
																								// para
																								// alocar
																								// o
																								// bicho
					nasce(false); // nasce
				} else if (key == 'e' || key == 'E') { // cria um novo bicho com
														// cabeça de elipse no
														// local do mouse
					definednagenerico(); // define a sequência de dna
					meudna[0] = (int) ((mouseX / escala) - ((width / escala) - width) / 2); // posição
																							// X
																							// para
																							// alocar
																							// o
																							// bicho
					meudna[1] = (int) ((mouseY / escala) - ((height / escala) - height) / 2); // posição
																								// Y
																								// para
																								// alocar
																								// o
																								// bicho
					meudna[15] = 1; // forma da cabeça
					nasce(false); // nasce
				} else if (key == 'r' || key == 'R') { // cria um novo bicho com
														// cabeça de retângulo
														// no local do mouse
					definednagenerico(); // define a sequência de dna
					meudna[0] = (int) ((mouseX / escala) - ((width / escala) - width) / 2); // posição
																							// X
																							// para
																							// alocar
																							// o
																							// bicho
					meudna[1] = (int) ((mouseY / escala) - ((height / escala) - height) / 2); // posição
																								// Y
																								// para
																								// alocar
																								// o
																								// bicho
					meudna[15] = 2; // forma da cabeça
					nasce(false); // nasce
				} else if (key == 't' || key == 'T') { // cria um novo bicho com
														// cabeça de triângulo
														// no local do mouse
					definednagenerico(); // define a sequência de dna
					meudna[0] = (int) ((mouseX / escala) - ((width / escala) - width) / 2); // posição
																							// X
																							// para
																							// alocar
																							// o
																							// bicho
					meudna[1] = (int) ((mouseY / escala) - ((height / escala) - height) / 2); // posição
																								// Y
																								// para
																								// alocar
																								// o
																								// bicho
					meudna[15] = 3; // forma da cabeça
					nasce(false); // nasce
				}
			}
		}

	}

	void controleteclado() {

		if (keyPressed) {
			if (key == '+') { // zoom :D
				escala += 0.01;
			} else if (key == '-') {
				escala -= 0.01;
			}
		}

		switch (result) { // WASD
		case NORTH:
			eubicho.pontoy[1] = eubicho.pontoy[1] - eubicho.velocidade;
			break;
		case EAST:
			eubicho.pontox[1] = eubicho.pontox[1] + eubicho.velocidade;
			break;
		case SOUTH:
			eubicho.pontoy[1] = eubicho.pontoy[1] + eubicho.velocidade;
			break;
		case WEST:
			eubicho.pontox[1] = eubicho.pontox[1] - eubicho.velocidade;
			break;
		case NORTH | EAST:
			eubicho.pontoy[1] = eubicho.pontoy[1] - eubicho.velocidade;
			eubicho.pontox[1] = eubicho.pontox[1] + eubicho.velocidade;
			break;
		case NORTH | WEST:
			eubicho.pontoy[1] = eubicho.pontoy[1] - eubicho.velocidade;
			eubicho.pontox[1] = eubicho.pontox[1] - eubicho.velocidade;
			break;
		case SOUTH | EAST:
			eubicho.pontoy[1] = eubicho.pontoy[1] + eubicho.velocidade;
			eubicho.pontox[1] = eubicho.pontox[1] + eubicho.velocidade;
			break;
		case SOUTH | WEST:
			eubicho.pontoy[1] = eubicho.pontoy[1] + eubicho.velocidade;
			eubicho.pontox[1] = eubicho.pontox[1] - eubicho.velocidade;
			break;
		} // WASD

		meuindexatual = bichos.indexOf(eubicho);

		strokeWeight(5); // só para saber qual é o seu :) [MEU BICHO]
							// //eubicho.pesodalinha
		noFill();
		stroke(eubicho.cor, 255); // eubicho.coralpha
		ellipse(eubicho.pontox[1], eubicho.pontoy[1], 100, 100);
		stroke(eubicho.corlinha, 255); // eubicho.corlinhaalpha
		ellipse(eubicho.pontox[1], eubicho.pontoy[1], 120, 120);

		if (eubicho.vida == false) {
			if (meuindexatual < bichos.size() - 1) {
				eubicho = bichos.get(meuindexatual + 1);
			} else {
				eubicho = bichos.get(0);
			}
		}

	}

	void definednagenerico() {

		meudna[0] = (int) (random(100, (displayWidth / escala) - 100)); // posição
																		// X
																		// para
																		// alocar
																		// o
																		// bicho
		meudna[1] = (int) (random(100, (displayHeight / escala) - 100)); // posição
																			// Y
																			// para
																			// alocar
																			// o
																			// bicho
		meudna[2] = (int) (random(1, 5)); // velocidadeauto (treme treme) É fixa
											// depois de gerada, só muda em
											// situações específicas
		meudna[3] = (int) (random(40, 100)); // easing (em cascata:
												// easing+o*o*easingaceleration)
		meudna[4] = (int) (random(5, 15)); // easingaceleration
		meudna[5] = (int) (random(50, 200)); // tamanho (retângulo possível)
		meudna[6] = (int) (random(4, 9)); // número de pontos do bicho. O index
											// é 0, o i é 1. Entre 3 e 7
		meudna[7] = (int) (random(1, 5)); // peso da linha do bicho
		meudna[8] = (int) (random(255)); // cores linha R
		meudna[9] = (int) (random(255)); // cores linha G
		meudna[10] = (int) (random(255)); // cores linha B
		meudna[11] = (int) (random(255)); // cores forma R
		meudna[12] = (int) (random(255)); // cores forma G
		meudna[13] = (int) (random(255)); // cores forma B
		meudna[14] = (int) (random(5, 50)); // diâmetro de cada forma (todas
											// elas) a partir do diam
		meudna[15] = (int) (random(1, 4)); // forma da cabeça
		meudna[16] = (int) (random(1, 4)); // forma do pescoço
		meudna[17] = (int) (random(1, 4)); // forma do rabo
		meudna[18] = (int) (random(127)); // para definir o instrumento de cada
											// som. São todos os instrumentos
		meudna[19] = (int) (random(99)); // define a nota que o bicho vai
											// reproduzir em 5 oitavas
											// diferentes
											// //(int)(escalanotas[(int)(random(escalanotas.length))]);
											// //usando uma escala específica
		meudna[20] = (int) (random(30, 50)); // equivale à quantidade de vida
												// inicial
		meudna[21] = (int) (random(50, 200)); // define um ponto de maturidade
												// para que possam cruzar
												// novamente
		meudna[22] = (int) (random(1, 11)); // pode fazer ou não algo: surtar,
											// caçar, fugir (1:chapado, 2:surta,
											// 3:berserker, 4:medroso,
											// 5:violento, 6:esperto / tarado>=3
											// / ataca com bando >=5)

	}

	void definednamesclado() {

		meudna[0] = (int) ((bichoculpado.pontox[1] + bichaculpada.pontox[1]) / 2); // posição
																					// X
																					// para
																					// alocar
																					// o
																					// bicho
		meudna[1] = (int) ((bichoculpado.pontoy[1] + bichaculpada.pontoy[1]) / 2); // posição
																					// Y
																					// para
																					// alocar
																					// o
																					// bicho
		meudna[2] = (int) ((bichoculpado.velocidadeautooriginal + bichaculpada.velocidadeautooriginal) / 2); // velocidadeauto
																												// (treme
																												// treme)
																												// É
																												// fixa
																												// depois
																												// de
																												// gerada,
																												// só
																												// muda
																												// em
																												// situações
																												// específicas
		meudna[3] = (int) ((bichoculpado.easing + bichaculpada.easing) / 2); // easing
																				// (em
																				// cascata:
																				// easing+o*o*easingaceleration)
		meudna[4] = (int) ((bichoculpado.easingaceleration + bichaculpada.easingaceleration) / 2); // easingaceleration
		meudna[5] = (int) ((bichoculpado.tamanho + bichaculpada.tamanho) / 2); // tamanho
																				// (retângulo
																				// possível)
		meudna[6] = (int) ((((float) (bichoculpado.numerodepontos + bichaculpada.numerodepontos)) / 2) + ((int) (random(
				4, 6)) * 0.1)); // número de pontos do bicho. O index é 0. Com
								// todos os pontos separados o menor número é 5.
								// A maluquice é para permitir q filho de
								// números ímpares não seja fixo
		meudna[7] = (int) (((bichoculpado.pesodalinha + bichaculpada.pesodalinha) / 2) + ((int) (random(
				4, 6)) * 0.1)); // peso da linha do bicho
		meudna[8] = (int) ((bichoculpado.corr + bichaculpada.corr) / 2); // cores
																			// linha
																			// R
		meudna[9] = (int) ((bichoculpado.corg + bichaculpada.corg) / 2); // cores
																			// linha
																			// G
		meudna[10] = (int) ((bichoculpado.corb + bichaculpada.corb) / 2); // cores
																			// linha
																			// B
		meudna[11] = (int) ((bichoculpado.corlinhar + bichaculpada.corlinhar) / 2); // cores
																					// forma
																					// R
		meudna[12] = (int) ((bichoculpado.corlinhag + bichaculpada.corlinhag) / 2); // cores
																					// forma
																					// G
		meudna[13] = (int) ((bichoculpado.corlinhab + bichaculpada.corlinhab) / 2); // cores
																					// forma
																					// B
		meudna[14] = (int) ((bichoculpado.formadiam + bichaculpada.formadiam) / 2); // diâmetro
																					// de
																					// cada
																					// forma
																					// (todas
																					// elas)
																					// a
																					// partir
																					// do
																					// diam
		meudna[15] = bichoculpado.forma1; // forma da cabeça
		meudna[16] = (int) (random(1, 4)); // forma do pescoço
		meudna[17] = (int) (random(1, 4)); // forma do rabo
		meudna[18] = (int) ((((float) (bichoculpado.instrumento + bichaculpada.instrumento)) / 2) + ((int) (random(
				4, 6)) * 0.1)); // para definir o instrumento de cada som. São
								// todos os instrumentos
		meudna[19] = (int) ((((float) (bichoculpado.nota + bichaculpada.nota)) / 2) + ((int) (random(
				4, 6)) * 0.1)); // define a nota que o bicho vai reproduzir em 5
								// oitavas diferentes
								// //nota=(int)(escalanotas[(int)(random(escalanotas.length))]);
								// //usando uma escala específica
		meudna[20] = (int) ((bichoculpado.energia + bichaculpada.energia) / 2); // equivale
																				// à
																				// quantidade
																				// de
																				// vida
																				// inicial
		meudna[21] = (int) ((bichoculpado.pontomadurocruza + bichaculpada.pontomadurocruza) / 2); // define
																									// um
																									// ponto
																									// de
																									// maturidade
																									// para
																									// que
																									// possam
																									// cruzar
																									// novamente
		meudna[22] = (int) ((((float) (bichoculpado.chance + bichaculpada.chance)) / 2) + ((int) (random(
				4, 6)) * 0.1)); // pode fazer ou não algo: surtar, caçar, fugir
								// (1:chapado, 2:surta, 3:berserker, 4:medroso,
								// 5:violento, 6:esperto / tarado>=3 / ataca com
								// bando >=5)

		// MUTAÇÕES no DNA de entrada da classe:

		if (random(1) < chancemutacao) { // mudar a velocidade automática
			meudna[2] = meudna[2] + (int) (random(-5, 5));
			if (meudna[2] < 1) {
				meudna[2] = 1;
			}
		}

		if (random(1) < chancemutacao) { // mudar o easing do corpo
			meudna[3] = meudna[3] + (int) (random(-100, 100));
			if (meudna[3] < 0) {
				meudna[3] = 0;
			}
		}

		if (random(1) < chancemutacao) { // mudar o easing entre os pontos do
											// corpo
			meudna[4] = meudna[4] + (int) (random(-10, 10));
			if (meudna[4] < 1) {
				meudna[4] = 1;
			}
		}

		if (random(1) < chancemutacao) { // mudar o espaço que o corpo ocupa
			meudna[5] = meudna[5] + (int) (random(-100, 100));
			if (meudna[5] < 10) {
				meudna[5] = 10;
			}
		}

		if (random(1) < chancemutacao) { // mudar o número de pontos do corpo
			meudna[6] = meudna[6] + (int) (random(-4, 10));
			if (meudna[6] < 4) {
				meudna[6] = 4;
			}
		}

		if (random(1) < chancemutacao) { // mudar a espessura da linha do corpo
			meudna[7] = meudna[7] + (int) (random(-5, 5));
			if (meudna[7] < 1) {
				meudna[7] = 1;
			}
		}

		if (random(1) < chancemutacao) { // mudar a cor
			meudna[8] = meudna[8] + (int) (random(-200, 200));
			meudna[9] = meudna[9] + (int) (random(-200, 200));
			meudna[10] = meudna[10] + (int) (random(-200, 200));
		}

		if (random(1) < chancemutacao) { // mudar a cor
			meudna[11] = meudna[11] + (int) (random(-200, 200));
			meudna[12] = meudna[12] + (int) (random(-200, 200));
			meudna[13] = meudna[13] + (int) (random(-200, 200));
		}

		if (random(1) < chancemutacao) { // mudar a forma da cabeça
			meudna[15] = (int) (random(1, 4));
		}

		if (random(1) < chancemutacao) { // mudar a forma do pescoço
			meudna[16] = (int) (random(1, 4));
		}

		if (random(1) < chancemutacao) { // mudar a forma do rabo
			meudna[17] = (int) (random(1, 4));
		}

		if (random(1) < chancemutacao) { // mudar o instrumento
			meudna[18] = (int) (random(127));
		}

		if (random(1) < chancemutacao) { // mudar a nota
			meudna[19] = (int) (random(127));
		}

		if (random(1) < chancemutacao) { // mudar a quantidade de vida inicial
			meudna[20] = meudna[20] + (int) (random(-20, 20));
			if (meudna[20] < 0) {
				meudna[20] = 0;
			}
		}

		if (random(1) < chancemutacao) { // mudar o ponto de maturação para
											// acasalar
			meudna[21] = meudna[21] + (int) (random(-100, 100));
			if (meudna[21] < 10) {
				meudna[21] = 10;
			}
		}

	}

	public void draw() {

		ambiente();
		iteratordobicho();
		nascemescladna();
		nascerandomicoautomatico();
		controleteclado();

	}

	void iteratordobicho() {

		bichoIt = bichos.iterator(); // iterador de bichos. Ele sabe onde estão
										// os bichos. Evita usar o i++

		while (bichoIt.hasNext()) { // enquanto houver um próximo na lista com
									// relação ao iterador
			tempbicho = (bicho) bichoIt.next(); // o tempbicho é usado para
												// fixar o mesmo bicho para
												// testar a vida, mover, mostrar
												// e morrer
			if (tempbicho.podre == true && bichos.size() > 2) { // condição para
																// morrer

				// if (tempbicho.forma1==1){ne=ne-1;} else if
				// (tempbicho.forma1==2){nr=nr-1;} else if
				// (tempbicho.forma1==3){nt=nt-1;} //contagem morte. Essa versão
				// inclui os bichos mortos ainda não podres

				bichoIt.remove(); // remove os mortos da lista
			} else { // criando e movendo os bichos
				tempbicho.interagemouse(); // função para interagir com o mouse
											// ou tela de toque
				tempbicho.sebate(); // função colisão
				tempbicho.vive(); // função move, ajusta e mostravivo
				tempbicho.morre(); // função morre
				tempbicho.semostra(); // função mostra (vivo ou morto)

			}
		}
	}

	void nasce(boolean comalteracoesforadna) {

		meubicho = new bicho(meudna[0], // posição X para alocar o bicho
				meudna[1], // posição Y para alocar o bicho
				meudna[2], // velocidadeauto (treme treme) É fixa depois de
							// gerada, só muda em situações específicas
				meudna[3], // easing (em cascata: easing+o*o*easingaceleration)
				meudna[4], // easingaceleration
				meudna[5], // tamanho (retângulo possível)
				meudna[6], // número de pontos do bicho. O index é 0. Com todos
							// os pontos separados o menor número é 5
				meudna[7], // peso da linha do bicho
				meudna[8], // cores linha R
				meudna[9], // cores linha G
				meudna[10], // cores linha B
				meudna[11], // cores forma R
				meudna[12], // cores forma G
				meudna[13], // cores forma B
				meudna[14], // diâmetro de cada forma (todas elas) a partir do
							// diam
				meudna[15], // forma da cabeça
				meudna[16], // forma do pescoço
				meudna[17], // forma do rabo
				meudna[18], // para definir o instrumento de cada som. São todos
							// os instrumentos
				meudna[19], // define a nota que o bicho vai reproduzir em 5
							// oitavas diferentes
							// //nota=int(escalanotas[int(random(escalanotas.length))]);
							// //usando uma escala específica
				meudna[20], // equivale à quantidade de vida inicial
				meudna[21], // define um ponto de maturidade para que possam
							// cruzar novamente
				meudna[22] // pode fazer ou não algo: surtar, caçar, fugir
							// (1:chapado, 2:surta, 3:berserker, 4:medroso,
							// 5:violento, 6:esperto / tarado>=3 / ataca com
							// bando >=5)
		);

		if (comalteracoesforadna) {
			alteracoesforadna();
		}

		if (meubicho.forma1 == 1) { // contagem nascimento
			ne = ne + 1;
		} else if (meubicho.forma1 == 2) {
			nr = nr + 1;
		} else if (meubicho.forma1 == 3) {
			nt = nt + 1;
		}

		bichos.add(meubicho); // adiciona ao final da lista bichos

	}

	void nascemescladna() {

		if (filanascimento == 1 && bichos.size() < maximobichos) { // nascimento
																	// da fera
																	// :D
																	// mistura
																	// papai e
																	// mamãe
																	// (não usei
																	// a
																	// meudna[]
																	// e o
																	// nasce()
																	// porque
																	// depois
																	// que cria
																	// e antes
																	// de
																	// adicionar
																	// à fila
																	// tenho que
																	// mudar
																	// pontox/y
																	// [NASCIMENTO]

			definednamesclado();
			nasce(true);

			if (som == true) {
				meubicho.pegainstrumento(meubicho.bancodadosinstrumento,
						meubicho.instrumento); // faz barulho quando nasce
				meubicho.tocanota(meubicho.nota, volume, duracao);
				stroke(meubicho.corlinhar, meubicho.corlinhag,
						meubicho.corlinhab, meubicho.corlinhaalpha); // para
																		// saber
																		// qual
																		// é o
																		// que
																		// está
																		// fazendo
																		// barulho
				fill(meubicho.corr, meubicho.corg, meubicho.corb,
						meubicho.coralpha / 3);
				ellipse(meubicho.pontox[meubicho.numerodepontos - 1],
						meubicho.pontoy[meubicho.numerodepontos - 1],
						meubicho.formadiam * 2, meubicho.formadiam * 2);
			}

			filanascimento = 0; // encerra fila de nascimentos
		}

	}

	void nascerandomicoautomatico() {

		if (ne <= minimoportipo - 1 && modoexterminio == false) { // nasce um
																	// automaticamente
																	// se sobrar
																	// somente
																	// um da
																	// mesma
																	// forma
																	// [GERA 1
																	// AUTOMATICAMENTE
																	// PARA CADA
																	// UMA DAS
																	// FORMAS]
			definednagenerico(); // define a sequência de dna
			meudna[15] = 1; // forma da cabeça
			nasce(false); // nasce
		}

		if (nr <= minimoportipo - 1 && modoexterminio == false) { // nasce um
																	// automaticamente
																	// se sobrar
																	// somente
																	// um da
																	// mesma
																	// forma
			definednagenerico(); // define a sequência de dna
			meudna[15] = 2; // forma da cabeça
			nasce(false); // nasce
		}

		if (nt <= minimoportipo - 1 && modoexterminio == false) { // nasce um
																	// automaticamente
																	// se sobrar
																	// somente
																	// um da
																	// mesma
																	// forma
			definednagenerico(); // define a sequência de dna
			meudna[15] = 3; // forma da cabeça
			nasce(false); // nasce
		}

	}

	public void setup() {

		try { // inicia o setup do som
			setupMIDI();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (comopengl == true) { // para ligar e desligar o opengl mais fácil
			size(displayWidth, displayHeight, OPENGL); // para gerar direto o
														// app
		} else {
			size(displayWidth, displayHeight); // para gerar direto o app
		}

		smooth(8); // suaviza a renderização das linhas e formas geométricas

		result = 0; // WASD resultado final que define a direção

		for (int i = 0; i < NUMERODEBICHOS; i++) { // criando todos os bichos da
													// array
			definednagenerico(); // define a sequência de dna
			nasce(false); // nasce
		}

		eubicho = bichos.get(meuindexatual);

	}

	void setupMIDI() throws MidiUnavailableException { // faz a bagunça do som

		synth = MidiSystem.getSynthesizer();
		// println("Synthesizer: " + synth.getDeviceInfo());

		synth.open();
		// println("Defaut soundbank: " +
		// synth.getDefaultSoundbank().getDescription());

		instruments = synth.getAvailableInstruments();

		// if ( synth.loadAllInstruments( synth.getDefaultSoundbank() ) ) {
		// System.out.println( "There are " + instruments.length +
		// " instruments." );
		// }

		// for ( int i = 0; i < instruments.length; i++ ) {
		// System.out.print( instruments[i].getName() + " >> ");
		// System.out.println( instruments[i].getPatch().getProgram() + "::" +
		// instruments[i].getPatch().getBank() );
		// }

		// println("Instruments: " + instruments.length);
		channels = synth.getChannels();
		// channels[0].programChange((int)(Math.random()*2+1),
		// (int)(Math.random()*127));
		// channels[0].noteOn(69, 100);
		// try {Thread.sleep((long) 800);}catch (InterruptedException e){ }
		// channels[0].noteOff(69);

	}
}
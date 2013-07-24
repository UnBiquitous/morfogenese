package org.unbiquitous.morfogenese;

//MORFOGÊNESE
//obra de arte computacional - 2013 - versão 192
//Tiago Barros Pontes e Silva
//tiagobarros@unb.br

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import com.google.common.collect.Sets;

import processing.core.PApplet;
import processing.video.Capture;

public class Morfogenese extends PApplet {
	private static final long serialVersionUID = 1571384757247787839L;
	
	public float escala = 1; // escala da cena [VS GLOBAIS]
	private float vezesescala = 1; // para multiplicar a escala e deixar menos espaço
							// com poucos
	private boolean escalaauto = true; // para utilizar a escala automática

	private int NUMERODEBICHOS = 30; // cria V do número inicial de bichos randômicos
	private int maximobichos = 300; // número máximo de bichos permitidos. Não há
							// nascimento acima desse número

	private boolean comopengl = false; // para ligar e desligar o opengl mais fácil
	private int minimoportipo = 2; // número mínimo de bichos por tipo (forma) para que
							// um novo semelhante seja gerado
	private boolean modoexterminio = true; // para ligar e desligar a possibilidade de
									// exteminar uma forma
	public int commetadados = 0; // para mostrar os números dos metadados dos bichos
	public int reagemouse = 1; // para reagir ao mouse (possui 3 tempos)

	private int quadrosporsegundo = 30; // taxa de quadros por segundo

	private float chancemutacao = 0.01f;

	public boolean som = true; // liga e desliga o som
	public float volume = 100; // volume geral do som dos bichos
	public float duracao = 0.5f; // duração da nota

	private final static int NORTH = 1; // cria um WASD para mover apenas 1 bicho para
								// testar
	private final static int EAST = 2;
	private final static int SOUTH = 4;
	private final static int WEST = 8;
	private int result; // WASD
	private int meuindexatual; // para controlar o index do bicho

	private Synthesizer synth = null;
	public MidiChannel[] channels = null;

	public Capture cam;
	private Capture cam2;
	public boolean camerasfuncionando;
	public boolean procurapelacor;
	public boolean pegacor;
	public float niveldeprecisaodacor = 100;

	private boolean ligasaida = false; // para ligar e desligar saída de Vs (economizar
								// processamento)
	private PrintWriter saida; // para fazer os gráficos das Vs

	public ArrayList<Bicho> bichos = new ArrayList<Bicho>(); // crio a arraylist
														// bichos. É uma array
														// de objetos. Bicho é a
														// classe

	// float[] escalanotas = {57, 60, 60, 60, 62, 64, 67, 67, 69, 72, 72, 72,
	// 74, 76, 79}; //escala para o som ficar um pouco mais harmônico. Prefiro o
	// som orgânico de engarrafamento

	public int filanascimento = 0; // para enviar o comando de que um deve nascer

	public int mudadia; // para ligar e desligar a mudança dia e noite
	private float corfundo = 0; // cor do background inicial
	private int sentidodia = 2; // para inverter o sentido da cor do fundo
	private float corfundor; // cor do background inicial
	private float corfundog; // cor do background inicial
	private float corfundob; // cor do background inicial
	private float tempcorfundor; // cor do background inicial
	private float tempcorfundog; // cor do background inicial
	private float tempcorfundob; // cor do background inicial
	public float posicaofinalmouseX;
	public float posicaofinalmouseY;

	private Bicho meubicho; // para criar um bicho novo
	public Bicho bichoculpado; // para ser um dos pais do bicho novo
	public Bicho bichaculpada; // para ser um dos pais do bicho novo
	public Bicho eubicho;

	public int nr; // estatística: número de retângulos
	public int nt; // estatística: número de triângulos
	public int ne; // estatística: número de elipses
	private int total; // estatística: número total de bichos

	public float adaptacaomedia;

	protected Runnable M_Listener;
	
	public void setMListener(Runnable M_Listener){
		this.M_Listener = M_Listener;
	}
	
	private void ambiente() {

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
				
				Set<Character> teclasDeCriacao = Sets.newHashSet('Q','E','R','T');
				
				if (teclasDeCriacao.contains(Character.toUpperCase(key))){
					int[] meudna = definednagenerico(); // define a sequência de dna
					meudna[0] = (int) ((mouseX / escala) - ((width / escala) - width) / 2); // posição X para alocar obicho
					meudna[1] = (int) ((mouseY / escala) - ((height / escala) - height) / 2); // posição Y para alocar o bicho
					switch(Character.toUpperCase(key)){
					case 'E' : meudna[15] = 1; // forma da cabeça elipse
						break;
					case 'R' : meudna[15] = 2; // forma da cabeça retângulo
						break;
					case 'T' : meudna[15] = 3; // forma da cabeça triângulo
						break;
					}
					nasce(false,meudna); // nasce
				}
//				else if (){
//					this.M_Listener.execute(key,position);
//				}
			}
		}

	}

	private void controleteclado() {

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

	private int[] definednagenerico() {

		int[] meudna = new int[24];
		
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

		return meudna;
	}

	private int[] definednamesclado() {

		int[] meudna = new int[24];
		
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

		return meudna;
	}

	public void draw() {

		ambiente();
		iteratordobicho();
		nascemescladna();
		nascerandomicoautomatico();
		controleteclado();

	}

	private synchronized void iteratordobicho() {

		Iterator<Bicho> it = bichos.iterator();
		
		while ( it.hasNext()) { // enquanto houver um próximo na lista com
									// relação ao iterador
			Bicho tempbicho = (Bicho) it.next(); // o tempbicho é usado para
												// fixar o mesmo bicho para
												// testar a vida, mover, mostrar
												// e morrer
			if (tempbicho.podre == true && bichos.size() > 2) { // condição para
																// morrer

				// if (tempbicho.forma1==1){ne=ne-1;} else if
				// (tempbicho.forma1==2){nr=nr-1;} else if
				// (tempbicho.forma1==3){nt=nt-1;} //contagem morte. Essa versão
				// inclui os bichos mortos ainda não podres

				it.remove(); // remove os mortos da lista
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

	private void nasce(boolean comalteracoesforadna, int[] meudna) {

		meubicho = new Bicho(this, meudna[0], // posição X para alocar o bicho
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
			alteracoesforadna(meubicho);
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

	private void alteracoesforadna(Bicho meubicho) {

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
	
	private void nascemescladna() {

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

			nasce(true, definednamesclado());

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

	private void nascerandomicoautomatico() {

		if (ne <= minimoportipo - 1 && modoexterminio == false) { 
			// nasce um automaticamente se sobrar somente
			// um da mesma forma
			// [GERA 1 AUTOMATICAMENTE PARA CADA UMA DAS FORMAS]
			int[] meudna = definednagenerico(); // define a sequência de dna
			meudna[15] = 1; // forma da cabeça
			nasce(false,meudna); // nasce
		}

		if (nr <= minimoportipo - 1 && modoexterminio == false) { 
			// nasce um automaticamente se sobrar somente
			// um da mesma forma
			int[] meudna = definednagenerico(); // define a sequência de dna
			meudna[15] = 2; // forma da cabeça
			nasce(false,meudna); // nasce
		}

		if (nt <= minimoportipo - 1 && modoexterminio == false) { 
			// nasce um automaticamente se sobrar somente
			// um da mesma forma
			int[] meudna = definednagenerico(); // define a sequência de dna
			meudna[15] = 3; // forma da cabeça
			nasce(false,meudna); // nasce
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
			nasce(false,definednagenerico()); // nasce
		}

		eubicho = bichos.get(meuindexatual);

	}

	private void setupMIDI() throws MidiUnavailableException { // faz a bagunça do som

		synth = MidiSystem.getSynthesizer();

		synth.open();
		channels = synth.getChannels();
	}
	
	//FIXME: remover este método
	public synchronized void criaUmQualquer(){
		int[] definednagenerico = definednagenerico();
		definednagenerico[0] = 10;
		definednagenerico[1] = 10;
		nasce(false,definednagenerico);
	}
	
	public synchronized void criaBicho(int[] dna){
		nasce(false,dna);
	}
}
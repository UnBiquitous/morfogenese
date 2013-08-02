package org.unbiquitous.morfogenese;

//MORFOGÊNESE
//obra de arte computacional - 2013 - versão 192
//Tiago Barros Pontes e Silva
//tiagobarros@unb.br

import java.awt.Point;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import processing.core.PApplet;
import processing.video.Capture;

import com.google.common.collect.Sets;

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

	public List<Bicho> bichos = new ArrayList<Bicho>(); // crio a arraylist
														// bichos. É uma array
														// de objetos. Bicho é a
														// classe
	public List<Bicho> novosbichos = new ArrayList<Bicho>();

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

	protected MorfoClickListener M_Listener;
	
	public void setMListener(MorfoClickListener M_Listener){
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
				tempcorfundor = tempcorfundor + bichos.get(i).cor.red();
				tempcorfundog = tempcorfundog + bichos.get(i).cor.green();
				tempcorfundob = tempcorfundob + bichos.get(i).cor.blue();
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
				tempcorfundor = tempcorfundor + bichos.get(i).cor.red();
				tempcorfundog = tempcorfundog + bichos.get(i).cor.green();
				tempcorfundob = tempcorfundob + bichos.get(i).cor.blue();
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
			int x = (int) ((mouseX / escala) - ((width / escala) - width) / 2);
			int y = (int) ((mouseY / escala) - ((height / escala) - height) / 2);
			Point mousePosition = new Point(x,y);
			if (keyPressed) {
				if (Sets.newHashSet('Q','E','R','T').contains(Character.toUpperCase(key))){
					DNA meudna = DNA.autoGenese(this, displayWidth, displayHeight, escala);
					meudna.position(mousePosition);
					switch(Character.toUpperCase(key)){
					case 'E' : meudna.formaCabeca(1); // forma da cabeça elipse
						break;
					case 'R' : meudna.formaCabeca(2); // forma da cabeça retângulo
						break;
					case 'T' : meudna.formaCabeca(3); // forma da cabeça triângulo
						break;
					}
					nasce(false,meudna); // nasce
				}
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
		stroke(eubicho.cor.color(), 255); // eubicho.coralpha
		ellipse(eubicho.pontox[1], eubicho.pontoy[1], 100, 100);
		stroke(eubicho.corLinha.color(), 255); // eubicho.corlinhaalpha
		ellipse(eubicho.pontox[1], eubicho.pontoy[1], 120, 120);

		if (eubicho.vida == false) {
			if (meuindexatual < bichos.size() - 1) {
				eubicho = bichos.get(meuindexatual + 1);
			} else {
				eubicho = bichos.get(0);
			}
		}

	}

	public void draw() {
		synchronized (bichos) {
			ambiente();
			iteratordobicho();
			nascemescladna();
			nascerandomicoautomatico();
			controleteclado();
		}
	}

	private  void iteratordobicho() {

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
		bichos.addAll(novosbichos);
		novosbichos.clear();
	}

	private void nasce(boolean comalteracoesforadna, DNA meudna) {
		meubicho = new Bicho(this,meudna);
		
		if (comalteracoesforadna) {
			alteracoesforadna(meubicho);
		}

		if (meubicho.formaCabeca == 1) { // contagem nascimento
			ne = ne + 1;
		} else if (meubicho.formaCabeca == 2) {
			nr = nr + 1;
		} else if (meubicho.formaCabeca == 3) {
			nt = nt + 1;
		}

		novosbichos.add(meubicho);
	}
	
	private void alteracoesforadna(Bicho meubicho) {

		meubicho.maturidade = 0; // zera a maturidade quando nasce
		meubicho.geracao = (abs(bichoculpado.geracao + bichaculpada.geracao) / 2)
				- (abs(bichoculpado.geracao - bichaculpada.geracao) / 2) + 1; 
		// seleciona o menor número da geração dos pais e soma 1

		if (bichoculpado.formaPescoco == bichaculpada.formaPescoco) { 
			// se o pescoço dos pais for igual o filho ganha energia
			meubicho.energia = meubicho.energia + 5;
			if (random(1) < 0.25) { // chance de 1/4 de mutação
				meubicho.formaCabeca = bichoculpado.formaPescoco;
			} else {
				meubicho.formaPescoco = bichoculpado.formaPescoco;
			}

			if (bichoculpado.formaRabo == bichaculpada.formaRabo) { 
				// se o rabo dos pais for igual o filho ganha mais energia
				meubicho.energia = meubicho.energia + 5;

				if (bichoculpado.formaRabo == bichoculpado.formaPescoco) { 
					// se o pescoço e o rabo dos pais forem todos iguais
					// a forma pode mudar a cabeça (mutação) :D
					meubicho.formaCabeca = bichoculpado.formaPescoco;
					meubicho.energia = meubicho.energia + 10;
				} else if (random(1) < 0.25) { // se não, chance de 1/4 de
												// mutação
					meubicho.formaCabeca = bichoculpado.formaRabo;
				} else {
					meubicho.formaRabo = bichoculpado.formaRabo;
				}
			}
		}

		meubicho.pontoy[1] = (bichoculpado.pontoy[1] + bichaculpada.pontoy[1]) / 2;
		meubicho.pontox[1] = (bichoculpado.pontox[1] + bichaculpada.pontox[1]) / 2;

		for (int i = 2; i < meubicho.numeroDePontos(); i++) {
			meubicho.pontoy[i] = meubicho.pontoy[1];
		}
		for (int j = 2; j < meubicho.numeroDePontos(); j++) {
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

		if (filanascimento == 1 && bichos.size() < maximobichos) { 
			// nascimento da fera :D mistura papai e mamãe (não usei
			// a meudna[] e o nasce() porque depois que cria e antes
			// de adicionar à fila tenho que mudar pontox/y [NASCIMENTO]

			//TODO: Não saquei o que esse nasce faz
			nasce(true, DNA.autoGenese(this, displayWidth, displayHeight, escala));

			if (som == true) {
				meubicho.pegainstrumento(meubicho.bancodadosinstrumento,
						meubicho.instrumento); // faz barulho quando nasce
				meubicho.tocanota(meubicho.notaMusical, volume, duracao);
				stroke(meubicho.corLinha.red(), meubicho.corLinha.green(),
						meubicho.corLinha.blue(), meubicho.corLinhaAlpha); // para saber qual é o que está fazendo barulho
				fill(meubicho.cor.red(), meubicho.cor.green(), meubicho.cor.blue(),
						meubicho.corAlpha / 3);
				ellipse(meubicho.pontox[meubicho.numeroDePontos() - 1],
						meubicho.pontoy[meubicho.numeroDePontos() - 1],
						meubicho.diametroDaForma * 2, meubicho.diametroDaForma * 2);
			}

			filanascimento = 0; // encerra fila de nascimentos
		}

	}

	private void nascerandomicoautomatico() {

		DNA meudna = DNA.autoGenese(this, displayWidth, displayHeight, escala);
		if (ne <= minimoportipo - 1 && modoexterminio == false) { 
			// nasce um automaticamente se sobrar somente
			// um da mesma forma
			// [GERA 1 AUTOMATICAMENTE PARA CADA UMA DAS FORMAS]
			meudna.formaCabeca(1);
			nasce(false,meudna); // nasce
		}

		if (nr <= minimoportipo - 1 && modoexterminio == false) { 
			// nasce um automaticamente se sobrar somente
			// um da mesma forma
			meudna.formaCabeca(2);
			nasce(false,meudna); // nasce
		}

		if (nt <= minimoportipo - 1 && modoexterminio == false) { 
			// nasce um automaticamente se sobrar somente
			// um da mesma forma
			meudna.formaCabeca(3);
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
			DNA meudna = DNA.autoGenese(this, displayWidth, displayHeight, escala);
			nasce(false,meudna); // nasce
		}
		bichos.addAll(novosbichos);
		novosbichos.clear();
		eubicho = bichos.get(meuindexatual);

	}

	private void setupMIDI() throws MidiUnavailableException { // faz a bagunça do som

		synth = MidiSystem.getSynthesizer();

		synth.open();
		channels = synth.getChannels();
	}
	
	public void criaBicho(DNA dna){
		synchronized (bichos) {
			nasce(false,dna);
		}
	}
}
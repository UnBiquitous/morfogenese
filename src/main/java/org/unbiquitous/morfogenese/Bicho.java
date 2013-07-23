package org.unbiquitous.morfogenese;

class Bicho { // classe bicho usada lá na array: cria Vs [CLASSE] [BICHO:
				// CADA INDIVÍDUO!!!]

	/**
	 * 
	 */
	private final Morfogenese morfogenese;
	private float positionx; // posição X para alocar o bicho
	private float positiony; // posição Y para alocar o bicho
	private float velocidadeauto; // velocidadeauto (treme treme) É fixa depois de
							// gerada, só muda em situações específicas
	public float easing; // easing (em cascata: easing+o*o*easingaceleration)
	public float easingaceleration; // easingaceleration
	public float tamanho; // tamanho (retângulo possível)
	public int numerodepontos; // número de pontos do bicho. O index é 0. Com todos
						// os pontos separados o menor número é 5
	public float pesodalinha; // peso da linha do bicho
	public float corr; // cores linha R
	public float corg; // cores linha G
	public float corb; // cores linha B
	public float corlinhar; // cores corlinha R
	public float corlinhag; // cores corlinha G
	public float corlinhab; // cores corlinha B
	public float formadiam; // diâmetro de cada forma (todas elas) a partir do diam
	public int forma1; // forma da cabeça
	public int forma2; // forma do pescoço
	public int formarabo; // forma do rabo
	public int instrumento; // para definir o instrumento de cada som
	public int nota; // define a nota que o bicho vai reproduzir
	public float energia; // equivale à quantidade de vida inicial
	public float pontomadurocruza; // define um ponto de maturidade para que possam
							// cruzar novamente
	public int chance; // pode fazer ou não algo: surtar, caçar, fugir (1:chapado,
				// 2:surta, 3:berserker, 4:medroso, 5:violento, 6:esperto /
				// tarado>=3 / ataca com bando >=5)

	public int cor;
	public int corlinha;
	public float coralpha; // alpha da linha associado à vida
	public float corlinhaalpha; // alpha da corlinha associado à vida
	public float velocidade; // velocidade WASD
	private float novachance; // contador para alterar a probabilidade da chance
	private float evoluichance; // para mudar de personalidade conforme ganha
						// experiência
	public float maturidade; // tempo para poder cruzar de novo
	private float angulo1; // para rodar as formas geométricas da cabeça
	private float angulo2; // para rodar as formas geométricas do pescoço
	private float angulorabo; // para rodar as formas geométricas do rabo
	public boolean vida; // vivo ou morto
	public boolean podre; // morto e podre
	public float velocidadeautooriginal; // para que ele tenda a retoranar a essa
									// velocidade
	private int cruza; // para poder ter 2 fazes no cruzamento
	private float bando; // V que determina se a energia coletiva é maior que a do
					// inimigo
	private float finalbando; // V que determina se a energia coletiva é maior que a
						// do inimigo
	private float tamanhoformadiam; // para que eles decidam a área de interação e
							// aprendam com os próprios erros
	private int sova; // V para o bicho perceber que está apanhando
	private int contagemmorte; // para contar os mortos
	public int bancodadosinstrumento; // para definidr o banco de dados do
								// instrumento
	public int geracao; // para saber de qual geração é o bicho
	private float atracao; // para alterar automaticamente o fator de atração
	private float minhaadaptacao;
	public float maxformadiam;
	private int numerodepontosdalinha;
	private float anguloAB;

	public float[] pontox; // cria a lista de pontos x e y
	public float[] pontoy;

	private float[] distx1; // cria a lista de distâncias entre os pontos de cada
					// bicho
	private float[] disty1;

	private int[] ordemxy;

	private float ponto1xdir; // influencia o ponto1 do bicho (por enquanto uso no
						// wandering da novachance)
	private float ponto1ydir;

	private float ponto1xdircor; // influencia o ponto1 do bicho: busca cor
	private float ponto1ydircor;

	private float ponto1xdirmouse; // influencia o ponto1 do bicho: mouse
	private float ponto1ydirmouse;

	private float resultantex; // para definir a influência do movimento
	private float resultantey;

	private float finalponto1xdirfome; // influencia o ponto1 do bicho: fome
	private float finalponto1ydirfome;
	private float finalponto1xdirfoge; // influencia o ponto1 do bicho: foge
	private float finalponto1ydirfoge;
	private float finalponto1xdirpersegue; // influencia o ponto1 do bicho: persegue
	private float finalponto1ydirpersegue;
	private float finalponto1xdirtarado; // influencia o ponto1 do bicho: tarado
	private float finalponto1ydirtarado;
	private float finalponto1xdirsatisfeito; // influencia o ponto1 do bicho:
										// satisfeito
	private float finalponto1ydirsatisfeito;
	private float finalponto1xdirgangue; // influencia o ponto1 do bicho: gangue
	private float finalponto1ydirgangue;
	private float finalponto1xdirafastapodre; // influencia o ponto1 do bicho:
										// afastapodre
	private float finalponto1ydirafastapodre;

	private float ponto1xdirfome; // influencia o ponto1 do bicho: fome
	private float ponto1ydirfome;
	private float ponto1xdirfoge; // influencia o ponto1 do bicho: foge
	private float ponto1ydirfoge;
	private float ponto1xdirpersegue; // influencia o ponto1 do bicho: persegue
	private float ponto1ydirpersegue;
	private float ponto1xdirtarado; // influencia o ponto1 do bicho: tarado
	private float ponto1ydirtarado;
	private float ponto1xdirsatisfeito; // influencia o ponto1 do bicho: satisfeito
	private float ponto1ydirsatisfeito;
	private float ponto1xdirgangue; // influencia o ponto1 do bicho: gangue
	private float ponto1ydirgangue;
	private float ponto1xdirafastapodre; // influencia o ponto1 do bicho:
									// afastapodre
	private float ponto1ydirafastapodre;

	Bicho( // define valores Vs no construtor com temporárias [CONSTRUTOR]
	Morfogenese morfogenese, 
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

		this.morfogenese = morfogenese;
		this.positionx = temppositionx; // posição X para alocar o bicho
		this.positiony = temppositiony; // posição Y para alocar o bicho
		this.velocidadeauto = tempvelocidadeauto; // velocidadeauto (treme treme)
												// É fixa depois de gerada,
												// só muda em situações
												// específicas
		this.easing = tempeasing; // easing (em cascata:
								// easing+o*o*easingaceleration)
		this.easingaceleration = tempeasingaceleration; // easingaceleration
		this.tamanho = temptamanho; // tamanho (retângulo possível)
		this.numerodepontos = tempnumerodepontos; // número de pontos do bicho. O
												// index é 0. Com todos os
												// pontos separados o menor
												// número é 5
		this.pesodalinha = temppesodalinha; // peso da linha do bicho
		this.corr = tempcorr; // cores linha R
		this.corg = tempcorg; // cores linha G
		this.corb = tempcorb; // cores linha B
		this.corlinhar = tempcorlinhar; // cores corlinha R
		this.corlinhag = tempcorlinhag; // cores corlinha G
		this.corlinhab = tempcorlinhab; // cores corlinha B
		this.formadiam = tempformadiam; // diâmetro de cada forma (todas elas) a
									// partir do diam
		this.forma1 = tempforma1; // forma da cabeça
		this.forma2 = tempforma2; // forma do pescoço
		this.formarabo = tempformarabo; // forma do rabo
		this.instrumento = tempinstrumento; // para definir o instrumento de cada
										// som
		this.nota = tempnota; // define a nota que o bicho vai reproduzir
		this.energia = tempenergia; // equivale à quantidade de vida inicial
		this.pontomadurocruza = temppontomadurocruza; // define um ponto de
													// maturidade para que
													// possam cruzar
													// novamente
		this.chance = tempchance; // pode fazer ou não algo: surtar, caçar, fugir
								// (1:chapado, 2:surta, 3:berserker,
								// 4:medroso, 5:violento, 6:esperto /
								// tarado>=3 / ataca com bando >=5)

		this.cor = this.morfogenese.color(corr, corg, corb);
		this.corlinha = this.morfogenese.color(corlinhar, corlinhag, corlinhab);
		this.coralpha = 50 + energia * 4; // características de cada bicho
										// geradas aleatoriamente e outras
										// Vs definidas automaticamente
		this.corlinhaalpha = 50 + energia * 4;
		this.velocidade = 5;
		this.novachance = this.morfogenese.random(100);
		this.maturidade = (int) this.morfogenese.random(pontomadurocruza);
		this.vida = true;
		this.podre = false;
		this.velocidadeautooriginal = tempvelocidadeauto;
		this.finalbando = energia;
		this.bancodadosinstrumento = (int) this.morfogenese.random(4);
		this.atracao = 0.5f;
		this.maxformadiam = (int) (this.morfogenese.random(40, 60));
		this.numerodepontosdalinha = (int) (this.morfogenese.random(this.numerodepontos - 4,
				this.numerodepontos - 2));

		this.pontox = new float[this.numerodepontos];
		this.pontoy = new float[this.numerodepontos];

		for (int i = 1; i < this.numerodepontos; i++) { // iniciando pontos x e y
													// com listas
			this.pontox[i] = (int) (this.morfogenese.random(this.positionx - this.tamanho, this.positionx
					+ this.tamanho));
		}
		for (int j = 1; j < this.numerodepontos; j++) {
			this.pontoy[j] = (int) (this.morfogenese.random(this.positiony - this.tamanho, this.positiony
					+ this.tamanho));
		}

		this.distx1 = new float[this.numerodepontos]; // distâncias inicial dos pontos
											// que define a forma do corpo
											// de cada bicho. Outras listas
		this.disty1 = new float[this.numerodepontos];

		for (int i = 1; i < this.numerodepontos; i++) { // captura das distâncias
													// iniciais dos pontos
													// para manter forma do
													// bicho. Listas de novo
			this.distx1[i] = Morfogenese.abs(Morfogenese.abs(this.pontox[1]) - Morfogenese.abs(this.pontox[i]));
		}
		for (int j = 1; j < this.numerodepontos; j++) {
			this.disty1[j] = Morfogenese.abs(Morfogenese.abs(this.pontoy[1]) - Morfogenese.abs(this.pontoy[j]));
		}

		this.ordemxy = new int[this.numerodepontosdalinha];

		for (int i = 1; i < this.numerodepontosdalinha; i++) {
			if (i + 2 < this.numerodepontos - 1) {
				this.ordemxy[i] = i + 2;
			} else {
				this.ordemxy[i] = (int) (this.morfogenese.random(2, this.numerodepontos - 1));
			}

		}

		randomizar(this.ordemxy);

	}

	private void randomizar(int[] a) {
		for (int k = 1; k < a.length - 2; k++) {
			int temp = a[k];
			int x = (int) this.morfogenese.random(1, a.length - 2);
			a[k] = a[x];
			a[x] = temp;
		}

	}

	public void semostra() {

		if (this.morfogenese.mudadia == 5) {
			cor = this.morfogenese.color(0, 0, 0);
			corlinha = this.morfogenese.color(0, 0, 0);
			coralpha = 255;
			corlinhaalpha = 255;
		} else {
			cor = this.morfogenese.color(corr, corg, corb);
			corlinha = this.morfogenese.color(corlinhar, corlinhag, corlinhab);
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

		this.morfogenese.noFill(); // desenho da estrutura
		this.morfogenese.beginShape();
		this.morfogenese.curveVertex(pontox[1], pontoy[1]);
		this.morfogenese.curveVertex(pontox[1], pontoy[1]);
		this.morfogenese.curveVertex(pontox[2], pontoy[2]);

		for (int w = 1; w < numerodepontosdalinha; w++) {

			this.morfogenese.curveVertex(pontox[ordemxy[w]], pontoy[ordemxy[w]]);

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
			this.morfogenese.curveVertex(pontox[numerodepontos - 2],
					pontoy[numerodepontos - 2]);
		}

		this.morfogenese.curveVertex(pontox[numerodepontos - 1], pontoy[numerodepontos - 1]);
		this.morfogenese.curveVertex(pontox[numerodepontos - 1], pontoy[numerodepontos - 1]);
		this.morfogenese.endShape();

	}

	private void desenhaforma(float coordx, float coordy, float angulorotacao,
			int tipoforma, float tamanho, float fator,
			float pesodalinhafator, int corfator, float coralphafator,
			int corlinhafator, float corlinhaalphafator) {

		this.morfogenese.pushMatrix(); // inicia a matrix, translada e roda ponto
		this.morfogenese.translate(coordx, coordy);
		this.morfogenese.rotate(Morfogenese.radians(angulorotacao));

		this.morfogenese.strokeWeight(pesodalinhafator);
		this.morfogenese.stroke(corlinhafator, corlinhaalphafator);
		this.morfogenese.fill(corfator, coralphafator);

		if (tipoforma == 2) { // forma cabeça ponto
			this.morfogenese.rectMode(Morfogenese.CENTER);
			this.morfogenese.rect(0, 0, tamanho / fator, tamanho / fator);
		} else if (tipoforma == 3) {
			this.morfogenese.triangle(0,
					(0 - tamanho / (2 * fator)) - tamanho / (3 * fator),
					(0 - tamanho / (2 * fator)) - tamanho / (6 * fator),
					0 + (tamanho / (fator)) / 2.6f, (0 + tamanho
							/ (2 * fator))
							+ tamanho / (6 * fator),
					0 + (tamanho / (fator)) / 2.6f);
		} else {
			this.morfogenese.ellipse(0, 0, tamanho / fator, tamanho / fator);
		}
		this.morfogenese.popMatrix(); // fehca matrix ponto
	}

	public void vive() {

		if (vida == true) { // se ele estiver vivo

			semove();
			buscacor();
			seajusta();
			semostravivo();

		}
	}

	private void semove() { // função para mover os bichos no automático e criar os
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

		pontox[1] = selimita(pontox[1], -((this.morfogenese.width / this.morfogenese.escala) - this.morfogenese.width) / 2,
				(this.morfogenese.width / this.morfogenese.escala) - ((this.morfogenese.width / this.morfogenese.escala) - this.morfogenese.width) / 2); // para
																	// não
																	// sair
																	// da
																	// tela
		pontoy[1] = selimita(pontoy[1], -((this.morfogenese.height / this.morfogenese.escala) - this.morfogenese.height) / 2,
				(this.morfogenese.height / this.morfogenese.escala) - ((this.morfogenese.height / this.morfogenese.escala) - this.morfogenese.height) / 2); // para
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

		if (this.morfogenese.random(1) < 0.005 * velocidadeauto) {
			nota = nota + (int) (this.morfogenese.random(-2, 2));
		}

		nota = (int) (selimita(nota, 0, 99));
		instrumento = (int) (selimita(instrumento, 0, 127));

		if (this.morfogenese.random(1) < 0.005 * velocidadeauto) {
			instrumento = instrumento + (int) (this.morfogenese.random(-2, 2));
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
			if (Morfogenese.abs(Morfogenese.abs(pontox[1]) - Morfogenese.abs(pontox[o])) > distx1[o]) {
				pontox[o] = movimentaeasing(pontox[o], pontox[1], easing
						+ o * o * easingaceleration);
			}
		}
		for (int p = 2; p < numerodepontos; p++) {
			if (Morfogenese.abs(Morfogenese.abs(pontoy[1]) - Morfogenese.abs(pontoy[p])) > disty1[p]) {
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

	private float movimentaeasing(float A, float B, float fator) {

		A = A + (B - A) / fator;
		return A;

	}

	private float movimentorandomico(float A, float limitemenor, float limitemaior,
			float fatormultiplica, float fatorsoma,
			float fatormultiplicaresultado, float fatorsomaresultado) {

		A = A
				+ (this.morfogenese.random(limitemenor * fatormultiplica + fatorsoma,
						limitemaior * fatormultiplica + fatorsoma)
						* fatormultiplicaresultado + fatorsomaresultado);
		return A;

	}

	private float desloca(float influencia, float eixo, float angulo, float fator) {

		if (eixo == 1) {
			influencia = influencia + (Morfogenese.cos(angulo)) * fator;
		}
		if (eixo == 2) {
			influencia = influencia + (Morfogenese.sin(angulo)) * fator;
		}
		return influencia;

	}

	private float selimita(float A, float limitemenor, float limitemaior) { // para
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

	private void buscacor() {

		if (this.morfogenese.camerasfuncionando && this.morfogenese.pegacor || this.morfogenese.camerasfuncionando
				&& this.morfogenese.procurapelacor) {

			float worldRecord = 500;
			int closestX = 0;
			int closestY = 0;
			float proximidade = 5;

			for (int x = 0; x < this.morfogenese.cam.width; x++) {
				for (int y = 0; y < this.morfogenese.cam.height; y++) {
					int loc = x + y * this.morfogenese.cam.width;
					int currentColor = this.morfogenese.cam.pixels[loc]; // TODO: FIX
					float r1 = this.morfogenese.red(currentColor);
					float g1 = this.morfogenese.green(currentColor);
					float b1 = this.morfogenese.blue(currentColor);
					float d = Morfogenese.dist(r1, g1, b1, corr, corg, corb);

					if (this.morfogenese.pegacor) {
						if (pontox[1] <= ((x * 24 / this.morfogenese.escala) - ((this.morfogenese.width / this.morfogenese.escala) - this.morfogenese.width) / 2)
								+ proximidade
								&& pontox[1] >= ((x * 24 / this.morfogenese.escala) - ((this.morfogenese.width / this.morfogenese.escala) - this.morfogenese.width) / 2)
										- proximidade
								&& pontoy[1] <= ((y * 26.667 / this.morfogenese.escala) - ((this.morfogenese.height / this.morfogenese.escala) - this.morfogenese.height) / 2)
										+ proximidade
								&& pontoy[1] >= ((y * 26.667 / this.morfogenese.escala) - ((this.morfogenese.height / this.morfogenese.escala) - this.morfogenese.height) / 2)
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
						closestX = (int) ((x * 24 / this.morfogenese.escala) - ((this.morfogenese.width / this.morfogenese.escala) - this.morfogenese.width) / 2);
						closestY = (int) ((y * 26.667 / this.morfogenese.escala) - ((this.morfogenese.height / this.morfogenese.escala) - this.morfogenese.height) / 2);
					}
				}
			}

			if (this.morfogenese.procurapelacor) {

				if (worldRecord < this.morfogenese.niveldeprecisaodacor) {

					float angulocor = Morfogenese.atan2((pontoy[1] - closestY),
							(pontox[1] - closestX)); // calcula o ângulo
														// entre os pontos
														// (trigonometria)

					ponto1xdircor = desloca(ponto1xdircor, 1, angulocor,
							-atracao);
					ponto1ydircor = desloca(ponto1ydircor, 2, angulocor,
							-atracao);

					if (this.morfogenese.commetadados >= 2) {
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

	private void seajusta() { // função com a IA de cada bicho [PENSA]

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
			instrumento = (int) (this.morfogenese.random(27, 95));
		}

		if (bancodadosinstrumento == 3 && instrumento > 46) { // para não
																// ficar em
																// silêncio
																// devido ao
																// buraco no
																// banco de
																// dados
			instrumento = (int) (this.morfogenese.random(46));
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
			chance = (int) (this.morfogenese.random(chance, 7));
			novachance = 0;
		}

		if (sova == 5) { // se está se lascando, diminui a área de contato
							// (não usa no bichos.get(t). porque o energia
							// também é assim)
			tamanhoformadiam = tamanhoformadiam - 0.1f;
			sova = 0;
		}

		minhaadaptacao = energia - this.morfogenese.adaptacaomedia;

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

	private float tendencia(float umponto, float limite, float incremento) { // tendência
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

	private void semostravivo() { // função para desenhar as formas [SEMOSTRA]

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

		if (this.morfogenese.som == true && this.morfogenese.random(1) < 0.005 * velocidadeauto) { // reproduz
																	// o som
																	// poucas
																	// vezes
			pegainstrumento(bancodadosinstrumento, instrumento);
			tocanota(nota, this.morfogenese.volume, this.morfogenese.duracao);
			desenhaforma(pontox[numerodepontos - 1],
					pontoy[numerodepontos - 1], angulorabo, 1, formadiam,
					0.5f, pesodalinha, cor, coralpha / 3, corlinha,
					corlinhaalpha); // para saber qual é o que está fazendo
									// barulho

		}

		if (this.morfogenese.commetadados >= 2) { // valor das Vs para visualização
									// individual

			for (int w = 1; w < numerodepontos; w++) {
				desenhaforma(pontox[w], pontoy[w], angulorabo, 1,
						formadiam, 7, pesodalinha, cor, coralpha, corlinha,
						corlinhaalpha);
			}

			this.morfogenese.fill(corr, corg, corb, 255); // preenchimento do texto na cor do
											// contorno do bicho!
			this.morfogenese.textSize(12);
			this.morfogenese.text("adp: " + (int) (minhaadaptacao), pontox[1] + formadiam
					+ 10, pontoy[1] - 15);
			this.morfogenese.text("vid: " + (int) (energia) + "  |  " + (int) (finalbando),
					pontox[1] + formadiam + 10, pontoy[1]); // texto em cada
															// bicho para
															// realizar os
															// testes
			this.morfogenese.text("som: " + (int) (bancodadosinstrumento) + "  |  "
					+ (int) (instrumento) + "  |  " + (int) (nota),
					pontox[1] + formadiam + 10, pontoy[1] + 15);
			this.morfogenese.text("tam: " + (numerodepontos - 1) + "  |  "
					+ (numerodepontosdalinha) + "  |  "
					+ (int) (tamanhoformadiam * 100) + "  |  "
					+ (int) (sova), pontox[1] + formadiam + 10,
					pontoy[1] + 30);
			this.morfogenese.text("cha: " + (int) (chance) + "  |  " + (int) (evoluichance)
					+ "  |  " + (int) (novachance), pontox[1] + formadiam
					+ 10, pontoy[1] + 45);
			this.morfogenese.text("mat: " + (int) (maturidade) + "  |  "
					+ (int) (pontomadurocruza), pontox[1] + formadiam + 10,
					pontoy[1] + 60);
			this.morfogenese.text("ger: " + geracao, pontox[1] + formadiam + 10,
					pontoy[1] + 75);
			this.morfogenese.text("ind: " + this.morfogenese.bichos.indexOf(this), pontox[1] + formadiam
					+ 10, pontoy[1] + 90);
		}
	}

	public void morre() { // método que determina as condições em que um bicho
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
					this.morfogenese.ne = this.morfogenese.ne - 1;
				} else if (forma1 == 2) {
					this.morfogenese.nr = this.morfogenese.nr - 1;
				} else if (forma1 == 3) {
					this.morfogenese.nt = this.morfogenese.nt - 1;
				}
			}

			contagemmorte = 1;

			if (energia < -10) { // apodrece
				podre = true;
			}
		}
	}

	public void sebate() {

		for (int t = this.morfogenese.bichos.indexOf(this) + 1; t < this.morfogenese.bichos.size(); t++) { // condição
																				// para
																				// a
																				// interseção
																				// [COLISÃO]

			if (contatoAeB(this, this.morfogenese.bichos.get(t)) == 1) { // de longe
																// define se
																// persegue
																// ou foge
																// [COLISÃO
																// LONGE]

				novachance = 0;
				this.morfogenese.bichos.get(t).novachance = 0;

				desenhalinhacontato(pontox[1], pontoy[1],
						this.morfogenese.bichos.get(t).pontox[1], this.morfogenese.bichos.get(t).pontoy[1]);

				if (forma1 == this.morfogenese.bichos.get(t).forma1) { // se for da mesma
														// forma... [COLISÃO
														// LONGE FORMA==]

					if (vida == true && this.morfogenese.bichos.get(t).vida == true) { // ...e
																		// estiverem
																		// vivos
																		// [COLISÃO
																		// LONGE
																		// FORMA==
																		// VIVOS]

						if (AeBmaduros(this, this.morfogenese.bichos.get(t))) { // procuram
																	// pelo
																	// próximo
																	// maduros
																	// [COLISÃO
																	// LONGE
																	// FORMA==
																	// VIVOS
																	// MADUROS]

							AquercruzarB(this, this.morfogenese.bichos.get(t)); // ESSE
																	// QUER
																	// CRUZAR
							AquercruzarB(this.morfogenese.bichos.get(t), this); // OUTRO
																	// QUER
																	// CRUZAR

						} else { // fogem se sem maturidade. O >5 é só para
									// manter o efeito de reprodução
									// [COLISÃO LONGE FORMA== VIVOS
									// IMATUROS]

							AdesinteressaB(this, this.morfogenese.bichos.get(t)); // ESSE
																		// BICHO
																		// SE
																		// DESINTERESSA
							AdesinteressaB(this.morfogenese.bichos.get(t), this); // OUTRO
																		// BICHO
																		// SE
																		// DESINTERESSA

							if (AeBseagruparem(this, this.morfogenese.bichos.get(t))) { // gangue:
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

								AgangueB(this, this.morfogenese.bichos.get(t)); // ESSE
																	// ENTRA
																	// PARA
																	// A
																	// GANGUE
								AgangueB(this.morfogenese.bichos.get(t), this); // OUTRO
																	// ENTRA
																	// PARA
																	// A
																	// GANGUE

							}
						}

					} else { // ...e estiver morto ele evita [COLISÃO LONGE
								// FORMA== ALGUÉM MORTO]

						AtemnojodeB(this, this.morfogenese.bichos.get(t)); // ESSE TEM
																// NOJO
						AtemnojodeB(this.morfogenese.bichos.get(t), this); // OUTRO TEM
																// NOJO

					}

				} else { // se as formas forem diferentes... [COLISÃO LONGE
							// FORMA!=]

					if (vida == true && this.morfogenese.bichos.get(t).vida == true) { // ...e
																		// estiverem
																		// vivos
																		// [COLISÃO
																		// LONGE
																		// FORMA!=
																		// VIVOS]

						if (AmaisfracoqueB(this, this.morfogenese.bichos.get(t))) { // se
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

							AfogedeB(this, this.morfogenese.bichos.get(t)); // EFEITO DE
																// FUGA
																// NESSE
							AatacaB(this.morfogenese.bichos.get(t), this); // EFEITO DE
																// CAÇA NO
																// OUTRO

						} else if (AmaisfracoqueB(this.morfogenese.bichos.get(t), this)) { // se
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

							AatacaB(this, this.morfogenese.bichos.get(t)); // EFEITO DE
																// CAÇA
																// NESSE
							AfogedeB(this.morfogenese.bichos.get(t), this); // EFEITO DE
																// FUGA NO
																// OUTRO

						}

					} else { // ...um dos dois estiver morto o outro procura
								// para comer [COLISÃO LONGE FORMA!= ALGUÉM
								// MORTO]

						AvaicomerB(this, this.morfogenese.bichos.get(t)); // EFEITO
																// NESSE
						AvaicomerB(this.morfogenese.bichos.get(t), this); // EFEITO NO
																// OUTRO

					}
				}

			} else if (contatoAeB(this, this.morfogenese.bichos.get(t)) == 2) { // de
																	// perto
																	// cruza
																	// ou
																	// briga
																	// ou
																	// come
																	// [COLISÃO
																	// PERTO]

				novachance = 0;
				this.morfogenese.bichos.get(t).novachance = 0;

				if (forma1 == this.morfogenese.bichos.get(t).forma1) { // [COLISÃO PERTO
														// FORMA==]

					if (vida == true && this.morfogenese.bichos.get(t).vida == true) { // [COLISÃO
																		// PERTO
																		// FORMA==
																		// VIVOS]

						if (AeBmaduros(this, this.morfogenese.bichos.get(t))) { // [COLISÃO
																	// PERTO
																	// FORMA==
																	// A
																	// VIVOS
																	// MADUROS]

							if (cruza < 200) { // se for do mesmo: cruza...
												// [COLISÃO PERTO FORMA== B
												// VIVOS CRUZANDO]

								AcruzacomB(this, this.morfogenese.bichos.get(t)); // EFEITO
																		// NESSE
								AcruzacomB(this.morfogenese.bichos.get(t), this); // EFEITO
																		// NO
																		// OUTRO

							} else { // ...depois reproduz [COLISÃO PERTO
										// FORMA== B VIVOS REPRODUZINDO]

								AreproduzcomB(this, this.morfogenese.bichos.get(t)); // EFEITO
																			// NESSE
								AreproduzcomB(this.morfogenese.bichos.get(t), this); // EFEITO
																			// NO
																			// OUTRO

								this.morfogenese.bichoculpado = (Bicho) this; // define
																	// quem
																	// são
																	// os
																	// bichosculpados:
																	// pai1
								this.morfogenese.bichaculpada = (Bicho) this.morfogenese.bichos.get(t); // define
																		// quem
																		// são
																		// os
																		// bichosculpados:
																		// pai2
								this.morfogenese.filanascimento = 1;

							}
						}
					}

				} else { // se for diferente: briga [COLISÃO PERTO FORMA!=]

					if (vida == true && this.morfogenese.bichos.get(t).vida == true) { // [COLISÃO
																		// PERTO
																		// FORMA!=
																		// VIVOS]

						AbrigacomB(this, this.morfogenese.bichos.get(t)); // ESSE BRIGA
						AbrigacomB(this.morfogenese.bichos.get(t), this); // OUTRO BRIGA

					} else { // [COLISÃO PERTO FORMA!= ALGUÉM MORTO]

						AcomeB(this, this.morfogenese.bichos.get(t)); // ESSE COME
						AcomeB(this.morfogenese.bichos.get(t), this); // OUTRO COME

					}
				}
			}

			for (int q = 3; q < this.morfogenese.bichos.get(t).numerodepontos - 2; q++) { // cai
																			// na
																			// teia
																			// do
																			// outro
				if (ApassarpelospontosdeB(this, this.morfogenese.bichos.get(t), q)) {

					AcainateiadeB(this, this.morfogenese.bichos.get(t), q); // ESSE CAI
																// NA TEIA
																// DO OUTRO

				}
			}

			for (int q = 3; q < numerodepontos - 2; q++) { // gruda na teia
															// desse
				if (ApassarpelospontosdeB(this.morfogenese.bichos.get(t), this, q)) {

					AcainateiadeB(this.morfogenese.bichos.get(t), this, q); // OUTRO CAI
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

	private float transferesomaparafinal(float finalponto, float somaponto) {

		if (somaponto != 0) {
			finalponto = somaponto;
		}
		return finalponto;
	}

	public void tocanota(int temponota, float tempovolume, float tempoduracao) { // inicia
																			// e
																			// encerra
																			// o
																			// som
																			// da
																			// nota
		this.morfogenese.channels[0].noteOn(temponota, (int) (tempovolume));
		try {
			Thread.sleep((long) tempoduracao);
		} catch (InterruptedException e) {
		}
		this.morfogenese.channels[0].noteOff(temponota);
	}

	public void pegainstrumento(int tempobancodadosinstrumento,
			int tempoinstrumento) { // define o instrumento
		this.morfogenese.channels[0].programChange(tempobancodadosinstrumento,
				tempoinstrumento);
	}

	private void AvaicomerB(Bicho A, Bicho B) {

		if (A.vida == true) {

			anguloAB = calculaanguloAB(anguloAB, A, B);

			A.ponto1xdirfome = desloca(A.ponto1xdirfome, 1, anguloAB,
					A.atracao * -1);
			A.ponto1ydirfome = desloca(A.ponto1ydirfome, 2, anguloAB,
					A.atracao * -1);

		}

	}

	private void AfogedeB(Bicho A, Bicho B) {

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

	private void AatacaB(Bicho A, Bicho B) {

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

	private void AquercruzarB(Bicho A, Bicho B) {

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

	private void AdesinteressaB(Bicho A, Bicho B) {

		anguloAB = calculaanguloAB(anguloAB, A, B);

		A.ponto1xdirsatisfeito = desloca(A.ponto1xdirsatisfeito, 1,
				anguloAB, A.atracao);
		A.ponto1ydirsatisfeito = desloca(A.ponto1ydirsatisfeito, 2,
				anguloAB, A.atracao);

	}

	private void AgangueB(Bicho A, Bicho B) {

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

		A.nota = (int) (movimentaeasing(A.nota, B.nota, 2) + ((int) (this.morfogenese.random(
				4, 6)) * 0.1)); // compartilha a nota

		A.instrumento = (int) (movimentaeasing(A.instrumento,
				B.instrumento, 2) + ((int) (this.morfogenese.random(4, 6)) * 0.1)); // compartilha
																	// o
																	// instrumento

		A.bancodadosinstrumento = B.bancodadosinstrumento; // compartilha o
															// banco de
															// dados de
															// instrumentos

	}

	private void AtemnojodeB(Bicho A, Bicho B) {

		anguloAB = calculaanguloAB(anguloAB, A, B);

		A.ponto1xdirafastapodre = desloca(A.ponto1xdirafastapodre, 1,
				anguloAB, A.atracao * 0.5f); // deixei assim para ficar com
												// a metade do valor
		A.ponto1ydirafastapodre = desloca(A.ponto1ydirafastapodre, 2,
				anguloAB, A.atracao * 0.5f);

	}

	private void AcruzacomB(Bicho A, Bicho B) {

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

		if (this.morfogenese.som == true && cruza % 20 == 0) { // reproduz barulho em
												// intervalos regulares
			pegainstrumento(A.bancodadosinstrumento, A.instrumento); // toca
																		// o
																		// som
			tocanota(A.nota, this.morfogenese.volume, this.morfogenese.duracao);
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

	private void AreproduzcomB(Bicho A, Bicho B) {

		A.energia = A.energia - 1; // perde energia para gerar filho
		A.tamanhoformadiam = A.tamanhoformadiam + 0.01f; // se está se dando
															// bem, aumenta
															// a área de
															// contato
		A.chance = A.chance + 1; // evolui a personalidade conforme ganha
									// experiência. Teve filho muda uma
									// direto
		A.maturidade = 0;

		A.pontox[1] = A.pontox[1] + 100 * ((int) (this.morfogenese.random(-2, 2))); // depois
																	// separa
																	// os
																	// pontos
																	// e se
																	// afastam
		A.pontoy[1] = A.pontoy[1] + 100 * ((int) (this.morfogenese.random(-2, 2)));

		A.cruza = 0;

	}

	private void AbrigacomB(Bicho A, Bicho B) {

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

		if (this.morfogenese.som == true) { // se o som estiver ligado
			pegainstrumento(A.bancodadosinstrumento, A.instrumento); // toca
																		// o
																		// som
			tocanota(A.nota, this.morfogenese.volume, this.morfogenese.duracao);
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

		A.pontox[1] = A.pontox[1] + (Morfogenese.cos(anguloAB)) * 5;
		A.pontoy[1] = A.pontoy[1] + (Morfogenese.sin(anguloAB)) * 5;

	}

	private void AcomeB(Bicho A, Bicho B) {

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

	private void AcainateiadeB(Bicho A, Bicho B, int numerodoponto) {

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
		A.nota = (int) (movimentaeasing(A.nota, B.nota, 2) + ((int) (this.morfogenese.random(
				4, 6)) * 0.1));
		A.instrumento = (int) (movimentaeasing(A.instrumento,
				B.instrumento, 2) + ((int) (this.morfogenese.random(4, 6)) * 0.1));
		A.bancodadosinstrumento = B.bancodadosinstrumento;

	}

	private int contatoAeB(Bicho A, Bicho B) { // PARA DEFINIR AS DUAS DISTÂNCIAS DE
										// CONTATO

		float distanciaAB = Morfogenese.dist(A.pontox[1], A.pontoy[1], B.pontox[1],
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

	private boolean AeBseagruparem(Bicho A, Bicho B) { // PARA DEFINIR A CONDIÇÃO DE
												// GANGUE

		float distanciaAB = Morfogenese.dist(A.pontox[1], A.pontoy[1], B.pontox[1],
				B.pontoy[1]); // calcula o tamanho da diagonal entre os
								// bichos

		if (distanciaAB > (A.formadiam + B.formadiam) * 1.5
				&& A.formarabo == B.formarabo) {
			return true;
		} else {
			return false;
		}

	}

	private boolean AmaisfracoqueB(Bicho A, Bicho B) {

		if (A.energia < B.energia || A.energia > B.energia
				&& A.energia < B.finalbando) {
			return true;
		} else {
			return false;
		}

	}

	private boolean AeBmaduros(Bicho A, Bicho B) {

		if (A.maturidade > A.pontomadurocruza
				&& B.maturidade > B.pontomadurocruza) {
			return true;
		} else {
			return false;
		}

	}

	private boolean ApassarpelospontosdeB(Bicho A, Bicho B, int nponto) {

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

	private void desenhalinhacontato(float pontoxA, float pontoyA, float pontoxB,
			float pontoyB) {

		if (this.morfogenese.commetadados >= 3) { // mostrador de distâncias de colisão
			this.morfogenese.noFill(); // desenho que liga os que interagem
			this.morfogenese.stroke(corlinhar, corlinhag, corlinhab, corlinhaalpha);
			this.morfogenese.strokeWeight(2);
			this.morfogenese.beginShape();
			this.morfogenese.curveVertex(pontoxA, pontoyA);
			this.morfogenese.curveVertex(pontoxA, pontoyA);
			this.morfogenese.curveVertex(pontoxB, pontoyB);
			this.morfogenese.curveVertex(pontoxB, pontoyB);
			this.morfogenese.endShape();
		}

	}

	private boolean mousetocarnessebicho() {

		if (this.morfogenese.posicaofinalmouseX > pontox[1] - (formadiam / 2)
				&& this.morfogenese.posicaofinalmouseX < pontox[1] + (formadiam / 2)
				&& this.morfogenese.posicaofinalmouseY > pontoy[1] - (formadiam / 2)
				&& this.morfogenese.posicaofinalmouseY < pontoy[1] + (formadiam / 2)
				||

				this.morfogenese.posicaofinalmouseX > pontox[2] - (formadiam / 4)
				&& this.morfogenese.posicaofinalmouseX < pontox[2] + (formadiam / 4)
				&& this.morfogenese.posicaofinalmouseY > pontoy[2] - (formadiam / 4)
				&& this.morfogenese.posicaofinalmouseY < pontoy[2] + (formadiam / 4)
				||

				this.morfogenese.posicaofinalmouseX > pontox[numerodepontos - 2]
						- (formadiam / 6)
				&& this.morfogenese.posicaofinalmouseX < pontox[numerodepontos - 2]
						+ (formadiam / 6)
				&& this.morfogenese.posicaofinalmouseY > pontoy[numerodepontos - 2]
						- (formadiam / 6)
				&& this.morfogenese.posicaofinalmouseY < pontoy[numerodepontos - 2]
						+ (formadiam / 6)
				||

				this.morfogenese.posicaofinalmouseX > pontox[numerodepontos - 1]
						- (formadiam / 10)
				&& this.morfogenese.posicaofinalmouseX < pontox[numerodepontos - 1]
						+ (formadiam / 10)
				&& this.morfogenese.posicaofinalmouseY > pontoy[numerodepontos - 1]
						- (formadiam / 10)
				&& this.morfogenese.posicaofinalmouseY < pontoy[numerodepontos - 1]
						+ (formadiam / 10)) {

			return true;
		} else {
			return false;
		}

	}

	public void interagemouse() {

		if (this.morfogenese.mousePressed == true) {

			float distponto1mouse = Morfogenese.dist(pontox[1], pontoy[1],
					this.morfogenese.posicaofinalmouseX, this.morfogenese.posicaofinalmouseY);
			float anguloentrebichoemouse = Morfogenese.atan2(
					(pontoy[1] - (this.morfogenese.posicaofinalmouseY)),
					(pontox[1] - (this.morfogenese.posicaofinalmouseX))); // calcula o ângulo
															// entre os
															// pontos
															// (trigonometria)

			if (this.morfogenese.mouseButton == Morfogenese.LEFT) {

				if (vida == true) {

					if (this.morfogenese.reagemouse == 1 && distponto1mouse < 200) { // reage
																	// ao
																	// mouse
																	// à
																	// distância
																	// afastando

						ponto1xdirmouse = desloca(ponto1xdirmouse, 1,
								anguloentrebichoemouse, 1);
						ponto1ydirmouse = desloca(ponto1ydirmouse, 2,
								anguloentrebichoemouse, 1);

						desenhaforma(this.morfogenese.posicaofinalmouseX,
								this.morfogenese.posicaofinalmouseY, angulo1, 1, 50,
								this.morfogenese.random(0.5f, 1), pesodalinha, cor,
								coralpha / 5, corlinha, corlinhaalpha / 5);

						desenhalinhacontato(pontox[1], pontoy[1],
								this.morfogenese.posicaofinalmouseX, this.morfogenese.posicaofinalmouseY);

					} else if (this.morfogenese.reagemouse == 2) { // reage ao mouse à
													// distância atraindo

						if (distponto1mouse < 300 && distponto1mouse > 100) {

							ponto1xdirmouse = desloca(ponto1xdirmouse, 1,
									anguloentrebichoemouse, -1);
							ponto1ydirmouse = desloca(ponto1ydirmouse, 2,
									anguloentrebichoemouse, -1);

							desenhaforma(this.morfogenese.posicaofinalmouseX,
									this.morfogenese.posicaofinalmouseY, -angulo1, forma1,
									formadiam, 0.5f, pesodalinha, cor,
									coralpha / 3, corlinha,
									corlinhaalpha / 3);

							desenhalinhacontato(pontox[1], pontoy[1],
									this.morfogenese.posicaofinalmouseX, this.morfogenese.posicaofinalmouseY);

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
						tocanota(nota, this.morfogenese.volume, this.morfogenese.duracao);
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

						if (this.morfogenese.keyPressed) {
							if (this.morfogenese.key == 'u' || this.morfogenese.key == 'U') {
								this.morfogenese.eubicho = this;
							}
						}

					}
				}

			} else if (this.morfogenese.mouseButton == Morfogenese.RIGHT) {

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

	private float calculaanguloAB(float angulo, Bicho A, Bicho B) { // calcula o
															// ângulo entre
															// os pontos
															// (trigonometria)

		angulo = Morfogenese.atan2((A.pontoy[1] - B.pontoy[1]),
				(A.pontox[1] - B.pontox[1]));
		return angulo;

	}

}
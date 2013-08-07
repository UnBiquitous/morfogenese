package org.unbiquitous.morfogenese;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DNA {

	private Point posicaoInicial;
	private float velocidadeAuto;
	private float easing;
	private float easingAcceleration;
	
	private float tamanho;
	private float pesoDaLinha;
	private float diametroDaForma;
	private int formaCabeca;
	private int formaPescoco;
	private int formaRabo;
	private int instrumento;
	private int notaMusical;
	private float energia;
	private float pontoDeMaturidadeParaCruzamento;
	private int chance;
	private boolean vida;
	private boolean podre;

	private Color cor;
	private Color corLinha;
	
	private float velocidade; // Init
	private float novachance; // Init
	private float maturidade; // Init
	private float velocidadeAutoOriginal; // = velocidadeAuto
	private float finalBando; // = energia
	private float atracao; // Init
	
	private float corLinhaAlpha; // Calculated
	private float corAlpha; // Calculated
	private int bancodadosinstrumento; // Calculated
	private float maxformadiam; // Calculated
	private int numerodepontosdalinha; // Calculated
	private float[] pontox; // Calculated
	private float[] pontoy; // Calculated
	private float[] distx1; // Calculated
	private float[] disty1; // Calculated
	private int[] ordemxy; // Calculated
	
	public Map<String, Object> toMap() {
		Map<String, Object> theMap = new HashMap<String, Object>();
		
		theMap.put("posicaoInicial", fromPoint(posicaoInicial));
		theMap.put("velocidadeAuto", velocidadeAuto);
		theMap.put("easing", easing);
		theMap.put("easingAcceleration", easingAcceleration);
		theMap.put("tamanho", tamanho);
		theMap.put("pesoDaLinha", pesoDaLinha);
		theMap.put("diametroDaForma", diametroDaForma);
		theMap.put("formaCabeca", formaCabeca);
		theMap.put("formaPescoco", formaPescoco);
		theMap.put("formaRabo", formaRabo);
		theMap.put("instrumento", instrumento);
		theMap.put("notaMusical", notaMusical);
		theMap.put("energia", energia);
		theMap.put("pontoDeMaturidadeParaCruzamento", pontoDeMaturidadeParaCruzamento);
		theMap.put("chance", chance);
		theMap.put("vida", vida);
		theMap.put("podre", podre);
		theMap.put("cor", fromColor(cor));
		theMap.put("corLinha", fromColor(corLinha));
		theMap.put("velocidade", velocidade);
		theMap.put("novachance", novachance);
		theMap.put("maturidade", maturidade);
		theMap.put("velocidadeAutoOriginal", velocidadeAutoOriginal);
		theMap.put("finalBando", finalBando);
		theMap.put("atracao", atracao);
		theMap.put("corLinhaAlpha", corLinhaAlpha);
		theMap.put("corAlpha", corAlpha);
		theMap.put("bancodadosinstrumento", bancodadosinstrumento);
		theMap.put("maxformadiam", maxformadiam);
		theMap.put("numerodepontosdalinha", numerodepontosdalinha);
		theMap.put("pontox", pontox);
		theMap.put("pontoy", pontoy);
		theMap.put("distx1", distx1);
		theMap.put("disty1", disty1);
		theMap.put("ordemxy", ordemxy);
		
		List<String> keySet = new ArrayList<String>(theMap.keySet());
		for(String k : keySet){
			if (theMap.get(k) == null){
				theMap.remove(k);
			}
		}
		
		return theMap;
	}
	
	public static DNA fromMap(Map<String, Object> theMap) {
		DNA dna = new DNA();
		dna.posicaoInicial( toPoint((String)theMap.get("posicaoInicial")));
		dna.velocidadeAuto( toFloat(theMap.get("velocidadeAuto")));
		dna.easing( toFloat(theMap.get("easing")));
		dna.easingAcceleration( toFloat(theMap.get("easingAcceleration")));
		dna.tamanho( toFloat(theMap.get("tamanho")));
		dna.pesoDaLinha( toFloat(theMap.get("pesoDaLinha")));
		dna.diametroDaForma( toFloat(theMap.get("diametroDaForma")));
		dna.formaCabeca( (Integer) theMap.get("formaCabeca"));
		dna.formaPescoco( (Integer) theMap.get("formaPescoco"));
		dna.formaRabo( (Integer) theMap.get("formaRabo"));
		dna.instrumento( (Integer) theMap.get("instrumento"));
		dna.notaMusical( (Integer) theMap.get("notaMusical"));
		dna.energia( toFloat(theMap.get("energia")));
		dna.pontoDeMaturidadeParaCruzamento( toFloat(theMap.get("pontoDeMaturidadeParaCruzamento")));
		dna.chance( (Integer) theMap.get("chance"));
		dna.vida( (Boolean) theMap.get("vida"));
		dna.podre( (Boolean) theMap.get("podre"));
		dna.cor( toColor((String) theMap.get("cor")));
		dna.corLinha( toColor( (String) theMap.get("corLinha")));
		dna.velocidade( toFloat(theMap.get("velocidade")));
		dna.novachance( toFloat( theMap.get("novachance")));
		dna.maturidade( toFloat( theMap.get("maturidade")));
		dna.velocidadeAutoOriginal( toFloat(theMap.get("velocidadeAutoOriginal")));
		dna.finalBando( toFloat(theMap.get("finalBando")));
		dna.atracao( toFloat(theMap.get("atracao")));
		dna.corLinhaAlpha( toFloat(theMap.get("corLinhaAlpha")));
		dna.corAlpha( toFloat(theMap.get("corAlpha")));
		dna.bancodadosinstrumento( (Integer) theMap.get("bancodadosinstrumento"));
		dna.maxformadiam( toFloat(theMap.get("maxformadiam")));
		dna.numerodepontosdalinha( (Integer) theMap.get("numerodepontosdalinha"));
		dna.pontox( toFloatArray(theMap.get("pontox")));
		dna.pontoy( toFloatArray(theMap.get("pontoy")));
		dna.distx1( toFloatArray(theMap.get("distx1")));
		dna.disty1( toFloatArray(theMap.get("disty1")));
		dna.ordemxy( toIntArray(theMap.get("ordemxy")));
		return dna;
	}
	
	private static Float toFloat(Object o){
		if (o instanceof Number){
			return ((Number) o).floatValue();
		}
		return Float.parseFloat(o.toString());
	}
	
	private static Integer toInt(Object o){
		if (o instanceof Number){
			return ((Number) o).intValue();
		}
		return Integer.parseInt(o.toString());
	}
	
	@SuppressWarnings("rawtypes")
	private static float[] toFloatArray(Object o){
		if(o == null) return null;
		Object[] original = null;
		if (o instanceof float[]){
			return (float[]) o;
		}else if (o instanceof Object[]){
			original = (Object[]) o;
		}else if (o instanceof List){
			original = ((List)o).toArray();
		}else{
			throw new IllegalArgumentException(
					String.format("parameter of type '%s' and value '%s'",
							o.getClass(), o.toString()
							)
					);
		}
		float[] result = new float[original.length];
		for(int i = 0 ; i < original.length; i ++){
			result[i] = toFloat(original[i]);
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private static int[] toIntArray(Object o){
		if(o == null) return null;
		Object[] original = null;
		if (o instanceof int[]){
			return (int[]) o;
		}else if (o instanceof Object[]){
			original = (Object[]) o;
		}else if (o instanceof List){
			original = ((List)o).toArray();
		}else{
			throw new IllegalArgumentException("for parameter "+o);
		}
		int[] result = new int[original.length];
		for(int i = 0 ; i < original.length; i ++){
			result[i] = toInt(original[i]);
		}
		return result;
	}
	
	private static Point toPoint(String param) {
		if (param == null) return null;
		String[] parts = stringTuple(param);
		return new Point(
					Integer.parseInt(parts[0]),
					Integer.parseInt(parts[1])
				);
	}

	private static String fromPoint(Point param) {
		if(param == null) return null;
		return String.format("(%s,%s)", param.x, param.y);
	}
	
	private static Color toColor(String param) {
		if (param == null) return null;
		String[] parts = stringTuple(param);
		return Color.color( 
					Integer.parseInt(parts[0]),
					Integer.parseInt(parts[1]),
					Integer.parseInt(parts[2])
				);
	}

	private static String fromColor(Color param) {
		return String.format("(%s,%s,%s)", 
				(int)param.red(), (int)param.green(), (int)param.blue());
	}

	private static String[] stringTuple(String param) {
		String noParenthesis = param.replace("(", "").replace(")", "");
		String[] parts = noParenthesis.split(",");
		return parts;
	}
	
	public static DNA autoGenese(
			int displayWidth, int displayHeight, 
			float escala ) {
		DNA meudna = new DNA()
			.posicaoInicial(new Point(
							(int) (random(100, (displayWidth / escala) - 100)),
							(int) (random(100, (displayHeight / escala) - 100))
							)
						)
			.velocidadeAuto((int) (random(1, 5)))
			.easing((int) (random(40, 100)))
			.easingAcceleration((int) (random(5, 15)))
			.tamanho((int) (random(50, 200)))
			.numerodepontosdalinha((int) (random(4, 9)))
			.pesoDaLinha((int) (random(1, 5)))
			.cor(Color.color((int) (random(255)), (int) (random(255)), (int) (random(255))))
			.corLinha(Color.color((int) (random(255)), (int) (random(255)), (int) (random(255))))
			.diametroDaForma((int) (random(5, 50)))
			.formaCabeca((int) (random(1, 4)))
			.formaPescoco((int) (random(1, 4)))
			.formaRabo((int) (random(1, 4)))
			.instrumento((int) (random(127)))
			.notaMusical((int) (random(99)))
			.energia((int) (random(30, 50)))
			.pontoDeMaturidadeParaCruzamento((int) (random(50, 200)))
			.chance((int) (random(1, 11)));

		return meudna;
	}
	
	//TODO: POG
	static float random(float low, float high){
		float diff = high - low;
		return random(diff)+low;
	}
	
	static float random(float high){
		return (float)(Math.random()*high);
	}
	
	public DNA posicaoInicial(Point position) {
		this.posicaoInicial = position;
		return this;
	}
	
	public DNA velocidadeAuto(float velocidadeAuto) {
		this.velocidadeAuto = velocidadeAuto;
		return this;
	}

	public DNA easing(float easing) {
		this.easing = easing;
		return this;
	}

	public DNA easingAcceleration(float easingAcceleration) {
		this.easingAcceleration = easingAcceleration;
		return this;
	}
	
	public DNA tamanho(float tamanho) {
		this.tamanho = tamanho;
		return this;
	}

	public DNA pesoDaLinha(float pesoDaLinha) {
		this.pesoDaLinha = pesoDaLinha;
		return this;
	}

	public DNA diametroDaForma(float diametroDaForma) {
		this.diametroDaForma = diametroDaForma;
		return this;
	}

	public DNA formaCabeca(int formaCabeca) {
		this.formaCabeca = formaCabeca;
		return this;
	}

	public DNA formaPescoco(int formaPescoco) {
		this.formaPescoco = formaPescoco;
		return this;
	}

	public DNA formaRabo(int formaRabo) {
		this.formaRabo = formaRabo;
		return this;
	}

	public DNA instrumento(int instrumento) {
		this.instrumento = instrumento;
		return this;
	}

	public DNA notaMusical(int notaMusical) {
		this.notaMusical = notaMusical;
		return this;
	}

	public DNA energia(float energia) {
		this.energia = energia;
		return this;
	}

	public DNA pontoDeMaturidadeParaCruzamento(float pontoDeMaturidadeParaCruzamento) {
		this.pontoDeMaturidadeParaCruzamento = pontoDeMaturidadeParaCruzamento;
		return this;
	}

	public DNA chance(int chance) {
		this.chance = chance;
		return this;
	}

	public DNA vida(boolean vida) {
		this.vida = vida;
		return this;
	}

	public DNA podre(boolean podre) {
		this.podre = podre;
		return this;
	}

	public DNA cor(Color cor) {
		this.cor = cor;
		return this;
	}

	public DNA corLinha(Color corLinha) {
		this.corLinha = corLinha;
		return this;
	}

	public DNA velocidade(float velocidade) {
		this.velocidade = velocidade;
		return this;
	}

	public DNA novachance(float novachance) {
		this.novachance = novachance;
		return this;
	}

	public DNA maturidade(float maturidade) {
		this.maturidade = maturidade;
		return this;
	}

	public DNA velocidadeAutoOriginal(float velocidadeAutoOriginal) {
		this.velocidadeAutoOriginal = velocidadeAutoOriginal;
		return this;
	}

	public DNA finalBando(float finalBando) {
		this.finalBando = finalBando;
		return this;
	}

	public DNA atracao(float atracao) {
		this.atracao = atracao;
		return this;
	}

	public DNA corLinhaAlpha(float corLinhaAlpha) {
		this.corLinhaAlpha = corLinhaAlpha;
		return this;
	}

	public DNA corAlpha(float corAlpha) {
		this.corAlpha = corAlpha;
		return this;
	}

	public DNA bancodadosinstrumento(int bancodadosinstrumento) {
		this.bancodadosinstrumento = bancodadosinstrumento;
		return this;
	}

	public DNA maxformadiam(float maxformadiam) {
		this.maxformadiam = maxformadiam;
		return this;
	}

	public DNA numerodepontosdalinha(int numerodepontosdalinha) {
		this.numerodepontosdalinha = numerodepontosdalinha;
		return this;
	}

	public DNA pontox(float[] pontox) {
		this.pontox = pontox;
		return this;
	}

	public DNA pontoy(float[] pontoy) {
		this.pontoy = pontoy;
		return this;
	}

	public DNA distx1(float[] distx1) {
		this.distx1 = distx1;
		return this;
	}

	public DNA disty1(float[] disty1) {
		this.disty1 = disty1;
		return this;
	}

	public DNA ordemxy(int[] ordemxy) {
		this.ordemxy = ordemxy;
		return this;
	}
	
	public Point posicaoInicial() {
		return this.posicaoInicial;
	}
	
	public float velocidadeAuto() {
		return this.velocidadeAuto;
	}

	public float easing() {
		return this.easing;
	}

	public float easingAcceleration() {
		return this.easingAcceleration;
	}
	
	public float tamanho() {
		return this.tamanho;
	}

	public float pesoDaLinha() {
		return this.pesoDaLinha;
	}

	public float diametroDaForma() {
		return this.diametroDaForma;
	}

	public int formaCabeca() {
		return this.formaCabeca;
	}

	public int formaPescoco() {
		return this.formaPescoco;
	}

	public int formaRabo() {
		return this.formaRabo;
	}

	public int instrumento() {
		return this.instrumento;
	}

	public int notaMusical() {
		return this.notaMusical;
	}

	public float energia() {
		return this.energia;
	}

	public float pontoDeMaturidadeParaCruzamento() {
		return this.pontoDeMaturidadeParaCruzamento;
	}

	public int chance() {
		return this.chance;
	}

	public boolean vida() {
		return this.vida;
	}

	public boolean podre() {
		return this.podre;
	}

	public Color cor() {
		return this.cor;
	}

	public Color corLinha() {
		return this.corLinha;
	}

	public float velocidade() {
		return this.velocidade;
	}

	public float novachance() {
		return this.novachance;
	}

	public float maturidade() {
		return this.maturidade;
	}

	public float velocidadeAutoOriginal() {
		return this.velocidadeAutoOriginal;
	}

	public float finalBando() {
		return this.finalBando;
	}

	public float atracao() {
		return this.atracao;
	}

	public float corLinhaAlpha() {
		return this.corLinhaAlpha;
	}

	public float corAlpha() {
		return this.corAlpha;
	}

	public int bancodadosinstrumento() {
		return this.bancodadosinstrumento;
	}

	public float maxformadiam() {
		return this.maxformadiam;
	}

	public int numerodepontosdalinha() {
		return this.numerodepontosdalinha;
	}

	public float[] pontox() {
		return this.pontox;
	}

	public float[] pontoy() {
		return this.pontoy;
	}

	public float[] distx1() {
		return this.distx1;
	}

	public float[] disty1() {
		return this.disty1;
	}

	public int[] ordemxy() {
		return this.ordemxy;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DNA){
			DNA that = (DNA) obj;
			return this == that || this.toMap().equals(that.toMap());
		}
		return super.equals(obj);
	}
}

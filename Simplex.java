
/**
 *
 * @author Daniel Gunna
 */
public class Simplex {

    private static final int COLUNA_ML = 0;
    private int quantRestricoes;
    private double[][] matriz;
    private double[][] matrizInferior;
    private double[] variaveis;
    private boolean[][] marcados;
    private double[][] restricoes;
    private int quantVariaveis;
    private int colunaPermitida;
    private int linhaPermitida;
    private int linhamembroLivre;

    /**
     * Funcao para encontra membro livre negativo
     *
     * @return Retorna o indice do membro livre negativo na matriz
     */
    private int getMembroLivreNegativo() {
        for (int linha = 1; linha < quantRestricoes; linha++) {
            if (matriz[linha][0] < 0) {
                return linha;
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Funcao que procura por um VNB Negativa na linha do ML negativo
     *
     * @param linha Linha do membro livre negativo
     * @return Retorna o indice do membro livre negativo na matriz
     */
    private int getVariavelNaoBasicaNegativa(int linha) {
        for (int coluna = 1; coluna < quantVariaveis; coluna++) {
            if (matriz[linha][coluna] < 0) {
                return coluna;
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Funcao que retorna a linha permitida com menor razao entro o ML e o
     * elemento da coluna permitida
     *
     * @param colunaPermitida Coluna Permitida
     * @return Retorna o indice da linha permitida
     */
    private int getLinhaMembroPermitido() {
        return getLinhaMenor();
    }

    /**
     * Metodo que multiplica a posicao inferior da linha do elemento permitido
     * pelo inverso do elemento permitido
     *
     * @param linhaPermitida Linha permitida
     *
     */
    private void multiplicaLinha(double inverso) {
        for (int coluna = 0; coluna < matriz[0].length; coluna++) {
            matrizInferior[linhaPermitida][coluna] = (matriz[linhaPermitida][coluna] == 0) ? 0 : matriz[linhaPermitida][coluna] * (inverso);
            marcados[linhaPermitida][coluna] = true;
        }
    }

    /**
     * Metodo que multiplica a posicao inferior da coluna do elemento permitido
     * pelo inverso do elemento permitido
     *
     * @param colunaPermitida Coluna permitida
     *
     */
    private void multiplicaColuna(double inverso) {
        for (int linha = 0; linha < matriz.length; linha++) {
            matrizInferior[linha][colunaPermitida] = (matriz[linha][colunaPermitida] == 0) ? 0 : matriz[linha][colunaPermitida] * ((-1) * inverso);
            marcados[linha][colunaPermitida] = true;
        }
    }

    /**
     * Funcao que retorna o indice da linha do elemento com menor razao
     * ML/Elemento da coluna permitida pelo inverso do elemento permitido
     *
     * @param colunaPermitida Coluna permitida
     * @return Indice da linha do elemento de menor razao
     */
    private int getLinhaMenor() {
        double menor = Double.MAX_VALUE;
        int linhaMenor = Integer.MAX_VALUE;
        for (int linha = 1; linha < quantRestricoes; linha++) {
            if (matriz[linha][colunaPermitida] != 0) {
                double razao = (matriz[linha][COLUNA_ML] / matriz[linha][colunaPermitida]);
                if (razao > 0 && razao < menor) {
                    menor = razao;
                    linhaMenor = linha;
                }
            }
        }
        return linhaMenor;
    }

    private void processarFuncao(String funcao) {

    }

    /**
     * Metodo que mostra a matriz do Simplex
     *
     */
    private void mostrar() {
        for (int x = 0; x < quantRestricoes + 1; x++) {
            for (int y = 0; y < quantVariaveis + 1; y++) {
                System.out.print("(" + matriz[x][y] + "/" + matrizInferior[x][y] + ") ");
            }
            System.out.print("\n");
        }
        System.out.println("===============================");
    }

    /**
     * Metodo que mostra a matriz do Simplex
     *
     */
    private void mostrar(boolean[][] matriz) {
        for (int x = 0; x < quantRestricoes + 1; x++) {
            for (int y = 0; y < quantVariaveis + 1; y++) {
                System.out.print("(" + matriz[x][y] + ") ");
            }
            System.out.print("\n");
        }
    }

    /**
     * Metodo que executa primeira parte do Simplex
     *
     */
    private void primeiroPasso() {
        linhamembroLivre = getMembroLivreNegativo();
        colunaPermitida = getVariavelNaoBasicaNegativa(linhamembroLivre);
        linhaPermitida = getLinhaMembroPermitido();
        System.out.println("Elemento permitido :" + matriz[linhaPermitida][colunaPermitida]);
        double inverso = (1 / matriz[linhaPermitida][colunaPermitida]);
        System.out.println("Inverso :" + inverso);
        multiplicaLinha(inverso);
        multiplicaColuna(inverso);
        mostrar(marcados);
        matrizInferior[linhaPermitida][colunaPermitida] = inverso;
    }

    private void simplex() {
        primeiroPasso();
    }

    private void preencheCelulaInferior(int linhaPermitida, int colunaPermitida) {
        for (int linha = 0; linha < matrizInferior.length; linha++) {
            for (int coluna = 0; coluna < matrizInferior[0].length; coluna++) {
                if (!marcados[linha][coluna]) {
                    matrizInferior[linha][coluna] = getMarcadoLinha(linha) * getMarcadoColuna(coluna);
                }
            }
        }
    }

    private double getMarcadoLinha(int linha) {
        for (int coluna = 0; coluna < matrizInferior.length; coluna++) {
            if (marcados[linha][coluna]) {
                return matrizInferior[linha][coluna];
            }
        }
        return Double.MAX_VALUE;
    }

    private double getMarcadoColuna(int coluna) {
        for (int linha = 0; linha < matriz.length; linha++) {
            if (marcados[linha][coluna]) {
                return matriz[linha][coluna];
            }
        }
        return Double.MAX_VALUE;
    }
     private void somaValores() {
        for (int linha = 0; linha < matriz.length; linha++) {
            for (int coluna = 0; coluna < matriz[0].length; coluna++) {
                  if(!marcados[linha][coluna]){
                      matriz[linha][coluna]+= matrizInferior[linha][coluna];
                  }
            }
        }
    }

    private void algoritmoDaTroca() {
        preencheCelulaInferior(linhaPermitida, colunaPermitida);
        mostrar();
        trocaValores();
        somaValores();
        matrizInferior = new double[matriz.length][matriz[0].length];
        marcados = new boolean[matriz.length][matriz[0].length];
        mostrar();
    }

    private void trocaValores() {
        trocaValoresLinha();
        trocaValoresColuna();
    }

    private void trocaValoresLinha() {
        int aux = 0;
        for (int coluna = 0; coluna < matriz[0].length; coluna++) {
            matriz[linhaPermitida][coluna] = matrizInferior[linhaPermitida][coluna];
        }
    }

    private void trocaValoresColuna() {
        int aux = 0;
        for (int linha = 0; linha < matriz.length; linha++) {
            if (linha != linhaPermitida) {
                matriz[linha][colunaPermitida] = matrizInferior[linha][colunaPermitida];
            }
        }
    }

    /**
     * Metodo que executa teste do algoritmo
     *
     */
    private void teste() {
        quantRestricoes = 3;
        quantVariaveis = 2;
        double[] variaveis = {80.0, 60.0};
        double[][] restricoes
                = {{-24.0, -4.0, -6.0},
                {16.0, 4.0, 2.0},
                {3.0, 0.0, 1.0}};

        double[][] matriz
                = {{0, 80.0, 60.0},
                {-24.0, -4.0, -6.0},
                {16.0, 4.0, 2.0},
                {3.0, 0.0, 1.0}};
        matrizInferior = new double[matriz.length][matriz[0].length];
        marcados = new boolean[matriz.length][matriz[0].length];

        this.matriz = matriz.clone();
        primeiroPasso();
        algoritmoDaTroca();

    }

    public static void main(String[] args) {

        new Simplex().teste();
    }



}

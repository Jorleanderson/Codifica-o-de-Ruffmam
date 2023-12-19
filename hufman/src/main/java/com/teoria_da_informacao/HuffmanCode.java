package com.teoria_da_informacao;

import java.util.*;

public class HuffmanCode {
	/*
	 * Implementação do Algoritmo de Huffman
	 * Compactação e Descompatação de um texto
	 * 
	 
	 */
	public static void main(String[] args) {
		// Texto exemplo para Compactar e Decodificação usando o Algoritmo de Huffman
        String test = "hoje nao e segunda feira"; // Tamanho do texto = 112 bits 
        
  
        // Percorre o texto contando os símbolos e montando um vetor de frequências.
        int[] charFreqs = new int[256];
        for (char c : test.toCharArray())
            charFreqs[c]++;
        
        // Criar a Arvore dos codigos para a Compactação
        HuffmanTree tree = buildTree(charFreqs);
        
        // Resultados das quantidade e o codigo da Compactação
        System.out.println("TABELA DE CODIGOS");
        System.out.println("SIMBOLO\tQUANTIDADE\tHUFFMAN CÓDIGO");
        printCodes(tree, new StringBuffer());
        
        // Compactar o texto
        String encode = encode(tree,test);
        // Mostrar o texto Compactado
        System.out.println("\nTEXTO COMPACTADO");
        System.out.println(encode); // Tamanho de 40 bits - Economia de 72 bits
        
        // Decodificar o texto
        System.out.println("\n\nTEXTO DECODIFICADO");
        System.out.println(decode(tree,encode));

    }

	/* Criar a árvore de Codificação - A partir da quantidade de frequências de cada letra 
	 *                                 cria-se uma árvore binária para a compactação do texto
	 * Parâmetro de Entrada:  charFreqs: array com quantidade de frequências de cada letra
	 * Parâmetro de Saída:    trees: Arvore binaria para a compactação e decodificação  
	 */
    public static HuffmanTree buildTree(int[] charFreqs) {
    	// Cria uma Fila de Prioridade 
    	// A Fila será criado pela ordem de frequência da letra no texto
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>(); 
        // Criar as Folhas da Arvore para cada letra 
        for (int i = 0; i < charFreqs.length; i++){
            if (charFreqs[i] > 0)
                trees.offer(new HuffmanLeaf(charFreqs[i], (char)i)); // Inser os elementos, nó folha, na fila de prioridade
        }
        // Percorre todos os elementos da fila
        // Criando a árvore binária de baixo para cima
        while (trees.size() > 1) {
            // Pega os dois nós com menor frequência
            HuffmanTree a = trees.poll(); // poll - Retorna o próximo nó da Fila ou NULL se não tem mais
            HuffmanTree b = trees.poll(); // poll - Retorna o próximo nó da Fila ou NULL se não tem mais
 
            // Criar os nós da árvore binária
            trees.offer(new HuffmanNode(a, b)); 
        }
        // Retorna o primeiro nó da árvore
        return trees.poll();
    }
 
    /* COMPACTAR a string 
     *     Parâmetros de Entrada: tree - Raiz da Arvore de compactação
     *     						  encode - Texto original 
     *     Parâmetros de Saída: encodeText- Texto Compactado
    */ 
    public static String encode(HuffmanTree tree, String encode){
    	assert tree != null;
    	
    	String encodeText = "";
        for (char c : encode.toCharArray()){
        	encodeText+=(getCodes(tree, new StringBuffer(),c)); //busca o codigo de cada letra
        }
    	return encodeText; // Retorna o texto Compactado
    }
    
    
    /* DECODIFICAR a string
     *     Parâmetros de Entrada: tree - Raiz da Arvore de compactação
     *     						  encode - Texto Compactado
     *     Parâmetros de Saída: decodeText- Texto decodificado
    */
    public static String decode(HuffmanTree tree, String encode) {
    	assert tree != null;
    	
    	String decodeText="";
    	HuffmanNode node = (HuffmanNode)tree;
    	for (char code : encode.toCharArray()){
    		if (code == '0'){ // Quando for igual a 0 é o Lado Esquerdo
    		    if (node.left instanceof HuffmanLeaf) { 
    		    	decodeText += ((HuffmanLeaf)node.left).value; // Retorna o valor do nó folha, pelo lado Esquerdo  
	                node = (HuffmanNode)tree; // Retorna para a Raíz da árvore para começar uma nova busca
	    		}else{
	    			node = (HuffmanNode) node.left; // Continua percorrendo a árvore pelo lado Esquerdo 
	    		}
    		}else if (code == '1'){ // Quando for igual a 1 é o Lado Direito
    		    if (node.right instanceof HuffmanLeaf) {
    		    	decodeText += ((HuffmanLeaf)node.right).value; //Retorna o valor do nó folha, pelo lado Direito
	                node = (HuffmanNode)tree; // Retorna para a Raíz da árvore  para começar uma nova busca
	    		}else{
	    			node = (HuffmanNode) node.right; // Continua percorrendo a árvore pelo lado Direito
	    		}
    		}
    	} // End for
    	return decodeText; // Retorna o texto Decodificado
    }    
    
    /* 
     * Método para percorrer a Arvore e mostra a tabela de compactação
     *     Parâmetros de Entrada: tree - Raiz da Arvore de compactação
     *     						  prefix - texto codificado com 0 e/ou 1
     */
    public static void printCodes(HuffmanTree tree, StringBuffer prefix) {
        assert tree != null;
        
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;
            
            // Imprime na tela a Lista
            System.out.println(leaf.value + "\t" + leaf.frequency + "\t\t" + prefix);
 
        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;
 
            // traverse left
            prefix.append('0');
            printCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length()-1);
 
            // traverse right
            prefix.append('1');
            printCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length()-1);
        }
    }
    
    /* 
     * Método para retornar o código compactado de uma letra (w)
     *     Parâmetros de Entrada: tree - Raiz da Arvore de compactação
     *     						  prefix - texto codificado com 0 e/ou 1
     *     						  w - Letra
     *     Parâmetros de Saída: prefix- Letra codificada
     */
    public static String getCodes(HuffmanTree tree, StringBuffer prefix, char w) {
        assert tree != null;
        
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;
            
            // Retorna o texto compactado da letra
            if (leaf.value == w ){
            	return prefix.toString();
            }
            
        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;
 
            // Percorre a esquerda
            prefix.append('0');
            String left = getCodes(node.left, prefix, w);
            prefix.deleteCharAt(prefix.length()-1);
 
            // Percorre a direita
            prefix.append('1');
            String right = getCodes(node.right, prefix,w);
            prefix.deleteCharAt(prefix.length()-1);
            
            if (left==null) return right; else return left;  //retorn o prefixo( a junção de zeros e um)
        }
		return null;
    }

}
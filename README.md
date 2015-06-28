AFDMinimizer
============
A DFA (Deterministic Finite Automaton) minimizer. Please, read this file for instructions on its capabilities as well as a usage guide. Both english and portuguese translations are provided in this readme file.

Requires Java 8.

Portuguese
-----------
Este programa consiste em um minimizador de AFDs (Autômatos Finitos Determinísticos), utilizando o algorítmo de Hopcroft para realizar a minimização. Algumas notas devem ser feitas quanto a este programa:

- Possui uma implementação genérica de um AFD. Por isso, ele pode:
    - Minimizar AFDs de qualquer tamanho, com um ou mais estados finais.
    - Reconhecer palavras utilizando um AFD (não disponível via linha de comando, mas funcionamente já implementada)
    - Gerar palavras a partir de um AFD percorrendo-o ao contrário.
    - Testar o autômato minimizado com palavras geradas pelo autômato original e vice-versa (opcionalmente, de forma automatizada)

- Possui uma implementação genérica de um APD
    - Utilizado para verificar a sintaxe do arquivo de entrada.
    - Com poucas mudanças, é capaz de lidar com APDs na entrada.

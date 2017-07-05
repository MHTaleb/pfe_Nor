/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

        /**
         *
         * @author Taleb
         */
        public class Counter {
            static char[] letters;
            static int[] counts;
            public static void main(String[] args) {
                letters = new char[]{'a','A','a','A','a','A','b','B','c','c','C','d','d','F','d','d'
                ,'d','e','E','f','F','f'};
                counts = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

                for (char letter : letters) {
                    final int index = letter-'A';
                    counts[index % 32]++;
                }
                for (int i = 0; i < 26; i++) {
                    char currentChar =(char) ('a'+i);
                    System.out.println(currentChar+" = "+counts[i]);
                }

            }
        }

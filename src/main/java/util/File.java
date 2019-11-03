/*
 * Universidade Federal de Pernambuco
 * Centro de Informática
 * Disciplina: Algoritmos e Estrutura de Dados
 *
 */
package util;

import java.io.*;

public class File {

    private BufferedReader in;
    private String[] buffer;
    private int nextChar;
    private int nextTokenLin, nextTokenCol;
    private int primLin, contLin;

     public File(String inputFilePath) {
         try {
             this.in = new BufferedReader(new FileReader(inputFilePath));

             this.initBuffer();

         } catch (IOException e) {
             throw new RuntimeException(e.toString());
         }
     }

    private void readLine() {
        if (this.contLin <= 0) {
            return;
        }

        this.buffer[this.primLin] = null;
        this.nextChar = 0;
        this.primLin++;
        this.contLin--;

        if (this.nextTokenLin >= 0 && this.nextTokenLin < this.primLin) {
            this.findNext();
        }

    }

    public char readChar() {
        if (this.contLin <= 0) {
            return '\0';
        }

        char newChar;
        String line = this.buffer[this.primLin];
        if (this.nextChar >= line.length()) {
            newChar = '\n';
            this.readLine();
        } else {
            newChar = line.charAt(this.nextChar++);
            if (newChar != ' ' && this.nextTokenLin >= 0) {
                this.findNext();
            }
        }

        return newChar;
    }

    private void initBuffer() throws IOException {
        this.buffer = new String[5];
        this.nextChar = 0;
        this.nextTokenLin = 0;
        this.primLin = this.contLin = 0;

        String line = this.in.readLine();
        if (line == null) {
            this.nextTokenLin = -1;
        } else {
            this.buffer[0] = line;
            this.contLin++;
            this.findNext();
        }
    }

    private int appendLine(String str) {
        if (this.contLin == 0) {
            this.primLin = 0;
        }

        if (this.primLin + this.contLin >= this.buffer.length) {
            String[] src = this.buffer;
            if (this.contLin >= this.buffer.length) {
                this.buffer = new String[2 * this.buffer.length];
            }

            System.arraycopy(src, this.primLin, this.buffer, 0, this.contLin);
            this.nextTokenLin -= this.primLin;
            this.primLin = 0;
        }

        buffer[this.primLin + this.contLin] = str;
        this.contLin++;
        return (this.primLin + this.contLin - 1);
    }

    private void findNext() {
        try {
            String line = this.buffer[this.primLin];
            if (line != null) {
                int size = line.length();
                for (int i = this.nextChar; i < size; i++)
                    if (line.charAt(i) != ' ') {
                        this.nextTokenCol = i;
                        return;
                    }
            }

            this.nextTokenLin = this.nextTokenCol = -1;
            while ((line = this.in.readLine()) != null) {
                int size = line.length();
                for (int i = 0; i < size; i++)
                    if (line.charAt(i) != ' ') {
                        this.nextTokenCol = i;
                        this.nextTokenLin = this.appendLine(line);
                        return;
                    }
                this.appendLine(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }
}

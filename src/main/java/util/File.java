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
    private PrintWriter out;
    private String[] buffer;
    private int nextChar;
    private int nextTokenLin, nextTokenCol;
    private int primLin, contLin;

     public File(String in) {
         try {
             this.in = new BufferedReader(new FileReader(in));
             this.out = null;

             this.initBuffer();

         } catch (IOException e) {
             throw new RuntimeException(e.toString());
         }
     }

    public String readLine() {
        if (this.contLin <= 0)
            return null;

        String line = this.buffer[this.primLin];
        if (this.nextChar > 0)
            if (this.nextChar >= line.length())
                line = "";
            else
                line = line.substring(this.nextChar, line.length()-1);

        this.buffer[this.primLin] = null;
        this.nextChar = 0;
        this.primLin++;
        this.contLin--;

        if (this.nextTokenLin >= 0 && this.nextTokenLin < this.primLin)
            this.findNext();

        return line;
    }

    public char readChar() {
        if (this.contLin <= 0)
            return '\0';

        char newChar;
        String line = this.buffer[this.primLin];
        if (this.nextChar >= line.length()) {
            newChar = '\n';
            this.readLine();
        } else {
            newChar = line.charAt(this.nextChar++);
            if (newChar != ' ' && this.nextTokenLin >= 0)
                this.findNext();
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
        if (this.contLin == 0)
            this.primLin = 0;

        if (this.primLin + this.contLin >= this.buffer.length) {
            String[] src = this.buffer;
            if (this.contLin >= this.buffer.length)
                this.buffer = new String[2 * this.buffer.length];

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

    public void print(char c) {
        this.out.print(c);
    }

    public void print(String s) {
        this.out.print(s);
    }

    public void print(int i) {
        this.out.print(i);
    }

    public void print(double d) {
        this.out.print(d);
    }

    public void print(double d, int dec) {
        this.out.print(this.formatDouble(d, dec));
    }

	private String formatDouble(double d, int dec) {
        if (dec <= 0) {
            return String.valueOf(Math.round(d));
        }
        StringBuffer res = new StringBuffer();
        long aprox = (int) Math.round(d * Math.pow(10, dec));
        if (d < 0) {
            aprox = -aprox;
            res.append('-');
        }
        String num = String.valueOf(aprox);
        int n = num.length() - dec;
        if (n <= 0) {
            res.append("0.");
            for (int i = 0; i < -n; i++)
                res.append('0');
            res.append(num);
        } else {
            char[] array = num.toCharArray();
            res.append(array, 0, n).append('.').append(array, n, dec);
        }
        return res.toString();
    }
}

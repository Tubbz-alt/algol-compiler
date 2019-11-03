/*
 * Universidade Federal de Pernambuco
 * Centro de Informática
 * Disciplina: Algoritmos e Estrutura de Dados
 *
 */
package util;

import java.io.*;

public class File {

    private static final int INITIAL_BUFFER_SIZE = 5;

    private BufferedReader inputBuffer;
    private String[] buffer;
    private int nextChar = 0;
    private int nextTokenLin = 0;
    private int firstLine = 0;
    private int lineCounter = 0;

    public File(String inputFilePath) {
        try {
            inputBuffer = new BufferedReader(new FileReader(inputFilePath));
            initBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public char readChar() {
        if (lineCounter <= 0) {
            return '\0';
        }

        char newChar;
        String line = buffer[firstLine];
        if (nextChar >= line.length()) {
            newChar = '\n';
            readLine();
        } else {
            newChar = line.charAt(nextChar++);
            if (newChar != ' ' && nextTokenLin >= 0) {
                findNext();
            }
        }

        return newChar;
    }


    private void initBuffer() throws IOException {
        buffer = new String[INITIAL_BUFFER_SIZE];
        String line = inputBuffer.readLine();

        if (line == null) {
            nextTokenLin = -1;
        } else {
            buffer[0] = line;
            lineCounter++;
            findNext();
        }
    }

    private void findNext() {
        try {
            String line = buffer[firstLine];
            if (line != null) {
                int size = line.length();
                for (int i = nextChar; i < size; i++)
                    if (line.charAt(i) != ' ') {
                        return;
                    }
            }

            nextTokenLin = -1;
            while ((line = inputBuffer.readLine()) != null) {
                int size = line.length();
                for (int i = 0; i < size; i++)
                    if (line.charAt(i) != ' ') {
                        nextTokenLin = appendLine(line);
                        return;
                    }
                appendLine(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    private void readLine() {
        if (lineCounter <= 0) {
            return;
        }

        buffer[firstLine] = null;
        nextChar = 0;
        firstLine++;
        lineCounter--;

        if ((nextTokenLin >= 0) && (nextTokenLin < firstLine)) {
            findNext();
        }
    }

    private int appendLine(String str) {
        if (lineCounter == 0) {
            firstLine = 0;
        }

        if (firstLine + lineCounter >= buffer.length) {
            String[] src = buffer;
            if (lineCounter >= buffer.length) {
                buffer = new String[2 * buffer.length];
            }

            System.arraycopy(src, firstLine, buffer, 0, lineCounter);
            nextTokenLin -= firstLine;
            firstLine = 0;
        }

        buffer[firstLine + lineCounter] = str;
        lineCounter++;
        return (firstLine + lineCounter - 1);
    }
}

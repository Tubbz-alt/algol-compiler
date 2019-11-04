/*
 * Universidade Federal de Pernambuco
 * Centro de Informática
 * Disciplina: Algoritmos e Estrutura de Dados
 *
 */
package util;

import java.io.*;

public class File {

    private static final char BLANK = ' ';
    private static final char LINE_BREAK = '\n';
    private static final char END_OF_FILE = '\0';

    private BufferedReader reader;
    private String buffer;
    private int nextPosition = 0;
    private boolean hasNextToken = true;

    public File(String inputFilePath) {
        try {
            reader = new BufferedReader(new FileReader(inputFilePath));
            buffer = "";

            readFirstLine();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    private void readFirstLine() throws IOException {
        String line = reader.readLine();

        if (line != null) {
            buffer = line;
            findNextCharacter();
            return;
        }

        hasNextToken = false;
    }

    private void findNextCharacter() {
        try {
            if (buffer == null) {
                ignoreBlankLines();
                return;
            }

            ignoreBlankCharacters();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    private void ignoreBlankCharacters() {
        for (int i = nextPosition; i < buffer.length(); i++) {
            if (buffer.charAt(i) != BLANK) {
                return;
            }
        }
    }

    private void ignoreBlankLines() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.length() == 0) {
                setBuffer(line);
                return;
            }

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) != BLANK) {
                    setBuffer(line);
                    return;
                }
            }
        }
        hasNextToken = false;
    }

    private void setBuffer(String str) {
        buffer = str;
    }

    public char readChar() {
        if (!hasNextToken) {
            return END_OF_FILE;
        }

        char currentChar;

        if (isLineBreak()) {
            currentChar = LINE_BREAK;
            readNextLine();
        } else {
            currentChar = buffer.charAt(nextPosition++);
            if (currentChar != BLANK) {
                findNextCharacter();
            }
        }

        return currentChar;
    }

    private boolean isLineBreak() {
        return nextPosition >= buffer.length();
    }

    private void readNextLine() {
        if (!hasNextToken) {
            return;
        }

        buffer = null;
        nextPosition = 0;
        findNextCharacter();
    }
}

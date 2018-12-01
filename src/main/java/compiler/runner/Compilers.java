/*
 * IFMG - COMPILERS - 2018
 */
package compiler.runner

/compilers;

import java.io.File;
import java.io.IOException;
import static lexical_analysis.LexicalAnalysis.contentFile;
import static lexical_analysis.LexicalAnalysis.file;
import static lexical_analysis.LexicalAnalysis.getToken;
import static lexical_analysis.LexicalAnalysis.loadArq;
import lexical_analysis.Token;
import syntactic_analysis.Parser;

/**
 *
 * @author Patricia Pieroni
 */
public class Compilers {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        new Compilers().loadFile();
        contentFile = loadArq(file.getAbsoluteFile().toPath());  
        System.out.println(contentFile);
        syntactic_analysis.Parser.parser(); 
        while (Parser.actualToken.getToken() != Token.EOF) {
            Parser.actualToken = getToken();
        }
          
    }
    
    private void loadFile(){
         file = new File(getClass().getResource("/input/teste.txt").getFile());
    }
    
}

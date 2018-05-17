/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import static java.lang.System.out;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Note;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 *
 * @author filip
 */
public class NLPtokenizer {

    public ObservableList<Note> getFilteredNotes(ObservableList<Note> notes) {

        ObservableList<Note> temp = FXCollections.observableArrayList();

        try (InputStream tokenModelStream = new FileInputStream(
                new File("en-token.bin"));
                InputStream nerModelInputStream = new FileInputStream(
                        new File("en-todo.bin"));) {

            TokenizerModel tokenizerModel = new TokenizerModel(tokenModelStream);
            Tokenizer tokenizer = new TokenizerME(tokenizerModel);

            TokenNameFinderModel nameModel
                    = new TokenNameFinderModel(nerModelInputStream);
            NameFinderME nameFinder = new NameFinderME(nameModel);

            for (Note note : notes) {

                String tokens[] = tokenizer.tokenize(note.getNote());
                Span nameSpans[] = nameFinder.find(tokens);
                //double[] spanProbabilities = nameFinder.probs(nameSpans);
                if (nameSpans.length != 0) {
                   temp.add(note);
                }

                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return temp;

    }

}

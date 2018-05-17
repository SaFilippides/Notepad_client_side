/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.stream.JsonParser;
import static javax.json.stream.JsonParser.Event.END_OBJECT;
import static javax.json.stream.JsonParser.Event.KEY_NAME;
import static javax.json.stream.JsonParser.Event.START_OBJECT;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import model.Note;

/**
 *
 * @author filip
 */
@Provider
@Consumes({"application/json"})
public class NoteJsonParser implements MessageBodyReader<List<Note>> {

    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, javax.ws.rs.core.MediaType mt) {
        return true;
    }

    @Override
    public List<Note> readFrom(Class<List<Note>> type, Type type1, Annotation[] antns, javax.ws.rs.core.MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {

        if (mt.getType().equals("application") && mt.getSubtype().equals("json")) {
            Note note = new Note();
            List<Note> values = new ArrayList();
            JsonParser parser = Json.createParser(in);
            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case START_OBJECT:
                        note = new Note();
                        break;
                    case END_OBJECT:
                        values.add(note);
                        break;
                    case KEY_NAME:
                        String key = parser.getString();
                        parser.next();
                        switch (key) {
                            case "id":
                                note.setId(parser.getInt());
                                break;
                            case "name":
                                note.setName(parser.getString());
                                break;
                            case "imagePath":
                                note.setImageURL(parser.getString());
                                break;
                            case "note":
                                note.setNote(parser.getString());
                                break;
                            case "user_id":
                                note.setUser_id(parser.getInt());
                                break;
                            case "city":
                                note.setCity(parser.getString());
                                break;
                            case "date":
                                note.setDate(parser.getString());
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
            return values;
        }
        throw new UnsupportedOperationException("Not supported MediaType: " + mt);

    }
}

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
import model.AppUser;

/**
 *
 * @author filip
 */
@Provider
@Consumes({"application/json"})
public class UserJsonParser implements MessageBodyReader<List<AppUser>> {

    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, javax.ws.rs.core.MediaType mt) {
        return true;
    }

    @Override
    public List<AppUser> readFrom(Class<List<AppUser>> type, Type type1, Annotation[] antns, javax.ws.rs.core.MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {

        if (mt.getType().equals("application") && mt.getSubtype().equals("json")) {
            AppUser user = new AppUser();
            List<AppUser> values = new ArrayList();
            JsonParser parser = Json.createParser(in);
            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case START_OBJECT:
                        user = new AppUser();
                        break;
                    case END_OBJECT:
                        values.add(user);
                        break;
                    case KEY_NAME:
                        String key = parser.getString();
                        parser.next();
                        switch (key) {
                            case "password":
                                user.setPassword(parser.getString());
                                break;
                            case "username":
                                user.setUsername(parser.getString());
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

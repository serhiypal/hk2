package org.serge.ws.rs.hk2.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.serge.ws.rs.hk2.TextDao;
import org.serge.ws.rs.hk2.TextService;
import org.serge.ws.rs.hk2.dto.Text;

@Service
public class TextServiceImpl implements TextService {

    private final TextDao textDao;

    @Inject
    public TextServiceImpl(TextDao textDao) {
        this.textDao = textDao;
    }

    @PostConstruct
    public void init() {
        System.out.println("POST CONSTRUCT");
    }

    @Override
    public Text newText(String text) {
        return textDao.create(new Text(null, text));
    }

}

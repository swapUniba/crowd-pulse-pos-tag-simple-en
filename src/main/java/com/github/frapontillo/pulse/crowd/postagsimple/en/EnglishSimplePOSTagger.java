package com.github.frapontillo.pulse.crowd.postagsimple.en;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.data.entity.Token;
import com.github.frapontillo.pulse.crowd.postagsimple.ISimplePOSTaggerOperator;
import com.github.frapontillo.pulse.spi.ISingleablePlugin;
import com.github.frapontillo.pulse.spi.VoidConfig;
import rx.Observable;

import java.util.List;

/**
 * @author Francesco Pontillo
 */
public class EnglishSimplePOSTagger extends ISingleablePlugin<Message, VoidConfig> {
    public final static String PLUGIN_NAME = "simplepostagger-en";

    @Override public String getName() {
        return PLUGIN_NAME;
    }

    @Override public VoidConfig getNewParameter() {
        return new VoidConfig();
    }

    @Override public Observable.Operator<Message, Message> getOperator(VoidConfig parameters) {
        EnglishSimplePOSTagger actualTagger = this;
        return new ISimplePOSTaggerOperator(this) {
            @Override public List<Token> posTagMessageTokens(Message message) {
                return actualTagger.singleItemProcess(message).getTokens();
            }
        };
    }

    @Override public Message singleItemProcess(Message message) {
        if (message.getTokens() == null) {
            return null;
        }

        // associate each POS with the corresponding Token
        for (Token token : message.getTokens()) {
            String simplePos = null;
            if (token.getPos().startsWith("NN")) {
                simplePos = "n";
            } else if (token.getPos().startsWith("VB")) {
                simplePos = "v";
            } else if (token.getPos().startsWith("JJ")) {
                simplePos = "a";
            } else if (token.getPos().startsWith("RB")) {
                simplePos = "r";
            }
            token.setSimplePos(simplePos);
        }

        return message;
    }
}

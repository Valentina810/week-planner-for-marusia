package com.github.valentina810.weekplannerformarusia.storage.session;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Builder
@EqualsAndHashCode
@ToString
public class Actions {

    private List<PrevAction> prevActions;

    public List<PrevAction> getPrevActions() {
        return prevActions == null ? new ArrayList<>() : prevActions;
    }

    public Optional<PrevAction> getLastPrevAction() {
        return prevActions.stream().max(Comparator.comparingInt(PrevAction::getNumber));
    }

    public void addAction(PrevAction prevAction) {
        getLastPrevAction()
                .ifPresentOrElse(
                        lastPrevAction -> prevAction.setNumber(lastPrevAction.getNumber() + 1),
                        () -> prevAction.setNumber(0)
                );
        if (prevActions.isEmpty()) {
            prevActions = new ArrayList<>();
        }
        prevActions.add(prevAction);
    }
}
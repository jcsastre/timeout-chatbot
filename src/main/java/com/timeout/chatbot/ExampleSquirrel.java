package com.timeout.chatbot;

import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.UntypedStateMachine;
import org.squirrelframework.foundation.fsm.UntypedStateMachineBuilder;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

public class ExampleSquirrel {

    // 1. Define SessionState Machine Event
    enum FSMEvent {
        ToA, ToB, ToC, ToD
    }

    // 2. Define SessionState Machine Class
    @StateMachineParameters(stateType=String.class, eventType=ExampleSquirrel.FSMEvent.class, contextType=Integer.class)
    static class StateMachineSample extends AbstractUntypedStateMachine {
        protected void fromAToB(String from, String to, ExampleSquirrel.FSMEvent event, Integer context) {
            System.out.println("Transition from '"+from+"' to '"+to+"' on event '"+event+
                "' with context '"+context+"'.");
        }

        protected void ontoB(String from, String to, ExampleSquirrel.FSMEvent event, Integer context) {
            System.out.println("Entry SessionState \'"+to+"\'.");
        }
    }

    public static void main(String[] args) {
        // 3. Build SessionState Transitions
        UntypedStateMachineBuilder builder = StateMachineBuilderFactory.create(ExampleSquirrel.StateMachineSample.class);
        builder.externalTransition().from("A").to("B").on(ExampleSquirrel.FSMEvent.ToB).callMethod("fromAToB");
        builder.onEntry("B").callMethod("ontoB");

        // 4. Use SessionState Machine
        UntypedStateMachine fsm = builder.newStateMachine("A");
        fsm.fire(ExampleSquirrel.FSMEvent.ToB, 10);

        System.out.println("Current state is "+fsm.getCurrentState());
    }
}

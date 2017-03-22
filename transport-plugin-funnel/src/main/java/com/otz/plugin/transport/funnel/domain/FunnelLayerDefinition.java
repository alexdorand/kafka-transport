package com.otz.plugin.transport.funnel.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 opentoolzone.com - Kafka Transport
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by alexdorand on 2016-12-08.
 */
public class FunnelLayerDefinition implements Serializable {

    private String layerName;
    private List<FunnelIngredientDefinition> ingredientDefinitions;
    private List<EventTrigger> eventTriggersWhenCompleted;

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public List<FunnelIngredientDefinition> getIngredientDefinitions() {
        return ingredientDefinitions;
    }

    public void setIngredientDefinitions(List<FunnelIngredientDefinition> ingredientDefinitions) {
        this.ingredientDefinitions = ingredientDefinitions;
    }

    public List<EventTrigger> getEventTriggersWhenCompleted() {
        return eventTriggersWhenCompleted;
    }

    public void setEventTriggersWhenCompleted(List<EventTrigger> eventTriggersWhenCompleted) {
        this.eventTriggersWhenCompleted = eventTriggersWhenCompleted;
    }

    public enum Funnels {
        awareness, interest, decision, action, sales
    }


    public static void main(String[] args) {
        FunnelLayerDefinition awareness = new FunnelLayerDefinition();

        awareness.setLayerName("awareness");
        awareness.setIngredientDefinitions(new ArrayList<>());
        awareness.getIngredientDefinitions().add(new FunnelIngredientDefinition("eceaa6c3-9774-44bf-9985-4f0a636e4c00", "Welcome Email"));
        awareness.getIngredientDefinitions().add(new FunnelIngredientDefinition("518aa5ad-d52f-498e-9451-cc52ab05f5bf", "CTA Account completion clicked"));

        FunnelLayerDefinition interest = new FunnelLayerDefinition();
        interest.setIngredientDefinitions(new ArrayList<>());
        interest.setLayerName("interest");
        interest.getIngredientDefinitions().add(new FunnelIngredientDefinition("fd8c8b8a-7b5d-420c-869e-162320c80520", "Profile type selected"));
        interest.getIngredientDefinitions().add(new FunnelIngredientDefinition("82c24046-21af-4180-a880-7476ffc30d6f", "Avatar set"));
        interest.getIngredientDefinitions().add(new FunnelIngredientDefinition("77c2e158-5222-463f-bc68-9dfbf22449f9", "Profile Background Image Set"));
        interest.getIngredientDefinitions().add(new FunnelIngredientDefinition("acdc9463-9861-416e-804e-de2c302609cb", "Default User Search Preference Created"));
        interest.getIngredientDefinitions().add(new FunnelIngredientDefinition("e848378c-3f37-4c3a-a6c9-d2d15722fdcc", "Default Project Search Preference Created"));

        FunnelLayerDefinition decision = new FunnelLayerDefinition();
        decision.setIngredientDefinitions(new ArrayList<>());
        decision.setLayerName("decision");
        decision.getIngredientDefinitions().add(new FunnelIngredientDefinition("f41754d8-ec14-414e-963f-126840c0503a", "Completed mandatory fields"));
        decision.getIngredientDefinitions().add(new FunnelIngredientDefinition("bc4c4aa2-7c61-487e-b9fc-b17a8be6c853", "User recommendation list for the user created"));


        FunnelLayerDefinition action = new FunnelLayerDefinition();
        action.setIngredientDefinitions(new ArrayList<>());
        action.setLayerName("action");
        action.getIngredientDefinitions().add(new FunnelIngredientDefinition("57027b0e-e786-4f4f-b262-862334388f1d", "Album is optimum"));
        action.getIngredientDefinitions().add(new FunnelIngredientDefinition("3038b61f-8f89-45b2-a7f5-70dc814d0c66", "Project distributed to potential users"));
        action.getIngredientDefinitions().add(new FunnelIngredientDefinition("cda4166c-c9ea-4684-8d5d-4940375e53e2", "Profile Shared On Facebook"));
        action.getIngredientDefinitions().add(new FunnelIngredientDefinition("3365ef8b-2034-40a0-a06c-af429e3ca7d9", "Profile Shared On Twitter"));


        FunnelLayerDefinition sales = new FunnelLayerDefinition();
        sales.setIngredientDefinitions(new ArrayList<>());
        sales.setLayerName("sales");
        sales.getIngredientDefinitions().add(new FunnelIngredientDefinition("627c3918-ba35-4422-bf9f-0e62ae2f4082", "Created the first project"));



    }
}

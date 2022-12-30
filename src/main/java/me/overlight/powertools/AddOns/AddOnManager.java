package me.overlight.powertools.AddOns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddOnManager {
    public static List<AddOn> addOns = new ArrayList<>();
    public static AddOn getAddOnByName(String name){
        for(AddOn on: addOns){
            if(on.name() == name){
                return on;
            }
        }
        return null;
    }
    public static void registerAddOn(AddOn ... addOn){
        addOns.addAll(Arrays.asList(addOn));
    }
    public static void unRegisterAddOn(AddOn ... addOn){
        try {
            addOns.removeAll(Arrays.asList(addOn));
        } catch(Exception ignored){ }
    }
}

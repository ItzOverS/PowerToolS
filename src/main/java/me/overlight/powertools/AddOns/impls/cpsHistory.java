package me.overlight.powertools.AddOns.impls;

import java.util.ArrayList;
import java.util.List;

public class cpsHistory {
    public cpsHistory() {

    }

    private final List<Integer> lmbHis = new ArrayList<>();
    private final List<Integer> rmbHis = new ArrayList<>();

    public enum cpsType{
        RMB,
        LMB
    }

    public void addCps(cpsType type, int range){
        if(type == cpsType.RMB) {
            rmbHis.add(range);
            if(rmbHis.size() > 20)
                rmbHis.remove(0);
        } else{
            lmbHis.add(range);
            if(lmbHis.size() > 20)
                lmbHis.remove(0);
        }
    }

    public Integer getHistoryIndex(cpsType type, int index){
        if(type == cpsType.RMB)
            if(index > rmbHis.size())
                return null;
            else
                return rmbHis.get(index);
        else
            if(index > lmbHis.size())
                return null;
            else
                return lmbHis.get(index);
    }

    public void clear(cpsType type){
        if(type == cpsType.RMB)
            rmbHis.clear();
        else
            lmbHis.clear();
    }

    public Integer getHistoryIndex(cpsType type){
        if(type == cpsType.RMB) {
            if (rmbHis.size() < 19)
                return null;
            return rmbHis.get(19);
        }
        else {
            if(lmbHis.size() < 19)
                return null;
            return lmbHis.get(19);
        }
    }

    public List<Integer> getHistory(cpsType type){
        if(type == cpsType.RMB)
            return rmbHis;
        else
            return lmbHis;
    }
}

package de.tud.textureAttack.model.algorithms;

public class Pair<KEY,VALUE> {
    private KEY key;
    private VALUE value;
    public Pair(KEY l, VALUE r){
        this.key = l;
        this.value = r;
    }
    public KEY getKey(){ return key; }
    public VALUE getValue(){ return value; }
    public void setKey(KEY key){ this.key = key; }
    public void setValue(VALUE value){ this.value = value; }
    
    public String toString(){
    	return "("+key+", "+value+")";
    }
    
}
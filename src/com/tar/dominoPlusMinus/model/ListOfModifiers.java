package com.tar.dominoPlusMinus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: goblin72
 * Date: 11.02.2015
 * Time: 10:20
 */
public class ListOfModifiers implements Serializable {
    List<Modifiers> modifiers;

    public ListOfModifiers() {
        this.modifiers = new ArrayList<Modifiers>();
    }

    public List<Modifiers> getModifiers() {
        if (modifiers==null)
            modifiers=new ArrayList<Modifiers>();
        return modifiers;
    }

    public void setModifiers(List<Modifiers> modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public String toString() {
        return "ListModifiers{" +
                "modifiers=" + modifiers +
                '}';
    }
}

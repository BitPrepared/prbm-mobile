package it.bitprepared.prbm.mobile.model;

import java.io.Serializable;

/**
 * Created by nicola on 21/08/16.
 */
public class EntityField implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;

    private String title;
    private String hint;
    private String value;
    private transient int ID;
    private EntityFieldType type;

    public EntityField(String title, String hint, int ID,
                       EntityFieldType type) {
        this.title = title;
        this.hint = hint;
        this.ID = ID;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getID() {
        return ID;
    }

    public EntityFieldType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getHint() {
        return hint;
    }
}

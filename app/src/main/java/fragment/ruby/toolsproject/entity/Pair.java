package fragment.ruby.toolsproject.entity;

import java.io.Serializable;

/**
 * 键值对
 */
public class Pair implements Serializable {
    
    private static final long serialVersionUID = 4144283388672875288L;

    public String key;
    public Object value;

    public Pair() {
    }

    public Pair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }

        if (o instanceof Pair) {
            Pair obj = (Pair) o;

            if (null == this.key && null == obj.key) {
                return true;
            }

            if (null == this.key || null == obj.key) {
                return false;
            }

            return this.key.equals(obj.key);
        }

        return false;
    }
}

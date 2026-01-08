package holymagic.typeservice.model;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.Types;
import java.util.Arrays;

public class PostgresSQLIntArrayType implements UserType<int[]> {

    @Override
    public int getSqlType() {
        return Types.ARRAY;
    }

    @Override
    public Class returnedClass() {
        return int[].class;
    }

    @Override
    public int[] deepCopy(int[] value) throws HibernateException {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public boolean isMutable() {
        return false;
    }


    @Override
    public int[] assemble(Serializable cached, Object owner) throws HibernateException {
        return (int[]) cached;
    }

}

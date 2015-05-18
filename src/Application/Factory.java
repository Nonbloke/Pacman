package Application;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by Sarah on 08/05/2015.
 */
public class Factory {

    private HashMap m_RegisteredProducts = new HashMap();

    public void registerProduct(String productID, Class productClass)
    {
        m_RegisteredProducts.put(productID, productClass);
    }

    public IDrawable createProduct(String productID) throws Exception
    {
        Class productClass = (Class)m_RegisteredProducts.get(productID);
        Constructor productConstructor = productClass.getDeclaredConstructor();
        return (IDrawable)productConstructor.newInstance(new Object[] { });
    }

    private static Factory instance=null;
    public static Factory getInstance() {
        if (instance==null) {
            instance=new Factory();
        }
        return instance;
    }
}

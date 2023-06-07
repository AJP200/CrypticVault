package application;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyStore;

public class keyStorage {
    public static void storeToKeyStore(SecretKey keyToStore, String password, String filepath) throws Exception{
        File file = new File(filepath);
        KeyStore javaKeyStore = KeyStore.getInstance("JCEKS");
        if (!file.exists()){
            javaKeyStore.load(null,null);
        }

        javaKeyStore.setKeyEntry("keyAlias", keyToStore, password.toCharArray(),null);
        OutputStream writeStream = new FileOutputStream(filepath);
        javaKeyStore.store(writeStream,password.toCharArray());

    }


}

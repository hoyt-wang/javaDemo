import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

/**
 * Created by hoyt on 2017/11/12.
 */

public class MD5TestCase {

    @Test
    public void md5Test() {
        //String salt = "%$%$1234asdaDFG%%^@#SDF#$#%";
        String password = "000000";
        //String newpwd = DigestUtils.md5Hex(salt + password);
        String pwd = DigestUtils.md5Hex(password);
        System.out.println(pwd);
    }

    @Test
    public void md5HashTest() {
        String salt = "%$%$1234asdaDFG%%^@#SDF#$#%";
        String password = "000000";
        String result = new SimpleHash("MD5",password,salt).toString();
        System.out.println(result);
    }
}

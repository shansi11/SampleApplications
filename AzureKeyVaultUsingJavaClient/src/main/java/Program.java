import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.KeyVaultClientService;
import com.microsoft.azure.keyvault.KeyVaultConfiguration;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.azure.keyvault.models.KeyOperationResult;
import com.microsoft.azure.keyvault.webkey.JsonWebKeyEncryptionAlgorithm;
import com.microsoft.windowsazure.Configuration;

public class Program {

	public static void main(String[] args)
			throws InterruptedException, ExecutionException, URISyntaxException, UnsupportedEncodingException {
		
		KeyVaultCredentials kvCred = new ClientSecretKeyVaultCredential("7741f26b-7862-4c21-a3f5-8c55d84f4191", "p2EvtoU8EdmmLDw5XkgotRD/UtQAXgERFq2VcZPe3os=");
		Configuration config = KeyVaultConfiguration.configure(null, kvCred);
		KeyVaultClient vc = KeyVaultClientService.create(config);
		//vc.getSecretAsync(arg0, arg1, arg2)
		System.out.println("1111111111111111111111111111111 "+vc.getSecretAsync("https://cargalinuxkeyvault.vault.azure.net/"));
		
	
	}
}


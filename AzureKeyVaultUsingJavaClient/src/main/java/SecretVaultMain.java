/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author snatarajan64
 */
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.KeyVaultClientService;
import com.microsoft.azure.keyvault.KeyVaultConfiguration;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.azure.keyvault.models.Secret;
import com.microsoft.windowsazure.Configuration;


public class SecretVaultMain {

	public static void main(String[] args)
			throws InterruptedException, ExecutionException, URISyntaxException, UnsupportedEncodingException {
		
		
		KeyVaultCredentials kvCred = new ClientSecretKeyVaultCredential("7741f26b-7862-4c21-a3f5-8c55d84f4191", "p2EvtoU8EdmmLDw5XkgotRD/UtQAXgERFq2VcZPe3os=");
		//kvCred.doAuthenticate(arg0, arg1)
		Configuration config = KeyVaultConfiguration.configure(null, kvCred);
		KeyVaultClient vc = KeyVaultClientService.create(config);
		

		System.out.println("getCredentials "+vc.getCredentials());
		String keyIdentifier = "https://cargalinuxkeyvault.vault.azure.net/";
		//CloudCredentials secret = vc.getCredentials();//SecretAsync( "https://cargalinuxkeyvault.vault.azure.net/", "CargaLinSecret" );
		
		String secretValue = null;
		Future<Secret> result = null;
		
		try {
			result = vc.getSecretAsync(keyIdentifier);
			System.out.println("result "+result.get());
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		/*try {
			Secret sec = result.get();
			//secretValue = result.get().getValue();
			System.out.println("secretValue "+sec);
	}catch (Exception e) {
		e.printStackTrace();
		
	}
			vc.getServiceClient().getExecutorService().shutdown();
			result.cancel(true);*/
		
				
		/*String textToEncrypt = "CargaLinSecret";

		byte[] byteText = textToEncrypt.getBytes("UTF-16");
		Future<KeyOperationResult> result = vc.encryptAsync(keyIdentifier, JsonWebKeyEncryptionAlgorithm.RSAOAEP, byteText); 
		
		KeyOperationResult keyoperationResult = result.get();
		System.out.println(keyoperationResult);
		result = vc.decryptAsync(keyIdentifier, "RSA-OAEP", keyoperationResult.getResult());

		String decryptedResult = new String(result.get().getResult(), "UTF-16");
		System.out.println(decryptedResult);*/
		//System.out.println("1111111111111111111111111111111 "+secretValue);
	}
}